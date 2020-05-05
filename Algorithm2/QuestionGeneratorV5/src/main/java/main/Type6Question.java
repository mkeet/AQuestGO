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
import org.semanticweb.owlapi.model.ClassExpressionType;
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
class Type6Question {
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
     * Constructor for Type 6 questions 
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type6Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
     public Type6Question(Ontology ontology) {
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
        System.out.println("TYPE 6 : "+ template);
        String [] words = LiguisticHandler.wordList(template);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = LiguisticHandler.getPropertyType(template);
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type3ResultSet resultSet = getValidResultSet(tokens, propertyType);
        
        if (resultSet.isNull()) {
            question = "No valid result set in the ontology";
        } else {
            question = LiguisticHandler.generateType3Sentence(resultSet, template);
        }
        
        return question;
    }
    public Type3ResultSet getResultSetQuestionMask(String templateMask) throws Exception{
       
        System.out.println("TYPE 6 : "+ templateMask);
        String [] words = LiguisticHandler.wordList(templateMask);
        
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = PropertyType.OP_UNKNOWN;
        
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        Type3ResultSet resultSet = getValidResultSet(tokens, propertyType);
         
        return resultSet;
    }
    private Type3ResultSet getValidResultSet(ArrayList<String> tokens, PropertyType propertyType) {
                
        QuantifierType quantifierType;
        String quantifier = tokens.get(2);
        if (quantifier.compareTo("Quantifier")==0) {
            int indice = (int) Math.round( Math.random() );
            switch (indice) {
                case 0: quantifierType = QuantifierType.SOME;
                break;
                default: quantifierType = QuantifierType.ONLY;
                break;
            }
        }
        else if (quantifier.compareTo("Quantifier-some")==0)
            quantifierType = QuantifierType.SOME;
        else quantifierType = QuantifierType.ONLY;
        
        String [] OPsubtokens = tokens.get(1).split(":");
        OWLClass classX;
        OWLObjectPropertyExpression OP;
        OWLClass classY;
        
       // if (tokens.get(0).toLowerCase().equals("thing")){
            classX = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        //}else{
        //    classX = factory.getOWLClass(tokens.get(0),prefixManager);
        //}
        
        //if (tokens.get(3).toLowerCase().equals("thing")){
            classY = reasoner.getTopClassNode().getRepresentativeElement();
        // For cases where the token is any other node in the ontology
        //}else{
        //    classY = factory.getOWLClass(tokens.get(3),prefixManager);
        // }
        
        // In cases where there is not object property specified (i.e: <ObjectProperty>)
        //if (OPsubtokens.length==1){
            OP = reasoner.getTopObjectPropertyNode().getRepresentativeElement();
        // In cases where the ontology is specified (i.e: <ObjectPropery:Verb>)
        //}else{
        //    OP = factory.getOWLObjectProperty(OPsubtokens[1], prefixManager);
        //}
        
        //System.out.println("Tokens : classX = " + classX.toString() + " OP = " + OP.toString() + " class Y = " + classY.toString());
        
        ArrayList<OWLClass> validSubclassesOfX = getValidSubclasses(classX);
        ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
        Type3ResultSet validResultSet = new Type3ResultSet();
        
        ArrayList <OWLClassExpression> tempX = new ArrayList<OWLClassExpression>();
        ArrayList <OWLClassExpression> tempY = new ArrayList<OWLClassExpression>();
        ArrayList <OWLObjectPropertyExpression> tempOP = new ArrayList<OWLObjectPropertyExpression>();
        ArrayList <String> tempQuant = new ArrayList<String>();
        Type3ResultSet r = new Type3ResultSet();
        
        if (!validSubclassesOfX.isEmpty()) {
            for (OWLClass superClass  : validSubclassesOfX) {
                for (Node<OWLClass> subClass: reasoner.getSubClasses(superClass, true)){
                    axioms = getSubclassAxioms(subClass.getRepresentativeElement());
                    
                    if (!axioms.isEmpty()){
                        r = validateAxioms(axioms,superClass,subClass.getRepresentativeElement(), OP, classY, quantifierType, propertyType);
                    }

                    if (!r.isNull()) {
                        //System.out.println("r:"+r);
                        tempX.add(r.getX());
                        tempY.add(r.getY());
                        tempOP.add(r.getOP());
                        tempQuant.add(r.getQuantifier());
                    }
                }
            }
            if (tempX.isEmpty()) {
                validResultSet = new Type3ResultSet();
            } else {
                Random rand = new Random();
                int index = rand.nextInt(tempX.size());
                validResultSet = new Type3ResultSet(tempX.get(index), tempY.get(index) ,tempOP.get(index) , tempQuant.get(index));
                //System.out.println("selected result set: "+validResultSet);
            }
         }
        
        return validResultSet;
    }

