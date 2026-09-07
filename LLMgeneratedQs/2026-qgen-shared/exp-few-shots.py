import os
os.environ["TORCHDYNAMO_DISABLE"] = "1"

import argparse
from xml.parsers.expat import model

import humanize as H

import numpy as np
import pandas as pd
import pyarrow.parquet as pq
from time import perf_counter

from tqdm import tqdm
from transformers import AutoTokenizer, AutoModelForCausalLM, AutoModelForSeq2SeqLM
import torch
import yaml

import sys
from pathlib import Path

class Tee:
    def __init__(self, *streams):
        self.streams = streams
    def write(self, data):
        for s in self.streams:
            s.write(data)
            s.flush()
    def flush(self):
        for s in self.streams:
            s.flush()


def build_message_with_roles(row, sys_p, n_exs, model_id):
    mm = []

    mm.append({"role": "system", "content": sys_p})
    for ax, q in zip(row["sample_axioms"][:n_exs], row["sample_questions"][:n_exs]):
        user_p = f"Based on the following OWL axiom, generate a question:\nAxiom: {ax}\nQuestion:"
        mm.append({"role": "user", "content": user_p})
        mm.append({"role": "assistant", "content": q})
    # final question
    
    final_user_p = f"Based on the following OWL axiom, generate a question:\nAxiom: {row['axiom']}\nQuestion:"
    mm.append({"role": "user", "content": final_user_p})
    return mm


def build_simple_prompt(row, sys_p, n_exs, model_id):
    mm = []

    mm.append(sys_p)
    mm.append("\n")
    for ax, q in zip(row["sample_axioms"][:n_exs], row["sample_questions"][:n_exs]):
        mm.append(f"Based on the following OWL axiom, generate a question:\nAxiom: {ax}\nQuestion: {q}\n")
    # final question
    
    final_user_p = f"Based on the following OWL axiom, generate a question:\nAxiom: {row['axiom']}\nQuestion:"
    mm.append(final_user_p)
    prompt = "\n".join(mm)
    return prompt

models_with_roles = ["meta-llama/Llama-3.1-8B-Instruct", 
                    "hugging-quants/Meta-Llama-3.1-70B-Instruct-GPTQ-INT4"
                        "mistralai/Mistral-7B-Instruct-v0.3", 
                     "Qwen/Qwen2.5-7B-Instruct", 
                     "Qwen/Qwen2.5-32B-Instruct",
                     "microsoft/phi-4"]
models_simple_prompt = ["google/gemma-2-9b-it", 
        "google/gemma-2-27b-it", 
        "google/flan-t5-xxl"]

def build_message(row, sys_p, n_exs, model_id):   
    if model_id in models_with_roles:
        return build_message_with_roles(row, sys_p, n_exs, model_id)
    elif model_id in models_simple_prompt:
        return build_simple_prompt(row, sys_p, n_exs, model_id)
    else:
        assert False, f"Unsupported model_id: {model_id}. See 'build_message' function"
#<


def generate_batched(conversations, tokenizer, model, batch_size=1, max_length=4096, max_new_tokens=64):
    # we need to pad for batching examples, but the tokenizer for the decoder-only models (here used) does not
    #   offer a pad_token, we use the eos_token.
    
    if "t5" in model.model_id:
        tokenizer.padding_side = "right"
    else:    
        tokenizer.padding_side = "left"    
        if tokenizer.pad_token is None:
            tokenizer.pad_token = tokenizer.eos_token

    results = []
    n_batches = (len(conversations) + batch_size - 1) // batch_size
    print(f"Number of batches: {n_batches} (batch size: {batch_size}, total conversations: {len(conversations)})")

    # greedy decoding for deterministic output
    # these are needed to suppress the warning
    model.generation_config.temperature = None
    model.generation_config.dtop_p = None

    for batch_idx, start in tqdm(enumerate(range(0, len(conversations), batch_size))):
        # if (batch_idx + 1) % 5 == 0 or batch_idx == n_batches - 1:
        #     print("*", end="", flush=True)
        # print()

        batch_messages = conversations[start:start + batch_size]
        if args.model_id in models_with_roles:
            enc = tokenizer.apply_chat_template(
                batch_messages,
                tokenize=True,
                add_generation_prompt=True,
                return_dict=True,
                return_tensors="pt",
                padding=True,
                truncation=True,
                max_length=max_length
            )
        elif args.model_id in models_simple_prompt:
            enc = tokenizer(
                batch_messages,
                return_tensors="pt",
                padding=True,
                truncation=True,
                max_length=max_length,
                add_special_tokens=True,
            )
        else:
            assert False, f"You should never be here"
        


        # verify the encoding
        # decoded = tokenizer.decode(
        #     enc["input_ids"][0],
        #     skip_special_tokens=False,  # IMPORTANT: keep special tokens to see the template
        # )
        # print(" -- DECODED --")
        # print(decoded)
        # print(" --")
        if (batch_idx+1) % 5 == 0:
            print("Using device:", model.device)

        enc = {k: v.to(model.device) for k, v in enc.items()}
        # print("encoded conversations moved to device:", device)
        
        prompt_lens = enc["attention_mask"].sum(dim=1)

        # input lengths per sample (exclude left padding)
        padded_prompt_length = enc["input_ids"].shape[1]

        with torch.no_grad():
            out = model.generate(**enc, max_new_tokens=max_new_tokens, do_sample=False)
        
        if "t5" in args.model_id:
            for i in range(out.size(0)):
                results.append( tokenizer.decode(out[i], skip_special_tokens=True).strip() )
        else:
            for i in range(out.size(0)):
                gen_tokens = out[i, padded_prompt_length:]
                results.append(tokenizer.decode(gen_tokens, skip_special_tokens=True).strip())
                
            #< for

    #< for batch_idx

    return results
