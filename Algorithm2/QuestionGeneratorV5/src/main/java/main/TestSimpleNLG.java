/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import de.uulm.ecs.ai.owlapi.krssparser.Token;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.Dictionary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
  import simplenlg.framework.*;
  import simplenlg.lexicon.*;
  import simplenlg.realiser.english.*;
  import simplenlg.phrasespec.*;
  import simplenlg.features.*;
/**
 *
 * @author toky.raboanary
 */
public class TestSimpleNLG {
    public static Lexicon lexicon = Lexicon.getDefaultLexicon();
    public static NLGFactory nlgFactory = new NLGFactory(lexicon);
    public static Realiser realiser = new Realiser(lexicon);
    
    static {
        String path = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/lexAccess2016.data";
        //lexicon = new NIHDBLexicon(path);
        
    }
    
    public static void testStanfordCoreNLP () {
        //String text = "It has contiguous stuff";
        String text = "It omnivore that";
        //Dictionary d = new Dictionary();
        
        CoreDocument coreDocument = new CoreDocument(text);
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        coreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        CoreLabel cVerb = coreLabels.get(2);
        String sTest = cVerb.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        if (sTest.startsWith("NN")) {
            System.out.println("NOun milay be ii xd xd ");
        }
        
        for(CoreLabel c : coreLabels ) {
            String s = c.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            System.out.println(c.originalText()+" -> "+s);
        }
        for(CoreLabel c : coreLabels ) {
            
            System.out.println("NER = "+c.ner());
            System.out.println("LEMMA = "+ c.lemma());
            System.out.println("CATEGORY = "+ c.lemma());
            String s = c.get(CoreAnnotations.LemmaAnnotation.class);
            System.out.println(c.originalText()+" ==> "+s);
            System.out.println(c.originalText()+" ==> "+c.get(CoreAnnotations.DictAnnotation.class));
        }
    }
    
    public static String XmlAnalyser(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.parse(input);
        
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());
        System.out.println("----------------------------");
         
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               //System.out.println("Student roll no : " 
                 // + eElement.getAttribute("rollno"));
               String base = eElement.getElementsByTagName("base").item(0).getTextContent();
               System.out.println("Base : "+ base);
          
               String category = eElement.getElementsByTagName("category").item(0).getTextContent();
               System.out.println("category : "+ category);
               
