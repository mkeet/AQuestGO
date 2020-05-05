/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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
class Type5Question {
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
     * Constructor for Type 5 questions 
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type5Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
    
    public Type5Question(Ontology ontology) {
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
        System.out.println("TYPE 5 : "+ template);
        String [] words = LiguisticHandler.wordList(template);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type5ResultSet resultSet = getValidResultSet(tokens);
        
        if (resultSet.isNull()) {
            question = "No valid result set in the ontology";
        } else {
            question = LiguisticHandler.generateType5Sentence(resultSet, template);
        }
        
        return question;
    }

    public Type5ResultSet getResultSetQuestionMask(String templateMask) throws Exception{
       
        System.out.println("TYPE 5 : "+ templateMask);
        String [] words = LiguisticHandler.wordList(templateMask);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = PropertyType.OP_UNKNOWN;
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type5ResultSet resultSet = getValidResultSet(tokens);
        return resultSet;
    }
    
    private Type5ResultSet getValidResultSet(ArrayList<String> tokens) {
        String [] OPsubtokens = tokens.get(1).split(":");
        OWLClass classX;
        OWLObjectPropertyExpression OP;
        OWLClass classY;
        
        if (tokens.get(0).toLowerCase().equals("thing")){
            classX = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        }else{
            classX = factory.getOWLClass(tokens.get(0),prefixManager);
        }
        
        // In cases where there is not object property specified (i.e: <ObjectProperty>)
        if (OPsubtokens.length==1){
            OP = reasoner.getTopObjectPropertyNode().getRepresentativeElement();
        // In cases where the ontology is specified (i.e: <ObjectPropery:Verb>)
        }else{
            OP = factory.getOWLObjectProperty(OPsubtokens[1], prefixManager);
        }
        
        //System.out.println("Tokens : classX = " + classX.toString() + " OP = " + OP.toString() + " class Y = " + classY.toString());
        
        ArrayList<OWLClass> validSubclassesOfX = getValidSubclasses(classX);
        ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
        Type5ResultSet validResultSet = new Type5ResultSet();
        
        ArrayList <OWLClassExpression> tempX = new ArrayList<OWLClassExpression>();
        ArrayList <OWLObjectPropertyExpression> tempOP = new ArrayList<OWLObjectPropertyExpression>();
        Type5ResultSet r = new Type5ResultSet();
        
        if (!validSubclassesOfX.isEmpty()) {
            for (OWLClass subclass  : validSubclassesOfX) {
                axioms = getSubclassAxioms(subclass);
                    
                if (!axioms.isEmpty()){
                    r = validateAxioms(axioms,subclass, OP);
                }

                if (!r.isNull()) {
                    //System.out.println("r:"+r);
                        tempX.add(r.getX());
                        tempOP.add(r.getOP());
                    }
                
            }
            if (tempX.isEmpty()) {
                validResultSet = new Type5ResultSet();
            } else {
                Random rand = new Random();
                int index = rand.nextInt(tempX.size());
                validResultSet = new Type5ResultSet(tempX.get(index), tempOP.get(index));
                //System.out.println("selected result set: "+validResultSet);
            }
         }
        
        return validResultSet;
    }
    
    private ArrayList<OWLClass> getValidSubclasses(OWLClass owlClass) {
        // Declare an empty arrayList to store all subclasses
        ArrayList <OWLClass> results = new ArrayList<>();
        
        // Get all subclasses of owlClass
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(owlClass, false);
        // Convert NodeSet<OWLClass> to Set<Node<OWLClass>>
        Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
        // Remove the bottom node (i.e: OWL:Nothing)
        subclassesNodes.remove(reasoner.getBottomClassNode());
        
        // For all subclasses make sure that the subclass is not an end node
        for (Node<OWLClass> subclass : subclassesNodes) {
            NodeSet<OWLClass> subsubclasses = reasoner.getSubClasses(subclass.getRepresentativeElement(), true);
            if (!subsubclasses.isEmpty()){
                results.add(subclass.getRepresentativeElement());
            }
        }
        
        // Return the set of valid subclasses
        return results;
    }

