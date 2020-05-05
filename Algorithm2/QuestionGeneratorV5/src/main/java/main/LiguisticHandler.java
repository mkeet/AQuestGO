/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 *
 * @author stevewang
 */
public class LiguisticHandler {

    public static String[] wordList (String template){
        String [] words = template.split(" ");
        return words;
    }

    static ArrayList<String> getTokens(String[] words) {
        ArrayList<String> tokens = new ArrayList<>();
        for (String word : words) {
            if (word.charAt(0)=='<' && word.charAt(word.length()-1)=='>'){
                tokens.add(word.substring(1, word.length()-1));
            
            // In cases of tokens at the end of the sentence
            }else if (word.charAt(0)=='<' && word.charAt(word.length()-2)=='>'){
                tokens.add(word.substring(1, word.length()-2));
            }
        }
        return tokens;
    }
    
    /**
     * Process String representations of OWLClass and OWLObjectPorperty to understandable word
     * @param input
     * @return 
     */
    public static String getClassName(String input)
    {
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
        String output = "";
        
        // return only chars of the '#'
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int x = input.indexOf('>');
            if (c == '#') {
                output = input.substring(i+1, x);
            }
        }
        output = addSpaceBeforeMaj(output);
        
        output = output.replace("-", " ");
        
        
        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }
    public static String stringProcessSimple (String input){
        String output = input;
        
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
    /**
     * Assuming all articles in the template is either "The" or "a"
     * replace "a" with "an" if the word follows starts with a vow
     * otherwise append " "+newWord to the sentence
     * and return the new sentence
     * @param output
     * @param newWord
     * @return new sentence
     */
    
    public static String removeArticleA(String output, String newWord) {
        String res = "";
        if (output.length()!=0){
                if (output.substring( output.length()-1, output.length()).equals("A")) {
                    String firstLetter = ""+newWord.charAt(0);
                    if (newWord.length()>1)
                        newWord = firstLetter.toUpperCase()+newWord.substring(1);
                    else newWord = newWord.toUpperCase();
                }
                res = output.substring(0, output.length()-1)+newWord;
        }
            else  { 
            String firstLetter = ""+newWord.charAt(0);
            if (newWord.length()>1)
                newWord = firstLetter.toUpperCase()+newWord.substring(1);
            else newWord = newWord.toUpperCase();
            
            res = newWord;
        }
        return res;
    }
    public static String removeArticleSome(String output, String newWord) {
        String res = "";
        if (output.length()!=0){
                if (output.substring( output.length()-4, output.length()).equals("Some")) {
                    String firstLetter = ""+newWord.charAt(0);
                    if (newWord.length()>1)
                        newWord = firstLetter.toUpperCase()+newWord.substring(1);
                    else newWord = newWord.toUpperCase();
                }
                res = output.substring(0, output.length()-4)+newWord;
        }
            else  { 
            String firstLetter = ""+newWord.charAt(0);
            if (newWord.length()>1)
                newWord = firstLetter.toUpperCase()+newWord.substring(1);
            else newWord = newWord.toUpperCase();
            
            res = newWord;
        }
        return res;
    }
    
    public static String checkArticle(String output, String article,String newWord) {
        //if the newWord strats with a vow and the article before it is not "The" or "the"
        //System.out.println("ato");
        
        // old checkVow(newWord) && 
         try {
            Entity perdurant = Singleton.getOntology().getEntity("Perdurant");
            String classMask = (newWord.charAt(0)+"").toUpperCase()+""+newWord.substring(1);
            
            System.out.println("");
            System.out.println("MISY perdurant =====>>>>> "+ newWord);
            System.out.println(" clas mask "+ classMask);
            
            Entity entNewWord = Singleton.getOntology().getEntity(classMask);
            
            
            //entNewWord.getEntityDefinition().Print();
            //entNewWord.getEntityDefinition().Print();
            if (entNewWord.isA(perdurant)) {
                System.out.println("FLY IS A PERDURANT");
                entNewWord.getEntityDefinition().Print();
                //System.out.println(entNewWord.);
                if (LinguisticClass.isOnlyNounWordNet(newWord)) {
                    newWord = "being "+LiguisticHandler.checkArticle2("a", newWord);
                    if (article.toLowerCase().equals("a")) {
                        newWord=""+ LinguisticClass.getPresentParticiple(newWord);
                        return removeArticleA(output, newWord);
                    }
                    if (article.toLowerCase().equals("<quantifier-some>") || article.toLowerCase().equals("<quantifier-only>")) {
                        newWord=""+ LinguisticClass.getPresentParticiple(newWord);
                        return removeArticleSome(output, newWord);
                    } 
                }
                if (LinguisticClass.isVerbWordNet(newWord)) {
                    
                     System.out.println("FLY IS a verb "+ article);
                    // newWord = LinguisticClass.getGerund(newWord);
                     if ( article.toLowerCase().equals("a")) {
                        newWord=""+LinguisticClass.getPresentParticiple(newWord);
                        return removeArticleA(output, newWord);
                    }
                    if (article.toLowerCase().equals("<quantifier-some>") || article.toLowerCase().equals("<quantifier-only>")) {
                        newWord=LinguisticClass.getPresentParticiple(newWord);
                        return ""+removeArticleSome(output, newWord);
                    } 
                }
            }
        }
        catch (Exception e) {
               e.printStackTrace();
        }
        
        if (LinguisticClass.isUncountableNoun(newWord) && article.toLowerCase().equals("a")) {
            return removeArticleA(output, newWord);
        }
        
        if (LinguisticClass.isOnlyVerbWordNet(newWord) && article.toLowerCase().equals("a")) {
            newWord=LinguisticClass.getGerund(newWord);
            return removeArticleA(output, newWord);
        }
        if (LinguisticClass.isOnlyVerbWordNet(newWord) && article.toLowerCase().equals("some")) {
            newWord=LinguisticClass.getGerund(newWord);
            return removeArticleSome(output, newWord);
        }  
       
        
        if ( (article.equals("a")) ) {
            // Change the article to "an"
            
            NPPhraseSpec subject = LinguisticClass.getNLGFactory().createNounPhrase(article, newWord);
            if (output.length()!=0)
                output = output.substring(0, output.length()-1) + LinguisticClass.getRealiser().realise(subject).getRealisation();
            else  {
                String res = LinguisticClass.getRealiser().realise(subject).getRealisation();
                String firstLetter = ""+res.charAt(0);
                output = firstLetter.toUpperCase()+res.substring(1);
            }
            // System.out.println(article+" "+newWord+" -> "+output);
            
            //output = output.substring(0, output.length()-1) + "an "+newWord;
        } 
        else if (article.equals("A")) {
            NPPhraseSpec subject = LinguisticClass.getNLGFactory().createNounPhrase(article.toLowerCase(), newWord);
            
            String result = LinguisticClass.getRealiser().realise(subject).getRealisation();
            if (result.length()>1) {
                String firstLetter = ""+result.charAt(0);
                result = firstLetter.toUpperCase()+result.substring(1);
            }
            else result = result.toUpperCase();
            if (output.length()!=0)
                output = output.substring(0, output.length()-1) + result;
            else  {
                String res = result;
                String firstLetter = ""+res.charAt(0);
                output = firstLetter.toUpperCase()+res.substring(1);
            }
            
            // System.out.println(article+" "+newWord+" -> "+output);
        } 
        else if (article.equals("")){
            String firstLetter = ""+newWord.charAt(0);
            output = firstLetter.toUpperCase()+newWord.substring(1);
        }else{
            output = output + " " + newWord; 
        }
        return output;
    }
    
    public static String checkArticle2( String article,String newWord) {
        //if the newWord strats with a vow and the article before it is not "The" or "the"
        String output;
        
        NPPhraseSpec subject = LinguisticClass.getNLGFactory().createNounPhrase(article, newWord);
        String res = LinguisticClass.getRealiser().realise(subject).getRealisation();
        System.out.println("checkArticle2 : "+article+" "+newWord+" -> "+res);
        return res;
        
    }
    
    public static String getRightArticle(String article, String newWord) {
        //if the newWord strats with a vow and the article before it is not "The" or "the"
        String output;
        
        NPPhraseSpec subject = LinguisticClass.getNLGFactory().createNounPhrase(article, newWord);
        String res = LinguisticClass.getRealiser().realise(subject).getRealisation();
        return res.split(" ")[0];
    }
    
    /**
     * Checks if the given word starts with a vow or not
     * @param word
     * @return boolean: true if word starts with a vow and
     * false if the word doesn't start with a vow
     */
    public static boolean checkVow(String word){
        if (word.charAt(0)=='a' || word.charAt(0)=='e' || word.charAt(0)=='i' || word.charAt(0)=='o' || word.charAt(0)=='u'
                || word.charAt(0)=='A' || word.charAt(0)=='E' || word.charAt(0)=='I' || word.charAt(0)=='O' || word.charAt(0)=='U') {
            return true;
        } else {
            return false;
        }
    }
    
    public static PropertyType getPropertyType(String template) {
        PropertyType pt = PropertyType.OP_UNKNOWN;
        if (template.contains("<OP_VERB>")) {
            pt = PropertyType.OP_VERB;
        }else if (template.contains("<OP_VERB_PREP>")) {
            pt = PropertyType.OP_VERB_PREP;
        }else if (template.contains("<OP_HAS_NOUNS>")) {
            pt = PropertyType.OP_HAS_NOUNS;
        }else if (template.contains("<OP_IS_NOUNS_PREP>")) {
            pt = PropertyType.OP_IS_NOUNS_PREP;
        }else if (template.contains("<OP_IS_PARTICIPLE_BY>")) {
            pt = PropertyType.OP_IS_PAST_PARTICIPLE_BY;
        }else if (template.contains("<OP_IS_PARTICIPLE_PREP>")) {
            pt = PropertyType.OP_IS_PAST_PARTICIPLE_PREP;
        }
        return pt;
    }
    
    
    public static QuestionType getQuestionType(String template) {
        QuestionType questionType = QuestionType.NOT_A_QUESTION_VERB;
        PropertyType propertyType = getPropertyType(template);
        
        String [] words = template.toLowerCase().split(" ");
        String firstWord = words[1];
        String secondWord = words[2];
        
        if ((firstWord.compareTo("does") == 0)||(firstWord.compareTo("do") == 0)) {
            questionType = QuestionType.YES_NO_VERB;
        }
        else if ((firstWord.compareTo("is") == 0)||(firstWord.compareTo("are") == 0)) {
            questionType = QuestionType.YES_NO_IS;
        }
        else if (firstWord.compareTo("which") == 0 || firstWord.compareTo("what") == 0) {
            if (secondWord.compareTo("does")==0)
                questionType = QuestionType.WHAT_DOES;
            else if (secondWord.compareTo("is")==0)
                questionType = QuestionType.WHAT_IS;
            else {
                if (propertyType == PropertyType.OP_IS_NOUNS_PREP || propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_BY
                        || propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_PREP )
                    questionType = QuestionType.WHAT_SUBJECT_IS;
                else 
                    questionType = QuestionType.WHAT_SUBJECT_VERB;
                }
        }
        else if (questionType == QuestionType.NOT_A_QUESTION_VERB) {
            if (propertyType == PropertyType.OP_IS_NOUNS_PREP || propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_BY
                        || propertyType == PropertyType.OP_IS_PAST_PARTICIPLE_PREP )
                    questionType = QuestionType.NOT_A_QUESTION_IS;
        }
        return questionType;
    }
    
    public static QuestionType getQuestionTypeOld2(String template) {
        String [] words = template.toLowerCase().split(" ");
        String wordVerbIs = "<OP_IS_";
        String firstWord = words[1];
        String secondWord = words[2];
        QuestionType questionType = QuestionType.NOT_A_QUESTION_VERB;
        if ((firstWord.compareTo("does") == 0)||(firstWord.compareTo("do") == 0)) {
           questionType = QuestionType.YES_NO_VERB;
        }
        else if ((firstWord.compareTo("is") == 0)||(firstWord.compareTo("are") == 0)) {
            questionType = QuestionType.YES_NO_IS;
            /*int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].contains(wordVerbIs))
                {
                    indiceProp = i; 
                    break;
                }                
            }
            if (indiceProp == -1)
                questionType = QuestionType.YES_NO_WITHOUT_PROPERTY;*/
            
        }
        else if (firstWord.compareTo("what") == 0) {
            if (secondWord.compareTo("does")==0)
                questionType = QuestionType.WHAT_DOES;
            else if (secondWord.compareTo("is")==0)
                questionType = QuestionType.WHAT_IS;
            else {
                
                questionType = QuestionType.WHAT_SUBJECT_VERB;
                
                // cheching if the property is a NonVerb
                int indiceProp = -1;
                for (int i = 0; i < words.length; i++) {
                    if(words[i].contains(wordVerbIs))
                    {
                        if (words[i-1].compareTo("is")== 0) {
                            indiceProp = i; 
                            break;
                        }
                    }
                }
                if (indiceProp != -1)
                    questionType = QuestionType.WHAT_SUBJECT_IS; 
                }
        }
        else if (firstWord.compareTo("true") == 0) {
            questionType = QuestionType.TRUE_FALSE_VERB;
            
            // cheching if the property is a NonVerb
            
            int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].compareTo(wordVerbIs) == 0)
                {
                    if (words[i-1].compareTo("is")== 0) {
                        indiceProp = i; 
                        break;
                    }
                }
            }
            if (indiceProp != -1)
                questionType = QuestionType.TRUE_FALSE_IS;            
        }
        if (questionType == QuestionType.NOT_A_QUESTION_VERB) {
            
            int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].compareTo(wordVerbIs) == 0)
                {
                    if (words[i-1].compareTo("is") == 0) {
                        indiceProp = i; 
                        break;
                    }
                }
            }
            if (indiceProp != -1)
                questionType = QuestionType.NOT_A_QUESTION_IS;   
        }
        return questionType;
    }
    
    public static PropertyType getPropertyTypeOld(String property) throws Exception { // with url
        PropertyType propertyType = PropertyType.OP_UNKNOWN;
        
        String propWithSpaces = stringProcess(property);
        String[] eltsProp = propWithSpaces.toLowerCase().split(" ");
        
        String firstWord = eltsProp[0];
        
        if (LinguisticClass.isIs(firstWord)) {
            if (eltsProp.length == 1) {
                propertyType = PropertyType.OP_UNKNOWN;
            }
            else {              
                
                String secondWord = eltsProp[1];
                String thirdWord = eltsProp[2];
                // checking if it as the form is-a-part-of
                // it means that the second part composed by article or noun or preposition (and the last one is a preposition)
                
                if (LinguisticClass.isVerbPastParticiple(secondWord)) {
                    if (thirdWord.compareTo("by") == 0 && eltsProp.length == 3) {
                        propertyType = PropertyType.OP_IS_PAST_PARTICIPLE_BY;
                    } else if (thirdWord.compareTo("by") == 0 && eltsProp.length == 4) {
                        String forthWord = eltsProp[3];
                        if (LinguisticClass.isPreposition(forthWord)){
                            propertyType = PropertyType.OP_IS_PAST_PARTICIPLE_BY;
                        }
                    }
                }
                else {
                    boolean isAllNounDetForm = false;
                    for (int i = 1; i < eltsProp.length; i++) {
                        String elt = eltsProp[i];
                        isAllNounDetForm = LinguisticClass.isNounDetPrep(elt);
                    }
                    String lastWord = eltsProp[eltsProp.length-1];
                    if(LinguisticClass.isPreposition(lastWord)) {
                        propertyType = PropertyType.OP_IS_NOUNS_PREP;
                    }
                    else {
                        /// form is-nouns (unknown)
                    }
                }
            }
        }
        else if (LinguisticClass.isHas(firstWord)) {
            boolean isAllNounDetForm = false;
            for (int i = 1; i < eltsProp.length; i++) {
                String elt = eltsProp[i];
                isAllNounDetForm = LinguisticClass.isNounDetPrep(elt);
            }
            if (isAllNounDetForm) { // eltsProp.length > 1 and all of them are noun or det or form
                
            }
        }
        else if (LinguisticClass.isVerb(firstWord)) {
            
        }
        else {
            propertyType = PropertyType.OP_UNKNOWN;
        }
        
        return propertyType;
    }
    
    public static QuestionType getQuestionTypeOld(String template) {
        String [] words = template.toLowerCase().split(" ");
        String wordNonVerb = "<objectproperty:nonverb>";
        String firstWord = words[1];
        String secondWord = words[2];
        QuestionType questionType = QuestionType.NOT_A_QUESTION_VERB;
        if ((firstWord.compareTo("does") == 0)||(firstWord.compareTo("do") == 0)) {
           questionType = QuestionType.YES_NO_VERB;
        }
        else if ((firstWord.compareTo("is") == 0)||(firstWord.compareTo("are") == 0)) {
            questionType = QuestionType.YES_NO_IS;
            int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].compareTo(wordNonVerb) == 0)
                {
                    indiceProp = i; 
                    break;
                }                
            }
            if (indiceProp == -1)
                questionType = QuestionType.YES_NO_WITHOUT_PROPERTY;
            
        }
        else if (firstWord.compareTo("what") == 0) {
            if (secondWord.compareTo("does")==0)
                questionType = QuestionType.WHAT_DOES;
            else if (secondWord.compareTo("is")==0)
                questionType = QuestionType.WHAT_IS;
            else {
                
                questionType = QuestionType.WHAT_SUBJECT_VERB;
                
                // cheching if the property is a NonVerb
                int indiceProp = -1;
                for (int i = 0; i < words.length; i++) {
                    if(words[i].compareTo(wordNonVerb) == 0)
                    {
                        if (words[i-1].compareTo("is")== 0) {
                            indiceProp = i; 
                            break;
                        }
                    }
                }
                if (indiceProp != -1)
                    questionType = QuestionType.WHAT_SUBJECT_IS; 
                }
        }
        else if (firstWord.compareTo("true") == 0) {
            questionType = QuestionType.TRUE_FALSE_VERB;
            
            // cheching if the property is a NonVerb
            
            int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].compareTo(wordNonVerb) == 0)
                {
                    if (words[i-1].compareTo("is")== 0) {
                        indiceProp = i; 
                        break;
                    }
                }
            }
            if (indiceProp != -1)
                questionType = QuestionType.TRUE_FALSE_IS;            
        }
        if (questionType == QuestionType.NOT_A_QUESTION_VERB) {
            
            int indiceProp = -1;
            for (int i = 0; i < words.length; i++) {
                if(words[i].compareTo(wordNonVerb) == 0)
                {
                    if (words[i-1].compareTo("is") == 0) {
                        indiceProp = i; 
                        break;
                    }
                }
            }
            if (indiceProp != -1)
                questionType = QuestionType.NOT_A_QUESTION_IS;   
        }
        return questionType;
    }
    
    public static String generateType1Sentence(Type1ResultSet result, String template) {
        // Initiate the output string to noting 
        String line = "";
        
        // Create a String array that contains only the words of the sentence.
        String [] words = template.split(" ");
        
        // Initial the article to nothing 
        String article = "";
        
        // Create integer count to keep track of which part of the result to be used
        int count = 0;
       
        QuestionType questionType = getQuestionType(template);
        System.out.println("QUESTION_TYPE = "+questionType);
        
        // For each for in the words array 
        for (int i = 0; i<words.length; i++) {
           
            // Update the article to the previous word
            if (i!=0){
                article = words[i-1];
            }
            
            // If the word is a token in the middle of the sentence
            if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-1)=='>'){
                
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType1Word(count, result, questionType));
                 count ++;
                
            // If the word is a token at the end of the sentence
            }else if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-2)=='>'){
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType1Word(count, result, questionType)) + words[i].charAt(words[i].length()-1);                
                count ++;
                
            
            // If it is the first word in the sentence
            }else if (line.equals("")){
                 line = words[i];
                 
            
            // if it is any other word
            }else{
                
                line = line + " " + words[i];
            } 
        }
        
        // Make sure the first word in the sentence is a capital letter
        String firstLetter = ""+line.charAt(0);
        line = firstLetter.toUpperCase()+line.substring(1);
        
        
        return line;
    }


    private static String subType1Word(int count, Type1ResultSet result, QuestionType questionType) {
        // Type 1 result set is in the format of :
        //  OWLClass X, OWLClass Y, OWLObjectProperty objectProperty
        
        // If count is 0 then return OWLCLass X
        if (count == 0 ) {
            return stringProcess(result.getX().toString());
            
        // If count is 1 then return OWLObjectProperty objectProperty 
        } else if (count == 1 ) { 
            String property = result.getOP().toString();
            return processProperty(property, questionType);
            
        // If count is 2 the return OWLClass Y  
        } else if (count == 2 ) {
            return stringProcess(result.getY().toString());
            
        // If anything else then it is an invalid token
        } else{
            return "<invalid>";
        }
    }
    private static String processProperty (String property, QuestionType questionType) {
        //System.out.println("property = "+property.replace(' ', '-'));
        PropertyClassification pc = new PropertyClassification(property);
        System.out.println(pc.getInfo());
        String newProp = pc.getNewFormattedProperty();
        if (null != questionType) switch (questionType) {
                case YES_NO_VERB:
                case WHAT_DOES:
                {
                    //String propVerb = property;
                    String inf = LinguisticClass.getInfinitiveWithoutToManyWords(newProp);
                    return inf;
                }
                case YES_NO_IS:
                    //String propNonVerb = property;
                    //String res = checkArticle2("a", propNonVerb);
                    
                    String res = LinguisticClass.getRemovedIs(newProp);
                   // System.out.println("RES YES_NO -> "+res+" "+newProp);
                    return res;
                case TRUE_FALSE_VERB:
                case WHAT_SUBJECT_VERB:
                case NOT_A_QUESTION_VERB:
                {
                    //String propVerb = property;
                    String thirdPers = LinguisticClass.getThirdPersonManyWords(newProp);
                    return thirdPers;
                }
                case TRUE_FALSE_IS: 
                case NOT_A_QUESTION_IS:
                case WHAT_SUBJECT_IS:
                case WHAT_IS:
                {                    
                    //String propNonVerb2 = property;
                    //String res2 = checkArticle2("a", propNonVerb2); 
                    String res2 = LinguisticClass.getRemovedIs(newProp);
                    return res2;
                }
                case YES_NO_WITHOUT_PROPERTY:
                {
                    String propVerb = property;
                    String gerund = LinguisticClass.getGerund(propVerb);
                    return gerund+ ">>>>>>>>>>>>> forgotten :) ";
                }
                default:
                    return property;                    
            }
            else return property;                      
    }
    static String generateType2Sentence(Type2ResultSet result, String template) {
        // Initiate the output string to noting 
        String line = "";
        
        // Create a String array that contains only the words of the sentence.
        String [] words = template.split(" ");
        
        // Initial the article to nothing 
        String article = "";
        
        // Create integer count to keep track of which part of the result to be used
        int count = 0;
        QuestionType questionType = getQuestionType(template);
        // For each for in the words array 
        for (int i = 0; i<words.length; i++) {
            
            // Update the article to the previous word
            if (i!=0){
                article = words[i-1];
            }
            
            // If the word is a token in the middle of the sentence
            if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-1)=='>'){
                
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType2Word(count, result, questionType));
                count ++;
                
            // If the word is a token at the end of the sentence
            }else if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-2)=='>'){
                
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType2Word(count, result, questionType)) + words[i].charAt(words[i].length()-1);
                count ++;
            
            // If it is the first word in the sentence
            }else if (line.equals("")){
                line = words[i];
            
            // if it is any other word
            }else{
                line = line + " " + words[i];
            }
        }
        
        // Make sure the first word in the sentence is a capital letter
        String firstLetter = ""+line.charAt(0);
        line = firstLetter.toUpperCase()+line.substring(1);
        return line;
    }
    
    private static String subType2Word(int count, Type2ResultSet result, QuestionType questionType) {
        // Type 2 result set is in the format of :
        // OWLClass X, OWLClass Y, OWLObjectProperty objectProperty
        
        // If count is 0 then return OWLCLass X
        if (count == 0 ) {
            return stringProcess(result.getX().toString());
            
        // If count is 1 then return OWLClass Y
        } else if (count == 1 ) {
            String property = result.getY().toString();
            String res = processProperty(property, questionType);
            if (LinguisticClass.isOnlyNounWordNet(res))
            {
                res = "being "+checkArticle2("a", res);
            }
                
            return res;
            
        // If anything else then it is an invalid token
        } else{
            return "<invalid>";
        }
    }

    public static String generateType3Sentence(Type3ResultSet result, String template) {
        // Initiate the output string to noting 
        String line = "";
        
        // Create a String array that contains only the words of the sentence.
        String [] words = template.split(" ");
        
        // Initial the article to nothing 
        String article = "";
        
        // Create integer count to keep track of which part of the result to be used
        int count = 0;
        
        QuestionType questionType = getQuestionType(template);
        
        // For each for in the words array 
        for (int i = 0; i<words.length; i++) {
            
            // Update the article to the previous word
            if (i!=0){
                article = words[i-1];
            }
            
            // If the word is a token in the middle of the sentence
            if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-1)=='>'){
                
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType3Word(count, result, questionType));
                count ++;
                
            // If the word is a token at the end of the sentence
            }else if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-2)=='>'){
                
                // Check the article and replace with the correction section of the result 
                line = checkArticle(line, article, subType3Word(count, result, questionType)) + words[i].charAt(words[i].length()-1);
                count ++;
            
            // If it is the first word in the sentence
            }else if (line.equals("")){
                line = words[i];
            
            // if it is any other word
            }else{
                line = line + " " + words[i];
            }
        }
        
        // Make sure the first word in the sentence is a capital letter
        String firstLetter = ""+line.charAt(0);
        line = firstLetter.toUpperCase()+line.substring(1);
        return line;
    }
    
    private static String subType3Word(int count, Type3ResultSet result, QuestionType questionType) {
        // Type 3 result set is in the format of :
        // OWLClass X, OWLClass Y, OWLObjectProperty objectProperty, String Quantifier
        
        // If count is 0 then return OWLCLass X
        if (count == 0 ) {
            return stringProcess(result.getX().toString());
            
        // If count is 1 then return OWLObjectProperty objectProperty 
        } else if (count == 1 ) {
            String property = result.getOP().toString();
            return processProperty(property, questionType);
            
        // If count is 2 then return String quantifier
        } else if (count == 2 ) {
            return result.getQuantifier();
            
        // If count is 3 the return OWLClass Y
        } else if (count == 3 ) {
            return stringProcess(result.getY().toString());
            
        // If anything else then it is an invalid token
        } else{
            return "<invalid>";
        }
    }

    static String generateType5Sentence(Type5ResultSet result, String template) {
        // Initate the output to an empty String 
        String line = "";
        
        // Create a String array that contains only the words of the sentence.
        String [] words = template.split(" ");
        
        // Initiate article to an empty string
        String article = "";
        
        // Create a counter to keep track of which part of the result to use
        int count = 0;
        
        System.out.println(template);
        QuestionType questionType = getQuestionType(template);
        
        // For each word in the tempate
        for (int i = 0; i<words.length; i++) {
            
            // Update the article to the previous word
            if (i!=0){
                article = words[i-1];
            }
            
            // For token in the midddle of the sentence 
            if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-1)=='>'){
                
                // Check the article and replace token with the appropriate part of the result
                line = checkArticle(line, article, subType5Word(count, result,questionType));
                count ++;
                
            // For token at the end of the sentence
            }else if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-2)=='>'){
                
                // Check the article and replace token with the appropriate part of the result
                line = checkArticle(line, article, subType5Word(count, result, questionType)) + words[i].charAt(words[i].length()-1);
                count ++;
                
            // If it is the first word of the sentence
            }else if (line.equals("")){
                line = words[i];
            
            // If it is any other word
            }else{
                
                line = line + " " + words[i];
            }
        }
        
        // Make sure that the first word of the sentence is a capital letter
        String firstLetter = ""+line.charAt(0);
        line = firstLetter.toUpperCase()+line.substring(1);
        return line;
    }

    private static String subType5Word(int count, Type5ResultSet result, QuestionType questionType) {
        // Type 5 result set in the format: 
        // OWLClass X, OWLObjectProject objectProperty
        
        // If count is 0 then return OWLClass X
        if (count == 0 ) {
            return stringProcess(result.getX().toString());
        
        // If count is 1 then return OWLObjectProperty objectProperty
        } else if (count == 1 ) {
            String property = stringProcess(result.getOP().toString());
            return processProperty(result.getOP().toString(), questionType);
            
        // If count is anything else, then token is invalid.
        } else{
            return "<invalid>";
        }
    }

    static String generateType0Sentence(Type0ResultSet resultSet, String template) {
        
        
        // Initate the output to an empty String 
        String line = "";
        
        // Create a String array that contains only the words of the sentence.
        String [] words = template.split(" ");
        
        // Initiate article to an empty string
        String article = "";
        
        // Create a counter to keep track of which part of the result to use
        int count = 0;
        
        // For each word in the tempate
        for (int i = 0; i<words.length; i++) {
            
            // Update the article to the previous word
            if (i!=0){
                article = words[i-1];
            }
            
            // For token in the midddle of the sentence 
            if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-1)=='>'){
                
                // Check the article and replace token with the appropriate part of the result
                line = checkArticle(line, article, subType0Word(count, resultSet));
                count ++;
                
            // For token at the end of the sentence
            }else if (words[i].charAt(0)=='<' && words[i].charAt(words[i].length()-2)=='>'){
                
                // Check the article and replace token with the appropriate part of the result
                line = checkArticle(line, article, subType0Word(count, resultSet)) + words[i].charAt(words[i].length()-1);
                count ++;
                
            // If it is the first word of the sentence
            }else if (line.equals("")){
                line = words[i];
            
            // If it is any other word
            }else{
                
                line = line + " " + words[i];
            }
        }
        
        // Make sure that the first word of the sentence is a capital letter
        String firstLetter = ""+line.charAt(0);
        line = firstLetter.toUpperCase()+line.substring(1);
        return line;
    }

    private static String subType0Word(int count, Type0ResultSet resultSet) {
        // Type 5 result set in the format: 
        // OWLClass X, OWLObjectProject objectProperty
        
        // If count is 0 then return OWLClass X
        if (count == 0 ) {
            return stringProcess(resultSet.getX().toString());
        
        // If count is 1 then return OWLObjectProperty objectProperty
        } else if (count == 1 ) {
            return stringProcess(resultSet.getY().toString());
            
        // If count is anything else, then token is invalid.
        } else{
            return "<invalid>";
        }
    }
    
}
