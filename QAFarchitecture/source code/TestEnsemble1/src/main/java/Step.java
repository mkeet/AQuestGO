import java.util.ArrayList;

public class Step {
    private String text;
    private ResultAx resultAx;
    private Step parent;

    public Step(){
        this.parent = null;
        this.text = "null";
    }

    public void setParent(Step parent) {
        this.parent = parent;
    }

    public Step getParent() {
        return parent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Step> getSteps() {
        ArrayList<Step> steps = new ArrayList<>();
        Step parent = this.getParent();
        steps.add(this);
        while (parent != null) {
            if (!parent.text.equals("null"))
                steps.add(parent);
            parent = parent.getParent();
        }
        return steps;
    }

    public ResultAxs getResultAxs() {
        ResultAxs res = new ResultAxs();
        for (Step step: this.getSteps()
             ) {
            res.add(step.getResultAx());
        }
        return res;
    }

    public ArrayList<String> getStepsStr() {
        ArrayList<String> str = new ArrayList<>();
        for (Step step: this.getSteps()
             ) {
            str.add(step.getText());
        }
        return str;
    }

    public void printSteps() {
        System.out.println("----- paths (Steps) -----");
        for (String s: this.getStepsStr()
             ) {
            System.out.println(s);
        }
        System.out.println();
    }

    public void setResultAx(ResultAx resultAx) {
        this.resultAx = resultAx;
    }

    public ResultAx getResultAx() {
        return resultAx;
    }
}
