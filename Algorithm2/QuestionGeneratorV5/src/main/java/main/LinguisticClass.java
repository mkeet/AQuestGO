/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package main;

/**
 *
 * @author toky.raboanary
 */


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
  import simplenlg.framework.*;
  import simplenlg.lexicon.*;
  import simplenlg.realiser.english.*;
  import simplenlg.phrasespec.*;
  import simplenlg.features.*;

public class LinguisticClass {
    private static Lexicon lexicon;
    private static NLGFactory nlgFactory;
    private static Realiser realiser;
    
    static String[] preps= { // not used (to get preposition we have used SimpleNLG)
        "across",
        "after",
        "against",
        "along",
        "around",
        "away",
        "back",
        "by",
        "down", 
        "for",
        "forward",
        "in",
        "into",
        "off",
        "on",
        "out",
        "over",
        "through",
        "to",
        "together",
        "up",
        "with"
    };
    
    static ArrayList<String> uncountableNouns;
    
    
    static {
        LinguisticClass.lexicon = Lexicon.getDefaultLexicon();
        LinguisticClass.nlgFactory = new NLGFactory(lexicon);
        LinguisticClass.realiser = new Realiser(lexicon);   
        initUncountableNouns();
    }
  //  
    public static boolean isUncountableNoun(String word) {

        for (String noun : uncountableNouns) {
            if (noun.equals(word))
                return true;
        }
        return false;
    }
    
    public static boolean isCountableNoun(String word) {
        return !isUncountableNoun(word);
    }
    
    
    public static boolean isIs(String word) {
        return word.toLowerCase().compareTo("is") == 0;
    }
    
    public static boolean isHas(String word) {
        return word.toLowerCase().compareTo("has") == 0;
    }
    
    public static boolean isVerb(String word) {
        return isVerbStanford(word);
    }
    
