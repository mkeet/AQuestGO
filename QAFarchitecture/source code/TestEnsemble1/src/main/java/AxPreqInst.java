public class AxPreqInst {
    private String text; // Manchester Syntax
    private Associations associations; // list of valid tokens with the chosen ontology element

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Associations getAssociations() {
        return associations;
    }

    public void setAssociations(Associations associations) {
        this.associations = associations;
    }

    public void print(){
        System.out.print(this.text);
        this.getAssociations().print();
        System.out.println();
    }

}
