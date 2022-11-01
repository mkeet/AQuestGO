import java.util.ArrayList;

public class AxPreqItem { // Axiom Prerequisites
    private int id;
    private String text;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setText(String text) {
        this.text = text.trim();
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getTextSlots(){
        ArrayList<String> results = new ArrayList<>();
        int begin = 0;
        int end = 0;
        String text = this.getText();
        for (int i = 0; i < text.length(); i++) {
            String c = text.substring(i, i+1);
            if (c.equals("[")){
                begin = i;
            }
            if (c.equals("]")){
                end = i+1;
                String newTextSlot = text.substring(begin, end);
                results.add(newTextSlot);
            }
        }
        return results;
    }

    public void print() {
        String s = "id= "+this.getId()+", text ="+this.getText()+ ", Slots =";
        for (String slot: this.getTextSlots()
             ) {
            s+= " "+slot+",";
        }
        System.out.println(s);
    }
}
