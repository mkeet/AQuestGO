import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

def plot(x, ys, errors, model_names, colors, line_formats, include_legend=False):
	plt.rcParams['font.family'] = 'serif'

	fig, ax = plt.subplots(figsize=(8, 4))

	formatted_model_names = {'mistralai/Mistral-7B-Instruct-v0.3':'Mistral-7B',
	 "meta-llama/Llama-3.1-8B-Instruct": 'Llama-3.1-8B', "google/gemma-2-9b-it":'Gemma-2-9b',
	  "google/flan-t5-xxl":"FLAN-T5-XXL", "Qwen/Qwen2.5-7B-Instruct":'Qwen2.5-7B', 
		 "Qwen/Qwen2.5-32B-Instruct":'Qwen2.5-32B', "microsoft/phi-4":'phi-4'}

	for idx in range(len(model_names)):
		ax.errorbar(x, ys[idx], yerr=errors[idx], fmt=line_formats[idx], capsize=3, 
					elinewidth=1, markersize=6, label=formatted_model_names[model_names[idx]], color=colors[idx])

	# Add grid, labels, and title
	ax.grid(axis='y', linestyle='--', alpha=0.7)
	ax.set_xticks(x)
	#ax.set_xlabel('Number of in-context examples', fontsize=11)
	ax.set_ylabel('score_combined (mean $\pm$ std)', fontsize=11)
	#ax.set_title('score_combined by number of in-context examples, per model', fontsize=12)

	if include_legend:
		plt.legend(
			title="Model",          
			ncol=4,
			loc="upper center",
			bbox_to_anchor=(0.5, -0.15),        
			frameon=True,
		)
		
		plt.subplots_adjust(bottom=0.3)
	plt.show()

def classify_axiom(text):
	clean_text = " ".join(text.lower().split())
	
	if "," in clean_text and ("exists" in clean_text or "only" in clean_text or 'some' in clean_text):
		return "hard"
	
	elif "exists" in clean_text or "only" in clean_text or "some" in clean_text:
		return "medium"
	
	elif "subclassof" in clean_text or 'equivalentto':
		return "simple"
	
	return "unknown"

def annotate_axioms(out_filename):
	in_filename = '20260305-all-runs-scores.csv'
	df = pd.read_csv(in_filename, sep=';')
	df['axiom_complexity'] = df['axiom'].apply(lambda axiom: classify_axiom(axiom))
	df.to_csv(out_filename, sep=';', index=False)

if __name__=='__main__':
	filename = '20260305-all-runs-scores_axiom_complexity_annotated.csv'
	#annotate_axioms(filename)



	df = pd.read_csv(filename, sep=';')
	axiom_complexities = ['simple', 'medium', 'hard']
	for axiom_complexity in axiom_complexities:
		alpha = 0.5
		beta = 0.5
		x = [0, 1, 2, 3, 6, 10]
		colors = ["#CC79A7", "#009E73", "#CA562C", "#000000", "#56B4E9", "#DF9F1D", "#0072B2"]
		line_formats = [':D', '-.^', '--s', ':X', '-.+', '--v', '-o']
		model_ids = ['mistralai/Mistral-7B-Instruct-v0.3', "meta-llama/Llama-3.1-8B-Instruct", "google/gemma-2-9b-it", "google/flan-t5-xxl", "Qwen/Qwen2.5-7B-Instruct", 
		 "Qwen/Qwen2.5-32B-Instruct", "microsoft/phi-4"]
		#model_ids = ["microsoft/phi-4"]

		y_vals = {}
		error_vals = {}

		filtered_df = df[df['axiom_complexity'] == axiom_complexity]

		filtered_df['final_score'] = filtered_df.apply(lambda row: alpha*row['score_rougeL'] + beta*row['score_bertscore_F1'], axis=1)
		for model_id in model_ids:
			for x_val in x:
				completely_filtered_df = filtered_df[(filtered_df['k_examples'] == x_val) & (filtered_df['model_id'] == model_id)]

				avg_val = completely_filtered_df.loc[:, 'final_score'].mean()
				std_val = completely_filtered_df.loc[:, 'final_score'].std()
				
				if not (model_id in y_vals):
					y_vals[model_id] = []
				if not (model_id in error_vals):
					error_vals[model_id] = []

				y_vals[model_id].append(avg_val)
				error_vals[model_id].append(std_val)

		print('Plotting {}'.format(axiom_complexity))
		plot(x, list(y_vals.values()), list(error_vals.values()), list(y_vals.keys()), colors, line_formats, axiom_complexity=='hard')
