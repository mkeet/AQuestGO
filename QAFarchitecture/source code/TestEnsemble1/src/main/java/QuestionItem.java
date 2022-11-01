import java.util.ArrayList;

public class QuestionItem {
    private String question;
    private String axiomsStr;
    private ArrayList<String> axioms;
    private String answersStr;
    private ArrayList<String> answers;

    public QuestionItem() {
        this.axioms = new ArrayList<>();
        this.answers = new ArrayList<>();
    }
    public void init(String line) {
        String[] content = line.split(";");
        this.setQuestion(content[0]);
        this.setAxiomsStr(content[1]);
        String[] contentAxioms = this.getAxiomsStr().split("%");
        for (String ca: contentAxioms
             ) {
            this.getAxioms().add(ca);
        }
        this.setAnswersStr(content[2]);
        String[] contentAnswers = this.getAnswersStr().split("%");
        for (String ca: contentAnswers
        ) {
            this.getAnswers().add(ca);
        }
    }

    public void setAnswersStr(String answersStr) {
        this.answersStr = answersStr;
    }

    public String getAnswersStr() {
        return answersStr;
    }

    public void setAxiomsStr(String axiomsStr) {
        this.axiomsStr = axiomsStr;
    }

    public String getAxiomsStr() {
        return axiomsStr;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public ArrayList<String> getAxioms() {
        return axioms;
    }
}
