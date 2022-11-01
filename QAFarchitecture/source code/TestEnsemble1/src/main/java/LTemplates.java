import java.util.ArrayList;

public class LTemplates extends ArrayList<LTemplate> {
    public void print() {
        System.out.println("----- Linguistic Templates -----");
        for (LTemplate lTemplate: this
        ) {
            lTemplate.print();
        }
        System.out.println();
    }
}
