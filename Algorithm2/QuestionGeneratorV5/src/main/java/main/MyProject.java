/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import static org.semanticweb.owlapi.vocab.OWLFacet.MAX_EXCLUSIVE;
import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 *
 * @author toky.raboanary
 */
public class MyProject{
    private  static OWLOntologyManager manager;
    private  static OWLOntology ontology;
    private  static OWLDataFactory factory;
    private  static OWLReasoner reasoner;
    private  static OWLOntologyFormat format; 
    private  static PrefixOWLOntologyFormat prefixFormat;
    private  static PrefixManager prefixManager;
    private static Boolean consistent;
    
    
    private static final String KOALA = "<?xml version=\"1.0\"?>\n"
        + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns=\"http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#\" xml:base=\"http://protege.stanford.edu/plugins/owl/owl-library/koala.owl\">\n"
        + "  <owl:Ontology rdf:about=\"\"/>\n"
        + "  <owl:Class rdf:ID=\"Female\"><owl:equivalentClass><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty><owl:hasValue><Gender rdf:ID=\"female\"/></owl:hasValue></owl:Restriction></owl:equivalentClass></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Marsupials\"><owl:disjointWith><owl:Class rdf:about=\"#Person\"/></owl:disjointWith><rdfs:subClassOf><owl:Class rdf:about=\"#Animal\"/></rdfs:subClassOf></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Student\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Person\"/><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">true</owl:hasValue></owl:Restriction><owl:Restriction><owl:someValuesFrom><owl:Class rdf:about=\"#University\"/></owl:someValuesFrom><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"KoalaWithPhD\"><owl:versionInfo>1.2</owl:versionInfo><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Restriction><owl:hasValue><Degree rdf:ID=\"PhD\"/></owl:hasValue><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasDegree\"/></owl:onProperty></owl:Restriction><owl:Class rdf:about=\"#Koala\"/></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"University\"><rdfs:subClassOf><owl:Class rdf:ID=\"Habitat\"/></rdfs:subClassOf></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Koala\"><rdfs:subClassOf><owl:Restriction><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">false</owl:hasValue><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf><owl:Restriction><owl:someValuesFrom><owl:Class rdf:about=\"#DryEucalyptForest\"/></owl:someValuesFrom><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Animal\"><rdfs:seeAlso>Male</rdfs:seeAlso><rdfs:subClassOf><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty><owl:minCardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:minCardinality></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf><owl:Restriction><owl:cardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:cardinality><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><owl:versionInfo>1.1</owl:versionInfo></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Forest\"><rdfs:subClassOf rdf:resource=\"#Habitat\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Rainforest\"><rdfs:subClassOf rdf:resource=\"#Forest\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"GraduateStudent\"><rdfs:subClassOf><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasDegree\"/></owl:onProperty><owl:someValuesFrom><owl:Class><owl:oneOf rdf:parseType=\"Collection\"><Degree rdf:ID=\"BA\"/><Degree rdf:ID=\"BS\"/></owl:oneOf></owl:Class></owl:someValuesFrom></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Student\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Parent\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Animal\"/><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty><owl:minCardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:minCardinality></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass><rdfs:subClassOf rdf:resource=\"#Animal\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"DryEucalyptForest\"><rdfs:subClassOf rdf:resource=\"#Forest\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Quokka\"><rdfs:subClassOf><owl:Restriction><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">true</owl:hasValue><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"TasmanianDevil\"><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"MaleStudentWith3Daughters\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Student\"/><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty><owl:hasValue><Gender rdf:ID=\"male\"/></owl:hasValue></owl:Restriction><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty><owl:cardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">3</owl:cardinality></owl:Restriction><owl:Restriction><owl:allValuesFrom rdf:resource=\"#Female\"/><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Degree\"/>\n  <owl:Class rdf:ID=\"Gender\"/>\n"
        + "  <owl:Class rdf:ID=\"Male\"><owl:equivalentClass><owl:Restriction><owl:hasValue rdf:resource=\"#male\"/><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty></owl:Restriction></owl:equivalentClass></owl:Class>\n"
        + "  <owl:Class rdf:ID=\"Person\"><rdfs:subClassOf rdf:resource=\"#Animal\"/><owl:disjointWith rdf:resource=\"#Marsupials\"/></owl:Class>\n"
        + "  <owl:ObjectProperty rdf:ID=\"hasHabitat\"><rdfs:range rdf:resource=\"#Habitat\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:ObjectProperty>\n"
        + "  <owl:ObjectProperty rdf:ID=\"hasDegree\"><rdfs:domain rdf:resource=\"#Person\"/><rdfs:range rdf:resource=\"#Degree\"/></owl:ObjectProperty>\n"
        + "  <owl:ObjectProperty rdf:ID=\"hasChildren\"><rdfs:range rdf:resource=\"#Animal\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:ObjectProperty>\n"
        + "  <owl:FunctionalProperty rdf:ID=\"hasGender\"><rdfs:range rdf:resource=\"#Gender\"/><rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#ObjectProperty\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:FunctionalProperty>\n"
        + "  <owl:FunctionalProperty rdf:ID=\"isHardWorking\"><rdfs:range rdf:resource=\"http://www.w3.org/2001/XMLSchema#boolean\"/><rdfs:domain rdf:resource=\"#Person\"/><rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#DatatypeProperty\"/></owl:FunctionalProperty>\n"
        + "  <Degree rdf:ID=\"MA\"/>\n</rdf:RDF>";
    
    
    public static OWLOntology load( OWLOntologyManager manager) throws OWLOntologyCreationException {
        // in this test, the ontology is loaded from a string
        return manager.loadOntologyFromOntologyDocument(new StringDocumentSource(KOALA));
    }
    public static OWLOntology loadCorrected( OWLOntologyManager manager) throws OWLOntologyCreationException {
        // in this test, the ontology is loaded from a string
        
        String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/koala_corrected.owl";
        File ontologyFile = new File(ontologyPath);
        return manager.loadOntologyFromOntologyDocument(ontologyFile);
    }
    
