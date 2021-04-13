# AQuestGO
## Answerable Question Generation from Ontologies 
 
 ## Overview
One can use ontologies for a range of tasks, including to generate questions from them, with the aim of automating the management of exercises in computer-assisted learning. Research into ontology-driven answerable question generation has focused on multiple choice questions (MCQs) only, whereas learners are also exposed to other types of questions, such as yes/no and short answer questions. Initial research with a use case, called Inquire Biology, showed that it is possible to create such ontology-based questions. However, but it is unknown how that can be done automatically and whether it would work beyond that use case. 

We investigated this for ten types of educationally useful question types, using multiple sentence formulation variants. Each question type has a template specification, axiom preconditions on the ontology, i.e., contents that an ontology has to have for that question to be able to be posed and have an answer, and an algorithm to generate the instantiated questions from the ontology. 
Three approaches were designed: 1) template variables using foundational ontology categories, 2) tailoring the templates by using the main classes from the domain ontology, and 3) sentences that were mostly driven by natural language generation techniques. 

These approaches were implemented and evaluated by means of a user evaluation. The evaluation showed that constraining the template variables with domain class resulted in slightly better quality questions than using DOLCE foundational ontology categories; however, the linguistic-driven templates far outperformed both on syntactic and semantic adequacy of the generated questions.

## Content on this repo

This repository contains implemented algorithms, the templates, the generated questions, the ontologies and the data used in the experiment.
- 'Algorithm1' and 'Algorithm2' contains the implemented algorithms in JAVA.
- 'QuestionsTemplates' contains the templates that we designed and used during the experiment.
- 'DataAnalysis.zip' contains the C# code with which we have done the data analysis.
- 'Algorithms' contains the algorithms: Algorithm 1, Algorithm 2 and OP Classifier.
- 'Ontologies' contains the ontologies used in the experiment.
- 'Data Used' contains the collected data (excel and CSV files).
- 'Report from data analysis' contains the results of the data analysis from the C# code.


## Contributors:
- Toky Raboanary
- Steve Wang
- Maria Keet

## Funding
In theory, yes, since it was specified as part of the "Quest: Querying Smart Text" project that was approved for funding in 2019 by SADiLaR, but they still haven't paid. The insights gained are also relevant within the scope of the NRF-funded <a href="http://www.meteck.org/MoReNL/">MoreNL project</a> for surface realisers for African languages.
