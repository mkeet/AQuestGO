import java.util.ArrayList;

public class AxPreqInsts extends ArrayList<AxPreqInst> {
    public void print() {
        System.out.println("---- Axioms prerequisites inst -----");
        for (AxPreqInst item: this
             ) {
            item.print();
        }
        System.out.println();
    }
}
