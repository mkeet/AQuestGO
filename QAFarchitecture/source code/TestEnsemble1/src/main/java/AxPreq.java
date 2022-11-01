import java.util.ArrayList;

public class AxPreq extends ArrayList<AxPreqItem> {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void print() {
        System.out.println("Ax "+this.getId());
        for (AxPreqItem item: this//.getAxPreqItems()
             ) {
            item.print();
        }
        System.out.println("--------------------");
    }
}
