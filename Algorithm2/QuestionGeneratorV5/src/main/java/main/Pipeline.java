/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 *
 * @author toky.raboanary
 */
public class Pipeline {
    private static Properties properties;
    private static String propertiesName = "tokenize, ssplit, pos, lemma";
    private static StanfordCoreNLP stanfordCoreNLP;
    
    private Pipeline() {
        
    }
    static {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
    }
    public static StanfordCoreNLP getPipeline()
    {
        if (stanfordCoreNLP == null)
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        //stanfordCoreNLP.
        return stanfordCoreNLP;
    }
   
    
}
