import java.util.ArrayList;

public class Associations extends ArrayList<AssociationTokenOntoElt> implements Cloneable{

    private String strPaths;

    public String getStrPaths() {
        return strPaths;
    }

    public void setStrPaths(String strPaths) {
        this.strPaths = strPaths;
    }

    public void print() {

        for (AssociationTokenOntoElt ass: this
             ) {
            ass.print();
        }
    }

    public boolean containsSlotToken(String textSlot) {
        for (AssociationTokenOntoElt assocTokenOntoElt: this
             ) {
            String textSlotTest = assocTokenOntoElt.getToken().getTextSlot();
            if (textSlot.equals(textSlotTest))
                return true;
        }
        return false;
    }

    public String getOntologyElt(String textSlot) {
        for (AssociationTokenOntoElt assocTokenOntoElt: this
        ) {
            String textSlotTest = assocTokenOntoElt.getToken().getTextSlot();
            if (textSlot.equals(textSlotTest))
                return assocTokenOntoElt.getOntologyElt();
        }
        return null;
    }

    public Object clone()  {
        Associations associations = new Associations();
        for (AssociationTokenOntoElt assoc: this
             ) {
            associations.add(assoc);
        }
        return associations;
    }
}
