/*
 * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import org.coode.owlapi.owlxmlparser.OWLClassElementHandler;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
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
public class Type1Question {
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
     * Constructor for Type 1 questions (yes/No or True/false questions that requires 2 particulars and a object relation)
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type1Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
    
    public Type1Question(Ontology ontology) {
        this.manager = ontology.getManager();
        this.ontology = ontology.getOWLOntology();
        this.factory = ontology.getFactory();
        this.reasoner = ontology.getReasoner();
        this.format = ontology.getFormat();
        this.prefixFormat = ontology.getPrefixFormat();
        this.prefixManager = ontology.getPrefixManager();
        sentence = "";
    }
    
    
    public String generateQuestion (String template) throws Exception{
        String question = "";
        
        System.out.println("TYPE 1 : "+ template);
        String [] words = LiguisticHandler.wordList(template);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = LiguisticHandler.getPropertyType(template);
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type1ResultSet resultSet = getValidResultSet(tokens, propertyType);
         
        if (resultSet.isNull()) {
            question = "No valid result set in the ontology";
        } else {
            question = LiguisticHandler.generateType1Sentence(resultSet, template);
        }
       
        return question;
    }
    public Type1ResultSet getResultSetQuestionMask(String templateMask) throws Exception{
        String question = "";
        
        System.out.println("TYPE 1 : "+ templateMask);
        String [] words = LiguisticHandler.wordList(templateMask);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = PropertyType.OP_UNKNOWN;
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type1ResultSet resultSet = getValidResultSet(tokens, propertyType);
         
        return resultSet;
    }
    
    private Type1ResultSet getValidResultSet(ArrayList<String> tokens, PropertyType propertyType) throws Exception {
        
     /*   System.out.println(" examples tokens question type 1 : ");
        for (String token : tokens) {
            System.out.println("token = "+ token);
        }*/
        
        
        
        //String [] OPsubtokens = tokens.get(1).split(":");
        OWLClass classX;
        OWLObjectPropertyExpression OP;
        OWLClass classY;
        
        //if (tokens.get(0).toLowerCase().equals("thing")){
            classX = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        //}else{
           // classX = factory.getOWLClass(tokens.get(0),prefixManager);
        //}
        
        //if (tokens.get(2).toLowerCase().equals("thing")){
            classY = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        //}else{
        //    classY = factory.getOWLClass(tokens.get(2),prefixManager);
        //}
        
        // In cases where there is not object property specified (i.e: <ObjectProperty>)
        //f (OPsubtokens.length==1){
        
        OP = reasoner.getTopObjectPropertyNode().getRepresentativeElement();
        
        // In cases where the ontology is specified (i.e: <ObjectPropery:Verb>)
        //}else{
            
            //System.out.println("\t\t\tOPsubtokens[1] >>>>> "+ OPsubtokens[1]);
           // OP = factory.getOWLObjectProperty  (OPsubtokens[1], prefixManager);
       // }
        
        //System.out.println("Tokens : classX = " + classX.toString() + " OP = " + OP.toString() + " class Y = " + classY.toString());
        
        ArrayList<OWLClass> validSubclassesOfX = getValidSubclasses(classX);
        ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
        Type1ResultSet validResultSet = new Type1ResultSet();
        
        ArrayList <OWLClassExpression> tempX = new ArrayList<OWLClassExpression>();
        ArrayList <OWLClassExpression> tempY = new ArrayList<OWLClassExpression>();
        ArrayList <OWLObjectPropertyExpression> tempOP = new ArrayList<OWLObjectPropertyExpression>();
        
        if (validSubclassesOfX.isEmpty()) {
            // Then check classX itself
            axioms = getSubclassAxioms(classX);
            validResultSet = validateAxioms(axioms,classX, OP, classY, propertyType);
            
        } else {
            // Otherwise check all subclasses

            for (OWLClass subclass  : validSubclassesOfX) {
                axioms = getSubclassAxioms(subclass);
                Type1ResultSet r = validateAxioms(axioms,subclass, OP, classY, propertyType);
                
                if (!r.isNull()) {
                    //System.out.println("r:"+r);
                    tempX.add(r.getX());
                    tempY.add(r.getY());
                    tempOP.add(r.getOP());
                }
            }
            if (tempX.isEmpty()) {
                validResultSet = new Type1ResultSet();
            } else {
                Random rand = new Random();
                int index = rand.nextInt(tempX.size());
                validResultSet = new Type1ResultSet(tempX.get(index), tempY.get(index), tempOP.get(index));
                //System.out.println("selected result set: "+validResultSet);
            }
         }
        
        return validResultSet;
    }

    private ArrayList<OWLClass> getValidSubclasses(OWLClass owlClass) {
        ArrayList <OWLClass> results = new ArrayList<>();
        
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(owlClass, false);
        Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
        subclassesNodes.remove(reasoner.getBottomClassNode());
        for (Node<OWLClass> subclass : subclassesNodes) {
            NodeSet <OWLClass> subsubclasses = reasoner.getSubClasses(owlClass, true);
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    }
    
    private ArrayList<OWLObjectPropertyExpression> getValidObjectProperty(OWLObjectPropertyExpression owlop) {
        ArrayList <OWLObjectPropertyExpression> results = new ArrayList<>();
        
        NodeSet<OWLObjectPropertyExpression> subObjectProperty = reasoner.getSubObjectProperties(owlop, false);
        Set<Node<OWLObjectPropertyExpression>> subclassesNodes = subObjectProperty.getNodes();
        subclassesNodes.remove(reasoner.getBottomObjectPropertyNode());
        for (Node<OWLObjectPropertyExpression> subclass : subclassesNodes) {
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    }

    private ArrayList<OWLClassAxiom> getSubclassAxioms(OWLClass classX) {
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        
        Set<OWLClassAxiom> allAxioms = ontology.getAxioms(classX);
        
        for (OWLClassAxiom axiom : allAxioms) {
            if (axiom.getAxiomType().equals(AxiomType.SUBCLASS_OF) && !axiom.getObjectPropertiesInSignature().isEmpty() ) {
                Set<OWLClass> classesInAxiom = axiom.getClassesInSignature();
                if (classesInAxiom.size()==2){
                    subclassAxioms.add(axiom);
                }
            }
        }
        
        return subclassAxioms;
    }

    private Type1ResultSet validateAxioms(ArrayList<OWLClassAxiom> axioms, OWLClass CurrentSubclass, OWLObjectPropertyExpression OP, OWLClass classY, PropertyType propertyType) throws Exception {
        ArrayList<Type1ResultSet> validResultSets = new ArrayList<Type1ResultSet>();
        
        ArrayList<OWLClass> subclassesOfY = getValidSubclasses(classY);
        ArrayList<OWLObjectPropertyExpression> subObjectProperty = getValidObjectProperty(OP);
        
        for (OWLClassAxiom axiom : axioms) {
            //System.out.println("Axiom : "+axiom);
            //System.out.println("is subclass of Y : "+ subclassesOfY.contains(OWLClassInSig(axiom,classX)));
            //System.out.println("is subObjectProperty : " + subObjectProperty.contains(OWLObjectProperyInSig(axiom)));
            OWLClass y = OWLClassInSig(axiom,CurrentSubclass);
            OWLObjectPropertyExpression op = OWLObjectProperyInSig(axiom);
            
            String strOP = op.toString();
            PropertyClassification pc ;
             //
            pc = new PropertyClassification(strOP);
          
            if (subclassesOfY.contains(y) && subObjectProperty.contains(op) && (pc.getPropertyType() == propertyType || PropertyType.OP_UNKNOWN == propertyType)){
                //System.out.println("valid Axiom");
                Type1ResultSet result = new Type1ResultSet(CurrentSubclass, y, op);
                //System.out.println("added result: "+result);
                validResultSets.add(result);
            }
        }
        //System.out.println("result size:" +validResultSets.size());
        if (validResultSets.isEmpty()){
            //System.out.println("valid results is empty");
            return new Type1ResultSet();
        } else {
            //System.out.println("valid results is not empty");
            return randomSelectResult(validResultSets);
        }        
    }

  
    private OWLClass OWLClassInSig(OWLClassAxiom axiom, OWLClass classX) {
        OWLClass output = null;
        Set<OWLClass> classes= axiom.getClassesInSignature();
        classes.remove(classX);
        for(OWLClass x : classes){
            output = x;
        }
        return output;
    }

    private OWLObjectPropertyExpression OWLObjectProperyInSig(OWLClassAxiom axiom) {
        OWLObjectPropertyExpression output = null;
        Set<OWLObjectProperty> ops = axiom.getObjectPropertiesInSignature();
        for(OWLObjectProperty x : ops){
            output = x;
        }
        return output;
    }

    
    
    private Type1ResultSet randomSelectResult(ArrayList<Type1ResultSet> validResultSets) {
        Random rand = new Random();
        return validResultSets.get(rand.nextInt(validResultSets.size()));
    }
    
   
}
