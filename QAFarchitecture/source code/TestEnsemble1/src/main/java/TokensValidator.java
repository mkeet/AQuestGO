import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserImpl;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.xml.transform.Result;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TokensValidator {
    private AxPreqs axPreqs;
    private Tokens tokens;

    public TokensValidator(AxPreqs axPreqs, Tokens tokens){
        this.axPreqs = axPreqs;
        this.tokens = tokens;
    }

    public TokensValidator(QTOnto qtOnto){
        this.axPreqs = qtOnto.getAxPreqs();
        Tokens tokens = new Tokens(qtOnto.getSlotSpecs());
        this.tokens = tokens;
    }

    private AxPreqInsts retrieveTextsToCheck(Tokens tokens, String text) {
        AxPreqInsts axPreqInsts = new AxPreqInsts();

        int length = 1;
        var tabRepetitionInv = new ArrayList<Integer>();
        for (Token token: tokens
             ) {
            length *= token.getOntoElts().size();
            System.out.println("length_temp = "+length);
            tabRepetitionInv.add(length/tokens.get(0).getOntoElts().size()); //
        }
        ArrayList<Integer> tabRepetition = new ArrayList<>();
        for (int i = tabRepetitionInv.size()-1; i >=0; i--) {
           // System.out.println(i);
            //System.out.println(tabRepetitionInv.get(i));
            tabRepetition.add(tabRepetitionInv.get(i));
        }
        for (int i: tabRepetition
             ){
            System.out.println("repetition = "+i);
        }

        System.out.println("LENGTH = "+length);

        String[][] array = new String[tokens.size()][length];

        int repetition = length;
        for (int i = 0; i < tokens.size(); i++) {
            //int block = 1;
            //for (int j = i+1; j < tokens.size(); j++) {
             //   block *= tokens.get(j).getOntoElts().size();
           // }

            repetition /= tokens.get(i).getOntoElts().size();
            //repetition = tabRepetition.get(i);
            System.out.println("Repetition for "+tokens.get(i).getTextSlot()+ " = "+repetition);
            int position = 0;
            int intK = 0;
            for (int k = 0; k < length; k++) {
               // System.out.println("length ="+length);
               // System.out.println("i ="+i);
                //System.out.println("k ="+k);
               // System.out.println("position ="+position+"/"+tokens.get(i).getOntoElts().size());
                array[i][k] = tokens.get(i).getOntoElts().get(position);
                intK++;
                if (intK>=repetition) {
                    intK = 0; position++;
                    if (position>tokens.get(i).getOntoElts().size()-1)
                        position = 0;
                }
            }
        }

        for (int j = 0; j < length; j++) {
            AxPreqInst axPreqInst = new AxPreqInst();
            String newText = text.toString();
            int i = 0;
            Associations assocs = new Associations();
            for (Token token: tokens
                 ) {
                newText = newText.replace(token.getTextSlot(), array[i][j]);
                AssociationTokenOntoElt assoc = new AssociationTokenOntoElt();
                assoc.setToken(token);
                assoc.setOntologyElt(array[i][j]);
                assocs.add(assoc);
                i++;
            }
            axPreqInst.setText(newText);
            axPreqInst.setAssociations(assocs);
            axPreqInsts.add(axPreqInst);
        }
        return axPreqInsts;
    }
    private ArrayList<String> results = new ArrayList<>();
    private ResultAxs resultAxs = new ResultAxs();

    public void getValidAxiom(int idAxItem, Tokens consideredTokens, AxPreq axPreq, ArrayList<String> paths, String path, ResultAxs pathResultAxs, Step step) {

        if (idAxItem >= axPreq.size())
            return;

        AxPreqItem axPreqItem = axPreq.get(idAxItem);
        Tokens tokensTemp = consideredTokens;
        Tokens tks = new Tokens();
        for (String textSlot : axPreqItem.getTextSlots()
        ) {
            // just limited to the slots that are used in the current item axiom prerequisites
            Token token = tokensTemp.getToken(textSlot);
            System.out.println(textSlot);
            System.out.println();
            if (token.getOntoElts().size()>0) {
                tks.add(token);
                System.out.println("Token == "+token.getTextSlot()+ " idAxItem = "+idAxItem);
                for (String ontoElt: token.getOntoElts()
                     ) {
                    System.out.println(ontoElt);
                }
            }

        }
        System.out.println("Token[tks] = "+tks.size());
        for (Token token: tks
        ){
            for (String s: token.getOntoElts()
            ) {
                System.out.println(s);
            }
        }
        System.out.println();

        AxPreqInsts axPreqInsts = retrieveTextsToCheck(tks, axPreqItem.getText()); // to be checked by the reasoner

        // sometimes, we need to refer to the original token, but exchanging the bad with the right one

        idAxItem++;
        for (AxPreqInst inst : axPreqInsts
        ) {
            if (inst.getText().contains("[")) {
                System.out.println("SEE check = " + inst.getText());
                return;
            }
           // System.out.println("SEE check = " + inst.getText());
            MyOnto.getManchesterOWLSyntaxParser().setStringToParse(inst.getText());
            OWLAxiom owlAxiom = MyOnto.getManchesterOWLSyntaxParser().parseAxiom();
            boolean entailed = MyOnto.getOWLReasoner().isEntailed(owlAxiom);

            if (entailed) {
                System.out.println("Entailed === "+(idAxItem-1)+"/"+ axPreq.size()+" "+inst.getText());

                Tokens newTokens = (Tokens) tokensTemp.clone();
                Associations associations = inst.getAssociations();
                for (AssociationTokenOntoElt assoc: associations
                     ) {
                    Token token = newTokens.getToken(assoc.getToken().getTextSlot());
                    token.clearOntoElts();
                    token.addOntoElt(assoc.getOntologyElt());
                }
/*                Tokens newTokens = (Tokens) tokensTemp.clone();*/
                newTokens.print();
                if (idAxItem == axPreq.size()) {
                    results.add(inst.getText());
                    ResultAx resultAx = new ResultAx();
                    resultAx.setAxPreqInst(inst);
                    resultAxs.add(resultAx);
                    ArrayList<String> myPaths = new ArrayList<>();
                    ResultAxs myPathResultAxs = new ResultAxs();
                    int coverage = idAxItem -1;
                    for (int i = 0; i<coverage; i++) {
                        myPaths.add(paths.get(paths.size()-coverage+i));
                        myPathResultAxs.add(pathResultAxs.get(paths.size()-coverage+i));
                    }
                    //resultAx.setPaths(myPathResultAxs);
                    resultAx.setPaths(step.getResultAxs());
                    int i = 0;
                    for (String p: myPaths
                         ) {
                        System.out.println(i+" = "+ p);
                        i++;
                    }

                    System.out.println("RESULT == "+inst.getText());
                    System.out.println("PATH = "+path);
                    step.printSteps();
                   // path ="";
                }
                else {
                    paths.add(inst.getText());
                    ResultAx resAx = new ResultAx();
                    resAx.setAxPreqInst(inst);
                    pathResultAxs.add(resAx);
                    path +=idAxItem+" - "+inst.getText()+", \n\r";
                    Step newStep = new Step();
                    newStep.setParent(step);
                    newStep.setText(inst.getText());
                    newStep.setResultAx(resAx);
                    getValidAxiom(idAxItem, newTokens, axPreq, paths, path, pathResultAxs, newStep);
                }
            }
        }
    }

    public ListResultAxs initAllValidAxioms(){
        ListResultAxs list = new ListResultAxs();
        for (AxPreq axPreq: this.getAxPreqs()
             ) {
            System.out.println("---- axPreq ----- ");
            axPreq.print();
            this.tokens.print();
            ResultAxs res = this.initValidAxioms(axPreq);
            list.add(res);
        }
        System.out.println();
        System.out.println("----- Results -----");
        list.print();
        return list;
    }
    public ResultAxs initValidAxioms(AxPreq axPreq){
        results = new ArrayList<>();
        resultAxs = new ResultAxs();
        ArrayList<String> paths = new ArrayList<>();
        ResultAxs pathResultAxs = new ResultAxs();
        String path = "";
        Step step = new Step();
        Tokens tokenTemps = (Tokens) this.tokens.clone();
        getValidAxiom(0, tokenTemps, axPreq, paths, path, pathResultAxs, step);
       /* System.out.println();
        System.out.println("----- Results -----");
        for (String res: results
             ) {
            System.out.println(res);
        }
        System.out.println();
        resultAxs.print();*/
        return resultAxs;
    }

    public AxPreqInsts getValidAxPreq(AxPreq axPreq) {

        Tokens tokensTemp = (Tokens) this.tokens.clone();
        AxPreqInsts axPreqInstsAll = new AxPreqInsts();



        for (AxPreqItem axPreqItem : axPreq
        ) {
            AxPreqInsts axPreqInstForItems = new AxPreqInsts();

            Tokens tks = new Tokens();
            for (String textSlot : axPreqItem.getTextSlots()
            ) {
                // just limited to the slots that are used in the current item axiom prerequisites
                Token token = tokensTemp.getToken(textSlot);
                if (token.getOntoElts().size()>0) {
                    tks.add(token);
                }
            }
            System.out.println("Token[tks] = "+tks.size());
            for (Token token: tks
                 ){
               // System.out.println(token.getTextSlot()+" "+token.getOntoElts().size());
                for (String s: token.getOntoElts()
                     ) {
                    System.out.println(s);
                }
            }
            System.out.println();

            // experiment
           //Tokens tokens2 = new Tokens();
            //tokens2.add(tks.get(0));
            //tokens2.add(tks.get(1));
            //tks = tokens2;

            AxPreqInsts axPreqInsts = retrieveTextsToCheck(tks, axPreqItem.getText());

            for (AxPreqInst inst : axPreqInsts
            ) {
                MyOnto.getManchesterOWLSyntaxParser().setStringToParse(inst.getText());
                //System.out.println(inst.getText() + " " + tks.size());
                //if (inst.getText().contains("Omnivore> EquivalentTo") && inst.getText().contains("Plant"))
                   // System.out.println("SEE check = "+inst.getText());
                OWLAxiom owlAxiom = MyOnto.getManchesterOWLSyntaxParser().parseAxiom();
                boolean entailed = MyOnto.getOWLReasoner().isEntailed(owlAxiom);
                if (entailed) {
                    //axPreqInstForItems.add(inst);
                    axPreqInstsAll.add(inst);
                }
            }
        }

        return axPreqInstsAll;
    }

    private Tokens getValidTokensFromValidAxiomPreq(AxPreqInst axPreqInst) {
        Tokens tokens = new Tokens();
        Associations assocs = axPreqInst.getAssociations();
        for (AssociationTokenOntoElt ass : assocs
        ) {
            Token token = tokens.getToken(ass.getToken().getTextSlot());
            if (token == null) {
                Token newToken = new Token();
                newToken.setSlotSpec(ass.getToken().getSlotSpec());
                newToken.addOntoElt(ass.getOntologyElt());
                tokens.add(newToken);
            } else {
                token.addOntoElt(ass.getOntologyElt());
            }
        }
        return tokens;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public AxPreqs getAxPreqs() {
        return axPreqs;
    }
}
