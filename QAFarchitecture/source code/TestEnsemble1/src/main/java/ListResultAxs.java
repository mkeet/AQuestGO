import java.util.ArrayList;

public class ListResultAxs extends ArrayList<ResultAxs> {

    public void print() {
        int i=0;
        System.out.println("----- List ResultAxs -----");
        for (ResultAxs res: this
             ) {
            System.out.println("ListResultAxs = "+i);
            res.print();
            i++;
        }
        System.out.println();
    }
}
