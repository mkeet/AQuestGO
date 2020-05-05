/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 *
 * @author stevewang
 */
public class Type7Question {
    
    //Global variables 
    private static OWLOntologyManager manager;
    private static OWLOntology ontology;
    private static OWLDataFactory factory;
    private static OWLReasoner reasoner;
    private static OWLOntologyFormat format; 
    private static PrefixOWLOntologyFormat prefixFormat;
    private static PrefixManager prefixManager;
    private String sentence;
    
    
    /**
     * Constructor for Type 7 questions (Definition Questions)
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type7Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
    
    public Type7Question(Ontology ontology) {
        this.manager = ontology.getManager();
        this.ontology = ontology.getOWLOntology();
        this.factory = ontology.getFactory();
        this.reasoner = ontology.getReasoner();
        this.format = ontology.getFormat();
        this.prefixFormat = ontology.getPrefixFormat();
        this.prefixManager = ontology.getPrefixManager();
        sentence = "";
    }
    
    public String generateQuestion (String template){
        // Initialise the output question to ""
        sentence = "";
        
        System.out.println("TYPE 7 : "+ template);
        // Split the template to words and store it to an array
        String[] arr = template.split(" ");
        
        String token = "";
        // For each word in the array
        for (int i=0; i<arr.length; i++){
            String word = arr[i];
            String article = "";
            if (i!=0){
                article = arr[i-1];
            }
            
            // If the word is in the form <word>
            if (word.charAt(0)=='<' && word.charAt(word.length()-1)=='>'){
                
                token = word.substring(1, word.length()-1);
                
                sentence = ProcessToken(sentence, article , token);
                
            // If the word is in the form <word>? or <word>. etc..
            } else if (word.charAt(0)=='<' && word.charAt(word.length()-2)=='>'){
                
                token = word.substring(1, word.length()-2);
                
                sentence = ProcessToken(sentence, article , token) + word.charAt(word.length()-1);
            
            // If word is a normal word and is the first word int he sentence
            }else if (sentence.equals("")){
                sentence = word;
                
            // if word is a normal word and is not the first word in the sentence.
            }else{
                sentence = sentence + " " + word;
            }
        }
        return sentence; 
    }
    public String generateQuestion (String template, String classe){
        // Initialise the output question to ""
        sentence = "";
        
        System.out.println("TYPE 7 : "+ template);
        // Split the template to words and store it to an array
        String[] arr = template.split(" ");
        
        String token = "";
        // For each word in the array
        for (int i=0; i<arr.length; i++){
            String word = arr[i];
            String article = "";
            if (i!=0){
                article = arr[i-1];
            }
            // If the word is in the form <word>
            if (word.charAt(0)=='<' && word.charAt(word.length()-1)=='>'){
                
                token = word.substring(1, word.length()-1);
                
                sentence = ProcessToken2(sentence, article , token, classe);
                
            // If the word is in the form <word>? or <word>. etc..
            } else if (word.charAt(0)=='<' && word.charAt(word.length()-2)=='>'){
                
                token = word.substring(1, word.length()-2);
                
                sentence = ProcessToken2(sentence, article , token, classe) + word.charAt(word.length()-1);
            
            // If word is a normal word and is the first word int he sentence
            }else if (sentence.equals("")){
                sentence = word;
                
            // if word is a normal word and is not the first word in the sentence.
            }else{
                sentence = sentence + " " + word;
            }
        }
        return sentence; 
    }
    
    /**
     * Process tokens and seperate top node with other cases
     * @param sentence
     * @param article
     * @param token
     * @return 
     */
    
    public String getValidClass() {
        OWLClass tokenClassPer = factory.getOWLClass("Perdurant", prefixManager);
        String nounPerdurant = subToken2(tokenClassPer);
        OWLClass tokenClassEnd = factory.getOWLClass("Endurant", prefixManager);
        String nounEndurant = subToken2(tokenClassEnd);
        
        String res = "No valid classes";
        
        int indice = (int) Math.round( Math.random() );
            switch (indice) {
                case 0: 
                    if(!nounPerdurant.equals(res))
                            res = nounPerdurant;
                    else res = nounEndurant;
                break;
                default: 
                    if(!nounEndurant.equals(res))
                            res = nounEndurant;
                    else res = nounPerdurant;
                    //res = nounEndurant;
                break;
            }
         return res;
    }
    
    private String ProcessToken(String sentence, String article, String token) {
        String output = sentence;
        
      //  System.out.println("sentence -> "+sentence);
        if (!token.equals("Perdurant")){
            // OWLClass thing = reasoner.getTopClassNode().getRepresentativeElement();
            OWLClass tokenClass = factory.getOWLClass(token, prefixManager);
            String noun = subToken(tokenClass);
            output = LiguisticHandler.checkArticle(output, article, noun);
            
            
        }else{ // Perdurant
            
            OWLClass tokenClass = factory.getOWLClass(token, prefixManager);
            String verb = subToken(tokenClass);
            if (LinguisticClass.isOnlyNounWordNet(verb)) {
                verb = "being "+LiguisticHandler.checkArticle2("a", verb);
            }
            String gerund = LinguisticClass.getPresentParticipleManyWords(verb);
            output = LiguisticHandler.checkArticle(output, article , gerund);
        }
        return output;
    }
    
