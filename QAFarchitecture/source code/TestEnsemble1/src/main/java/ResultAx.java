import com.sun.nio.sctp.Association;

import javax.xml.transform.Result;
import java.util.ArrayList;

public class ResultAx {

    private AxPreqInst axPreqInst;
    private ResultAxs paths;

    public ResultAx(){
        this.paths = new ResultAxs();
    }

    public void setPaths(ResultAxs paths) {
        this.paths = paths;
    }

    public ResultAxs getPaths() {
        return paths;
    }

    public Associations getAssociations() {
        return this.getAxPreqInst().getAssociations();
    }

    public void setAxPreqInst(AxPreqInst axPreqInst) {
        this.axPreqInst = axPreqInst;
    }

    public AxPreqInst getAxPreqInst() {
        return axPreqInst;
    }

    public Associations getCompiledAssociations(){
        Associations compiledAssociations = (Associations) this.getAssociations().clone();
        for (ResultAx resultAx: this.getPaths()
             ) {
            if (resultAx != null) { // no path
                for (AssociationTokenOntoElt assocTokenOntoElt : resultAx.getAssociations()
                ) {
                    if (!compiledAssociations.containsSlotToken(assocTokenOntoElt.getToken().getTextSlot())) {
                        compiledAssociations.add(assocTokenOntoElt);
                    }
                }
            }
        }
        return compiledAssociations;
    }
    public ArrayList<String> getArrayStrPaths() {
        ArrayList<String> results = new ArrayList<>();
        results.add(this.getAxPreqInst().getText());
        for (ResultAx resultAx: this.getPaths()
        ) {
            if (resultAx != null) {
                results.add(resultAx.getAxPreqInst().getText());
            }
        }
        return results;
    }
    public String getStrPaths() {
        ArrayList<String> arrResults = this.getArrayStrPaths();
        String allStr = "";
        for (String str: arrResults
        ) {
            allStr+=str+"%";
        }
        if (allStr.length()>0)
            allStr = allStr.substring(0, allStr.length()-1);
        return allStr;
    }
}

