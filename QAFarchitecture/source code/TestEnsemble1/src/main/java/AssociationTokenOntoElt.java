

public class AssociationTokenOntoElt implements Cloneable{
    private Token token;
    private String OntologyElt;

    public String getOntologyElt() {
        return OntologyElt;
    }

    public void setOntologyElt(String ontologyElt) {
        this.OntologyElt = ontologyElt;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }


    public void print() {
        System.out.print(this.getToken().getTextSlot()+" -> "+this.getOntologyElt());
    }

    public Object clone() throws CloneNotSupportedException {
        AssociationTokenOntoElt assoc  = new AssociationTokenOntoElt();
        assoc.setOntologyElt(this.getOntologyElt());
        assoc.setToken((Token)this.getToken().clone());
        return assoc;
    }
}
