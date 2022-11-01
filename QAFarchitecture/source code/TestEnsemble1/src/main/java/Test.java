//import org.semanticweb.HermiT.ReasonerFactory;

import net.sf.extjwnl.JWNLException;
import org.semanticweb.owlapi.apibinding.OWLManager;

        import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserImpl;
        import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.util.ArrayList;

public class Test {
    // https://github.com/owlcs/owlapi/issues/548 extremely important
    public static void main(String[] args) throws OWLOntologyCreationException {



        String ontologyAWO = "AWO";
        String ontologyAWO_inf = "AWO-inf-3";
        String ontologyStuff = "stuff3";
        String ontologyStuff_inf = "stuff3-inf";
        String ontologyBioTop = "BioTop";
        String ontologyBioTop_inf = "BioTop-inf";
        String ontologyCopyright = "CopyrightAll2";
        String ontologyLatin = "LatinDance";
        String ontologyCultural = "cultural-ON3";
        String ontologyModel1 = "ModellingStyle1-inf";
        String ontologyModel2 = "ModellingStyle2-inf";

        try {
            UtilsLinguistic.init();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        Variables.macComputer(false);
        String pathSep = Variables.getPathSep();
        String ontologyName = ontologyModel2;
        String filename = Variables.getOntologiesFileFolder()+pathSep+ ontologyName + ".owl";

        MyOnto.initAll(filename);

         testReasonerAndParser();

         //testQTOnto();

         //testVerbaliser();

        //testQTOnto2("BioTop", true);

        Test.testQTOntoComplex(true);

        //testQTOntoModel1();

        //String folder = "BioTop";
        //String fileToCheck = "D:\\Research\\MyDirectoryX\\MSTR2022\\Experiments\\QTOntoDetails\\"+folder+"\\all.txt";
        //String reportFile = "D:\\Research\\MyDirectoryX\\MSTR2022\\Experiments\\QTOntoDetails\\"+folder+"\\reportAll.txt";
       // checkAnswerability(fileToCheck, reportFile);

       /* int[] integers={1,5,4,8,9,4,2,4545,12};
        for (int i: integers
             ) {
            System.out.println(i);
            if (i == 8)
                break;
        }*/

    }

    public static void testVerbaliser() {
        Verbaliser verbaliser = null;
        try {
            verbaliser = new Verbaliser();
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        OWLObjectProperty validOWLOP = MyOnto.getValidOWLOP("eaten-by");
        PropertyClassification opClassification = new PropertyClassification(validOWLOP.toString(), verbaliser);
        System.out.println("toto");
        System.out.println(opClassification.getFormattedProperty());
        System.out.println(opClassification.getPropertyType());
    }
    public static void testQTOnto2(String folder, boolean detail){

        Verbaliser verbaliser = null;
        try {
            verbaliser = new Verbaliser();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        String p = "proper part";
        String s = verbaliser.addArticleIfNecessary("a", p);
        System.out.println(s);
        System.out.println(verbaliser.isNounWordNet("proper"));



        if (detail)
        {
            folder = "D:\\Research\\MyDirectoryX\\MSTR2022\\Experiments\\QTOntoDetails\\"+folder+"\\";
        }
        else {
            folder = "D:\\Research\\MyDirectoryX\\MSTR2022\\Experiments\\"+folder+"\\";
        }
        ArrayList<String> filenames = new ArrayList<>();
        String filename1 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Yes-No-2-part-1-rel.xml";
        filenames.add(filename1);
        //String filename2 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Yes-No-2-part-1-rel-2.xml";
        //filenames.add(filename2);
        String filename3 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Yes-No-2-part-1-rel+1-quant-some.xml";
        filenames.add(filename3);
        String filename4 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Yes-No-2-part-1-rel+1-quant-only.xml";
        filenames.add(filename4);
        String filename5 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Equival-classes.xml";
        filenames.add(filename5);
        String filename5b = "D:\\Research\\MyDirectoryX\\MSTR2022\\Equival-classes-perdurant.xml";
        filenames.add(filename5b);
        String filename6 = "D:\\Research\\MyDirectoryX\\MSTR2022\\Yes-No-1-part-1-rel.xml";
        filenames.add(filename6);
        String filename7 = "D:\\Research\\MyDirectoryX\\MSTR2022\\What-2-part-1-rel.xml";
        filenames.add(filename7);
        String filename8 = "D:\\Research\\MyDirectoryX\\MSTR2022\\What-2-part-1-rel-quant-some.xml";
        filenames.add(filename8);
        String filename9 = "D:\\Research\\MyDirectoryX\\MSTR2022\\What-2-part-1-rel-quant-only.xml";
        filenames.add(filename9);
        String filename10= "D:\\Research\\MyDirectoryX\\MSTR2022\\What-1-part-1-rel.xml";
        filenames.add(filename10);
        String filename11= "D:\\Research\\MyDirectoryX\\MSTR2022\\Definition.xml";
        filenames.add(filename11);

        ArrayList<String> allQuestions = new ArrayList<>();

        for (String filename: filenames
             ) {
            QTOnto qtOnto = new QTOnto(filename);
            qtOnto.print();

            filename = filename.replace('\\','@');
            String[] temp = filename.split("@");
            String filenameResult = temp[temp.length-1];
            System.out.println("1- "+filenameResult);
            filenameResult = filenameResult.substring(0, filenameResult.length()-4)+".txt";
            filenameResult = folder+filenameResult;
            System.out.printf("2- "+filenameResult);
            //System.out.println(filename);
            System.out.println("Type of questions = "+qtOnto.getTypeOfQuestions());
            allQuestions.add("Type of questions = "+qtOnto.getTypeOfQuestions());
            TokensValidator validator = new TokensValidator(qtOnto);
            ListResultAxs listResultAxs = validator.initAllValidAxioms();
            System.out.println(filename);
            listResultAxs.print();
            ArrayList<String> questions = null;
            if (detail) {
                questions = verbaliser.realiseWithDetails(listResultAxs, qtOnto);
            }
            else {
                questions = verbaliser.realise(listResultAxs, qtOnto);
            }
            System.out.println("myfile=="+filenameResult);
            FileManager.saveFile(filenameResult, questions);

            allQuestions.addAll(questions);
            allQuestions.add("");
            System.out.println();
            System.out.println("----- Questions -----");
            for (String question: questions
            ) {
                System.out.println(question);
            }
            System.out.println();

            System.out.println("===================== ALL QUESTIONS =====================");
            for (String question: allQuestions
                 ) {
                System.out.println(question);
            }
            System.out.println();
            String filenameAll = folder+"all.txt";
            FileManager.saveFile(filenameAll,allQuestions);
        }
    }

    public static void testQTOntoComplex( boolean detail) {

        Verbaliser verbaliser = null;
        try {
            verbaliser = new Verbaliser();
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\Model1.xml";

        QTOnto qtOnto = new QTOnto(filename);
        qtOnto.print();
        System.out.println("Type of questions = " + qtOnto.getTypeOfQuestions());
        TokensValidator validator = new TokensValidator(qtOnto);
        ListResultAxs listResultAxs = validator.initAllValidAxioms();
        System.out.println(filename);
        listResultAxs.print();
        ArrayList<String> questions = null;
        if (detail) {
            questions = verbaliser.realiseWithDetails(listResultAxs, qtOnto);
        } else {
            questions = verbaliser.realise(listResultAxs, qtOnto);
        }

        System.out.println();
        for (String question : questions
        ) {
            System.out.println(question);
        }


    }

    public static void testQTOnto() {

        //String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\QTOnto1.xml";
        String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\QTOnto2.xml";
        //String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\n-hop.xml";
        QTOnto qtOnto = new QTOnto(filename);
        qtOnto.printTest();
    }


    public static void testQTOntoModel1() {

        //String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\QTOnto1.xml";
        String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\Model1.xml";
        //String filename = "D:\\Research\\MyDirectoryX\\MSTR2022\\n-hop.xml";
        QTOnto qtOnto = new QTOnto(filename);
        qtOnto.printTest();
    }
    public static void checkAnswerability(String filename, String filenameOutput) {
        QuestionItems qItems = new QuestionItems();
        qItems.loadFromFile(filename);
        ArrayList<String> report = qItems.computeChecking();
        FileManager.saveFile(filenameOutput, report);
    }


    public static void testReasonerAndParser() throws OWLOntologyCreationException {

        OWLOntology ontology = MyOnto.getOWLOntology();
        System.out.println(ontology.getAxioms(AxiomType.SUBCLASS_OF));

        // OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);

        System.out.println(ontology.getAxioms(AxiomType.SUBCLASS_OF));

        String in = "p max 1 owl:real";
        OWLOntologyManager m = MyOnto.getOWLOntologyManager();
        OWLDataFactory df = m.getOWLDataFactory();
        OWLReasoner reasoner = MyOnto.getOWLReasoner();


        OWLClass b = df.getOWLClass(IRI.create("urn:test#", "B"));
        OWLClass a = df.getOWLClass(IRI.create("urn:test#", "A"));
        OWLOntology o = m.createOntology();
        m.addAxiom(o, df.getOWLDeclarationAxiom(df.getOWLObjectProperty(IRI.create("urn:test#", "B"))));
        m.addAxiom(o, df.getOWLDeclarationAxiom(b));
        m.addAxiom(o, df.getOWLDeclarationAxiom(a));


        String manchesterSyntax = " Class: <file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Animal> " +
                "SubClassOf: <file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Endurant> DisjointWith: " +
                "<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Plant>";
        String manchesterSyntax2 = " :Animal EquivalentTo :Leaf";
        //
        ManchesterOWLSyntaxParserImpl parser = (ManchesterOWLSyntaxParserImpl) OWLManager.createManchesterParser();
        parser.setDefaultOntology(ontology);
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        System.out.println(ontologyIRI);
        //parser.getPrefixManager().setDefaultPrefix("file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#");

    String omnivore = "<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Omnivore> " +
            "EquivalentTo((<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#eats> some " +
            "<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Animal>) and " +
            "(<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#eats> some " +
            "<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Plant>) and " +
            "(<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#eats> some " +
            "((<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#part-of> " +
            "some <file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Animal>) " +
            "and (<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#part-of>" +
            " some <file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#Plant>))))";
        OWLDataFactory factory = m.getOWLDataFactory();

        OWLClass clsA = factory.getOWLClass(IRI.create(ontologyIRI + "#Lion"));
        System.out.println(clsA);



        //parser.setStringToParse(":Giraffe SubclassOf(:eats only (:Leaf or :Twig))");
       /* parser.setStringToParse(":Warthog SubclassOf((:eats some :Animal)\n" +
                " and (:eats some :FruitingBody)\n" +
                " and (:eats some :Grass)\n" +
                " and (:eats some :Root))");*/

        String test = ":eaten-by InverseOf: :eats";
        //String test = ":<file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#has-part> InverseOf: <file:/Applications/Protege_4.1_beta/AfricanWildlifeOntology1.owl#eats>";
        // String test = ":eaten-by SubpropertyOf: (:owl:topObjectProperty)";
        //String test = "symmetric :eaten-by";
        parser.setStringToParse(test);

        //  parser.setStringToParse(manchesterSyntax2);
        //OWLClassExpression parseClassExpression = parser.parseClassExpression();
        //System.out.println(parseClassExpression);
        //OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsA, parseClassExpression);

        //OWLAxiom axiom = parser.parseAxiom();
       // System.out.println(axiom);

        System.out.println("start");
       // boolean entailed = reasoner.isEntailed(axiom);
     //   System.out.println(entailed);

        /*NodeSet<OWLClass> subClasses = reasoner.getSubClasses(parseClassExpression);
        Set<OWLClass> flattened = subClasses.getFlattened();
        for (OWLClass cl: flattened
             ) {
            System.out.println(cl);
        }*/


        // OWLClassAxiom classAxiom = new OWLSubClassOfAxiom

        /*OntologyConfigurator ontologyConfigurator = new OntologyConfigurator();
        ManchesterOWLSyntaxParserImpl parser = new ManchesterOWLSyntaxParserImpl(ontologyConfigurator, factory);
        System.out.println(manchesterSyntax);
        OWLClassExpression cl = parser.parseClassExpression();
        System.out.printf("result = "+cl.toString());*/

        // OWLClassAxiom owlClassAxiom = parser.parseClassAxiom();

        //System.out.println(owlClassAxiom);
    }
    public void shouldSupportPunningClassesAndPropertiesInManchesterSyntax() throws OWLOntologyCreationException {

        // assertEquals(parseClassExpression, df.getOWLObjectUnionOf(a, b));
    }


}
