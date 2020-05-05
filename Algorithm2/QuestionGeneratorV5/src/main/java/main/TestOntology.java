/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 *
 * @author toky.raboanary
 */
public class TestOntology {
    private String filenameTemplate;
    private String filenameOntology;
    //public ArrayList<>
    
    public TestOntology(String filenameTemplate, String filenameOntology){
        this.filenameTemplate = filenameTemplate;
        this.filenameOntology = filenameOntology;
    }
    
    public TestOntology() {
        this.filenameTemplate = null;
        this.filenameOntology = null;
    }
    public void Test() {
        try {
            
            String toto = "toto";
            String tata = "toto";
            
            if(toto.equals(tata))
                System.out.println("iiiii xd xd mety ilay equals");
            
            
            
           /// WORDNET :) 
           
           
            Dictionary dico = Dictionary.getDefaultResourceInstance();
            IndexWordSet lookupAllIndexWords = dico.lookupAllIndexWords("phloem");
            
            IndexWord dog = dico.getIndexWord(POS.NOUN, "water");
            
            
            if (dog!= null) {
                System.out.println("dog = "+dog.toString());
                
                List<Synset> senses = dog.getSenses();
                for(Synset sens : senses) {
                    System.out.println("type = "+sens.getType().toString());
                    System.out.println("sens "+sens.toString());
                }
            }
            if (lookupAllIndexWords != null)
            {
                System.out.println("");
                System.out.println("TSY NULL aloha wordBySenseKey "+lookupAllIndexWords.toString());
                System.out.println("");
                // for (IndexWord indexWord: lookupAllIndexWords ) {
                    
                //}
                //lookupAllIndexWords.
            }
            
            
            
            //System.out.println("Omnivore => "+ wordBySenseKey.getSenseKey());
            //System.out.println("Omnivore => "+ wordBySenseKey.getSynset().getType().getName());
            Ontology ontology = Singleton.getOntology();
            
            Node<OWLObjectPropertyExpression> topObjectPropertyNode = ontology.getReasoner().getTopObjectPropertyNode();
            String size = topObjectPropertyNode.getRepresentativeElement().toString();
            NodeSet<OWLObjectPropertyExpression> props = ontology.getReasoner().getSubObjectProperties(topObjectPropertyNode.getRepresentativeElement(), false);
            
            int i=0;
            for(Node<OWLObjectPropertyExpression> owlObj : props) 
            {
                String rep = owlObj.getRepresentativeElement().toString();
                
                System.out.println(">>>>>>>>>>> all >>>>>>>>>> "+rep);
                if (rep.startsWith("<")){
                    System.out.println(rep);
                    PropertyClassification pc = new PropertyClassification(rep);
                    System.out.println("\t"+pc.getInfo());
                    System.out.println("\t -> path "+pc.getPath());
                    pc.printReprot();
                }
                i++;
                //if (i>2) break;
            }
           
            System.out.println("");
            System.out.println("");
           // topObjectPropertyNode.
            
           // System.out.println("size = "+size);
            for(OWLObjectPropertyExpression owlObj : topObjectPropertyNode.getEntitiesMinusTop()) {
                System.out.println((owlObj.toString()));
            }
            Entity thing = ontology.getEntity("Animal");
            thing.printAllObjectProperties();
            
            
            
            ArrayList<OWLClass> subclasses = thing.getSubclasses();
            System.out.println("milay "+subclasses.size());
            for(OWLClass classe : subclasses) {
                Entity entity = ontology.getEntity(classe);
                entity.printAllObjectProperties();
            }
            
            /* System.out.println("************ LIONA ************ [BEGIN]");
            Entity liona = ontology.getEntity("Liona");
            liona.Print();
            System.out.println("************ LIONA ************ [END]");
            System.out.println("");
            System.out.println("************ LION ************ [BEGIN]");
            Entity lion = ontology.getEntity("lion");
            lion.Print();
            System.out.println("************ LION ************ [END]");            
            System.out.println("");
            System.out.println("************ LION/LIONA/LEEU ************ [BEGIN]");
            Entity leaf = ontology.getEntity("lion");
            EntityDefinition definitionLion = leaf.getEntityDefinition();
            definitionLion.Print();
            System.out.println("************ LION/LIONA/LEEU ************ [END]");
            System.out.println("IS a LION a LEAF? "+ lion.isA(leaf));
            Entity animal = ontology.getEntity("animal");
            System.out.println("IS a LION an ANIMAL? "+ lion.isA(animal));*/
            
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(TestOntology.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("misy tsy mety 1");
        } catch (FileNotFoundException ex) {
            System.out.println("misy tsy mety 2");
            Logger.getLogger(TestOntology.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            System.out.println("misy tsy mety 3");
            Logger.getLogger(TestOntology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void TestKetrika() {
        try {
            KetrikaAlg k = new KetrikaAlg();
            if (this.filenameTemplate == null)
                k.init();
            else k.init(filenameTemplate, filenameOntology);
            k.printTemplates();
          //  k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE1);
          //  k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE2);
            //ArrayList<String> questions = k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE1_QUANTIFIED);
            //for (int i = 0; i < questions.size(); i++) {
            //    System.out.println(questions.get(i));
           // } 
            
            //k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_TYPE1);
            
            //k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_TYPE2);
            
            // WHAT_QUESTIONS_QUANTIFIED
            //k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_QUANTIFIED);
            
            //k.getQuestion(KetrikaAlg.DEFINE_QUESTIONS);
            
           // k.getQuestion(KetrikaAlg.EQUIVALENCE_QUESTIONS);
            ArrayList<String> lines = k.getQuestion(KetrikaAlg.TRUE_FALSE_QUESTIONS_QUANTIFIED); 
            for (int i = 0; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }
            
            KetrikaTest2();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> KetrikaTest2() throws FileNotFoundException, Exception {
         KetrikaAlg k = new KetrikaAlg();
         if (this.filenameTemplate == null)
            k.init();
         else k.init(filenameTemplate, filenameOntology);
         
        ArrayList<String> lines = new ArrayList<String>();
        lines.addAll(k.getQuestion(KetrikaAlg.DEFINE_QUESTIONS));
        lines.addAll(k.getQuestion(KetrikaAlg.EQUIVALENCE_QUESTIONS));
        lines.addAll(k.getQuestion(KetrikaAlg.TRUE_FALSE_QUESTIONS));
        lines.addAll(k.getQuestion(KetrikaAlg.TRUE_FALSE_QUESTIONS_QUANTIFIED));
        lines.addAll(k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_QUANTIFIED));
        lines.addAll(k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_TYPE1));
        lines.addAll(k.getQuestion(KetrikaAlg.WHAT_QUESTIONS_TYPE2));
        lines.addAll(k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE1));
        lines.addAll(k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE1_QUANTIFIED));
        lines.addAll(k.getQuestion(KetrikaAlg.YES_NO_QUESTION_TYPE2));
        
        for (int i = 0; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }
        return lines;
    }
    public ArrayList<String> KetrikaTestIteration(KetrikaAlg k, int iteMax, String questionType) throws FileNotFoundException, Exception {
        ArrayList<String> lines = new ArrayList<String>();
        boolean reached = false;
        int ite2 = 0;
        while (!reached) {           
            ArrayList<String> questions = k.getQuestion(questionType);
            for (int i = 0; i < questions.size(); i++) {
                String question = questions.get(i);
                System.out.println("question ("+i+")="+question+ " "+(question).split(Pattern.quote(".")).length+" size questions ="+questions.size());
                String[] tab = question.split(Pattern.quote("."));
                for (int j = 0; j < tab.length; j++) {
                    System.out.println(j+" -"+tab[j]);
                }
                String numero = tab[0];
                boolean exist = false;
                for (int j = 0; j < lines.size(); j++) {
                    String validQuestion = lines.get(j);
                    if (validQuestion.startsWith(numero))
                        exist = true;
                }
                if (!exist)
                    lines.add(question);
            }
            if (ite2 >= iteMax)
                reached = true; 
            ite2++;
        }
        return lines;
    }
    
    public ArrayList<String> KetrikaTestAllIteration(int iteMax) throws FileNotFoundException, Exception {
        KetrikaAlg k = new KetrikaAlg();
        if (this.filenameTemplate == null)
            k.init();
        else k.init(filenameTemplate, filenameOntology);
        
        ArrayList<String> lines = new ArrayList<String>();
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.DEFINE_QUESTIONS ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.EQUIVALENCE_QUESTIONS ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.TRUE_FALSE_QUESTIONS ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.TRUE_FALSE_QUESTIONS_QUANTIFIED ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.WHAT_QUESTIONS_QUANTIFIED ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.WHAT_QUESTIONS_TYPE1 ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.WHAT_QUESTIONS_TYPE2 ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.YES_NO_QUESTION_TYPE1 ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.YES_NO_QUESTION_TYPE1_QUANTIFIED ));
        
        lines.addAll(KetrikaTestIteration(k, iteMax, KetrikaAlg.YES_NO_QUESTION_TYPE2 ));
        
        System.out.println("solution [begin]");
        for (int i = 0; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }
         System.out.println("solution [end]");
        return lines;
    }
}
