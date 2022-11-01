import java.util.ArrayList;

public class Tokens extends ArrayList<Token> implements Cloneable{

    public Tokens() {

    }
    public Tokens(SlotSpecs slotSpecs) {
        for (SlotSpec slotSpec: slotSpecs
             ) {
            Token token = new Token(slotSpec);
            this.add(token);
        }
    }
    public Token getToken(String slot) { // dictionary is better (to be updated)
        for (Token token: this
             ) {
            if (token.getTextSlot().equals(slot))
                return token;
        }
        return null;
    }

    public void print() {
        System.out.println("----- Tokens and the ontology elements -----");
        for (Token token: this
             ) {
            token.print();
        }
        System.out.println();
    }
    public Object clone()
    {
        Tokens tokens = new Tokens();
        for (Token token: this
             ) {
            try {
                tokens.add((Token)token.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return tokens;
    }
}
