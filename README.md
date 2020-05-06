# AQuestGO
## Answerable Question Generation from Ontologies 
 
 ## Overview
One can use ontologies for a range of tasks, inculding to generate questions from them, with the aim of automating the management of exercises in computer-assisted learning. Research into ontology-driven answerable question generation has focused on multiple choice questions (MCQs) only, whereas learners are also exposed to other types of questions, such as yes/no and short answer questions. Initial research with a use case, called Inquire Biology, showed that it is possible to create such ontology-based questions. However, but it is unknown how that can be done automatically and whether it would work beyond that use case. 

We investigated this for ten types of educationally useful questions with additional sentence formulation variants. Each question type has a template specification, axiom preconditions on the ontology, and an algorithm to generate the instantiated questions  from the ontology. 
Three variants were designed: template variables using foundational ontology categories, using main classes from the domain ontology, and sentences mostly driven by natural language generation techniques. 

The user evaluation showed that constraining the template variables with domain class resulted in slightly better quality questions than using DOLCE categories, but the linguistic-driven templates far outperform both on syntactic and semantic adequacy of the generated questions.

## Content on this repo

This repository contains an implementation of the algorithms and the templates we designed and used for testing.

## Contributors:
- Toky Raboanary
- Steve Wang
- Maria Keet

