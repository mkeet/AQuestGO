import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;

public class QuestionItems extends ArrayList<QuestionItem> {
    public void loadFromFile(String filename){
        ArrayList<String> lines = FileManager.readAllLines(filename);
        for (String line: lines
             ) {
            QuestionItem qItem = new QuestionItem();
            if (line.split(";").length>2) {
                qItem.init(line);
                this.add(qItem);
            }
        }
    }
    public ArrayList<String> computeChecking() {
        int countNotEntailed = 0;
        ArrayList<String> report = new ArrayList<>();
        for (QuestionItem qItem: this
             ) {
            ArrayList<String> axioms = qItem.getAxioms();

            boolean entailed = true;
            for (String axiom: axioms
                 ) {
                System.out.println(axiom);
                MyOnto.getManchesterOWLSyntaxParser().setStringToParse(axiom);
                OWLAxiom owlAxiom = MyOnto.getManchesterOWLSyntaxParser().parseAxiom();
                entailed = MyOnto.getOWLReasoner().isEntailed(owlAxiom);
                if(!entailed) {
                    countNotEntailed++;
                    break;
                }
            }
            if (entailed) {
                System.out.println(qItem.getQuestion());
                report.add(qItem.getQuestion());
                for (String axiom : axioms
                ) {
                    System.out.println("\t" + axiom);
                    report.add("\t" + axiom);
                }
                System.out.println("\t<Answarable>:");
                report.add("\t<Answarable>:");
                for (String answer: qItem.getAnswers()
                     ) {
                    System.out.println("\t\t"+answer);
                    report.add("\t\t"+answer);
                }
            }
        }
        System.out.println();
        report.add("");
        System.out.println("Final report:");
        report.add("Final report:");
        System.out.println("\tn(valid questions) = "+(this.size() - countNotEntailed));
        report.add("\tn(valid questions) = "+(this.size() - countNotEntailed));
        System.out.println("\tn(invalid questions) = "+(countNotEntailed));
        report.add("\tn(invalid questions) = "+(countNotEntailed));
        return report;
    }
}