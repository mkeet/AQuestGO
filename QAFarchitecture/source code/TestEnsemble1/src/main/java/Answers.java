import java.util.ArrayList;

public class Answers extends ArrayList<Answer> {

    public void print() {
        System.out.println("----- Answers -----");
        for (Answer answer: this
        ) {
            answer.print();
        }
        System.out.println();
    }
}