#<



def main(args):
    # read dataset: this is the filtered raw dataset, with the correct ontologies and with a minimum number of questions for each question type.
    df = pq.read_table(args.dataset).to_pandas()

    print("- OUTPUT FOLDER:", args.out_fld)
    # check for any results already present in the output folder, if so, print a warning and exit to avoid overwriting results
    res_files = [f for f in os.listdir(args.out_fld) if f.startswith("results-") and f.endswith(".parquet")]
    if res_files:
        print("Warning: Found existing result files in the output folder:")
        for f in res_files:
            print(f"\t- {f}")
        assert False, "Exiting to avoid overwriting results."
    
    print("- DATASET")
    print("\t- filename:", args.dataset)
    print("\t- shape:", df.shape)

    if args.dev:
        for o, gdf in df.groupby("ontology"):
            print("ontology:", o, "size:", len(gdf))
            print(gdf.groupby("question_type").size())
        print(10* "*")

    seed = args.seed
    print("Random seed:", seed)
    
    if args.dev:
        subsample_dev = 1
        print(f"DEV SET: subsampling {subsample_dev} examples per ontology and question type...")
        df = df.groupby(["ontology", "question_type"]).sample(subsample_dev, random_state=seed)
    
    print("DATASET LOAD COMPLETE. Dataset size:", len(df), "dev:", args.dev)
    # <<< 1


    # >>> 1b, save dataset to output folder
    # save dataset in parquet format
    df.to_parquet(os.path.join(args.out_fld, "dataset.parquet"), index=False)

    # add log file
    log_f = open(os.path.join(args.out_fld, "log.txt"), "a")
    sys.stdout = Tee(sys.stdout, log_f)
    sys.stderr = Tee(sys.stderr, log_f)

    # create temporary file in out folder to signal process is running
    with open(os.path.join(args.out_fld, "RUNNING.tmp"), "w") as f:
        f.write("This file indicates that the experiment is still running.\n")
    # <<< 1b
    
    # >>> 2 model
    # Load model directly
    tokenizer = AutoTokenizer.from_pretrained(args.model_id, use_fast=True)
    if args.model_id == "google/flan-t5-xxl":
        model = AutoModelForSeq2SeqLM.from_pretrained(args.model_id,
        device_map="cuda:0",          # load straight onto GPU
        dtype=torch.float16,    # or torch.bfloat16 if your GPU supports it well
        low_cpu_mem_usage=True)
    else:
        # check the model size,
        
        a_dict = dict(
            device_map="cuda:0",
            max_memory={0: "28GiB"},
            dtype=torch.float16,
            low_cpu_mem_usage=True,
        )
        if "27b"in args.model_id.lower() or "32b" in args.model_id.lower(): 
            print("User asked a very large model, using quantization")
            from transformers import BitsAndBytesConfig
            import bitsandbytes as bnb
            print("bnb version:", bnb.__version__)

            quant_config = BitsAndBytesConfig(
                load_in_4bit=True,
                bnb_4bit_quant_type="nf4",
                bnb_4bit_use_double_quant=True,
                bnb_4bit_compute_dtype=torch.float16,
            )
            a_dict["quantization_config"] = quant_config

        model = AutoModelForCausalLM.from_pretrained(
            args.model_id,
            **a_dict
            )
    print("Using device:", model.device)

    model.model_id = args.model_id

    # device = args.device
    # print(f"Moving model to device: {device}... ", end="")
    # model.to(device)
    df["model_id"] = args.model_id
    # <<< 2


    for k_i, k in enumerate(args.k_examples):
        kdf = df.copy()
        print()
        print(30* "** ")
        print(f"{k_i+1}/{len(args.k_examples)} - FEW SHOT EXPERIMENT WITH k={k}")
        
        # >>> 3 build the messages
        #   MODEL DEPENDANT
        print(f"Using prompts with number of examples k= {k}.")
        conversations = []
        sys_p = "You are a helpful assistant that generates questions in natural language based on OWL axioms provided."
        for i in range(df.shape[0]):
            row = df.iloc[i]
            messages = build_message(row, sys_p, k, args.model_id)
            conversations.append(messages)
        print(f"Built {len(conversations)} messages with FEW SHOT EXAMPLES set to {k}.")
        print("EXAMPLE")
        print(conversations[0])
        kdf["k_examples"] = k
        print()
        # <<< 3


        # >>> 4: Generation
        in_convs = conversations  # [:10]
        t0 = perf_counter()
        bs = args.bs
        if k > 5:
            bs = 1
            print(f"Reducing batch size from {args.bs} to {bs} for k={k} to avoid OOM.")
        
            

        preds = generate_batched(in_convs, tokenizer, model, batch_size=bs, max_new_tokens=args.max_new_tokens)
        t1 = perf_counter()
        print(f"Generation completed in {H.naturaldelta(t1 - t0)}. Generated {len(preds)} questions.")
        # <<< 4

        # >>> 5: save results
        kdf["generated"] = preds
        filename = os.path.join(args.out_fld, f"results-{k}-examples.parquet.tmp")
        kdf.to_parquet(filename, index=False)
        filename2 = filename.replace(".tmp", "")
        os.replace(filename, filename2)

        print("saved results to:", filename2)
        print()
        print("SAMPLE")
        print(kdf[["question", "generated"]].head())
        print()
        # <<< 5
    # remove temporary RUNNING file
    os.remove(os.path.join(args.out_fld, "RUNNING.tmp"))
    #< for over k
    return args

