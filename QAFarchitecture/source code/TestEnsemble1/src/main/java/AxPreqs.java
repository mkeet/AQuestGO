import java.util.ArrayList;

public class AxPreqs extends ArrayList<AxPreq> { // set of axiom prerequisites
    public void print() {
        System.out.println("----- Axiom prerequisites -----");
        for (AxPreq axPreq : this
             ) {
            axPreq.print();
        }
        System.out.println();
    }
}
