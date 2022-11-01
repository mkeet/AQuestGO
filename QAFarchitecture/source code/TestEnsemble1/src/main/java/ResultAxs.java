import java.util.ArrayList;

public class ResultAxs extends ArrayList<ResultAx> {

    public ListAssociations getListAssociations() {
        ListAssociations listAssociations = new ListAssociations();
        System.out.println("----- Getting list of associations -----");
        int i = 0;
        for (ResultAx resultAx: this
        ) {
            System.out.println("List "+i);
            Associations compiledAss = resultAx.getCompiledAssociations();
            for (AssociationTokenOntoElt assoc: compiledAss
            ) {
                System.out.println(assoc.getToken().getTextSlot()+" => "+assoc.getOntologyElt());
            }
            if (!listAssociations.containsAssociations(compiledAss)) {
                compiledAss.setStrPaths(resultAx.getStrPaths()); // this is not mandatory, this is just to store the path
                                                                 // that proves the answerability of the generated question
                                                                 // w.r.t the ontology.
                listAssociations.add(compiledAss);
            }
            i++;
        }
        return listAssociations;
    }

    public void printOld() {
        System.out.println("----- Final results -----");
        int i = 0;
        for (ResultAx resultAx: this
             ) {
            System.out.println("Result "+i+": ");
            for (AssociationTokenOntoElt assoc: resultAx.getCompiledAssociations()
                 ) {
                System.out.println(assoc.getToken().getTextSlot()+" => "+assoc.getOntologyElt());
            }
            System.out.println();
            i++;
        }
    }

    public void print() {
        ListAssociations listAssociations = this.getListAssociations();
        int i = 0;
        for (Associations associations: listAssociations
             ) {
            System.out.println("Result "+i+": ");
            for (AssociationTokenOntoElt assoc: associations
            ) {
                System.out.println(assoc.getToken().getTextSlot()+" => "+assoc.getOntologyElt());
            }
            System.out.println();
            i++;
        }

    }
}
