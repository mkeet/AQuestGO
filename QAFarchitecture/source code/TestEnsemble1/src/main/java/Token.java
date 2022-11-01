import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import java.util.ArrayList;
import java.util.Set;

public class Token implements Cloneable{
    private SlotSpec slotSpec;
    private ArrayList<String> ontoElts;

    public Token(SlotSpec slotSpec) {
        this.slotSpec = slotSpec;
        this.retrieveValidOntoElts();
    }
    public Token(){
        this.ontoElts = new ArrayList<>();
    }

    public SlotSpec getSlotSpec() {
        return slotSpec;
    }

    public void setSlotSpec(SlotSpec slotSpec) {
        this.slotSpec = slotSpec;
    }
    public void clearOntoElts() {
        this.ontoElts.clear();
    }
    public void addOntoElt(String ontoEltStr) {
        if (!this.ontoElts.contains(ontoEltStr))
            this.ontoElts.add(ontoEltStr);
    }

    private void retrieveValidOntoElts() {
        this.ontoElts = new ArrayList<>();
        if (this.getSlotSpec().getNature().equals("Class")) {
            if (this.getSlotSpec().getRoute().equals("OnlyMe")){
                String classStr = this.getSlotSpec().getOntoRep();
                OWLClass cl = MyOnto.getValidOWLClass(classStr);
                if (cl != null) {
                    this.ontoElts.add(cl.toString());
                }
            } else if (this.getSlotSpec().getRoute().equals("MeAndChildren")){
                String classStr = this.getSlotSpec().getOntoRep();
                OWLClass cl = MyOnto.getValidOWLClass(classStr);
                if (cl!= null) {
                    this.ontoElts.add(cl.toString());
                    Set<OWLClass> subClasses = MyOnto.getOWLReasoner().getSubClasses(cl, false).getFlattened();
                    ArrayList<String> children = Utils.SetOfClassesToStr(subClasses);
                    ArrayList<String> trueChildren = this.excludeClasses(children);
                    this.ontoElts.addAll(trueChildren);
                }
            } else if(this.getSlotSpec().getRoute().equals("OnlyChildren")){
                String classStr = this.getSlotSpec().getOntoRep();
                OWLClass cl = MyOnto.getValidOWLClass(classStr);
                if (cl!= null) {
                    Set<OWLClass> subClasses = MyOnto.getOWLReasoner().getSubClasses(cl, false).getFlattened();
                    ArrayList<String> children = Utils.SetOfClassesToStr(subClasses);
                    ArrayList<String> trueChildren = this.excludeClasses(children);
                    this.ontoElts.addAll(trueChildren);
                }
            }
        }

        if (this.getSlotSpec().getNature().equals("OP")) {
            if (this.getSlotSpec().getRoute().equals("OnlyMe")) {
                String opStr = this.getSlotSpec().getOntoRep();
                OWLObjectProperty op = MyOnto.getValidOWLOP(opStr);
                if (op!=null) {
                    this.ontoElts.add(op.toString());
                }
            }else if (this.getSlotSpec().getRoute().equals("MeAndChildren")){
                String opStr = this.getSlotSpec().getOntoRep();
                OWLObjectProperty op = MyOnto.getValidOWLOP(opStr);
                if (op!=null) {
                    this.ontoElts.add(op.toString());
                    Set<OWLObjectPropertyExpression> subOPs = MyOnto.getOWLReasoner().getSubObjectProperties(op, false).getFlattened();
                    ArrayList<String> children = Utils.SetOfOPsToStr(subOPs);

                    //excluding some OPs (not useful)
                    ArrayList<String> trueChildren = this.excludeOPs(children);
/*                    ArrayList<String> trueChildren = new ArrayList<>();
                    ArrayList<String> excludedElt = new ArrayList<>();
                    for (String strExcl:this.getSlotSpec().getExcludes()
                    ) {
                        OWLObjectProperty opExcl = MyOnto.getValidOWLOP(strExcl);
                        if (opExcl != null) {
                            excludedElt.add(opExcl.toString());
                        }
                    }
                    for (String child: children
                    ) {
                        if (!excludedElt.contains(child)) {
                            trueChildren.add(child);
                        }
                    }*/
                    this.ontoElts.addAll(trueChildren);
                }
            } else if(this.getSlotSpec().getRoute().equals("OnlyChildren")){
                String opStr = this.getSlotSpec().getOntoRep();
                OWLObjectProperty op = MyOnto.getValidOWLOP(opStr);
                if(op!=null) {
                    Set<OWLObjectPropertyExpression> subOPs = MyOnto.getOWLReasoner().getSubObjectProperties(op, false).getFlattened();
                    ArrayList<String> children = Utils.SetOfOPsToStr(subOPs);
                    ArrayList<String> trueChildren = this.excludeOPs(children);
                    //excluding some OPs (not useful)
                   /* ArrayList<String> trueChildren = new ArrayList<>();
                    ArrayList<String> excludedElt = new ArrayList<>();
                    for (String strExcl:this.getSlotSpec().getExcludes()
                         ) {
                        System.out.println("str = "+strExcl);
                        OWLObjectProperty opExcl = MyOnto.getValidOWLOP(strExcl);
                        if (opExcl != null) {
                            excludedElt.add(opExcl.toString());
                            System.out.println("Excluded = "+opExcl.toString());
                        }
                    }
                    for (String child: children
                    ) {
                        System.out.println("Children = "+child);
                        if (!excludedElt.contains(child)) {
                            trueChildren.add(child);
                        }
                    }
                    for (String str: trueChildren
                         ) {
                        System.out.println("TrueChildren = "+str);
                    }*/
                    this.ontoElts.addAll(trueChildren);
                }
            }
        }
    }
    public ArrayList<String> excludeClasses(ArrayList<String> children){
        //excluding some OPs (not useful)
        ArrayList<String> trueChildren = new ArrayList<>();
        ArrayList<String> excludedElt = new ArrayList<>();
        for (String strExcl:this.getSlotSpec().getExcludes()
        ) {
            String[] elements = strExcl.split("/");
            String key = elements[0];
            String parameter = "";
            if (elements.length>1) {
                parameter = elements[1];
            }
            OWLClass clExcl = MyOnto.getValidOWLClass(key);
            if (clExcl != null) {
                excludedElt.add(clExcl.toString());
                ArrayList<String> keyChildren = new ArrayList<>();
                if (parameter.equals("all")) {
                    Set<OWLClass> subClassesKey = MyOnto.getOWLReasoner().getSubClasses(clExcl, false).getFlattened();
                    keyChildren = Utils.SetOfClassesToStr(subClassesKey);
                }
                excludedElt.addAll(keyChildren);
            }
        }
        for (String child: children
        ) {
            if (!excludedElt.contains(child)) {
                trueChildren.add(child);
            }
        }
        return trueChildren;
    }
    public ArrayList<String> excludeOPs(ArrayList<String> children){
        //excluding some OPs (not useful)
        ArrayList<String> trueChildren = new ArrayList<>();
        ArrayList<String> excludedElt = new ArrayList<>();
        for (String strExcl:this.getSlotSpec().getExcludes()
        ) {
            String[] elements = strExcl.split("/");
            String key = elements[0];
            String parameter = "";
            if (elements.length>1) {
                parameter = elements[1];
            }
            OWLObjectProperty opExcl = MyOnto.getValidOWLOP(key);
            if (opExcl != null) {
                excludedElt.add(opExcl.toString());
                ArrayList<String> keyChildren = new ArrayList<>();
                if (parameter.equals("all")) {
                    Set<OWLObjectPropertyExpression> subOPs = MyOnto.getOWLReasoner().getSubObjectProperties(opExcl, false).getFlattened();
                    keyChildren = Utils.SetOfOPsToStr(subOPs);
                }
                excludedElt.addAll(keyChildren);
            }
        }
        for (String child: children
        ) {
            if (!excludedElt.contains(child)) {
                trueChildren.add(child);
            }
        }
        return trueChildren;
    }

    public String getTextSlot() {
        return this.getSlotSpec().getTextSlot();
    }

    public ArrayList<String> getOntoElts() {
        return ontoElts;
    }

    public void print(){
        String res = "Id ="+this.getSlotSpec().getId()+", Slot = "+this.getTextSlot()+", Ontology elements =";
        for (String oElt: this.getOntoElts()
             ) {
            res+=" "+oElt+",";
        }
        res = res.substring(0, res.length()-1);
        System.out.println(res+"*");
    }

    public Object clone() throws CloneNotSupportedException {
        Token token = new Token(this.getSlotSpec());
        return token;
    }
}