    private ArrayList<OWLClass> getValidSubclasses(OWLClass owlClass) {
        // Declare an ArrayList to store valid subclasses of classX
        ArrayList<OWLClass> results = new ArrayList<>();
        
        // Get all subclasses of owlClass
        NodeSet<OWLClass> subclasses = reasoner.getSubClasses(owlClass, false);
        // Convert NodeSet<OWLClass> to Set<Node<OWLClass>>
        Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
        // Remove the bottom class (i.e: OWL:Nothing)
        subclassesNodes.remove(reasoner.getBottomClassNode());
        
        // For every subclass, add it to the arraylist
        for (Node<OWLClass> subclass : subclassesNodes) {
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    }

    private ArrayList<OWLClassAxiom> getSubclassAxioms(OWLClass classX) {
        // Declare an arraylist to store all valid axioms 
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        
        // Get all axioms that involves classX
        Set<OWLClassAxiom> allAxioms = ontology.getAxioms(classX);
        
        // For each of the axiom 
        for (OWLClassAxiom axiom : allAxioms) {
            // Check if the axiom is a subclass axiom and that it have one object proprerty 
            // (this is because we don't subclass axioms such as lion is a subclass of animal)
            if (axiom.getAxiomType().equals(AxiomType.SUBCLASS_OF) && !axiom.getObjectPropertiesInSignature().isEmpty() ) {
                Set<OWLClass> classesInAxiom = axiom.getClassesInSignature();
                if (classesInAxiom.size()==2){
                    subclassAxioms.add(axiom);
                }
            }
        }
        
        return subclassAxioms;
    }

    private Type3ResultSet validateAxioms(ArrayList<OWLClassAxiom> axioms, OWLClass CurrentsuperClass, OWLClass CurrentSubclass, OWLObjectPropertyExpression OP, OWLClass classY, QuantifierType quantifierType, PropertyType propertyType) {
        // Declare an arrayList to store all valid result set
        ArrayList<Type3ResultSet> validResultSets = new ArrayList<Type3ResultSet>();
        
        // Get all subclasses of class Y
        ArrayList<OWLClass> subclassesOfY = getValidSubclasses(classY);
        // Get all valid sub-object property of OP
        ArrayList<OWLObjectPropertyExpression> subObjectProperty = getValidObjectProperty(OP);
        
        // For every axiom
        for (OWLClassAxiom axiom : axioms) {
            //System.out.println("Axiom : "+axiom);
            //System.out.println("is subclass of Y : "+ subclassesOfY.contains(OWLClassInSig(axiom,CurrentSubclass)));
            //System.out.println("is subObjectProperty : " + subObjectProperty.contains(OWLObjectProperyInSig(axiom)));
            OWLClass y = OWLClassInSig(axiom,CurrentSubclass);
            OWLObjectPropertyExpression op = OWLObjectProperyInSig(axiom);
            
            String strOP = op.toString();
            PropertyClassification pc ;
             //
            pc = new PropertyClassification(strOP);
            
            if (subclassesOfY.contains(y) && subObjectProperty.contains(op) &&  (pc.getPropertyType() == propertyType || PropertyType.OP_UNKNOWN == propertyType)){
                for (OWLClassExpression Class : axiom.getNestedClassExpressions()) {
                    if (quantifierType == QuantifierType.ONLY && Class.getClassExpressionType().equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM)) {
                        validResultSets.add(new Type3ResultSet(CurrentsuperClass, y, op, "only"));
                        break;
                    }
                    if (quantifierType == QuantifierType.SOME && Class.getClassExpressionType().equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM)){
                        validResultSets.add(new Type3ResultSet(CurrentsuperClass, y, op, "some"));
                        break;
                    }
                }
            }
        }
        //System.out.println("result size:" +validResultSets.size());
        if (validResultSets.isEmpty()){
            //System.out.println("valid results is empty");
            return new Type3ResultSet();
        } else {
            //System.out.println("valid results is not empty");
            return randomSelectResult(validResultSets);
        }
    }

    private ArrayList<OWLObjectPropertyExpression> getValidObjectProperty(OWLObjectPropertyExpression owlop) {
        // Declare an arrayList to store all valid sub-object properties of OP
        ArrayList <OWLObjectPropertyExpression> results = new ArrayList<>();
        
        // Get the set if all sub-opject property of owlop
        NodeSet<OWLObjectPropertyExpression> subObjectProperty = reasoner.getSubObjectProperties(owlop, false);
        // Convert NodeSet<OWLObjectPropertyExpression> to Set<Node<OWLObjectPropertyExpression>>
        Set<Node<OWLObjectPropertyExpression>> subclassesNodes = subObjectProperty.getNodes();
        // Remove the bottom object property (i.e: OWL:Nothing)
        subclassesNodes.remove(reasoner.getBottomObjectPropertyNode());
        
        // For evary subclass add it to the arraylist
        for (Node<OWLObjectPropertyExpression> subclass : subclassesNodes) {
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    }
    
    private OWLClass OWLClassInSig(OWLClassAxiom axiom, OWLClass classX) {
        // Declare and initiate the output 
        OWLClass output = null;
        
        // Get a set of the classes associated witht the axiom 
        Set<OWLClass> classes= axiom.getClassesInSignature();
        // Remove the domain class
        classes.remove(classX);
        // Set the output to the range class
        for(OWLClass x : classes){
            output = x;
        }
        
        return output;
    }

    private OWLObjectPropertyExpression OWLObjectProperyInSig(OWLClassAxiom axiom) {
        // Delcase and intiate the output 
        OWLObjectPropertyExpression output = null;
        
        // get a set of all object property associated with the axiom (Usally only one axiom)
        Set<OWLObjectProperty> ops = axiom.getObjectPropertiesInSignature();
        // Set the output to first object property 
        for(OWLObjectProperty x : ops){
            output = x;
        }
        return output;
    }

    private Type3ResultSet randomSelectResult(ArrayList<Type3ResultSet> validResultSets) {
        Random rand = new Random();
        return validResultSets.get(rand.nextInt(validResultSets.size()));
    }
    
}
