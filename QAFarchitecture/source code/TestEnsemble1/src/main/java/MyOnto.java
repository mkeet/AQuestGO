import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserImpl;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class MyOnto {
    private static String filename;
    private static OWLOntology owlOntology;
    private static OWLReasoner owlReasoner;
    private static OWLOntologyManager owlOntologyManager;
    private static String iri;
    private static OWLDataFactory owlDataFactory;
    private static ArrayList<OWLClass> owlClasses;
    private static ArrayList<OWLObjectProperty> owlObjectProperties;
    private static ManchesterOWLSyntaxParserImpl manchesterOWLSyntaxParser;

    public static void initAll(String filename) {
        MyOnto.filename = filename;
        MyOnto.owlOntologyManager = OWLManager.createOWLOntologyManager();
        MyOnto.owlOntology = null;

        try {
            MyOnto.owlOntology = MyOnto.owlOntologyManager.loadOntologyFromOntologyDocument(new File(MyOnto.filename));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
        MyOnto.owlReasoner = reasonerFactory.createReasoner(MyOnto.owlOntology, config);
        MyOnto.owlReasoner.precomputeInferences();
        boolean consistent = MyOnto.owlReasoner.isConsistent();
        System.out.println("Consistent: " + consistent);
        System.out.println("\n");
        IRI ontologyIRI = MyOnto.owlOntology.getOntologyID().getOntologyIRI().get();
        MyOnto.iri = ontologyIRI.getIRIString();
        MyOnto.owlDataFactory = MyOnto.owlOntologyManager.getOWLDataFactory();

        OWLClass thing = MyOnto.getOWLReasoner().getTopClassNode().getRepresentativeElement();
        MyOnto.owlClasses = new ArrayList<>();
        Set<OWLClass> subClasses = MyOnto.getOWLReasoner().getSubClasses(thing, false).getFlattened();
        for (OWLClass subClass: subClasses
             ) {
            if (!subClass.toString().contains("owl:Nothing"))
                MyOnto.owlClasses.add(subClass);
        }
        //MyOnto.owlClasses.remove(MyOnto.getOWLReasoner().getBottomClassNode().getRepresentativeElement());

        OWLObjectProperty topOP = MyOnto.getOWLReasoner().getTopObjectPropertyNode().getRepresentativeElement().getNamedProperty();
        MyOnto.owlObjectProperties = new ArrayList<>();
        Set<OWLObjectPropertyExpression> subOPs = MyOnto.getOWLReasoner().getSubObjectProperties(topOP, false).getFlattened();
        for (OWLObjectPropertyExpression subOP: subOPs
        ) {
            if (!subOP.getNamedProperty().getNamedProperty().toString().contains("owl:bottomObjectProperty")) {
                MyOnto.owlObjectProperties.add(subOP.getNamedProperty());
            }
        }
        //MyOnto.owlObjectProperties.remove(MyOnto.getOWLReasoner().getBottomObjectPropertyNode().getRepresentativeElement().getNamedProperty());

        MyOnto.manchesterOWLSyntaxParser = (ManchesterOWLSyntaxParserImpl) OWLManager.createManchesterParser();
        MyOnto.manchesterOWLSyntaxParser.setDefaultOntology(MyOnto.owlOntology);



    }

    public static OWLClass getValidOWLClass(String classStr){
        if (classStr.trim().equals(""))
            return null;

        if (classStr.equals("owl:Thing"))
            return MyOnto.getOWLReasoner().getTopClassNode().getRepresentativeElement();

        for (OWLClass owlClass: MyOnto.getOWLClasses()
             ) {
            if (owlClass.toString().endsWith(classStr+">")) {
                return owlClass;
            }
        }
        return null;
    }
    public static OWLObjectProperty getValidOWLOP(String opStr){
        if (opStr.trim().equals(""))
            return null;
        if (opStr.equals("owl:topObjectProperty"))
            return MyOnto.getOWLReasoner().getTopObjectPropertyNode().getRepresentativeElement().getNamedProperty();

        for (OWLObjectProperty owlOP: MyOnto.getOWLObjectProperties()
        ) {
            //System.out.println(owlOP.getNamedProperty().toString());
            if (owlOP.toString().endsWith(opStr+">")) {
                return owlOP;
            }
        }
        return null;
    }

    public static OWLReasoner getOWLReasoner() {
        return MyOnto.owlReasoner;
    }

    public static OWLOntology getOWLOntology() {
        return MyOnto.owlOntology;
    }

    public static String getOntoIRI(){
        return MyOnto.iri;
    }

    public static OWLDataFactory getOWLDataFactory() {
        return MyOnto.owlDataFactory;
    }
    public static OWLOntologyManager getOWLOntologyManager(){
        return MyOnto.owlOntologyManager;
    }

    public static ArrayList<OWLClass> getOWLClasses() {
        return owlClasses;
    }

    public static ArrayList<OWLObjectProperty> getOWLObjectProperties() {
        return owlObjectProperties;
    }

    public static ManchesterOWLSyntaxParserImpl getManchesterOWLSyntaxParser() {
        return manchesterOWLSyntaxParser;
    }
}