    public static boolean isVerbWordNet (String word) {
        
        try {
            IndexWord dog = Singleton.getDicoWordNet().getIndexWord(POS.VERB, word);
            if (dog == null)
                return false;
            return true;
        } catch (JWNLException ex) {
            ex.printStackTrace();
            Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public static boolean isOnlyVerbWordNet (String word) {
        if (isVerbWordNet(word) && !isNounWordNet(word)) {
            return true;
        }
        return false;
    }
    public static boolean isOnlyNounWordNet (String word) {
        if (!isVerbWordNet(word) && isNounWordNet(word)) {
            return true;
        }
        return false;
    }
    public static boolean isNounWordNet (String word) {
        
        try {
            IndexWord dog = Singleton.getDicoWordNet().getIndexWord(POS.NOUN, word);
            if (dog == null)
                return false;
            return true;
        } catch (JWNLException ex) {
            ex.printStackTrace();
            Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }    
    public static boolean isAdjWordNet (String word) {
        
        try {
            IndexWord dog = Singleton.getDicoWordNet().getIndexWord(POS.ADJECTIVE, word);
            if (dog == null)
                return false;
            return true;
        } catch (JWNLException ex) {
            ex.printStackTrace();
            Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
        
    public static boolean isVerbInfinitive(String word) throws Exception {
        
        if (isVerbStanford(word)) {
            if (word.equals(LinguisticClass.getInfinitiveWithoutTo(word)))
                return true;
        }
        return false;
    }
    
    public static boolean isVerbPast(String word) throws Exception {
        if (isVerbStanford(word)) {
            if (word.equals(LinguisticClass.getPast(word)))
                return true;
        }
        return false;
    }
    
    public static boolean isVerbPastParticiple(String word) throws Exception {
        if (isVerbStanford(word)) {
            if (word.equals(LinguisticClass.getPastParticiple(word)))
                return true;
        }
        return false;
    }
    
    public static boolean isAdjectiveParticiple(String word) throws Exception {
        return isVerbPastParticiple(word) || isVerbPresentParticiple(word) || isAdjective(word);
    }
    
    public static boolean isVerbPresentParticiple(String word) throws Exception {
        if (isVerbStanford(word)) {
            if (word.equals(LinguisticClass.getPresentParticiple(word)))
                return true;
        }
        return false;
    }
    public static boolean isVerbPresent3s(String word) throws Exception {
        if (isVerbStanford(word)) {
            if (word.equals(LinguisticClass.getThirdPerson(word)))
                return true;
        }
        return false;
    }   
    
    public static boolean isDeterminer(String word) {
        String s = word.toLowerCase();
        return s.compareTo("a") == 0 || s.compareTo("an") == 0 || s.compareTo("the") == 0;
    }
    
    public static boolean isNoun(String word) {
      /*  WordElement wElt = lexicon.lookupWord(word);
        TokenType tokenType = getTokenType(wElt, word);
        return tokenType == TokenType.NOUN; */
      
      String text = "It has "+word;
        CoreDocument coreDocument = new CoreDocument(text);
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        coreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        CoreLabel nn = coreLabels.get(2);
        String sTest = nn.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        if (sTest.startsWith("NN")) {
            return true;
        }
        return false;
    }
    public static boolean isPreposition(String word) {
        //System.out.println(" prep = " +word );
        WordElement wElt = lexicon.lookupWord(word);
        TokenType tokenType = getTokenType(wElt, word);
        return tokenType == TokenType.PREPOSITION;
    }
    
    public static boolean isAdjective(String word) {
        //WordElement wElt = lexicon.lookupWord(word);
        //TokenType tokenType = getTokenType(wElt, word);
        //return tokenType == TokenType.ADJECTIVE;
        
        String text = "It has "+word+ " thing";
        CoreDocument coreDocument = new CoreDocument(text);
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        coreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        CoreLabel nn = coreLabels.get(2);
        String sTest = nn.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        if (sTest.startsWith("JJ")) {
            return true;
        }
        return false;
    }
    
    public static boolean isNounDetPrep(String word) {
        return isNoun(word) || isDeterminer(word) || isPreposition(word);
    }
    
    public static String getGerund(String verb) {
        String[] partsOfVerb = verb.split(" ");
        
        String verbPart = partsOfVerb[0];
        String restOfVerb = "";
        for (int i = 1; i < partsOfVerb.length; i++) {
            restOfVerb += partsOfVerb[i]+" ";
        }
       
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verbPart);        
        verb3.setFeature(Feature.PROGRESSIVE, true);        
        String resultVerb3 = TestSimpleNLG.realiser.realise(verb3).getRealisation();
        return (resultVerb3+ " "+restOfVerb).trim();
    }
    
    private static boolean isPrep(String s) {
        for (int i = 0; i < preps.length; i++) {
            if (preps[i].equals(s))
                return true;
        }
        return false;
    }
    
    public static boolean isVerbStanford(String s) {
        String text = "It "+s+" that";
        CoreDocument coreDocument = new CoreDocument(text);
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        coreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();
        CoreLabel cVerb = coreLabels.get(1);
        String sTest = cVerb.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        
        if (sTest.startsWith("VB")) {
            return true;
        }
        return false;
    }
    public static TokenType getTokenType(WordElement wElement, String word) {
        String category = wElement.getCategory().toString().toLowerCase();
        
        //System.out.println("category = "+category+" realisation -> "+wElement.getBaseForm());
        if (isPrep(word.toLowerCase()))
            return TokenType.PREPOSITION;
        else if(isVerbStanford(word))
            return TokenType.VERB;
        else if (category.compareTo("noun") == 0)
            return TokenType.NOUN;
        else if (category.compareTo("preposition") == 0) {
            return TokenType.PREPOSITION;
       // } else if (category.compareTo("verb") == 0) {
       //     return TokenType.VERB;
        } else if (category.compareTo("determiner") == 0) {
            return TokenType.DETERMINER;
        }
        else if (category.compareTo("adjective") == 0) {
            return TokenType.ADJECTIVE;
        }
        else 
        {
            return TokenType.UNKNOWN;
        }
    }
    
    public static String getInfinitiveWithoutTo(String v) {
        
        String verb;
        Dictionary dicoWordNet = Singleton.getDicoWordNet();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }
        
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);     
        verb3.setFeature(Feature.PERSON, Person.FIRST);     
        String resultVerb3 = LinguisticClass.getRealiser().realise(verb3).getRealisation();

        return resultVerb3;
    }
    public static String getInfinitiveWithoutToManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = getInfinitiveWithoutTo(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }
    public static String getLemmaVerb (String v) {
        try {
            
                Dictionary dicoWordNet = Singleton.getDicoWordNet();
                IndexWordSet lookupAllIndexWords = dicoWordNet.lookupAllIndexWords(v);
                IndexWord[] indexWordArray = lookupAllIndexWords.getIndexWordArray();

                System.out.println("Explination : "+ lookupAllIndexWords.toString());
            
                for (IndexWord indexWord : indexWordArray) {
                    System.out.println("\n array processing :-) ");
                    System.out.println(indexWord.getPOS()+" -> "+ indexWord.getLemma());
                    if (indexWord.getPOS() == POS.VERB) {
                        return indexWord.getLemma();
                    }
                }
            }
        
        catch(JWNLException ex) {
            Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }
    public static String getThirdPerson(String v) {
       
        
            String verb;
            Dictionary dicoWordNet = Singleton.getDicoWordNet();
            
            try {
                IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
                verb = indexWord.getLemma();
                System.out.println("iiiiii ->>>>>>> tokony ho hita amin'iozay o :) ");
            }
            catch(Exception e) {
                verb = v;
            }
            if(true) { //v.startsWith("inh")
                try {
                System.out.println(v+ " ----------------------- (startswith inh) "+v+" -> "+verb);
                System.out.println("Verb = "+isVerbWordNet(v));
                System.out.println("Verb = "+isNounWordNet(v));

                IndexWordSet lookupAllIndexWords = dicoWordNet.lookupAllIndexWords(v);
                IndexWord[] indexWordArray = lookupAllIndexWords.getIndexWordArray();
                
                for (IndexWord indexWord : indexWordArray) {
                System.out.println("\n array processing :-) ");
                System.out.println(indexWord.getPOS()+" -> "+ indexWord.getLemma());
                }

                IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, verb);
                if (indexWord != null)
                System.out.println(v+" -> "+indexWord.toString());
                else System.out.println(v+" is not a verb :-)");
                } catch (JWNLException ex) {
                Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
            
            VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);
            verb3.setFeature(Feature.TENSE, Tense.PRESENT);
            verb3.setFeature(Feature.PERSON, Person.THIRD);
            verb3.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
            String resultVerb3 = LinguisticClass.getRealiser().realise(verb3).getRealisation();
            return resultVerb3;
        
       /* if(true) { //v.startsWith("inh")
            try {
                System.out.println(v+ " ----------------------- (startswith inh) "+v+" -> "+verb);
                System.out.println("Verb = "+isVerbWordNet(v));
                System.out.println("Verb = "+isNounWordNet(v));
                
                
                for (IndexWord indexWord : indexWordArray) {
                    System.out.println("\n array processing :-) ");
                    System.out.println(indexWord.getPOS()+" -> "+ indexWord.getLemma());
                }
                
                IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, verb);
                if (indexWord != null)
                    System.out.println(v+" -> "+indexWord.toString());
                else System.out.println(v+" is not a verb :-)");
            } catch (JWNLException ex) {
                Logger.getLogger(LinguisticClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }*/
    }
    public static String getThirdPersonManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = getThirdPerson(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }
    public static String getPast(String v) {
        String verb;
        Dictionary dicoWordNet = Singleton.getDicoWordNet();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }
        
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.TENSE, Tense.PAST);     
        verb3.setFeature(Feature.PERSON, Person.THIRD);   
        verb3.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        String resultVerb3 = LinguisticClass.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }
    
    public static String getPastManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = getPast(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }    
    
    public static String getPastParticiple(String v) {
        
        String verb;
        Dictionary dicoWordNet = Singleton.getDicoWordNet();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }
        
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);     
        String resultVerb3 = LinguisticClass.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }
    
    public static String getPastParticipleManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = getPastParticiple(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }
    public static String getPresentParticiple(String v) {
        String verb;
        Dictionary dicoWordNet = Singleton.getDicoWordNet();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }
        
        
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);        
        verb3.setFeature(Feature.FORM, Form.GERUND);     
        String resultVerb3 = LinguisticClass.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }    
    
    public static String getPresentParticipleManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = getPresentParticiple(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }   
    
    public static String getRemovedIs (String verb) {
        if (verb.startsWith("is")&& verb.length()>2) {
            return verb.substring(2).trim();
        }
        return verb;
    }
    
    public static String removeUrl(String input){
        String output = "";
        
        // return only chars of the '#'
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int x = input.indexOf('>');
            if (c == '#') {
                output = input.substring(i+1, x);
            }
        }
        return output;
    }
    public static String stringProcess (String input){
        //System.out.println(input);
        String output= removeUrl(input);
        output = addSpaceBeforeMaj(output);
        output = output.replace("-", " ");
        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }
    public static String stringProcessNormal (String input){
        
        String output= input;
        output = addSpaceBeforeMaj(output);
        output = output.replace("-", " ");
        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }
    
    
    public static String normalizeSpacing(String input) {
        String output = "";
        for (int i = 0; i<input.length(); i++) {
            char c = input.charAt(i);
            if (c==' '){
                int j = i+1;
                char d = input.charAt(j);
                if (j<input.length() && d==' ')
                    continue;
                else output+=""+c;
                    
            }else
                output+=""+c;
        }
        return output;
    }
    public static void initUncountableNouns2() { // iiii naive way :) of course, I will load it from a list of uncountable noun
        uncountableNouns = new ArrayList<>();
        uncountableNouns.add("water");
        uncountableNouns.add("phloem");
        uncountableNouns.add("land");
        uncountableNouns.add("money");
        uncountableNouns.add("universe");
        uncountableNouns.add("life");
        uncountableNouns.add("canonicity");
        uncountableNouns.add("land");
        uncountableNouns.add("xylem");
    }
    public static void initUncountableNouns() { 
        uncountableNouns = new ArrayList<>();
        
        String filename = "src/uncountableNouns.txt";
        File fTemp = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(fTemp);
            while (scanner.hasNext()){
            String line = scanner.nextLine();
            uncountableNouns.add(line);
            
            System.out.println(line);
        }
        } catch (FileNotFoundException ex) {
            initUncountableNouns2();
        }
        
    }   
    public static String addSpaceBeforeMaj(String input) { // example PlantParts -> Plant Parts
        String output =""+input.charAt(0);
        
        
        for (int i = 1; i<input.length(); i++) {
            String caractere = ""+input.charAt(i);
            if (caractere.compareTo(" ")!=0 && caractere.compareTo(caractere.toUpperCase())== 0) {
                output+=" ";
            }
            output+=caractere;
        }
        return output;
    }
    
    
    public static NLGFactory getNLGFactory() {
        return LinguisticClass.nlgFactory;
    }
    public static Lexicon getLexicon() {
        return LinguisticClass.lexicon;
    }
    public static Realiser getRealiser() {
        return LinguisticClass.realiser;
    }
}
