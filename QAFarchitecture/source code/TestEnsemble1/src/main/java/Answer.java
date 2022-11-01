public class Answer {
    private int id;
    private String type;
    private String text;
    private LTemplates lTemplates;

    public Answer()
    {
        this.lTemplates = new LTemplates();
    }
    public void setText(String text) {
        this.text = text.trim();
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLTemplates(LTemplates lTemplates) {
        this.lTemplates = lTemplates;
    }

    public LTemplates getLTemplates() {
        return lTemplates;
    }

    public String getInfo() {
        if (this.getType().equals("boolean")) {
            if (this.getText().equals("1"))
                return "Yes/True";
            else if (this.getText().equals("0"))
                return "No/False";
            else {
                return "-";
            }
        }
        else if (this.getText().equals("axiom")) {
            return this.getText();
        }
        else return "LTA";
    }

    public void print () {
        if (!this.getType().equals("LT")) {
            System.out.println("id= " + this.getId() + ", type= " + this.getType() + ", text= " + this.getText());
        }
        else {
            System.out.println("id= " + this.getId() + ", type= " + this.getType());
            this.getLTemplates().print();
        }
    }
}