               String id = eElement.getElementsByTagName("id").item(0).getTextContent();
               System.out.println("id =  "+ id);
               
               
             //  String default_infl = eElement.getElementsByTagName("default_infl").item(0).getTextContent();
             //  System.out.println("default_infl : "+default_infl);
            }
        }
        return "toto"; 
    }
    
    public static TokenType getTokenType(WordElement wElement) {
        String category = wElement.getCategory().toString();
        if (category.compareTo("noun") == 0)
            return TokenType.NOUN;
        else if (category.compareTo("preposition") == 0) {
            return TokenType.PREPOSITION;
        } else if (category.compareTo("verb") == 0) {
            return TokenType.VERB;
        } else if (category.compareTo("determiner") == 0) {
            return TokenType.DETERMINER;
        }
        else 
        {
            return TokenType.UNKNOWN;
        }
    }
    
    public static void Test() throws Exception{
        // sady tazomina ny mot no tazomina ny WordElement
        System.out.println("titi");
        testStanfordCoreNLP();
        
        System.out.println("toto");
        WordElement wEltEat = lexicon.lookupWord("eat");
        ElementCategory categoryVerb =  wEltEat.getCategory();
        
        NPPhraseSpec subjectq = LinguisticClass.getNLGFactory().createNounPhrase("a", "herbivore");
        String res = LinguisticClass.getRealiser().realise(subjectq).getRealisation();
        System.out.println("eh oh ------->>>>> "+res);
        
        WordElement wElt = lexicon.lookupWord("live");
        Set<String> features = wElt.getAllFeatureNames();
        for(String s : features) {
            System.out.println("feature => "+ s);
        }
        boolean isVerb = wElt.isA(categoryVerb);
        System.out.println("live verb?= "+isVerb);
        
        System.out.println("children LIVE "+wElt.getChildren().size());
        ElementCategory category =  wElt.getCategory();
        
        TokenVerb tVerb = new TokenVerb("participate", wElt);
        tVerb.print();
        
        String str = category.toString();
        System.out.println("category "+str);
        
        //String str = wElt.
        //System.out.println(str);
        
        //List<NLGElement> children = wElt.getChildren();
       
        //for(NLGElement child : children) {
        //    System.out.println(child.toString());
        //}
        String defEats = wElt.toXML();
        System.out.println("eats def XML = ");
        System.out.println(defEats);
        try {
            XmlAnalyser(defEats);
        } catch(Exception e) {
            
        }
        NLGElement s1 = nlgFactory.createSentence("my dog is happy");
        String output = realiser.realiseSentence(s1);
        System.out.println(output); 
        
        NLGElement s2 = nlgFactory.createSentence("do he die");
        String output2 = realiser.realiseSentence(s2);
        System.out.println(output2); 
        
        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Jacques");
        p.setVerb("chases");
        p.setObject("the monkey");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        
        String output_3 = realiser.realiseSentence(p); // Realiser created earlier.
        System.out.println(output_3);
        
        SPhraseSpec pp = nlgFactory.createClause();
        /// adjectives and modifiers
        NPPhraseSpec subject = nlgFactory.createNounPhrase("Mary");
        NPPhraseSpec object = nlgFactory.createNounPhrase("the monkey");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("chase");
        verb.addModifier("only");
        subject.addModifier("fast");
        
        
        pp.setSubject(subject);
        pp.setObject(object);
        pp.setVerb(verb);
        String output3 = realiser.realiseSentence(pp); // Realiser created earlier.
        
        String test = "the rockdassie live on only of the land";
        SPhraseSpec ppp = nlgFactory.createClause();
        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("the rock dassie");
        NPPhraseSpec object2 = nlgFactory.createNounPhrase("land");
        VPPhraseSpec verb2 = nlgFactory.createVerbPhrase("lives");
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase("lives on");
        
        verb3.setFeature(Feature.PROGRESSIVE, true);
        
        String resultVerb3 = TestSimpleNLG.realiser.realise(verb3).getRealisation();
        System.out.println("result = "+resultVerb3);
        
        System.out.println("gerund = "+getGerund("lives"));
        System.out.println("inf = "+getInfinitiveWithoutTo("lives"));
        System.out.println("3rdPers = "+getThirdPerson("live"));
        System.out.println("pas = "+LinguisticClass.getPast("live"));
        System.out.println("pas = "+LinguisticClass.getPastParticiple("live"));
        // manala auxiliaire is
        
        
        PPPhraseSpec pprep = nlgFactory.createPrepositionPhrase();
        pprep.setPreposition("on");
        verb2.addComplement(pprep);
        verb2.addModifier("only");
        
        WordElement wElement = lexicon.getWord("eats");
        
        //subject2.addModifier("fast");
        
        ppp.setSubject(subject2);
        ppp.setObject(object2);
        ppp.setVerb(verb2);
        String output4 = realiser.realiseSentence(ppp); // Realiser created earlier.
        System.out.println(output4);
        ppp.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        String output5 = realiser.realiseSentence(ppp); 
        System.out.println(output5);  
        MultipleSubjectsObjectsComplements();
        
    }
    
    public static String getGerund(String verb) {
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.PROGRESSIVE, true);        
        String resultVerb3 = TestSimpleNLG.realiser.realise(verb3).getRealisation();
        String[] tab = resultVerb3.split(" ");
        String res = "";
        for (int i = 1; i < tab.length; i++) {
            res+=tab[i];
        }
        return res;
    }
    public static String getInfinitiveWithoutTo(String verb) {
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);     
        verb3.setFeature(Feature.PERSON, Person.FIRST);     
        String resultVerb3 = TestSimpleNLG.realiser.realise(verb3).getRealisation();

        return resultVerb3;
    }
    public static String getThirdPerson(String verb) {
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);     
        verb3.setFeature(Feature.PERSON, Person.THIRD);   
        verb3.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        String resultVerb3 = TestSimpleNLG.realiser.realise(verb3).getRealisation();
        return resultVerb3;
    }
    public static void MultipleSubjectsObjectsComplements() {
        System.out.println("");
        System.out.println("MultipleSubjectsObjectsComplements");
        SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec subject1 = nlgFactory.createNounPhrase("Mary");
        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("an", "giraffe");
        NPPhraseSpec subject3 = nlgFactory.createNounPhrase("my", "dog");
        
        
        
        CoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase(subject1, subject2); 
        subj.addCoordinate(subject3);
        p.setObject("the monkey");
        p.setVerb("chase");
        
                  // may revert to nlgFactory.createCoordinatedPhrase( subject1, subject2 ) ;
        p.setSubject(subj);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        String output = realiser.realiseSentence(p); 
        
        // obj.setFeature(Feature.CONJUNCTION, "or");
        
        System.out.println(output);  
        
        
        String stuff = "stuff";
        
        boolean valiny = LinguisticClass.isNoun(stuff);
        System.out.println("valiny = "+ valiny);
        
        
        
        //SPhraseSpec p = nlgFactory.createClause("Mary", "chase", "the monkey");
        // p.addComplement("in the park");
        
        // the same as :
        
         /*NPPhraseSpec place = nlgFactory.createNounPhrase("park");
        place.setDeterminer("the");
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
        pp.addComplement(place);
        pp.setPreposition("in");
        p.addComplement(pp);*/
        
    }
}
