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
public class Type3Question {
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
     * Constructor for Type 3 questions 
     * @param manager
     * @param ontology
     * @param factory
     * @param reasoner
     * @param format
     * @param prefixFormat
     * @param prefixManager 
     */
    public Type3Question(OWLOntologyManager manager, OWLOntology ontology,OWLDataFactory factory,OWLReasoner reasoner,OWLOntologyFormat format, PrefixOWLOntologyFormat prefixFormat,PrefixManager prefixManager){
        this.manager = manager;
        this.ontology = ontology;
        this.factory = factory;
        this.reasoner = reasoner;
        this.format = format;
        this.prefixFormat = prefixFormat;
        this.prefixManager = prefixManager;
        sentence = "";
    }
    
    public Type3Question(Ontology ontology) {
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
        System.out.println("TYPE 3 : "+ template);
        String [] words = LiguisticHandler.wordList(template);
        
        try {
        ArrayList<String> tokens = LiguisticHandler.getTokens(words);
        
        PropertyType propertyType = LiguisticHandler.getPropertyType(template);
        // tokens are in the format [OWLClass, OWLObjectProperty, OWLClass]
        
        Type3ResultSet resultSet = getValidResultSet(tokens, propertyType);
        
        if (resultSet.isNull()) {
            question = "No valid result set in the ontology";
        } else {
            question = LiguisticHandler.generateType3Sentence(resultSet, template);
        }
        }
        catch (Exception e) {
                
                }
        return question;
    }
    public Type3ResultSet getResultSetQuestionMask(String templateMask) throws Exception{
       
        System.out.println("TYPE 3 : "+ templateMask);
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
            
            if (propertyType == PropertyType.OP_VERB)
            {
                System.out.println("[AAAA] ---> indice = "+indice);
                indice = 1;
            }
            else System.out.println("[AAAI] ---> indice = "+indice+" iiii -> "+propertyType);
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
        
        //System.out.println("Question type 3 -> ");
        for (int i = 0; i < tokens.size(); i++) {
          //  System.out.println("OPsubtokens -> "+tokens.get(i)+" "+tokens.size());
        }
        //System.out.println("");
        OWLClass classX;
        OWLObjectPropertyExpression OP;
        OWLClass classY;
        
        //if (tokens.get(0).toLowerCase().equals("thing")){
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
        //}
        
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
        
        if (validSubclassesOfX.isEmpty()) {
            // Then check classX itself
            axioms = getSubclassAxioms(classX);
            validResultSet = validateAxioms(axioms,classX, OP, classY, quantifierType, propertyType);
        }  else {
            // Otherwise check all subclasses

            for (OWLClass subclass  : validSubclassesOfX) {
                axioms = getSubclassAxioms(subclass);
                Type3ResultSet r = validateAxioms(axioms,subclass, OP, classY, quantifierType, propertyType);
                
                if (quantifierType == QuantifierType.ONLY)
                    System.out.println("[iii] --> X:"+subclass+" op ="+ OP.toString()+ " Y: "+classY+ "rNull?"+r.isNull() + " axioms "+axioms.toString());
                if (!r.isNull()) {
                    //System.out.println("r:"+r);
                    tempX.add(r.getX());
                    tempY.add(r.getY());
                    tempOP.add(r.getOP());
                    tempQuant.add(r.getQuantifier());
                    
                    Type3ResultSet tempType3 = new Type3ResultSet(r.getX(), r.getY(), r.getOP(), r.getQuantifier());
                    System.out.println("resultSet3QuestionMaskAll = "+ tempType3.toString());
                }
            }
            if (tempX.isEmpty()) {
                validResultSet = new Type3ResultSet();
            } else {
                Random rand = new Random();
                int index = rand.nextInt(tempX.size());
                validResultSet = new Type3ResultSet(tempX.get(index), tempY.get(index), tempOP.get(index), tempQuant.get(index));
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
    
    private Type3ResultSet validateAxioms(ArrayList<OWLClassAxiom> axioms, OWLClass CurrentSubclass, OWLObjectPropertyExpression OP, OWLClass classY, QuantifierType quantifierType, PropertyType propertyType) {
        ArrayList<Type3ResultSet> validResultSets = new ArrayList<Type3ResultSet>();
        
        ArrayList<OWLClass> subclassesOfY = getValidSubclasses(classY);
        ArrayList<OWLObjectPropertyExpression> subObjectProperty = getValidObjectProperty(OP);
        
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
            if (pc.getFormattedProperty().equals("eats"))
                System.out.println(">>EATS "+axiom.toString());
                    
            if (subclassesOfY.contains(y) && subObjectProperty.contains(op) &&  (pc.getPropertyType() == propertyType || PropertyType.OP_UNKNOWN == propertyType)){
                for (OWLClassExpression Class : axiom.getNestedClassExpressions()) {
                    System.out.println("nestedClass = "+Class.toString()+" "+quantifierType+ " "+Class.getClassExpressionType().toString());
                    if (/*quantifierType ==QuantifierType.ONLY &&*/ Class.getClassExpressionType().equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM)) {
                        Type3ResultSet type3Res = new Type3ResultSet(CurrentSubclass, y, op, "only");
                        System.out.println("type3Res = "+type3Res.toString()+" validResultsSetsCount (avant) = "+validResultSets.size());
                        validResultSets.add(new Type3ResultSet(CurrentSubclass, y, op, "only"));
                        for (int i = 0; i < validResultSets.size(); i++) {
                             System.out.println("validResult_"+i+" (non official): "+ validResultSets.get(i));
                        }
                        
                    } 
                    if (/*quantifierType ==QuantifierType.SOME &&*/ Class.getClassExpressionType().equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM)){
                        validResultSets.add(new Type3ResultSet(CurrentSubclass, y, op, "some"));
                        
                    }
                }
            }
            System.out.println("validResultsSetsCount (avant2) = "+validResultSets.size());
            
        }
        System.out.println("result size:" +validResultSets.size());
        if (validResultSets.isEmpty()){
            //System.out.println("valid results is empty");
            return new Type3ResultSet();
        } else {
            
            for (int i = 0; i < validResultSets.size(); i++) {
                System.out.println("validResult_"+i+" : "+ validResultSets.get(i));
            }
            //System.out.println("valid results is not empty");
            Type3ResultSet resSelected = randomSelectResult(validResultSets);
            System.out.println("selected = "+resSelected.toString());
            return resSelected;
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
    
    private Type3ResultSet randomSelectResult(ArrayList<Type3ResultSet> validResultSets) {
        Random rand = new Random();
        return validResultSets.get(rand.nextInt(validResultSets.size()));
    }
    
}
