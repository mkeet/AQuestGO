/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
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
class Type0Question {
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
     * Constructor for Type 0 questions 
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type0Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
    
    public Type0Question(Ontology ontology) {
        this.manager = ontology.getManager();
        this.ontology = ontology.getOWLOntology();
        this.factory = ontology.getFactory();
        this.reasoner = ontology.getReasoner();
        this.format = ontology.getFormat();
        this.prefixFormat = ontology.getPrefixFormat();
        this.prefixManager = ontology.getPrefixManager();
        sentence = "";
    }
    
    String generateQuestion(String template) {
        String question = "";
        
        String [] words = LiguisticHandler.wordList(template);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type0ResultSet resultSet = getValidResultSet(tokens);
        
        if (resultSet.isNull()) {
            question = "No valid equivant classes in the ontology";
        } else {
            question = LiguisticHandler.generateType0Sentence(resultSet, template);
        }
        
        return question;
    }
    
    public Type0ResultSet getResultSetQuestionMask(String templateMask) throws Exception{
       
        System.out.println("TYPE 0 : "+ templateMask);
        String [] words = LiguisticHandler.wordList(templateMask);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = PropertyType.OP_UNKNOWN;
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type0ResultSet resultSet = getValidResultSet(tokens);
         
        return resultSet;
    }

    private Type0ResultSet getValidResultSet(ArrayList<String> tokens) {
        OWLClass classX; 
        OWLClass classY;
        
        if (tokens.get(0).toLowerCase().equals("thing")){
            classX = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        }else{
            classX = factory.getOWLClass(tokens.get(0),prefixManager);
        }
        
        if (tokens.get(1).toLowerCase().equals("thing")){
            classY = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        }else{
            classY = factory.getOWLClass(tokens.get(1),prefixManager);
        }
        
        ArrayList<OWLClass> validSubclassesOfX = getValidSubclasses(classX, classY);
        
        ArrayList <OWLClassExpression> tempX = new ArrayList<OWLClassExpression>();
        ArrayList <OWLClassExpression> tempY = new ArrayList<OWLClassExpression>();
        
        Type0ResultSet validResultSet = getValidEquivalentPairs(classX, classY);
        
        if (validSubclassesOfX.isEmpty()) {
            validResultSet = getValidEquivalentPairs(classX, classY);
        } else {
            for (OWLClass subclass : validSubclassesOfX) {
                Type0ResultSet r = getValidEquivalentPairs(subclass, classY);
                
                if (!r.isNull()) {
                    // System.out.println("r:"+r);
                    tempX.add(r.getX());
                    tempY.add(r.getY());
                }
                
            }
            
            if (tempX.isEmpty()) {
                validResultSet = new Type0ResultSet();
            } else {
                Random rand = new Random();
                int index = rand.nextInt(tempX.size());
                validResultSet = new Type0ResultSet(tempX.get(index), tempY.get(index));
                //System.out.println("selected result set: "+validResultSet);
            }
            
        }
        
        return validResultSet;
        
    }

    private ArrayList<OWLClass> getValidSubclasses(OWLClass owlClassX, OWLClass owlClassY) {
        // Declare an ArrayList to store valid subclasses of classX
        ArrayList <OWLClass> results = new ArrayList<>();
        
        // Get all subclasses of owlClassY
        NodeSet<OWLClass> subclassesofY = reasoner.getSubClasses(owlClassY, false);
        
        // Get all subclasses of owlClassX
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(owlClassX, false);
        // Convert NodeSet<OWLClass> to Set<Node<OWLClass>>
        Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
        // Remove the bottom class (i.e: OWL:Nothing)
        subclassesNodes.remove(reasoner.getBottomClassNode());
        
        // For every subclass, add it to the arraylist
        for (Node<OWLClass> subclass : subclassesNodes) {
            // Each of the subclasses is also a subclass of owlClassY
            if (subclassesofY.containsEntity(subclass.getRepresentativeElement())){
            results.add(subclass.getRepresentativeElement());
            }
        }
        
        return results;
    }

    private Type0ResultSet getValidEquivalentPairs(OWLClass classX, OWLClass classY) {
        //System.out.println("checking ClassX " + classX);
        // Declare a arrayList to store all valid result set
        ArrayList <Type0ResultSet> results = new ArrayList<Type0ResultSet>();
        
        // Get the set of all equivalent classes of classX
        Set<OWLClassExpression> equivalentClasses = classX.getEquivalentClasses(ontology);
        
        
        for (OWLClassExpression equalClass : equivalentClasses) {
            if ( equalClass.getClassExpressionType().equals(ClassExpressionType.OWL_CLASS)){
                //System.out.println(" X: "+ classX +  " equal Class: "+ equalClass);
                results.add(new Type0ResultSet(classX, equalClass));
                
            }
        }
        
        if (results.isEmpty()){
            //System.out.println("valid results is empty");
            return new Type0ResultSet();
        } else {
            //System.out.println("valid results is not empty");
            return RandomSelect(results);
        }
    }

    private Type0ResultSet RandomSelect(ArrayList<Type0ResultSet> results) {
        Random rand = new Random();
        return results.get(rand.nextInt(results.size()));
    }
    
}
