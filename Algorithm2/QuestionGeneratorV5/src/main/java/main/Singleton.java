/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author toky.raboanary
 */
public class Singleton {
   
    private static Ontology ontology;
    
    public static void setOntology(Ontology ontology) {
        Singleton.ontology = ontology;
    }
    public static Ontology getOntology() throws OWLOntologyCreationException, FileNotFoundException {
        String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology1.owl";
        return getOntology(ontologyPath);
    }
    public static Ontology getOntology(String ontologyPath) throws OWLOntologyCreationException, FileNotFoundException {
        if (Singleton.ontology == null) {
            Singleton.ontology = new Ontology();
            
             //String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/movieontology.owl";
            //String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/btl2.owl";
           // String ontologyPath = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/AfricanWildlifeOntology1.owl";
            // String ontologyPath  = "/Users/toky.raboanary/Researches/PhD UCT/FAIR2019_Questions generation/From Steve/stuff3.owl";
            Singleton.ontology.loadOntology(ontologyPath);
        }
        return Singleton.ontology;
    }
    
    private static Dictionary dico ;
    
    public static Dictionary getDicoWordNet(){
        try {
        if (dico == null)
            dico = Dictionary.getDefaultResourceInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return dico;
    }
    
    public static String getFormatted(String definition) throws Exception {
        Entity thing = getOntology().getEntity("Thing");
        String prefix = thing.getLabel().split("#")[0];
        prefix = prefix.substring(1);
        //System.out.println("PREFIX = ");
        return definition.replace(prefix+"#", "");
        
    }
}