    public static void Init(File QuestionTemplate, File OntologyFile) throws OWLOntologyCreationException, FileNotFoundException  {
        MyProject.manager = OWLManager.createOWLOntologyManager();
        MyProject.ontology = manager.loadOntologyFromOntologyDocument(OntologyFile);
        MyProject.factory = manager.getOWLDataFactory();
        MyProject.reasoner = new Reasoner(ontology);
        MyProject.format = manager.getOntologyFormat(ontology);
        MyProject.prefixFormat = format.asPrefixOWLOntologyFormat();
        MyProject.prefixManager = new DefaultPrefixManager(prefixFormat);
        reasoner.precomputeInferences();
        MyProject.consistent = MyProject.reasoner.isConsistent();
    }
    
    public static void Init() throws OWLOntologyCreationException, FileNotFoundException  {
        String questionTemplatePath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/Templates.txt";
        File questionTemplateFile = new File(questionTemplatePath);
        String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology1.owl";
        File ontologyFile = new File(ontologyPath);
        Init(questionTemplateFile, ontologyFile);
    }
    
    public static void Test() {
        
        OWLLiteral literal = factory.getOWLLiteral("toto");
        
        // ketrika property
        System.out.println("------------ OWL Class -------------");
        OWLClass animalClass = factory.getOWLClass("animal", prefixManager);

        System.out.println("animal = "+animalClass.toString());
        System.out.println("animal = "+animalClass.getIRI().toString());
        
        NodeSet<OWLClass> subClses = reasoner.getSubClasses(animalClass, true); // tokony asiana recursivité
        Set<OWLClass> clses = subClses.getFlattened(); // 
        for (OWLClass cls : clses) {
            System.out.println("Mother Node: " + cls);
            // reasoner.getDisjointClasses(cls) ilaina amin'ilay fanamboarana No questions
               //ilaina amin'ilay mijery parent
            
            NodeSet<OWLClass> subClses2 = reasoner.getSubClasses(cls, true);
            
            Set<OWLClass> clses2 = subClses2.getFlattened();
            for (OWLClass cls2 : clses2) {
                System.out.println("\t\t" + cls2);
            }
        }
        
        OWLClass lionClass = factory.getOWLClass("lion", prefixManager);
        NodeSet<OWLClass> parents =reasoner.getSuperClasses(lionClass, false);
        Set<OWLClass> clsesParents = subClses.getFlattened();
        for (OWLClass cls : clsesParents) {
            System.out.println("Parents Node (LION): " + cls);
        }
        
        System.out.println("prefixManager = "+prefixManager.toString());
        
        // ketrika property
        System.out.println("------------ Object Property -------------"); // tokony asiana recursivité
        OWLObjectProperty objProp =  factory.getOWLObjectProperty("Verb", prefixManager);
        System.out.println("<Verb>");
        Set<OWLObjectPropertyExpression> objProps = objProp.getSubProperties(ontology);
        
        System.out.println("ketrika : "+objProps.size());
        
        for(OWLObjectPropertyExpression prop : objProps) {
            System.out.println("Sub prop : "+prop.toString());
            Set<OWLClassExpression> expressions = prop.getRanges(ontology);
            
            for(OWLClassExpression expression: expressions) {
                System.out.println("\tRange Expression : "+expression.toString());
            }
        }
        
        // axiom manipulation : 
        OWLClass clsA = factory.getOWLClass("A", prefixManager);
        OWLClass clsB = factory.getOWLClass("B", prefixManager);
        
        System.out.println(clsA.toString());
        
        
        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsA, clsB);
        // We now add the axiom to the ontology, so that the ontology states
        // that A is a subclass of B. To do this we create an AddAxiom change
        // object. At this stage neither classes A or B, or the axiom are
        // contained in the ontology. We have to add the axiom to the ontology.
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        // We now use the manager to apply the change
        manager.applyChange(addAxiom);
        
