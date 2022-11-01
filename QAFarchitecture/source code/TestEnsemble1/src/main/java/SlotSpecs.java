import java.util.ArrayList;

public class SlotSpecs extends ArrayList<SlotSpec> {
    public void print() {
        System.out.println("----- SlotSpecs -----");
        for (SlotSpec slotSpec: this
             ) {
            slotSpec.print();
        }
        System.out.println();
    }
}
