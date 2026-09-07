#!/bin/bash

# dataset

# DATASET_PATH=./dataset/qgenllm-ds-seed-123-few-shots.parquet
# DATASET_PATH=./dataset/qgenllm-ds-seed-234--10-few-shots.parquet
DATASET_PATH=./dataset/qgenllm-ds-seed-345--10-few-shots.parquet

# model_id
# MODEL_ID=meta-llama/Llama-3.1-8B-Instruct
#MODEL_ID=mistralai/Mistral-7B-Instruct-v0.3
MODEL_ID=google/gemma-2-9b-it

# k examples
K_EXAMPLES="10 6 3 2 1 0"

# output folder
OUT_FLD=./out-2026

# dev flag
DEV_FLAG=0

# device
DEVICE=cuda

# batch size
BS=4

# max new tokens
MAX_NEW_TOKENS=512

# seed can be any number, using the same one as for the dataset for no specific reason
SEED=123


sanitize() {
  # keep it simple: replace / and spaces; drop other problematic chars if you want
  echo "$1" | tr '/ ' '__'
}

DATASET_NAME="$(basename "$DATASET_PATH")"
DATASET_STEM="${DATASET_NAME%.*}"
MODEL_KEY="$(sanitize "$MODEL_ID")"
DATASET_KEY="$(sanitize "$DATASET_STEM")"

EXP_OUT_DIR=$OUT_FLD/${MODEL_KEY}/${DATASET_KEY}

echo "Experiment output will be saved to: ${EXP_OUT_DIR}"
mkdir -p $EXP_OUT_DIR

source ../.venv/bin/activate
echo "Read python environment"
python exp-few-shots.py \
    --dataset $DATASET_PATH \
    --out_fld $EXP_OUT_DIR \
    --seed $SEED \
    --model_id $MODEL_ID \
    --k_examples $K_EXAMPLES \
    --bs $BS \
    --max_new_tokens $MAX_NEW_TOKENS \
    --device $DEVICE \
    --dev $DEV_FLAG