    private ArrayList<OWLClassAxiom> getSubclassAxioms(OWLClass classX) {
        // Declare a ArrayList to store all valid axiom 
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        
        // Get all axioms the related to classX 
        Set<OWLClassAxiom> allAxioms = ontology.getAxioms(classX);
        
        // For each of the axioms
        for (OWLClassAxiom axiom : allAxioms) {
            // If it is a subclass_of type axiom and has a object propety in the axiom 
            //(this is because we don't an axiom such as lion is a subclass of animal where no object property is involved)
            if (axiom.getAxiomType().equals(AxiomType.SUBCLASS_OF) && !axiom.getObjectPropertiesInSignature().isEmpty() ) {
                //  if the axiom contains only the domain and the one single range of object property
                // (axiom such as giaff eats only leaf or twig is for future work)
                Set<OWLClass> classesInAxiom = axiom.getClassesInSignature();
                if (classesInAxiom.size()==2){
                    subclassAxioms.add(axiom);
                }
            }
        }
        
        // Return the set of valid axioms relating to classX
        return subclassAxioms;
    }
    
    private Type5ResultSet validateAxioms(ArrayList<OWLClassAxiom> axioms, OWLClass CurrentSubclass, OWLObjectPropertyExpression OP) {
        ArrayList<Type5ResultSet> validResultSets = new ArrayList<Type5ResultSet>();
        
        ArrayList<OWLObjectPropertyExpression> subObjectProperty = getValidObjectProperty(OP);
        
        for (OWLClassAxiom axiom : axioms) {
            //System.out.println("Axiom : "+axiom);
            //System.out.println("is subclass of Y : "+ subclassesOfY.contains(OWLClassInSig(axiom,classX)));
            //System.out.println("is subObjectProperty : " + subObjectProperty.contains(OWLObjectProperyInSig(axiom)));
            OWLObjectPropertyExpression op = OWLObjectProperyInSig(axiom);
            
            if (subObjectProperty.contains(op)){
                //System.out.println("valid Axiom");
                Type5ResultSet result = new Type5ResultSet(CurrentSubclass, op);
                //System.out.println("added result: "+result);
                validResultSets.add(result);
            }
        }
        //System.out.println("result size:" +validResultSets.size());
        if (validResultSets.isEmpty()){
            //System.out.println("valid results is empty");
            return new Type5ResultSet();
        } else {
            //System.out.println("valid results is not empty");
            return randomSelectResult(validResultSets);
        }
        
        
    }
    
    private ArrayList<OWLObjectPropertyExpression> getValidObjectProperty(OWLObjectPropertyExpression owlop) {
        // Declare an ArrayList of OWLObjectPropertyExprssion to store all valid object property
        ArrayList <OWLObjectPropertyExpression> results = new ArrayList<>();
        
        // Get all sub-objectProperty of owlop
        NodeSet<OWLObjectPropertyExpression> subObjectProperty = reasoner.getSubObjectProperties(owlop, false);
        // Convert NodeSet<OWLObjectPropertyExpression> to Node<Set<OWLObjectPropertyExpression>>
        Set<Node<OWLObjectPropertyExpression>> subclassesNodes = subObjectProperty.getNodes();
        // Remove bottom object property (i.e: OWL:Nothing)
        subclassesNodes.remove(reasoner.getBottomObjectPropertyNode());
        
        // Add remaining object properties to the arraylist
        for (Node<OWLObjectPropertyExpression> subclass : subclassesNodes) {
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    } 
    
    private OWLObjectPropertyExpression OWLObjectProperyInSig(OWLClassAxiom axiom) {
        // Declare and initiate output 
        OWLObjectPropertyExpression output = null;
        
        // Get all object property related to the axiom (Unsually only one object property)
        Set<OWLObjectProperty> ops = axiom.getObjectPropertiesInSignature();
        
        // Set the output to the first object property in the set
        for(OWLObjectProperty x : ops){
            output = x;
        }
        
        return output;
    }

    private Type5ResultSet randomSelectResult(ArrayList<Type5ResultSet> validResultSets) {
        Random rand = new Random();
        return validResultSets.get(rand.nextInt(validResultSets.size()));
    }
    
}
