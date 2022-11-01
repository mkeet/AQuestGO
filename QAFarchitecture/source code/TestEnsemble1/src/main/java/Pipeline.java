import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;
import net.sf.extjwnl.dictionary.Dictionary;

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
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}