        String ontologyPath2 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology2_save.owl";
        File ontologyFile2 = new File(ontologyPath2);
         IRI destination = IRI.create(ontologyFile2);
        
       
        try {
            manager.saveOntology(ontology, destination);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(MyProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            // createPropertyAssertions
            shouldCreatePropertyAssertions();
            System.out.println("Vita");
        } catch (Exception ex) {
            Logger.getLogger(MyProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void shouldCreatePropertyAssertions() throws Exception {
        // We can specify the properties of an individual using property
        // assertions Get a copy of an ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        String ontologyPath2 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/ketrika_creation1.owl";
        File ontologyFile2 = new File(ontologyPath2);
         IRI destination = IRI.create(ontologyFile2);
         
         
        IRI ontologyIRI = IRI.create("/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/");
        OWLOntology ontology = manager.createOntology(destination);
        // Get hold of a data factory from the manager and set up a prefix
        // manager to make things easier
        OWLDataFactory factory = manager.getOWLDataFactory();
        PrefixManager pm = new DefaultPrefixManager(ontologyIRI.toString());
        // Let's specify the :John has a wife :Mary Get hold of the necessary
        
        
        // class creation
        OWLClass person = factory.getOWLClass("Person", pm);
        OWLClass student = factory.getOWLClass("Student", pm);
        
         OWLAxiom axiom = factory.getOWLSubClassOfAxiom(student, person);
        // We now add the axiom to the ontology, so that the ontology states
        // that A is a subclass of B. To do this we create an AddAxiom change
        // object. At this stage neither classes A or B, or the axiom are
        // contained in the ontology. We have to add the axiom to the ontology.
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        // We now use the manager to apply the change
        manager.applyChange(addAxiom);
        
        
        // individuals and object property
        OWLNamedIndividual john = factory.getOWLNamedIndividual("John", pm);
        OWLNamedIndividual mary = factory.getOWLNamedIndividual("Mary", pm);
        
        // specifying Mary as a Person (Axiom)
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(person, mary);
        
        // updating the ontology by inserting the corresponding axiom
        manager.addAxiom(ontology, classAssertion);
        
        OWLObjectProperty hasWife = factory.getOWLObjectProperty("hasWife", pm);
        
        
        // To specify that :John is related to :Mary via the :hasWife property
        // we create an object property assertion and add it to the ontology
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(hasWife, john,
            mary);
        manager.addAxiom(ontology, propertyAssertion);
        // Now let's specify that :John is aged 51. Get hold of a data property
        // called :hasAge
        OWLDataProperty hasAge = factory.getOWLDataProperty("hasAge", pm);
        // To specify that :John has an age of 51 we create a data property
        // assertion and add it to the ontology
        OWLDataPropertyAssertionAxiom dataPropertyAssertion = factory.getOWLDataPropertyAssertionAxiom(hasAge, john,
            51);
        manager.addAxiom(ontology, dataPropertyAssertion);
        // Note that the above is a shortcut for creating a typed literal and
        // specifying this typed literal as the value of the property assertion.
        // That is, Get hold of the xsd:integer datatype
        OWLDatatype integerDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
        // Create a typed literal. We type the literal "51" with the datatype
        OWLLiteral literal = factory.getOWLLiteral("51", integerDatatype);
        // Create the property assertion and add it to the ontology
        OWLAxiom ax = factory.getOWLDataPropertyAssertionAxiom(hasAge, john, literal);
        manager.addAxiom(ontology, ax);
        
        
        // Restrictions with method (property) 
        // subclass of property
        
        OWLObjectProperty hasPart = factory.getOWLObjectProperty("hasPart",pm);
        OWLClass nose = factory.getOWLClass("Nose",pm);
        // Now create a restriction to describe the class of individuals that
        // have at least one part that is a kind of nose
        OWLClassExpression hasPartSomeNose = factory.getOWLObjectSomeValuesFrom(hasPart, nose);
        // Obtain a reference to the Head class so that we can specify that
        // Heads have noses
        OWLClass head = factory.getOWLClass("Head", pm);
        // We now want to state that Head is a subclass of hasPart some Nose, to
        // do this we create a subclass axiom, with head as the subclass and
        // "hasPart some Nose" as the superclass (remember, restrictions are
        // also classes - they describe classes of individuals -- they are
        // anonymous classes).
        OWLSubClassOfAxiom ax2 = factory.getOWLSubClassOfAxiom(head, hasPartSomeNose);
        // Add the axiom to our ontology
        AddAxiom addAx = new AddAxiom(ontology, ax2);
        manager.applyChange(addAx);
        
        // print taxonomy of Thing
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
        reasoner.precomputeInferences();
        
        boolean consistent = reasoner.isConsistent();
        System.out.println("consistency = "+consistent);
        Node<OWLClass> topNode = reasoner.getTopClassNode();
        print(topNode, reasoner, 0, pm);
        
        
        // shouldLookAtRestrictions
        OWLClass head2 = factory.getOWLClass("Head", pm);
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(singleton(ontology));
        
        for (OWLSubClassOfAxiom ax3 : ontology.getSubClassAxiomsForSubClass(head2)) {
            ax3.getSuperClass().accept(restrictionVisitor);
            System.out.println("    axiom"+ax3.toString()); // mila amboarina ny forme pour le futur
            
        }
        /*System.out.println("Restricted properties for " + head2
         + ": " + restrictionVisitor.getRestrictedProperties().size());
         for (OWLObjectPropertyExpression prop : restrictionVisitor.getRestrictedProperties()) {
            System.out.println(" " + prop);
         }*/
        
        /************************************/
        // creating and reading annotations //
        /************************************/
        
        OWLAnnotation commentAnnoHead = factory.getOWLAnnotation(factory.getRDFSComment(), factory.getOWLLiteral(
            "Head the upper part of a human (animal)", "en"));
        // Specify that the class has an annotation - to do this we attach
        // an entity annotation using an entity annotation axiom (remember,
        // classes are entities)
        OWLAxiom ax4 = factory.getOWLAnnotationAssertionAxiom(head2.getIRI(), commentAnnoHead);
        // Add the axiom to the ontology
        manager.applyChange(new AddAxiom(ontology, ax4));
        
        OWLAnnotation commentAnnoHead2 = factory.getOWLAnnotation(factory.getRDFSIsDefinedBy(), factory.getOWLLiteral(
            "loha", "mg"));
        // Specify that the class has an annotation - to do this we attach
        // an entity annotation using an entity annotation axiom (remember,
        // classes are entities)
        OWLAxiom ax5 = factory.getOWLAnnotationAssertionAxiom(head2.getIRI(), commentAnnoHead2);
        // Add the axiom to the ontology
        manager.applyChange(new AddAxiom(ontology, ax5));
        
        OWLLiteral lit = factory.getOWLLiteral("loha","en");
        OWLAnnotationProperty owlAnnoWordProp = factory.getOWLAnnotationProperty("word", pm);
        
        
        OWLAnnotation commentAnnoHead3 = factory.getOWLAnnotation(factory.getRDFSIsDefinedBy(), factory.getOWLLiteral(
            "tete", "fr"));
        // Specify that the class has an annotation - to do this we attach
        // an entity annotation using an entity annotation axiom (remember,
        // classes are entities)
        OWLAxiom ax6 = factory.getOWLAnnotationAssertionAxiom(head2.getIRI(), commentAnnoHead3);
        // Add the axiom to the ontology
        manager.applyChange(new AddAxiom(ontology, ax6));
        
        
        // tokony asiana automatique annotation ho an ny ontology atsofoka ka mot tena fampiasa andavanandro ilay izy 
       
        OWLAnnotation anno = factory.getOWLAnnotation(owlAnnoWordProp, lit);
        
         AddOntologyAnnotation addAnno = new AddOntologyAnnotation(ontology, anno);
         manager.applyChange(addAnno);
         
        OWLAnnotationProperty label = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        
        //OWLAxiom ax5 = factory.getOWLAnnotationAssertionAxiom(head2.getIRI(), );
        
        
        Set<OWLAnnotationAssertionAxiom> annotations = ontology.getAnnotationAssertionAxioms(head2.getIRI());
        
        System.out.println("head : ");
        for (OWLAnnotationAssertionAxiom annot : annotations) {
            System.out.println("     annotation of head2 : "+ annot.toString());
        }
        
        Set<OWLAnnotationPropertyRangeAxiom> annotProps = ontology.getAnnotationPropertyRangeAxioms(label);
        for (OWLAnnotationPropertyRangeAxiom annot : annotProps) {
            System.out.println("     annotation Prop of head2 : "+ annot.toString());
        }
        
        for (OWLClass cls : ontology.getClassesInSignature()) {
            // Get the annotations on the class that use the label property
            OWLAnnotationSubject oas;
             //Set<OwlAnnotatio> annots =  ontology.getAnnotationAssertionAxioms(cls.getIRI(),label);
             //                System.out.println(cls + " -> " +
            //                 val.getLiteral());
                        
                    
              
        } 
         
        // Dump the ontology to System.out
        manager.saveOntology(ontology, destination);
       // manager.saveOntology(ontology, new StreamDocumentTarget(new ByteArrayOutputStream()));
       
       // 27.09.2019
        // 
        MyProject.shouldCreateInferredAxioms();
        
        MyProject.shouldWalkOntology();
        
        MyProject.shouldQueryWithReasoner();
        
        MyProject.shouldExtractModules();
        
        MyProject.owlPrimer();
    } 
    
    private static void CheckDataContent() throws Exception {
        
        String ontologyPath2 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/ketrika_creation1.owl";
        File ontologyFile2 = new File(ontologyPath2);
         IRI destination = IRI.create(ontologyFile2);
         
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFile2);
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        Reasoner reasoner = new Reasoner(ontology);
        OWLOntologyFormat format = manager.getOntologyFormat(ontology);
        PrefixOWLOntologyFormat prefixFormat = format.asPrefixOWLOntologyFormat();
        PrefixManager prefixManager = new DefaultPrefixManager(prefixFormat);
        
       //OWLAnnotation commentAnnoHead = factory.getOWLAnnotationProperty("", prefixManager)
       
        
        // Let's specify the :John has a wife :Mary Get hold of the necessary
    }
    private static void print(Node<OWLClass> parent, OWLReasoner reasoner, int depth, PrefixManager pm) {
        // We don't want to print out the bottom node (containing owl:Nothing
        // and unsatisfiable classes) because this would appear as a leaf node
        // everywhere
        if (parent.isBottomNode()) {
            return;
        }
        // Print an indent to denote parent-child relationships
        printIndent(depth);
        // Now print the node (containing the child classes)
        printNode(parent, pm);
        for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
            assert child != null;
            // Recurse to do the children. Note that we don't have to worry
            // about cycles as there are non in the inferred class hierarchy
            // graph - a cycle gets collapsed into a single node since each
            // class in the cycle is equivalent.
            print(child, reasoner, depth + 1, pm);
        }
    }
    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
             System.out.print(" ");
        }
    }

    private static void printNode( Node<OWLClass> node, PrefixManager pm) {
        // The default prefix used here is only an example.
        // For real ontologies, choose a meaningful prefix - the best
        // choice depends on the actual ontology.
       
        // Print out a node as a list of class names in curly brackets
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext();) {
            OWLClass cls = it.next();
            DefaultPrefixManager dpm = new DefaultPrefixManager(pm);
            // User a prefix manager to provide a slightly nicer shorter name
            String shortForm = dpm.getShortForm(cls);
            System.out.println("class = "+shortForm);
            
        }
    }
    
     public static void shouldCreateInferredAxioms() throws Exception {
        // Create a reasoner factory.
        // See Profiles for a list of known reasoner factories; note that you
        // will need to add the reasoner and any dependency to the classpath for
        // this to work.
        // The structural reasoner is a mock reasoner that does no inferences;
        // it is used only for examples.
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        // Uncomment the line below reasonerFactory = new
        // PelletReasonerFactory(); Load an example ontology - for the purposes
        // of the example, we will just load the ontology.
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = load(man);
        // Create the reasoner and classify the ontology
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        // To generate an inferred ontology we use implementations of inferred
        // axiom generators to generate the parts of the ontology we want (e.g.
        // subclass axioms, equivalent classes axioms, class assertion axiom
        // etc. - see the org.semanticweb.owlapi.util package for more
        // implementations). Set up our list of inferred axiom generators
        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        gens.add(new InferredSubClassAxiomGenerator());
        
        for(InferredAxiomGenerator<? extends OWLAxiom> ite : gens) {
            System.out.println(ite.getLabel());
        }
        // Put the inferred axioms into a fresh empty ontology - note that there
        // is nothing stopping us stuffing them back into the original asserted
        // ontology if we wanted to do this.
        OWLOntology infOnt = man.createOntology();
        // Now get the inferred ontology generator to generate some inferred
        // axioms for us (into our fresh ontology). We specify the reasoner that
        // we want to use and the inferred axiom generators that we want to use.
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
        iog.fillOntology(man, infOnt);
        // Save the inferred ontology. (Replace the IRI with one that is
        // appropriate for your setup)
        
        String ontologyPath2 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/ketrika_creation2.owl";
        File ontologyFile2 = new File(ontologyPath2);
         IRI destination = IRI.create(ontologyFile2);
        
        man.saveOntology(infOnt, destination);
        
        
         String ontologyPath3 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/ketrika_creation3.owl";
        File ontologyFile3 = new File(ontologyPath3);
         IRI destination2 = IRI.create(ontologyFile3);
        
        man.saveOntology(ont, destination2);
        
        /// raha ohatra tonga dia recreena ilay ontology complete dia ito ny atao :) 
    }
    public static void shouldWalkOntology() throws Exception {
        // This example shows how to use an ontology walker to walk the asserted
        // structure of an ontology. Suppose we want to find the axioms that use
        // a some values from (existential restriction) we can use the walker to
        // do this. We'll use the Koala ontology as an example. Load the
        // ontology from the web:
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = load(man);
        // Create the walker. Pass in the koala ontology - we need to put it
        // into a set though, so we just create a singleton set in this case.
        OWLOntologyWalker walker = new OWLOntologyWalker(singleton(ont));
        // Now ask our walker to walk over the ontology. We specify a visitor
        // who gets visited by the various objects as the walker encounters
        // them. We need to create out visitor. This can be any ordinary
        // visitor, but we will extend the OWLOntologyWalkerVisitor because it
        // provides a convenience method to get the current axiom being visited
        // as we go. Create an instance and override the
        // visit(OWLObjectSomeValuesFrom) method, because we are interested in
        // some values from restrictions.
        
        //walker.walkStructure(v);
        OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(walker) {
            @Override
            public Object visit(OWLObjectSomeValuesFrom ce) {
                
                System.out.println(" " + getCurrentAxiom()+ " "+ce.toString());
                return super.visit(ce); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        
        
        // Now ask the walker to walk over the ontology structure using our
        // visitor instance.
        walker.walkStructure(visitor);
    } 
    
    public static void shouldQueryWithReasoner() throws Exception {
        // We will load the Koala ontology and query it using a reasoner
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = loadCorrected(man);
        
        
        // print just IRI of one ontology :) 
        
        System.out.println("ontology koala corrected : [begin]");
        printOntologyAndImports(man, ont);
         System.out.println("ontology koala corrected : [end]");
        
        
        // For this particular ontology, we know that all class, properties
        // names etc. have IRIs that is made up of the ontology IRI plus # plus
        // the local name
        String prefix = ont.getOntologyID().getOntologyIRI() + "#";
        // Create a reasoner. We will use Pellet in this case. Make sure that
        // the latest version of the Pellet libraries are on the runtime class
        // path
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        // Uncomment the line below reasonerFactory = new
        // PelletReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
        // Now we can query the reasoner, suppose we want to determine the
        // properties that instances of Quokka must have
        OWLClass quokka = man.getOWLDataFactory().getOWLClass(IRI.create(prefix, "Quokka"));
        // printProperties(man, ont, reasoner, quokka);
        // We can also ask if the instances of a class must have a property
        OWLClass koala = man.getOWLDataFactory().getOWLClass(IRI.create(prefix, "KoalaWithPhD"));
        OWLObjectProperty hasDegree = man.getOWLDataFactory().getOWLObjectProperty(IRI.create(prefix + "hasDegree"));
        if (hasProperty(man, reasoner, koala, hasDegree)) {
             System.out.println("Instances of " + koala
             + " have a degree");
        }
    } 
    private static boolean hasProperty(OWLOntologyManager man, OWLReasoner reasoner, OWLClass cls,
        OWLObjectPropertyExpression prop) {
        // To test whether the instances of a class must have a property we
        // create a some values from restriction and then ask for the
        // satisfiability of the class interesected with the complement of this
        // some values from restriction. If the intersection is satisfiable then
        // the instances of the class don't have to have the property,
        // otherwise, they do.
        OWLDataFactory dataFactory = man.getOWLDataFactory();
        OWLClassExpression restriction = dataFactory.getOWLObjectSomeValuesFrom(prop, dataFactory.getOWLThing());
        // Now we see if the intersection of the class and the complement of
        // this restriction is satisfiable
        OWLClassExpression complement = dataFactory.getOWLObjectComplementOf(restriction);
        OWLClassExpression intersection = dataFactory.getOWLObjectIntersectionOf(cls, complement);
        return !reasoner.isSatisfiable(intersection);
    }
    
    private static void printOntologyAndImports(OWLOntologyManager manager, OWLOntology ontology) {
        // Print ontology IRI and where it was loaded from (they will be the
        // same)
        printOntology(manager, ontology);
        // List the imported ontologies
        for (OWLOntology o : ontology.getImports()) {
            printOntology(manager, o);
        }
    }

    /**
     * Prints the IRI of an ontology and its document IRI.
     * 
     * @param manager
     *        The manager that manages the ontology
     * @param ontology
     *        The ontology
     */
    private static void printOntology( OWLOntologyManager manager,  OWLOntology ontology) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
         System.out.println(ontologyIRI == null ? "anonymous" : ontologyIRI
        .toQuotedString());
         System.out.println(" from " + documentIRI.toQuotedString());
    }
    
     public static void shouldExtractModules() throws Exception {
        // Create our manager
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
        // Load the Koala ontology
        OWLOntology ont = loadCorrected(man);
        // We want to extract a module for all toppings. We therefore have to
        // generate a seed signature that contains "Quokka" and its
        // subclasses. We start by creating a signature that consists of
        // "Quokka".
        OWLClass toppingCls = dataFactory.getOWLClass(IRI.create(ont.getOntologyID().getOntologyIRI() + "#Marsupials"));
        Set<OWLEntity> sig = new HashSet<OWLEntity>();
        sig.add(toppingCls);
        // We now add all subclasses (direct and indirect) of the chosen
        // classes. Ideally, it should be done using a DL reasoner, in order to
        // take inferred subclass relations into account. We are using the
        // structural reasoner of the OWL API for simplicity.
        Set<OWLEntity> seedSig = new HashSet<OWLEntity>();
        OWLReasoner reasoner = new StructuralReasoner(ont, new SimpleConfiguration(), BufferingMode.NON_BUFFERING);
        for (OWLEntity ent : sig) {
            seedSig.add(ent);
            if (OWLClass.class.isAssignableFrom(ent.getClass())) {
                NodeSet<OWLClass> subClasses = reasoner.getSubClasses((OWLClass) ent, false);
                seedSig.addAll(subClasses.getFlattened());
            }
        }
        
        for (OWLEntity ent : seedSig) {
            String info = ent.toString();
            System.out.println("info -> "+ info);
        }
        // We now extract a locality-based module. For most reuse purposes, the
        // module type should be STAR -- this yields the smallest possible
        // locality-based module. These modules guarantee that all entailments
        // of the original ontology that can be formulated using only terms from
        // the seed signature or the module will also be entailments of the
        // module. In easier words, the module preserves all knowledge of the
        // ontology about the terms in the seed signature or the module.
        SyntacticLocalityModuleExtractor sme = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.STAR);
        
        String ontologyPath3 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/module_Quokka.owl";
        File ontologyFile3 = new File(ontologyPath3);
         IRI destination2 = IRI.create(ontologyFile3);
         
        
        OWLOntology mod = sme.extractAsOntology(seedSig, destination2);
        
         man.saveOntology(mod, destination2);
        // The module can now be saved as usual
    }
     
     // eto isika izao ii :) tena minankanina kely ii xd
     
  public static void owlPrimer() throws Exception {
        // The OWLOntologyManager is at the heart of the OWL API, we can create
        // an instance of this using the OWLManager class, which will set up
        // commonly used options (such as which parsers are registered etc.
        // etc.)
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // We want to create an ontology that corresponds to the ontology used
        // in the OWL Primer. Every ontology has a IRI that uniquely identifies
        // the ontology. The IRI is essentially a name for the ontology. Note
        // that the IRI doesn't necessarily point to a location on the web - in
        // this example, we won't publish the ontology at the URL corresponding
        // to the ontology IRI below.
        IRI ontologyIRI = IRI.create("http://example.com/owlapi/families");
        // Now that we have a IRI for out ontology, we can create the actual
        // ontology. Note that the create ontology method throws an
        // OWLOntologyCreationException if there was a problem creating the
        // ontology.
        OWLOntology ont = manager.createOntology(ontologyIRI);
        // We can use the manager to get a reference to an OWLDataFactory. The
        // data factory provides a point for creating OWL API objects such as
        // classes, properties and individuals.
        OWLDataFactory factory = manager.getOWLDataFactory();
        // We first need to create some references to individuals. All of our
        // individual must have IRIs. A common convention is to take the IRI of
        // an ontology, append a # and then a local name. For example we can
        // create the individual 'John', using the ontology IRI and appending
        // #John. Note however, that there is no reuqirement that a IRI of a
        // class, property or individual that is used in an ontology have a
        // correspondance with the IRI of the ontology.
        OWLIndividual john = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#John"));
        OWLIndividual mary = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Mary"));
        OWLIndividual susan = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Susan"));
        OWLIndividual bill = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Bill"));
        // The ontologies that we created aren't contained in any ontology at
        // the moment. Individuals (or classes or properties) can't directly be
        // added to an ontology, they have to be used in axioms, and then the
        // axioms are added to an ontology. We now want to add some facts to the
        // ontology. These facts are otherwise known as property assertions. In
        // our case, we want to say that John has a wife Mary. To do this we
        // need to have a reference to the hasWife object property (object
        // properties link an individual to an individual, and data properties
        // link and individual to a constant - here, we need an object property
        // because John and Mary are individuals).
        OWLObjectProperty hasWife = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasWife"));
        // Now we need to create the assertion that John hasWife Mary. To do
        // this we need an axiom, in this case an object property assertion
        // axiom. This can be thought of as a "triple" that has a subject, john,
        // a predicate, hasWife and an object Mary
        OWLObjectPropertyAssertionAxiom axiom1 = factory.getOWLObjectPropertyAssertionAxiom(hasWife, john, mary);
        // We now need to add this assertion to our ontology. To do this, we
        // apply an ontology change to the ontology via the OWLOntologyManager.
        // First we create the change object that will tell the manager that we
        // want to add the axiom to the ontology
        AddAxiom addAxiom1 = new AddAxiom(ont, axiom1);
        // Now we apply the change using the manager.
        manager.applyChange(addAxiom1);
        // Now we want to add the other facts/assertions to the ontology John
        // hasSon Bill Get a refernece to the hasSon property
        OWLObjectProperty hasSon = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasSon"));
        // Create the assertion, John hasSon Bill
        OWLAxiom axiom2 = factory.getOWLObjectPropertyAssertionAxiom(hasSon, john, bill);
        // Apply the change
        manager.applyChange(new AddAxiom(ont, axiom2));
        // John hasDaughter Susan
        OWLObjectProperty hasDaughter = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasDaughter"));
        OWLAxiom axiom3 = factory.getOWLObjectPropertyAssertionAxiom(hasDaughter, john, susan);
        manager.applyChange(new AddAxiom(ont, axiom3));
        // John hasAge 33 In this case, hasAge is a data property, which we need
        // a reference to
        OWLDataProperty hasAge = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hasAge"));
        // We create a data property assertion instead of an object property
        // assertion
        OWLAxiom axiom4 = factory.getOWLDataPropertyAssertionAxiom(hasAge, john, 33);
        manager.applyChange(new AddAxiom(ont, axiom4));
        // In the above code, 33 is an integer, so we can just pass 33 into the
        // data factory method. Behind the scenes the OWL API will create a
        // typed constant that it will use as the value of the data property
        // assertion. We could have manually created the constant as follows:
        OWLDatatype intDatatype = factory.getIntegerOWLDatatype();
        OWLLiteral thirtyThree = factory.getOWLLiteral("33", intDatatype);
        // We would then create the axiom as follows:
        factory.getOWLDataPropertyAssertionAxiom(hasAge, john, thirtyThree);
        // However, the convenice method is much shorter! We can now create the
        // other facts/assertion for Mary. The OWL API uses a change object
        // model, which means we can stack up changes (or sets of axioms) and
        // apply the changes (or add the axioms) in one go. We will do this for
        // Mary
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasSon, mary, bill));
        axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasDaughter, mary, susan));
        axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasAge, mary, 31));
        // Add facts/assertions for Bill and Susan
        axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasAge, bill, 13));
        axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasAge, mary, 8));
        // Now add all the axioms in one go - there is a convenience method on
        // OWLOntologyManager that will automatically generate the AddAxiom
        // change objects for us. We need to specify the ontology that the
        // axioms should be added to and the axioms to add.
        manager.addAxioms(ont, axioms);
        // Now specify the genders of John, Mary, Bill and Susan. To do this we
        // need the male and female individuals and the hasGender object
        // property.
        OWLIndividual male = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#male"));
        OWLIndividual female = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#female"));
        OWLObjectProperty hasGender = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasGender"));
        Set<OWLAxiom> genders = new HashSet<OWLAxiom>();
        genders.add(factory.getOWLObjectPropertyAssertionAxiom(hasGender, john, male));
        genders.add(factory.getOWLObjectPropertyAssertionAxiom(hasGender, mary, female));
        genders.add(factory.getOWLObjectPropertyAssertionAxiom(hasGender, bill, male));
        genders.add(factory.getOWLObjectPropertyAssertionAxiom(hasGender, susan, female));
        // Add the facts about the genders
        manager.addAxioms(ont, genders);
        // Domain and Range Axioms //At this point, we have an ontology
        // containing facts about several individuals. We now want to specify
        // more information about the various properties that we have used. We
        // want to say that the domains and ranges of hasWife, hasSon and
        // hasDaughter are the class Person. To do this we need various domain
        // and range axioms, and we need a reference to the class Person First
        // get a reference to the person class
        OWLClass person = factory.getOWLClass(IRI.create(ontologyIRI + "#Person"));
        // Now we add the domain and range axioms that specify the domains and
        // ranges of the various properties that we are interested in.
        Set<OWLAxiom> domainsAndRanges = new HashSet<OWLAxiom>();
        // Domain and then range of hasWife
        domainsAndRanges.add(factory.getOWLObjectPropertyDomainAxiom(hasWife, person));
        domainsAndRanges.add(factory.getOWLObjectPropertyRangeAxiom(hasWife, person));
        // Domain and range of hasSon and also hasDaugher
        domainsAndRanges.add(factory.getOWLObjectPropertyDomainAxiom(hasSon, person));
        domainsAndRanges.add(factory.getOWLObjectPropertyRangeAxiom(hasSon, person));
        domainsAndRanges.add(factory.getOWLObjectPropertyDomainAxiom(hasDaughter, person));
        domainsAndRanges.add(factory.getOWLObjectPropertyRangeAxiom(hasDaughter, person));
        // We also have the domain of the data property hasAge as Person, and
        // the range as integer. We need the integer datatype. The XML Schema
        // Datatype IRIs are used for data types. The OWL API provide a built in
        // set via the XSDVocabulary enum.
        domainsAndRanges.add(factory.getOWLDataPropertyDomainAxiom(hasAge, person));
        OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();
        domainsAndRanges.add(factory.getOWLDataPropertyRangeAxiom(hasAge, integerDatatype));
        // Now add all of our domain and range axioms
        manager.addAxioms(ont, domainsAndRanges);
        // Class assertion axioms //We can also explicitly say than an
        // individual is an instance of a given class. To do this we use a Class
        // assertion axiom.
        OWLClassAssertionAxiom classAssertionAx = factory.getOWLClassAssertionAxiom(person, john);
        // Add the axiom directly using the addAxiom convenience method on
        // OWLOntologyManager
        manager.addAxiom(ont, classAssertionAx);
        // Inverse property axioms //We can specify the inverse property of
        // hasWife as hasHusband We first need a reference to the hasHusband
        // property.
        OWLObjectProperty hasHusband = factory.getOWLObjectProperty(IRI.create(ont.getOntologyID().getOntologyIRI()+ "#hasHusband"));
        // The full IRI of the hasHusband property will be
        // http://example.com/owlapi/families#hasHusband since the IRI of our
        // ontology is http://example.com/owlapi/families Create the inverse
        // object properties axiom and add it
        manager.addAxiom(ont, factory.getOWLInverseObjectPropertiesAxiom(hasWife, hasHusband));
        // Sub property axioms //OWL allows a property hierarchy to be
        // specified. Here, hasSon and hasDaughter will be specified as
        // hasChild.
        OWLObjectProperty hasChild = factory.getOWLObjectProperty(IRI.create(ont.getOntologyID().getOntologyIRI()
            + "#hasChild"));
        OWLSubObjectPropertyOfAxiom hasSonSubHasChildAx = factory.getOWLSubObjectPropertyOfAxiom(hasSon, hasChild);
        // Add the axiom
        manager.addAxiom(ont, hasSonSubHasChildAx);
        // And hasDaughter, which is also a sub property of hasChild
        manager.addAxiom(ont, factory.getOWLSubObjectPropertyOfAxiom(hasDaughter, hasChild));
        // Property characteristics //Next, we want to say that the hasAge
        // property is Functional. This means that something can have at most
        // one hasAge property. We can do this with a functional data property
        // axiom First create the axiom
        OWLFunctionalDataPropertyAxiom hasAgeFuncAx = factory.getOWLFunctionalDataPropertyAxiom(hasAge);
        // Now add it to the ontology
        manager.addAxiom(ont, hasAgeFuncAx);
        // The hasWife property should be Functional, InverseFunctional,
        // Irreflexive and Asymmetric. Note that the asymmetric property axiom
        // used to be called antisymmetric - older versions of the OWL API may
        // refer to antisymmetric property axioms
        Set<OWLAxiom> hasWifeAxioms = new HashSet<OWLAxiom>();
        hasWifeAxioms.add(factory.getOWLFunctionalObjectPropertyAxiom(hasWife));
        hasWifeAxioms.add(factory.getOWLInverseFunctionalObjectPropertyAxiom(hasWife));
        hasWifeAxioms.add(factory.getOWLIrreflexiveObjectPropertyAxiom(hasWife));
        hasWifeAxioms.add(factory.getOWLAsymmetricObjectPropertyAxiom(hasWife));
        // Add all of the axioms that specify the characteristics of hasWife
        manager.addAxioms(ont, hasWifeAxioms);
        // SubClass axioms //Now we want to start specifying something about
        // classes in our ontology. To begin with we will simply say something
        // about the relationship between named classes Besides the Person class
        // that we already have, we want to say something about the classes Man,
        // Woman and Parent. To say something about these classes, as usual, we
        // need references to them:
        OWLClass man = factory.getOWLClass(IRI.create(ontologyIRI + "#Man"));
        OWLClass woman = factory.getOWLClass(IRI.create(ontologyIRI + "#Woman"));
        OWLClass parent = factory.getOWLClass(IRI.create(ontologyIRI + "#Parent"));
        // It is important to realise that simply getting references to a class
        // via the data factory does not add them to an ontology - only axioms
        // can be added to an ontology. Now say that Man, Woman and Parent are
        // subclasses of Person
        manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(man, person));
        manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(woman, person));
        manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(parent, person));
        // Restrictions //Now we want to say that Person has exactly 1 Age,
        // exactly 1 Gender and, only has gender that is male or female. We will
        // deal with these restrictions one by one and then combine them as a
        // superclass (Necessary conditions) of Person. All anonymous class
        // expressions extend OWLClassExpression. First, hasAge exactly 1
        OWLDataExactCardinality hasAgeRestriction = factory.getOWLDataExactCardinality(1, hasAge);
        // Now the hasGender exactly 1
        OWLObjectExactCardinality hasGenderRestriction = factory.getOWLObjectExactCardinality(1, hasGender);
        // And finally, the hasGender only {male female} To create this
        // restriction, we need an OWLObjectOneOf class expression since male
        // and female are individuals We can just list as many individuals as we
        // need as the argument of the method.
        
        OWLObjectOneOf maleOrFemale = factory.getOWLObjectOneOf(male, female);
        // Now create the actual restriction
        OWLObjectAllValuesFrom hasGenderOnlyMaleFemale = factory.getOWLObjectAllValuesFrom(hasGender, maleOrFemale);
        // Finally, we bundle these restrictions up into an intersection, since
        // we want person to be a subclass of the intersection of them
        OWLObjectIntersectionOf intersection = factory.getOWLObjectIntersectionOf(hasAgeRestriction,
            hasGenderRestriction, hasGenderOnlyMaleFemale);
        // And now we set this anonymous intersection class to be a superclass
        // of Person using a subclass axiom
        manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(person, intersection));
        // Restrictions and other anonymous classes can also be used anywhere a
        // named class can be used. Let's set the range of hasSon to be Person
        // and hasGender value male. This requires an anonymous class that is
        // the intersection of Person, and also, hasGender value male. We need
        // to create the hasGender value male restriction - this describes the
        // class of things that have a hasGender relationship to the individual
        // male.
        OWLObjectHasValue hasGenderValueMaleRestriction = factory.getOWLObjectHasValue(hasGender, male);
        // Now combine this with Person in an intersection
        OWLClassExpression personAndHasGenderValueMale = factory.getOWLObjectIntersectionOf(person,
            hasGenderValueMaleRestriction);
        // Now specify this anonymous class as the range of hasSon using an
        // object property range axioms
        manager.addAxiom(ont, factory.getOWLObjectPropertyRangeAxiom(hasSon, personAndHasGenderValueMale));
        // We can do a similar thing for hasDaughter, by specifying that
        // hasDaughter has a range of Person and hasGender value female. This
        // time, we will make things a little more compact by not using so many
        // variables
        OWLClassExpression rangeOfHasDaughter = factory.getOWLObjectIntersectionOf(person, factory.getOWLObjectHasValue(
            hasGender, female));
        manager.addAxiom(ont, factory.getOWLObjectPropertyRangeAxiom(hasDaughter, rangeOfHasDaughter));
        // Data Ranges and Equivalent Classes axioms //In OWL 2, we can specify
        // expressive data ranges. Here, we will specify the classes Teenage,
        // Adult and Child by saying something about individuals ages. First we
        // take the class Teenager, all of whose instance have an age greater or
        // equal to 13 and less than 20. In Manchester Syntax this is written as
        // Person and hasAge some int[>=13, <20] We create a data range by
        // taking the integer datatype and applying facet restrictions to it.
        // Note that we have statically imported the data range facet vocabulary
        // OWLFacet
        OWLFacetRestriction geq13 = factory.getOWLFacetRestriction(MIN_INCLUSIVE, factory.getOWLLiteral(13));
        // We don't have to explicitly create the typed constant, there are
        // convenience methods to do this
        OWLFacetRestriction lt20 = factory.getOWLFacetRestriction(MAX_EXCLUSIVE, 20);
        // Restrict the base type, integer (which is just an XML Schema
        // Datatype) with the facet restrictions.
        OWLDataRange dataRng = factory.getOWLDatatypeRestriction(integerDatatype, geq13, lt20);
        // Now we have the data range of greater than equal to 13 and less than
        // 20 we can use this in a restriction.
        OWLDataSomeValuesFrom teenagerAgeRestriction = factory.getOWLDataSomeValuesFrom(hasAge, dataRng);
        // Now make Teenager equivalent to Person and hasAge some int[>=13, <20]
        // First create the class Person and hasAge some int[>=13, <20]
        OWLClassExpression teenagePerson = factory.getOWLObjectIntersectionOf(person, teenagerAgeRestriction);
        OWLClass teenager = factory.getOWLClass(IRI.create(ontologyIRI + "#Teenager"));
        OWLEquivalentClassesAxiom teenagerDefinition = factory.getOWLEquivalentClassesAxiom(teenager, teenagePerson);
        manager.addAxiom(ont, teenagerDefinition);
        // Do the same for Adult that has an age greater than 21
        OWLDataRange geq21 = factory.getOWLDatatypeRestriction(integerDatatype, factory.getOWLFacetRestriction(
            MIN_INCLUSIVE, 21));
        OWLClass adult = factory.getOWLClass(IRI.create(ontologyIRI + "#Adult"));
        OWLClassExpression adultAgeRestriction = factory.getOWLDataSomeValuesFrom(hasAge, geq21);
        OWLClassExpression adultPerson = factory.getOWLObjectIntersectionOf(person, adultAgeRestriction);
        OWLAxiom adultDefinition = factory.getOWLEquivalentClassesAxiom(adult, adultPerson);
        manager.addAxiom(ont, adultDefinition);
        // And finally Child
        OWLDataRange notGeq21 = factory.getOWLDataComplementOf(geq21);
        OWLClass child = factory.getOWLClass(IRI.create(ontologyIRI + "#Child"));
        OWLClassExpression childAgeRestriction = factory.getOWLDataSomeValuesFrom(hasAge, notGeq21);
        OWLClassExpression childPerson = factory.getOWLObjectIntersectionOf(person, childAgeRestriction);
        OWLAxiom childDefinition = factory.getOWLEquivalentClassesAxiom(child, childPerson);
        manager.addAxiom(ont, childDefinition);
        // Different individuals //In OWL, we can say that individuals are
        // different from each other. To do this we use a different individuals
        // axiom. Since John, Mary, Bill and Susan are all different
        // individuals, we can express this using a different individuals axiom.
        OWLDifferentIndividualsAxiom diffInds = factory.getOWLDifferentIndividualsAxiom(john, mary, bill, susan);
        manager.addAxiom(ont, diffInds);
        // Male and Female are also different
        manager.addAxiom(ont, factory.getOWLDifferentIndividualsAxiom(male, female));
        // Disjoint classes //Two say that two classes do not have any instances
        // in common we use a disjoint classes axiom:
        OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(man, woman);
        manager.addAxiom(ont, disjointClassesAxiom);
        // Ontology Management //Having added axioms to out ontology we can now
        // save it (in a variety of formats). RDF/XML is the default format
        // System.out.println("RDF/XML: ");
        
         String ontologyPath3 = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/family.owl";
        File ontologyFile3 = new File(ontologyPath3);
         IRI destination2 = IRI.create(ontologyFile3);
        
        
        manager.saveOntology(ont, destination2);
        // OWL/XML
        // System.out.println("OWL/XML: ");
        //manager.saveOntology(ont, new OWLXMLDocumentFormat(), new StringDocumentTarget());
        // Manchester Syntax
        // System.out.println("Manchester syntax: ");
     //   manager.saveOntology(ont, new ManchesterSyntaxDocumentFormat(), new StringDocumentTarget());
        // Turtle
        // System.out.println("Turtle: ");
      //  manager.saveOntology(ont, new TurtleDocumentFormat(), new StringDocumentTarget());
    }   
     
    
//Create property assertion line : 535
   

}
