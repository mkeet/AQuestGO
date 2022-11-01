public class LTemplate {
    private int id;
    private String text;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.trim();
    }

    public void print() {
        System.out.println("id= "+this.getId()+", text= "+this.getText());
    }
}
