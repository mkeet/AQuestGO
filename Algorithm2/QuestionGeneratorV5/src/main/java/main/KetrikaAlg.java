/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * 
 * @author toky.raboanary
 */
public class KetrikaAlg {
    
    public static final String YES_NO_QUESTION_TYPE1 = "Yes-No 2 particular 1 relation";
    public static final String YES_NO_QUESTION_TYPE1_MASK = "<Thing> <ObjectProperty> <Thing>";
    public static final String YES_NO_QUESTION_TYPE1_QUANTIFIED = "Yes-No 2 particular 1 relation + quantifier";
    public static final String YES_NO_QUESTION_TYPE1_QUANTIFIED_MASK = "<Thing> <ObjectProperty> <Quantifier> <Thing>";
    public static final String YES_NO_QUESTION_TYPE2 = "Yes-No 1 particular 1 relation";
    public static final String YES_NO_QUESTION_TYPE2_MASK = "<Thing> <Perdurant>";
    public static final String EQUIVALENCE_QUESTIONS = "Equivalence";
    public static final String EQUIVALENCE_QUESTIONS_MASK = "<Thing> <Thing>";
    public static final String TRUE_FALSE_QUESTIONS = "True-False";
    public static final String TRUE_FALSE_QUESTIONS_MASK = "<Thing> <ObjectProperty> <Thing>";
    public static final String TRUE_FALSE_QUESTIONS_QUANTIFIED = "True-False + quantifier";
    public static final String TRUE_FALSE_QUESTIONS_QUANTIFIED_MASK = "<Thing> <ObjectProperty> <Quantifier> <Thing>";
    public static final String WHAT_QUESTIONS_TYPE1 = "What 2 particular 1 relation";
    public static final String WHAT_QUESTIONS_TYPE1_MASK = "<Thing> <ObjectProperty> <Thing>";
    public static final String WHAT_QUESTIONS_QUANTIFIED = "What 2 particular 1 relation + quantifier";
    public static final String WHAT_QUESTIONS_QUANTIFIED_MASK = "<Thing> <ObjectProperty> <Quantifier> <Thing>";
    public static final String WHAT_QUESTIONS_TYPE2 = "What 1 particular 1 relation";
    public static final String WHAT_QUESTIONS_TYPE2_MASK = "<Thing> <ObjectProperty>";
    public static final String DEFINE_QUESTIONS = "Define";
    public static final String DEFINE_QUESTIONS_MASK = "<Thing>";
    
    private ArrayList<String>  keywords;
    private HashMap<String, ArrayList<String>> dico;
            
    private Ontology ontology;
    
    public void init () throws FileNotFoundException, OWLOntologyCreationException {
        String filenameTemplate = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/templates_complex_2.txt";
        this.init(filenameTemplate,  "");
    }
    public void init(String filenameTemplate, String filenameOntology) throws FileNotFoundException, OWLOntologyCreationException {
        
        keywords= new ArrayList<String>();
        keywords.add(YES_NO_QUESTION_TYPE1);
        keywords.add(YES_NO_QUESTION_TYPE1_QUANTIFIED);
        keywords.add(YES_NO_QUESTION_TYPE2);
        keywords.add(EQUIVALENCE_QUESTIONS);
        keywords.add(TRUE_FALSE_QUESTIONS);
        keywords.add(TRUE_FALSE_QUESTIONS_QUANTIFIED);
        keywords.add(WHAT_QUESTIONS_TYPE1);
        keywords.add(WHAT_QUESTIONS_QUANTIFIED);
        keywords.add(WHAT_QUESTIONS_TYPE2);
        keywords.add(DEFINE_QUESTIONS);
        
        File fTemp = new File(filenameTemplate);
        Scanner scanner = new Scanner(fTemp);
        
        this.dico = new HashMap<String, ArrayList<String>>();
        ArrayList<String> lines = new ArrayList<String>();
        
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            lines.add(line);
        }
        
