import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilsLinguistic {

    private static Lexicon lexicon;
    private static NLGFactory nlgFactory;
    private static Realiser realiser;
    private static ArrayList<String> articles;
    private static ArrayList<String> articlesStart;
    private static ArrayList<String> uncountableNouns;
    private  static Dictionary dictionary;


    public static void init() throws JWNLException {
        articles = new ArrayList<>();
        articles.add("a");
        articles.add("an");
        articles.add("some");

        articlesStart = new ArrayList<>();
        articlesStart.add("A");
        articlesStart.add("An");
        articlesStart.add("Some");
        dictionary = Dictionary.getDefaultResourceInstance();

        UtilsLinguistic.initUncountableNouns();

        lexicon = Lexicon.getDefaultLexicon();
        nlgFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);

    }

    public static Dictionary getDictionary() {
        return dictionary;
    }

    public static ArrayList<String> getArticlesStart() {
        return articlesStart;
    }

    public static ArrayList<String> getArticles() {
        return articles;
    }

    public static String stringProcess(String input) {
        String output = "";
        //System.out.println("InputGeneral="+input);
        // return only chars of the '#
        try {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                int x = input.indexOf('>');
                if (c == '#') {
                    output = input.substring(i + 1, x);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("".equals(output)) {
            String[] arr = input.split("/");
            output = arr[arr.length - 1];
            int x2 = output.indexOf('>');
            //System.out.println("output = "+output);
            if (output.equals("owl:Thing")) //shortFormProvider no ketrika tsara eto
                output = "thing";
            else
                output = output.substring(0, x2);
        }
        output = addSpaceBeforeMaj(output);

        output = output.replace("-", " ");
        output = output.replace("_", " ");

        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }

    public static String stringProcessSimple(String input) {
        String output = input;

        output = addSpaceBeforeMaj(output);

        output = output.replace("-", " ");
        output = output.replace("_", " ");

        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }

    public static String normalizeSpacing(String input) {
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ') {
                int j = i + 1;
                char d = input.charAt(j);
                if (j < input.length() && d == ' ')
                    continue;
                else output += "" + c;

            } else
                output += "" + c;
        }
        return output;
    }

    public static String addSpaceBeforeMaj(String input) { // example PlantParts -> Plant Parts
        //if (input =="")
        //  input = "error";
        String output = "" + input.charAt(0);


        for (int i = 1; i < input.length(); i++) {
            String caractere = "" + input.charAt(i);
            if (caractere.compareTo(" ") != 0 && caractere.compareTo(caractere.toUpperCase()) == 0) {
                int j = i + 1;
                if (j < input.length()) {
                    String nextCar = "" + input.charAt(j);
                    if (nextCar.compareTo(nextCar.toUpperCase()) != 0) {
                        output += " ";
                    }
                }
                //else output+=" ";
            }
            output += caractere;
        }
        return output;
    }

    public static String getLastWordIfArticles(String output) {
        String[] words = output.split(" ");
        if (words.length > 1) {
            if (articles.contains(words[words.length - 1])) {
                return words[words.length - 1];
            }
        } else if (words.length == 1) {
            if (articlesStart.contains(words[0])) {
                return words[0];
            }
        }
        return null;
    }

    public static void initUncountableNouns() {
        uncountableNouns = new ArrayList<>();

        String filename = "src/uncountableNouns.txt";

        File fTemp = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(fTemp);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                uncountableNouns.add(line);

                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            initUncountableNouns2();
        }
    }

    public static void initUncountableNouns2() {
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


    public static String removeUrl(String input){
        /*String output = "";

        // return only chars of the '#'
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int x = input.indexOf('>');
            if (c == '#') {
                output = input.substring(i+1, x);
            }
        }
        return output;*/


        String output = "";

        // return only chars of the '#
        try {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                int x = input.indexOf('>');
                if (c == '#') {
                    output = input.substring(i+1, x);
                }
            }
        }
        catch (Exception e) {
        }
        System.out.println("INPUT ==== "+input);
        if (input.equals("owl:Thing"))
            return "thing";
        if ("".equals(output)) {
            String[] arr = input.split("/");
            output = arr[arr.length-1];
            int x2 = output.indexOf('>');
            output = output.substring(0, x2);
            System.out.println("MISY ILAY PROBLEMO -> "+output);
        }

        return output;
    }
    public static String getRightArticle(String article, String newWord) {
        //if the newWord strats with a vow and the article before it is not "The" or "the"
        String output;


        NPPhraseSpec subject = UtilsLinguistic.getNLGFactory().createNounPhrase(article, newWord);
        String res = UtilsLinguistic.getRealiser().realise(subject).getRealisation();
        return res.split(" ")[0];
    }

    public static Realiser getRealiser() {
        return realiser;
    }

    public static Lexicon getLexicon() {
        return lexicon;
    }

    public static NLGFactory getNLGFactory() {
        return nlgFactory;
    }

    public static String stringProcessNormal (String input){

        String output= input;
        output = addSpaceBeforeMaj(output);
        output = output.replace("-", " ");
        output = output.replace("_", " ");
        output = output.toLowerCase();
        output = normalizeSpacing(output);
        return output.trim();
    }
}