#< main


# ------------------------------------------------------------------------------- __main__
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Few shot experiment")
    parser.add_argument(
        "--dataset", type=str, help="path to parquet file with the dataset", required=True)
    parser.add_argument("--seed", type=int, help="random seed for sampling the few shot examples", default=123)
    parser.add_argument("--model_id", type=str, help="model id to use for generation", required=True)
    parser.add_argument("--k_examples", type=int, nargs="+", help="number of few shot examples to add in the prompt", required=True)
    parser.add_argument("--out_fld", type=str, help="output folder to save the generated examples", required=True)
    parser.add_argument("--bs", type=int, help="batch size for generation", default=1)
    parser.add_argument("--max_new_tokens", type=int, help="max number of new tokens to generate", default=512)
    parser.add_argument("--device", type=str, help="device to use for generation (e.g., cuda:0)", default="cpu")
    parser.add_argument("--dev", type=int, help="whether to run in dev mode (only on a subset of the data)", default=0)
    
    args = parser.parse_args()
    args.dev = (args.dev == 1)

    print("CLI ARGS:")
    for k, v in vars(args).items():
        print(f"- {k}: {v}")
    print()

    if args.dev:
        args.out_fld = os.path.join("_out-dev", args.out_fld)
        print(f"Output folder changed to: {args.out_fld} - DEV SET TO 1")


    os.makedirs(args.out_fld, exist_ok=True)
    t0 = perf_counter()
    args = main(args)
    t1 = perf_counter()
    print("--- all done ---")
    print("Elapsed time:", H.naturaldelta(t1 - t0))

    # save args to a yaml file in the output folder
    filename = os.path.join(args.out_fld, "args.yaml")
    with open(filename, "w") as f:
        yaml.dump(vars(args), f)
    print("saved args to", filename)
    
    # save timing info
    timing_info = {
        "total_time_seconds": t1 - t0,
        "total_time_human_readable": H.naturaldelta(t1 - t0)
    }
    filename = os.path.join(args.out_fld, "timing.yaml")
    with open(filename, "w") as f:
        yaml.dump(timing_info, f)
    print("saved timing info to", filename)
    print()

    print("all done")
#<