import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;

public class ListAssociations extends ArrayList<Associations>{

    public boolean containsAssociations(Associations associations) {

        String model = "";
        ArrayList<String> models = new ArrayList<>();
        for (AssociationTokenOntoElt assoc: associations
             ) {
            models.add(assoc.getToken().getTextSlot()+" "+assoc.getOntologyElt());
        }
        Collections.sort(models);
        for (String m: models
             ) {
            model+=m+" ";
        }

        for (Associations assocs: this
             ) {
            String comp = "";
            ArrayList<String> comps = new ArrayList<>();
            for (AssociationTokenOntoElt assoc: assocs
                 ) {
                comps.add(assoc.getToken().getTextSlot()+" "+assoc.getOntologyElt());

            }
            Collections.sort(comps);
            for (String c: comps
            ) {
                comp+=c+" ";
            }
            System.out.println("----- model -----");
            System.out.println(model);
            System.out.println("----- comp -----");
            System.out.println(comp);
            System.out.println();

            if (model.equals(comp)) {
                return true;
            }
        }
        return false;
    }
}