    private String ProcessToken2(String sentence, String article, String token, String classe) {
        String output = sentence;
        
      //  System.out.println("sentence -> "+sentence);
        if (!token.equals("Perdurant")){
            //OWLClass thing = reasoner.getTopClassNode().getRepresentativeElement();
            String noun = LiguisticHandler.stringProcessSimple(classe) ;
            output = LiguisticHandler.checkArticle(output, article, noun);
            
        }else{ // Perdurant
            
            String verb = LiguisticHandler.stringProcessSimple(classe) ;
            if (LinguisticClass.isOnlyNounWordNet(verb)) {
                verb = "being "+LiguisticHandler.checkArticle2("a", verb);
            }
            String gerund = LinguisticClass.getPresentParticipleManyWords(verb);
            output = LiguisticHandler.checkArticle(output, article , gerund);
        }
        return output;
    }
    /**
     * substitute OWLClass with any subclass if it is defined in the ontology
     * @param word
     * @return 
     */
    private String subToken(OWLClass word) {
        // Create an empty arraylist to store all valid OWLClasses
        ArrayList <OWLClassExpression> validOWLClasses = new ArrayList<OWLClassExpression>();
        
        
        // Get all subclasses of the token
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(word, false);
        
        // Create RDFS for the defined by annotation property
        OWLAnnotationProperty isDefinedBy = factory.getRDFSIsDefinedBy();
        
        // For each of the subclasses check if it satifies either one of 3 criteriors:
        for (Node<OWLClass> subclass : subclasses){
            
            // Get all annotations of the subclass
            Set<OWLAnnotation> DefinitionAnnotations = subclass.getRepresentativeElement().getAnnotations(ontology, isDefinedBy);
            
            // Get a set of all equivalence classes
            Set<OWLClassExpression> equivalentClasses= subclass.getRepresentativeElement().getEquivalentClasses(ontology);
            
            // Get a set of all superclasses except OWL:Thing
            Set<OWLClassExpression> superClasses = subclass.getRepresentativeElement().getSuperClasses(ontology);
            superClasses.remove(reasoner.getTopClassNode().getRepresentativeElement());
            
            // Get a set of all subclasses
            Set<OWLClassExpression> suClasses = subclass.getRepresentativeElement().getSubClasses(ontology);
            suClasses.remove(reasoner.getBottomClassNode().getRepresentativeElement());
            
            // If the token has isDefinedBy annotations
            // if token has equivalent classes
            // If token is superclass or subclass of any other class
            if ((!DefinitionAnnotations.isEmpty()) || (!equivalentClasses.isEmpty()) || (!suClasses.isEmpty()) || (!superClasses.isEmpty())){
                
                // Add this class to the arrayList of valid classes
                validOWLClasses.add(subclass.getRepresentativeElement());
            }
            
        }
        
        // randomly select a class 
        if (validOWLClasses.isEmpty()){
            return "No valid classes";
        }else{
            try {
            OWLClassExpression selected = randomSelect(validOWLClasses);
            return LiguisticHandler.stringProcess(selected.toString());
            }
            catch (Exception e){
                e.printStackTrace();
                return "No valid classes";
            }
        }
    }
    private String subToken2(OWLClass word) {
        // Create an empty arraylist to store all valid OWLClasses
        ArrayList <OWLClassExpression> validOWLClasses = new ArrayList<OWLClassExpression>();
        
        
        // Get all subclasses of the token
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(word, false);
        
        // Create RDFS for the defined by annotation property
        OWLAnnotationProperty isDefinedBy = factory.getRDFSIsDefinedBy();
        
        // For each of the subclasses check if it satifies either one of 3 criteriors:
        for (Node<OWLClass> subclass : subclasses){
            
            // Get all annotations of the subclass
            Set<OWLAnnotation> DefinitionAnnotations = subclass.getRepresentativeElement().getAnnotations(ontology, isDefinedBy);
            
            // Get a set of all equivalence classes
            Set<OWLClassExpression> equivalentClasses= subclass.getRepresentativeElement().getEquivalentClasses(ontology);
            
            // Get a set of all superclasses except OWL:Thing
            Set<OWLClassExpression> superClasses = subclass.getRepresentativeElement().getSuperClasses(ontology);
            superClasses.remove(reasoner.getTopClassNode().getRepresentativeElement());
            
            // Get a set of all subclasses
            Set<OWLClassExpression> suClasses = subclass.getRepresentativeElement().getSubClasses(ontology);
            suClasses.remove(reasoner.getBottomClassNode().getRepresentativeElement());
            
            // If the token has isDefinedBy annotations
            // if token has equivalent classes
            // If token is superclass or subclass of any other class
            if ((!DefinitionAnnotations.isEmpty()) || (!equivalentClasses.isEmpty()) || (!suClasses.isEmpty()) || (!superClasses.isEmpty())){
                
                // Add this class to the arrayList of valid classes
                validOWLClasses.add(subclass.getRepresentativeElement());
            }
            
        }
        
        // randomly select a class 
        if (validOWLClasses.isEmpty()){
            return "No valid classes";
        }else{
            try {
            OWLClassExpression selected = randomSelect(validOWLClasses);
            return LiguisticHandler.getClassName(selected.toString());
            }
            catch (Exception e){
                e.printStackTrace();
                return "No valid classes";
            }
        }
    }
    /**
     * Select a random OWLClass from the given list
     * @param validOWLClasses
     * @return 
     */
    private OWLClassExpression randomSelect(ArrayList<OWLClassExpression> validOWLClasses) {
        Random rand = new Random();
        return validOWLClasses.get(rand.nextInt(validOWLClasses.size()));
    }
}