        String key = lines.get(0);
        ArrayList<String> elements = new ArrayList<String>();
        for (int i = 1; i < lines.size(); i++) {
            System.out.println(lines.get(i));
            elements.add(lines.get(i));
            if (i+1< lines.size() && isKeyword(lines.get(i+1)))
            {
                System.out.println("key before : "+key);
                dico.put(key, elements);
                key = lines.get(i+1);
                System.out.println("key after : "+key);
                elements = new ArrayList<String>();
                i++;
            }
            if (i+1 >= lines.size()) {
                dico.put(key,  elements);
            }
            
        }
        if (filenameOntology.equals(""))
            this.ontology = Singleton.getOntology();
        else 
            this.ontology = Singleton.getOntology(filenameOntology);
    }
    
    public String getMask(String questionType) {
        if (questionType.equals(YES_NO_QUESTION_TYPE1))
            return YES_NO_QUESTION_TYPE1_MASK;
        if (questionType.equals(YES_NO_QUESTION_TYPE1_QUANTIFIED))
            return YES_NO_QUESTION_TYPE1_QUANTIFIED_MASK;
        if (questionType.equals(YES_NO_QUESTION_TYPE2))
            return YES_NO_QUESTION_TYPE2_MASK;
        if (questionType.equals(TRUE_FALSE_QUESTIONS))
            return TRUE_FALSE_QUESTIONS_MASK;
        if (questionType.equals(TRUE_FALSE_QUESTIONS_QUANTIFIED))
            return TRUE_FALSE_QUESTIONS_QUANTIFIED_MASK;
        if (questionType.equals(WHAT_QUESTIONS_QUANTIFIED))
            return WHAT_QUESTIONS_QUANTIFIED_MASK;
        if (questionType.equals(WHAT_QUESTIONS_TYPE2))
            return WHAT_QUESTIONS_TYPE2_MASK;
        if (questionType.equals(WHAT_QUESTIONS_TYPE1))
            return WHAT_QUESTIONS_TYPE1_MASK;
        if (questionType.equals(DEFINE_QUESTIONS))
            return DEFINE_QUESTIONS_MASK;
         if (questionType.equals(EQUIVALENCE_QUESTIONS))
            return EQUIVALENCE_QUESTIONS_MASK;
        return "";
    }
    
    public int getIdTypeQuestion(String questionType) {
        int qt = -1;
             if (questionType.equals(YES_NO_QUESTION_TYPE1) || questionType.equals(TRUE_FALSE_QUESTIONS)){
                    qt = 1;
                // If it is Yes/No question with 1 particular and a single relation
                // Then generate Type 2 questions
                }else if (questionType.equals(YES_NO_QUESTION_TYPE2)){
                    qt = 2;
                // If it is quantified Yes/No or True/False question with 2 particulars and a single relation
                // Then generate Type 3 question
                }else if (questionType.equals(YES_NO_QUESTION_TYPE1_QUANTIFIED) || questionType.equals(TRUE_FALSE_QUESTIONS_QUANTIFIED)){
                    qt = 3;
                // If it is what questions with 2 particular and a single relation
                // Then generate Type 4 questions
                }else if (questionType.equals(WHAT_QUESTIONS_TYPE1)){
                    qt = 4;
                // If it is what question with 1 particular and a single relation 
                // Then generate Type 5 question 
                }else if (questionType.equals(WHAT_QUESTIONS_TYPE2)){
                    qt = 5;
                // If it is quantified what questions with 2 particulars and a single relation 
                // Then generate Type 6 question 
                } else if (questionType.equals(WHAT_QUESTIONS_QUANTIFIED)){
                    qt = 6;
                } else if (questionType.equals(DEFINE_QUESTIONS) ){
                    qt = 7;
                // If it is any other question types
                // Then generate normal questions 
                }else if (questionType.equals(EQUIVALENCE_QUESTIONS)){
                    qt=0;
                }
        return qt;
    }
    
    public ArrayList<String> getQuestion(String questionType) throws Exception {
        int qt = getIdTypeQuestion(questionType);
        ArrayList<String> questions = new ArrayList<String>();
        String question="";
        String templMask = getMask(questionType);
        switch (qt) {
            case 1:
                Type1Question type1Question = new Type1Question(ontology);
                Type1ResultSet resultSet1QuestionMask = type1Question.getResultSetQuestionMask(templMask);
                System.out.println("resultSet1QuestionMask = "+ resultSet1QuestionMask.toString());
                if (resultSet1QuestionMask.getX() == null || resultSet1QuestionMask.getY() == null)
                    return questions;
                PropertyClassification pc = new PropertyClassification(resultSet1QuestionMask.getOP().toString());
                ArrayList<String> appTemplates = getAppTemplates(questionType, pc);
                System.out.println("[XXX] ---> QuestionType="+questionType+", PC="+pc.getPropertyType().toString()+"");
                for (int i = 0; i < appTemplates.size(); i++) {
                    String q = LiguisticHandler.generateType1Sentence(resultSet1QuestionMask, appTemplates.get(i));
                    System.out.println("Question : "+q);
                    questions.add(q);
                }
                
                break;
            case 2:
                Type2Question type2Question = new Type2Question(ontology);
                Type2ResultSet resultSet2QuestionMask = type2Question.getResultSetQuestionMask(templMask);
                System.out.println("resultSet2QuestionMask = "+ resultSet2QuestionMask.toString());
                if (resultSet2QuestionMask.getX() == null || resultSet2QuestionMask.getY() == null)
                    return questions;
                String q = LiguisticHandler.generateType2Sentence(resultSet2QuestionMask, this.dico.get(questionType).get(0));
                System.out.println("Question : "+q);
                questions.add(q);
                
                //question = OWLHandler.generateYesNoType2(templMask);
                break;
            case 3:
                Type3Question type3Question = new Type3Question(ontology);
                Type3ResultSet resultSet3QuestionMask = type3Question.getResultSetQuestionMask(templMask);
                System.out.println("resultSet3QuestionMask = "+ resultSet3QuestionMask.toString());
                if (resultSet3QuestionMask.getX() == null || resultSet3QuestionMask.getY() == null)
                    return questions;
                PropertyClassification pc3 = new PropertyClassification(resultSet3QuestionMask.getOP().toString());
                
                System.out.println("[SSSSS] ---> QuestionType="+questionType+", PC="+pc3.getPropertyType().toString()+" quantifier= "+resultSet3QuestionMask.getQuantifier());
                ArrayList<String> appTemplates3 = getAppTemplatesQuant(questionType, pc3, resultSet3QuestionMask.getQuantifier());
                
                for (int i = 0; i < appTemplates3.size(); i++) {
                    System.out.println("--------------ddsdsd-----------------"+appTemplates3.get(i)
                            +" count "+appTemplates3.size()+" pc = "+pc3.getInfo());
                    System.out.println(resultSet3QuestionMask.toString());
                    
                    
                    String q3 = LiguisticHandler.generateType3Sentence(resultSet3QuestionMask, appTemplates3.get(i));
                    System.out.println("Question : "+q3);
                    questions.add(q3);
                }
                //question = OWLHandler.generateYesNoQuantified(templMask);
                break;
            case 4:
                Type4Question type4Question = new Type4Question(ontology);
                Type1ResultSet resultSet4QuestionMask = type4Question.getResultSetQuestionMask(templMask);
                if (resultSet4QuestionMask.getX() == null || resultSet4QuestionMask.getY() == null)
                    return questions;
                System.out.println("resultSet4QuestionMask = "+ resultSet4QuestionMask.toString());
                PropertyClassification pc4 = new PropertyClassification(resultSet4QuestionMask.getOP().toString());
                ArrayList<String> appTemplates4 = getAppTemplates(questionType, pc4);
                
                for (int i = 0; i < appTemplates4.size(); i++) {
                    String q4 = LiguisticHandler.generateType1Sentence(resultSet4QuestionMask, appTemplates4.get(i));
                    System.out.println("Question : "+q4);
                    questions.add(q4);
                }
                
                //question = OWLHandler.generateWhatType1(templMask);
                break;
            case 5:
                Type5Question type5Question = new Type5Question(ontology);
                Type5ResultSet resultSet5QuestionMask = type5Question.getResultSetQuestionMask(templMask);
                System.out.println("resultSet5QuestionMask = "+ resultSet5QuestionMask.toString());
                if (resultSet5QuestionMask.getX() == null)
                    return questions;
                System.out.println("OP = "+resultSet5QuestionMask.getOP().toString());
                PropertyClassification pc5 = new PropertyClassification(resultSet5QuestionMask.getOP().toString());
                System.out.println("OP = "+resultSet5QuestionMask.getOP().toString());
                ArrayList<String> appTemplates5 = getAppTemplates(questionType, pc5);
                
                for (int i = 0; i < appTemplates5.size(); i++) {
                    System.out.println("Ambotra question ");
                    String q5 = LiguisticHandler.generateType5Sentence(resultSet5QuestionMask, appTemplates5.get(i));
                    System.out.println("Question : "+q5);
                    questions.add(q5);
                }
               // question = OWLHandler.generateWhatType2 (templMask);
                break;
                
            case 6:
                Type6Question type6Question = new Type6Question(ontology);
                Type3ResultSet resultSet6QuestionMask = type6Question.getResultSetQuestionMask(templMask);
                System.out.println("resultSet6QuestionMask = "+ resultSet6QuestionMask.toString());
                if (resultSet6QuestionMask.getX() == null || resultSet6QuestionMask.getY() == null)
                    return questions;
                
                
                PropertyClassification pc6 = new PropertyClassification(resultSet6QuestionMask.getOP().toString());
                ArrayList<String> appTemplates6 = getAppTemplatesQuant(questionType, pc6, resultSet6QuestionMask.getQuantifier());
                
                System.out.println("[TTT] ---> PC="+pc6.getPropertyType().toString()+" quantifier= "+resultSet6QuestionMask.getQuantifier());
                for (int i = 0; i < appTemplates6.size(); i++) {
                    System.out.println("--------------ddsdsd-----------------"+appTemplates6.get(i)
                            +" count "+appTemplates6.size()+" pc = "+pc6.getInfo());
                    String q6 = LiguisticHandler.generateType3Sentence(resultSet6QuestionMask, appTemplates6.get(i));
                    System.out.println("Question : "+q6);
                    questions.add(q6);
                }
                
                // question =  OWLHandler.generateWhatQuantified(templMask);
                break;
            case 7:
                Type7Question type7Question = new Type7Question(ontology);
                String classe = type7Question.getValidClass();
                if (classe.equals("No valid classes"))
                    return questions;
                System.out.println("classe = "+classe);
                Entity ent = ontology.getEntity(classe);
                //ent.Print();
                String model = this.getModelEndurantPerdurant(classe);
                //System.out.println("model = "+model);
                ArrayList<String> appTemplates7 = getAppTemplatesModel(questionType, model);
                
                for (int i = 0; i < appTemplates7.size(); i++) {
                    
                    String q7 = type7Question.generateQuestion(appTemplates7.get(i), classe);
                    System.out.println("Question : "+q7);
                    questions.add(q7);
                }
                //question = OWLHandler.generateDefinitionQuestion(templMask);
                break;
            case 0:
                 Type0Question type0Question = new Type0Question(ontology);
                 Type0ResultSet resultSet0QuestionMask = type0Question.getResultSetQuestionMask(templMask);
                 if (resultSet0QuestionMask.getX() == null)
                    return questions;
                 String model2 = this.getModelEndurantPerdurant(LiguisticHandler.getClassName(resultSet0QuestionMask.getX().toString()));
                 ArrayList<String> appTemplates0 = getAppTemplatesModel(questionType, model2);
                 for (int i = 0; i < appTemplates0.size(); i++) {
                    
                    String q0 = LiguisticHandler.generateType0Sentence(resultSet0QuestionMask, appTemplates0.get(i));
                    System.out.println("Question : "+q0);
                    questions.add(q0);
                }
                //question = OWLHandler.generateEquivalenceQuestions(templMask);
                break;

            default:
                break;
        }
        return questions;
    }
    public ArrayList<String> getAppTemplates(String questionType, PropertyClassification pc){
        ArrayList<String> templates = this.dico.get(questionType);
        ArrayList<String> appTemplates = new ArrayList<String>();
        
        PropertyType propertyType = pc.getPropertyType();
        System.out.println("info iiiii -> "+pc.getInfo());
        System.out.println(templates.get(0));
        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).contains("<"+propertyType.toString().replaceAll("PAST_", "")+ ">")) {
                System.out.println(templates.get(i));
                System.out.println(propertyType.toString());
                appTemplates.add(templates.get(i));
            }
        }
        System.out.println("count = "+appTemplates.size());
        return appTemplates;
    }
    public ArrayList<String> getAppTemplatesModel(String questionType, String model){
        ArrayList<String> templates = this.dico.get(questionType);
        ArrayList<String> appTemplates = new ArrayList<String>();
        
        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).contains(model)) {
                System.out.println(templates.get(i));
                System.out.println(model);
                appTemplates.add(templates.get(i));
            }
        }
        System.out.println("count = "+appTemplates.size());
        return appTemplates;
    }
    
    public String getModelEndurantPerdurant(String nom) throws Exception {
        Entity perdurant = ontology.getEntity("Perdurant");
        Entity endurant = ontology.getEntity("Endurant");
        
        //perdurant.Print();
        //endurant.Print();
        
        
        Entity entityCheck = ontology.getEntity(nom);
        
        
        //entityCheck.Print();
        System.out.println("Perdurant? " +entityCheck.isA(perdurant));
        System.out.println("Endurant? " +entityCheck.isA(endurant));
        if (perdurant != null && entityCheck != null)
        {    
            if (entityCheck.isA(perdurant))
                return "<Perdurant>";
        }
        
        if (endurant != null && entityCheck != null)
        {    
            if (entityCheck.isA(endurant))
                return "<Endurant>";
        }
        
        return "invalid";
    }
    
    private String getQuantifierModelTemp(String quantifier) {
        return "<Quantifier-"+quantifier+">";
    }
    public ArrayList<String> getAppTemplatesQuant(String questionType, PropertyClassification pc, String quantifier){
        ArrayList<String> templates = this.dico.get(questionType);
        ArrayList<String> appTemplates = new ArrayList<String>();
        
        PropertyType propertyType = pc.getPropertyType();
        System.out.println("QUANTIFIER ===== "+ quantifier);
        System.out.println("info iiiii -> "+pc.getInfo());
        System.out.println(templates.get(0));
        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).contains("<"+propertyType.toString()+ ">") && templates.get(i).contains(getQuantifierModelTemp(quantifier))) {
                System.out.println(templates.get(i));
                System.out.println(propertyType.toString());
                appTemplates.add(templates.get(i));
            }
        }
        System.out.println("count = "+appTemplates.size());
        return appTemplates;
    }
    
    public void printTemplates() {
        int nTemp = 0;
        System.out.println("size = "+this.dico.size());
        this.dico.entrySet().forEach(entry->{
        System.out.println(entry.getKey()+ " -> "+entry.getValue().size());
            
        for(String val : entry.getValue())
        {
            System.out.println("\t"+val);  
        }});
    }
    
    
    public boolean isKeyword(String word) {
        for (String s : keywords ) {
            if (word.equals(s))
                return true;
        }
        return false;
    }
    
}
