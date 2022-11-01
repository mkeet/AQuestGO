import com.sun.source.tree.WhileLoopTree;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import simplenlg.features.*;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Verbaliser {

    private Lexicon lexicon;
    private NLGFactory nlgFactory;
    private Realiser realiser;
    private ArrayList<String> keywordsRenderingOP;
    private Dictionary dictionary;
    private PropertyClassification propertyClassification;
    private String[] preps= {
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

    public Verbaliser() throws JWNLException {
        this.lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);
        this.realiser = new Realiser(lexicon);

        this.keywordsRenderingOP = new ArrayList<>();
        this.keywordsRenderingOP.add("infinitive");
        this.keywordsRenderingOP.add("gerund");
        this.keywordsRenderingOP.add("first");
        this.keywordsRenderingOP.add("third");

        this.dictionary = Dictionary.getDefaultResourceInstance();

        this.propertyClassification = new PropertyClassification(this);
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public Realiser getRealiser() {
        return realiser;
    }

    public NLGFactory getNlgFactory() {
        return nlgFactory;
    }

    public String render(String template, Associations associations) { // association can be separated from the axiom prerequisites
        String[] words = template.split(" ");
        String output = "";

        for (int i = 0; i <template.length(); i++) {
            String c = template.substring(i,i+1);
            if (!c.equals("[")) {
                output+=c;
            }
            else { // starting of a token
                String token = c;
                while (!c.equals("]")){
                    i++;
                    c = template.substring(i, i+1);
                    token +=c;
                }
                //System.out.println("normal Token = "+token);
                String[] tokenKeyword = token.split("/");
                String keyword = "default";
                //System.out.println("normal Token = "+token+" - split size= "+tokenKeyword.length);

                if (tokenKeyword.length > 1) {
                    token = tokenKeyword[0]+"]";
                    keyword = tokenKeyword[1].substring(0, tokenKeyword[1].length()-1);
                    System.out.println("tokenKeyword = "+token+" --> "+keyword);
                }

                for (AssociationTokenOntoElt assoc: associations
                ) {

                    System.out.println("Keyword_IN_1 = "+keyword+" token = "+token);
                    if (assoc.getToken().getTextSlot().equals(token)) {

                        String selectedWord = UtilsLinguistic.stringProcess(assoc.getOntologyElt());
                        System.out.println("Keyword_IN_2 = "+keyword+" token = "+token);
                        if (assoc.getToken().getSlotSpec().getNature().equals("Class")) {
                            FormattedValue fv = new FormattedValue();
                            String formattedClass = getRenderedClass(selectedWord, keyword, output, assoc, fv);
                            if (formattedClass == null)
                                return null; // the category of the class is not found
                            if (fv.getRemoveString() != 0)
                                output=output.substring(0, output.length()-fv.getRemoveString());
                            output+=formattedClass;
                        } else if (assoc.getToken().getSlotSpec().getNature().equals("OP")) {
                            System.out.println("Keyword_IN_3 = "+keyword+" token = "+token);
                            String formattedOP = getRenderedOP(selectedWord, keyword, output, assoc);
                            if (formattedOP == null)
                                return null; // the category of the OP is not found
                            output+=formattedOP;
                        }
                        else output += selectedWord;
                    }
                }
            }
        }
        return this.normaliseCapSentence(output.trim());
    }

    private String normaliseCapSentence(String sentence){
        String output = "";
        if (sentence.length()>0) {
            output = sentence.substring(0,1).toUpperCase();
        }
        for (int i = 1; i < sentence.length(); i++) {

            String c = sentence.substring(i,i+1);
            if (c.equals(".")) {
                i++;
                if (i+1<sentence.length()-1) {
                    c = sentence.substring(i,i+1);
                    if (c.equals(" ")){
                        i++;
                        output+=". "+sentence.substring(i, i+1).toUpperCase();
                    }
                }
                else {
                    output+=".";
                }
            }
            else if (c.equals(":")) {
                i++;
                if (i+1<sentence.length()-1) {
                    c = sentence.substring(i,i+1);
                    if (c.equals(" ")){
                        i++;
                        output+=": "+sentence.substring(i, i+1).toUpperCase();
                    }
                }
                else {
                    output+=":";
                }
            }
            else if (c.equals("?")) {
                i++;
                if (i+1<sentence.length()-1) {
                    c = sentence.substring(i,i+1);
                    if (c.equals(" ")){
                        i++;
                        output+="? "+sentence.substring(i, i+1).toUpperCase();
                    }
                }
                else {
                    output+="?";
                }
            }
            else {
                output+= c;
            }
        }
        return output;
    }
    private String getRenderedNoun(String noun) {

        if (UtilsLinguistic.isCountableNoun(noun)) {
            NPPhraseSpec p = this.getNlgFactory().createNounPhrase("a", noun);
            String newWord = realiser.realise(p).getRealisation();
            return newWord;
        }
        return noun;
    }

    private String getRenderedClass(String cl, String keyword, String output, AssociationTokenOntoElt assoc, FormattedValue fv) {
        // category
            // not considered (AQuestGO didn't have this features. However, we have just to add requirements here if needed)

        // transformation
        if (keyword.equals("noArticle"))
            return cl;
        String ontoElt = assoc.getOntologyElt();
        boolean isPerdurant = false;
        OWLClass perdurant = MyOnto.getValidOWLClass("Perdurant");
        if (perdurant != null) {
            String perdurantStr = perdurant.toString();
            String strToParse = ontoElt+" SubClassOf("+perdurantStr+")";
            MyOnto.getManchesterOWLSyntaxParser().setStringToParse(strToParse);
            OWLAxiom owlAxiom = MyOnto.getManchesterOWLSyntaxParser().parseAxiom();
            boolean entailed = MyOnto.getOWLReasoner().isEntailed(owlAxiom);
            if (entailed) {
                isPerdurant = true;
            }
        }

        if (keyword.equals("default")) {
            keyword = "noun";
            if (isPerdurant) {
                if (isVerb(cl))
                    keyword = "gerund";
                else keyword = "being";
            }
            /*} else if (isVerb(cl)) {
                keyword = "gerund";
            }*/
        }
        if (keyword.equals("noun")) {
            String checkArticle = UtilsLinguistic.getLastWordIfArticles(output);
            if (checkArticle == null) {
                String[] elements = cl.split(" ");
                String countTest = elements[0];
                for (int i = 0; i < elements.length; i++) {
                    if (this.isNounWordNet(elements[i])) {
                        countTest = elements[i];
                        break;
                    }
                }
                if (UtilsLinguistic.isCountableNoun(countTest)) {
                    NPPhraseSpec p = this.getNlgFactory().createNounPhrase("a", cl);
                    String newWord = realiser.realise(p).getRealisation();
                    return newWord;
                }
            }
        }

        if (keyword.equals("gerund")) {
            String checkArticle = UtilsLinguistic.getLastWordIfArticles(output);
            if (checkArticle == null) {
                return getGerund(cl);
            }
            else {
                fv.setRemoveString(checkArticle.length()+1);
                return getGerund(cl);
            }
        }
        if (keyword.equals("infinitive")) {
            return this.getInfinitiveWithoutTo(cl);
        }
        if (keyword.equals("first")) {
            return this.getInfinitiveWithoutTo(cl);
        }
        if (keyword.equals("third")) {
            return this.getThird(cl);
        }
        if (keyword.equals("being")) {
            return "being "+ this.getRenderedNoun(cl);
        }
        return cl;
    }

    public PropertyClassification getPropertyClassification() {
        return propertyClassification;
    }

    public String removeArticleIfNecessary(String word) {
        if (word.toLowerCase().startsWith("a "))
            return word.substring(2);
        if (word.toLowerCase().startsWith("an "))
            return word.substring(3);
        return word;
    }
    public String addArticleIfNecessary(String article, String word) {

        if (word.toLowerCase().startsWith("a ") || word.toLowerCase().startsWith("an "))
            return word;
        String[] elements = word.split(" ");
        String countTest = elements[0];
        for (int i = 0; i < elements.length; i++) {
            if (this.isNounWordNet(elements[i])) {
                countTest = elements[i];
                break;
            }
        }
        if (word.equals("part") || word.equals("part of")) // 'part' and 'part of' are an exception //
            return word;
        if (UtilsLinguistic.isCountableNoun(countTest)) { // part is an exception // || (countTest.equals("part") && elements.length>1)
            NPPhraseSpec p = this.getNlgFactory().createNounPhrase(article, word);
            String newWord = realiser.realise(p).getRealisation();
            return newWord;
        }
        return word;
    }

    private String getRenderedOP(String op, String keyword, String output, AssociationTokenOntoElt assoc) {
        //category
        System.out.println("OP keyword ==== "+keyword);

        if (keyword.startsWith("*")) {
            String[] parameters = keyword.split("-");
            System.out.println("OP Noun ==== " + op);
            String paramPropertyType = parameters[0];
            String paramForm = null;
            if (parameters.length > 1) {
                paramForm = parameters[1];
            }

            this.getPropertyClassification().classifyOP(assoc.getOntologyElt());
            PropertyType propertyType = this.getPropertyClassification().getPropertyType();
            System.out.println(propertyType + " " + this.getPropertyClassification().getFormattedProperty() + " keyword =" + paramPropertyType);
            if (paramPropertyType.equals("*" + propertyType)) {
                System.out.println(propertyType + " " + this.getPropertyClassification().getFormattedProperty());
                String formattedOP = this.getPropertyClassification().getFormattedProperty();
                if (paramForm != null) {
                    if (paramForm.equals("infinitive")) {
                        if (formattedOP.startsWith("has ")) {
                            formattedOP = formattedOP.substring(4);
                            formattedOP = "have " + this.addArticleIfNecessary("a", formattedOP);
                        } else if (formattedOP.startsWith("have ")) {
                            formattedOP = formattedOP.substring(5);
                            formattedOP = "have " + this.addArticleIfNecessary("a", formattedOP);
                        } else formattedOP = this.getInfinitiveWithoutTo(formattedOP);
                    } else if (paramForm.equals("third")) {
                        if (formattedOP.startsWith("have ")) {
                            formattedOP = formattedOP.substring(5);
                            formattedOP = "has " + this.addArticleIfNecessary("a", formattedOP);
                        } else if (formattedOP.startsWith("has ")) {
                            formattedOP = formattedOP.substring(4);
                            formattedOP = "has " + this.addArticleIfNecessary("a", formattedOP);
                        }
                        else formattedOP = this.getThird(formattedOP);
                    } else if (paramForm.equals("nouns")) {
                        if (formattedOP.startsWith("have ")) {
                            formattedOP = this.addArticleIfNecessary("a",formattedOP.substring(5));
                        }
                        if (formattedOP.startsWith("has ")) {
                            formattedOP = this.addArticleIfNecessary("a",formattedOP.substring(4));
                        }
                    } else if (paramForm.equals("nounsNoArticle")) {
                        System.out.println("FORMMMAAATTT 1 = "+formattedOP);
                        if (formattedOP.startsWith("have ")) {
                            System.out.println("FORMMMAAATTT 2 = "+formattedOP);
                            formattedOP =  formattedOP.substring(5);
                            formattedOP = this.removeArticleIfNecessary(formattedOP);
                        }
                        if (formattedOP.startsWith("has ")) {
                            formattedOP = formattedOP.substring(4);
                            System.out.println("FORMMMAAATTT = "+formattedOP);
                            formattedOP = this.removeArticleIfNecessary(formattedOP);
                        }
                    }
                }

                return formattedOP;
            }
            return null;
        }

        // transformation
        if (keyword.equals("default"))
            keyword = "infinitive";
        if (keyword.equals("infinitive")) {
            return this.getInfinitiveWithoutTo(op);
        }
        if (keyword.equals("first")) {
            return this.getInfinitiveWithoutTo(op);
        }
        if (keyword.equals("gerund")) {
            return this.getGerund(op);
        }
        if (keyword.equals("third")) {
            return this.getThird(op);
        }

        return op;
    }

    public ArrayList<String> realise(ListResultAxs listResultAxs, QTOnto qtOnto){

        ArrayList<String> questions = new ArrayList<>();

        for (ResultAxs resultAxs: listResultAxs
        ) {
            ListAssociations listAssociations = resultAxs.getListAssociations();
            for (LTemplate lTemplate: qtOnto.getLTemplates()
            ) {
                System.out.println("template = "+lTemplate.getId()+" "+lTemplate.getText());
                for (Associations associations: listAssociations
                ) {
                    String output = this.render(lTemplate.getText(), associations);
                    if (output!= null) {
                        System.out.println(output);

                        if (!questions.contains(output)){
                            questions.add(output);
                        }
                    }
                }
            }
        }
        return questions;
    }

    public ArrayList<String> realiseWithDetails(ListResultAxs listResultAxs, QTOnto qtOnto){

        ArrayList<String> questions = new ArrayList<>();

        for (ResultAxs resultAxs: listResultAxs
        ) {
            ListAssociations listAssociations = resultAxs.getListAssociations();

            for (LTemplate lTemplate: qtOnto.getLTemplates()
            ) {
                System.out.println("template = "+lTemplate.getId()+" "+lTemplate.getText());
                for (Associations associations: listAssociations
                ) {
                    String output = this.render(lTemplate.getText(), associations);
                    if (output!= null) {
                        System.out.println(output+";"+associations.getStrPaths());
                        Answers answers = qtOnto.getAnswers();
                        String answerStr = "";
                        for (Answer answer: answers
                        ) {
                            if (answer.getType().equals("boolean")) {
                                answerStr += answer.getInfo()+"%";
                            }
                            else if (answer.getType().equals("axiom")) {
                                String axiomStr = "";
                                String text = answer.getText().trim();
                                int begin = 0;
                                int end = 0;
                                for (int i = 0; i < text.length(); i++) {
                                    String c = text.substring(i, i+1);
                                    if (c.equals("[")){
                                        begin = i;
                                        do{
                                            i++;
                                            c = text.substring(i, i+1);
                                        }
                                        while (!c.equals("]"));
                                        end = i+1;
                                        String textSlot = text.substring(begin, end);
                                        //System.out.println("SLOTFINDED = "+textSlot);
                                        String ontologyElt = associations.getOntologyElt(textSlot);
                                        //System.out.println("ontology element = "+ontologyElt);
                                        if (ontologyElt != null) {
                                            axiomStr+=ontologyElt;
                                        }
                                    }
                                    else axiomStr+=c;

                                }
                                answerStr += axiomStr+"%";
                            }
                            else if (answer.getType().equals("LT")) {
                                LTemplates lTemplates = answer.getLTemplates();
                                for (LTemplate lT: lTemplates
                                     ) {
                                    String answerLT = this.render(lT.getText() , associations);
                                    if (answerLT!= null){
                                        answerStr+=answerLT+"%";
                                    }
                                }
                            }
                        }
                        questions.add(output+";"+associations.getStrPaths()+";"+answerStr);
                    }
                }
            }
        }
        return questions;
    }
    public String getInfinitiveWithoutTo(String v) {
        System.out.println("ontological element? INF "+v);
        if (v.equals("inheres in")) // ontological element
            return "inhere in";

        String verb;
        try {
            IndexWord indexWord = this.getDictionary().getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }

        VPPhraseSpec verb3 = this.getNlgFactory().createVerbPhrase(verb);
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);
        verb3.setFeature(Feature.PERSON, Person.FIRST);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();

        return resultVerb3;
    }
    public String getThird(String v) {
        System.out.println("ontological element? THIRD "+v);
        if (v.equals("inheres in")) // ontological element
            return "inheres in";

        String verb;
        try {
            IndexWord indexWord = this.getDictionary().getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }

        VPPhraseSpec verb3 = this.getNlgFactory().createVerbPhrase(verb);
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);
        verb3.setFeature(Feature.PERSON, Person.THIRD);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();

        return resultVerb3;
    }
    public String getGerund(String verb) {
        String[] partsOfVerb = verb.split(" ");

        String verbPart = partsOfVerb[0];
        String restOfVerb = "";
        for (int i = 1; i < partsOfVerb.length; i++) {
            restOfVerb += partsOfVerb[i]+" ";
        }

        VPPhraseSpec verb3 = this.getNlgFactory().createVerbPhrase(verbPart);
        verb3.setFeature(Feature.PROGRESSIVE, true);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();

        String res = (resultVerb3+ " "+restOfVerb).trim();
        res = res.substring(3); // this is to remove "is " at the the begining of "res"


        return res;
    }
    public  String getPresentParticiple(String v) {
        String verb;
        Dictionary dicoWordNet = this.getDictionary();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }

        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);
        verb3.setFeature(Feature.FORM, Form.GERUND);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }
    public String getPresentParticipleManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = this.getPresentParticiple(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }

    public boolean isVerb(String word) {
        return isVerbStanford(word);
    }
    public boolean isVerbWordNet (String word) {

        try {
            IndexWord dog = this.getDictionary().getIndexWord(POS.VERB, word);
            if (dog == null)
                return false;
            return true;
        } catch (JWNLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getPastParticiple(String v) {

        String verb;
        Dictionary dicoWordNet = this.getDictionary();
        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }

        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);
        verb3.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }

    public String getPast(String v) {
        String verb;
        Dictionary dicoWordNet = this.getDictionary();
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
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;
    }

    public String getThirdPersonManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = this.getThirdPerson(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }

    public String getThirdPerson(String v) {
        String verb;
        Dictionary dicoWordNet = this.getDictionary();

        try {
            IndexWord indexWord = dicoWordNet.getIndexWord(POS.VERB, v);
            verb = indexWord.getLemma();
        }
        catch(Exception e) {
            verb = v;
        }
        if(true) { //v.startsWith("inh")
            try {

                IndexWordSet lookupAllIndexWords = dicoWordNet.lookupAllIndexWords(v);
                IndexWord[] indexWordArray = lookupAllIndexWords.getIndexWordArray();

            } catch (JWNLException ex) {
            }
        }
        VPPhraseSpec verb3 = nlgFactory.createVerbPhrase(verb);
        verb3.setFeature(Feature.TENSE, Tense.PRESENT);
        verb3.setFeature(Feature.PERSON, Person.THIRD);
        verb3.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        String resultVerb3 = this.getRealiser().realise(verb3).getRealisation();
        return resultVerb3;

    }

    public String getLemmaVerb (String v) {
        try {
            Dictionary dicoWordNet = this.getDictionary();
            IndexWordSet lookupAllIndexWords = dicoWordNet.lookupAllIndexWords(v);
            IndexWord[] indexWordArray = lookupAllIndexWords.getIndexWordArray();

           //  System.out.println("Explination : "+ lookupAllIndexWords.toString());

            for (IndexWord indexWord : indexWordArray) {
               // System.out.println("\n array processing :-) ");
               // System.out.println(indexWord.getPOS()+" -> "+ indexWord.getLemma());
                if (indexWord.getPOS() == POS.VERB) {
                    return indexWord.getLemma();
                }
            }
        }
        catch(JWNLException ex) {
            Logger.getLogger(Verbaliser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }
    public String getInfinitiveWithoutToManyWords(String verb) {
        String res = "";
        String[] words = verb.split(" ");
        String firstWord = this.getInfinitiveWithoutTo(words[0]);
        for (int i = 1; i < words.length; i++) {
            res+= words[i]+" ";
        }
        res= (firstWord+" "+res).trim();
        return res;
    }
    public boolean isNounWordNet (String word) {

        try {
            IndexWord dog = this.getDictionary().getIndexWord(POS.NOUN, word);
            if (dog == null)
                return false;
            return true;
        } catch (JWNLException ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            Logger.getLogger(Verbaliser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isVerbPastParticiple(String word){
        if (isVerb(word)) {
            if (word.equals(this.getPastParticiple(word)))
                return true;
        }
        return false;
    }

    public boolean isPreposition(String word) {
        //System.out.println(" prep = " +word );
        WordElement wElt = lexicon.lookupWord(word);
        TokenType tokenType = getTokenType(wElt, word);
        return tokenType == TokenType.PREPOSITION;
    }

    public  TokenType getTokenType(WordElement wElement, String word) {
        String category = wElement.getCategory().toString().toLowerCase();

        //System.out.println("category = "+category+" realisation -> "+wElement.getBaseForm());
        if (isPrep(word.toLowerCase()))
            return TokenType.PREPOSITION;
        else if(isVerb(word)) // stanford
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
    private boolean isPrep(String s) {
        for (int i = 0; i < preps.length; i++) {
            if (preps[i].equals(s))
                return true;
        }
        return false;
    }

    public boolean isDeterminer(String word) {
        String s = word.toLowerCase();
        return s.compareTo("a") == 0 || s.compareTo("an") == 0 || s.compareTo("the") == 0;
    }

    public static boolean isNoun(String word) {
      /*  WordElement wElt = lexicon.lookupWord(word);
        TokenType tokenType = getTokenType(wElt, word);
        return tokenType == TokenType.NOUN; */

        String text = "It has "+word;
        //System.out.println("RandriaAAA_1");
        CoreDocument coreDocument = new CoreDocument(text);
        //System.out.println("RandriaAAA_2");
        StanfordCoreNLP coreNLP = Pipeline.getPipeline();
        //System.out.println("RandriaAAA_3");
        coreNLP.annotate(coreDocument);
        //System.out.println("RandriaAAA_4");
        List<CoreLabel> coreLabels = coreDocument.tokens();
        //System.out.println("RandriaAAA_5");
        CoreLabel nn = coreLabels.get(2);
        String sTest = nn.get(CoreAnnotations.PartOfSpeechAnnotation.class);
        //System.out.println("RandriaAAA_6");
        if (sTest.startsWith("NN")) {
            return true;
        }
        return false;
    }

    public  boolean isAdjectiveParticiple(String word)  {
        return isVerbPastParticiple(word) || isVerbPresentParticiple(word) || isAdjective(word);
    }

    public boolean isVerbPresentParticiple(String word) {
        if (isVerb(word)) {
            if (word.equals(this.getPresentParticiple(word)))
                return true;
        }
        return false;
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
}
