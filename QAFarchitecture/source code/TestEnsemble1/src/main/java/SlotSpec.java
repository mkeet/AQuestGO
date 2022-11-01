import java.util.ArrayList;

public class SlotSpec {
    private int id;
    private String text;
    private String slot;
    private String meaning;
    private String route;
    private String nature;
    private String textSlot;
    private String ontoRep;
    private ArrayList<String> excludes;
    public SlotSpec(){
        this.textSlot = null;
        this.ontoRep = null;
        this.excludes = new ArrayList<>();
    }

    public void setExcludes(ArrayList<String> excludes) {
        this.excludes = excludes;
    }

    public ArrayList<String> getExcludes() {
        return excludes;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNature() {
        return nature;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getSlot() {
        return slot;
    }

    public void print() {
        System.out.println("id= "+this.getId()+ ", nature="+ this.getNature()+", route="+ this.getRoute()+", text= "+this.getText());
    }
    private void initOntoRepAndTextSlot(){
        String[] elts = this.getText().split(" ");
        this.textSlot = elts[0];
        if (elts.length > 1) {
            this.ontoRep = elts[1];
        } else {
            if (this.getNature().equals("Class")) {
                this.ontoRep = "owl:Thing";
            } else if (this.getNature().equals("OP")) {
                this.ontoRep = "owl:topObjectProperty";
            }
        }
    }

    public String getOntoRep() {
        this.initOntoRepAndTextSlot();
        return ontoRep;
    }

    public String getTextSlot() {
        this.initOntoRepAndTextSlot();
        return textSlot;
    }
}
