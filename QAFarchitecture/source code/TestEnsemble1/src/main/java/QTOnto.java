import net.sf.extjwnl.JWNLException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class QTOnto {
    private String filename;
    private String typeOfQuestions;
    private Document doc;
    private SlotSpecs slotSpecs;
    private AxPreqs aps;
    private LTemplates lTemplates;
    private Answers answers;

    public QTOnto(String filename){

        try {
            this.filename = filename;
            File file = new File(this.filename);
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            this.doc = doc;
            this.retrieveTQ();
            this.slotSpecs = new SlotSpecs();
            this.retrieveSlotSpecs();
            this.aps = new AxPreqs();
            this.retrieveAxPreqs();
            this.lTemplates = new LTemplates();
            this.retrieveLTs();
            this.answers = new Answers();
            this.retrieveAnswers();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }

    public Document getDoc() {
        return doc;
    }

    private void retrieveTQ(){
        this.typeOfQuestions = this.getDoc().getElementsByTagName("TQ").item(0).getTextContent();
    }

    private void retrieveSlotSpecs(){
        NodeList nodeList = this.getDoc().getElementsByTagName("SlotSpec");
        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                SlotSpec slotSpec = new SlotSpec();
                String text = node.getTextContent().trim();
                slotSpec.setText(text);
                String nature = eElement.getAttribute("nature").trim();
                slotSpec.setNature(nature);
                String route = eElement.getAttribute("route").trim();
                slotSpec.setRoute(route);
                String id = eElement.getAttribute("id").trim();

                slotSpec.setId(Integer.parseInt(id));

                String excludes = eElement.getAttribute("excludes").trim();
                ArrayList<String> excls = new ArrayList<>();
                if (excludes != null) {
                    String[] ontoEltsToExclude = excludes.split(" ");
                    for (String str : ontoEltsToExclude
                    ) {
                        excls.add(str);
                    }
                }

                slotSpec.setExcludes(excls);
                this.getSlotSpecs().add(slotSpec);
            }
        }
    }

    private void retrieveAxPreqs(){
        NodeList nodeList = this.getDoc().getElementsByTagName("AP");

        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                AxPreq axPreq = new AxPreq();
                String id = eElement.getAttribute("id").trim();
                axPreq.setId(Integer.parseInt(id));

                NodeList nodeListItem = eElement.getElementsByTagName("APitem");

                for (int itr2 = 0; itr2 < nodeListItem.getLength(); itr2++)
                {
                    Node node2 = nodeListItem.item(itr2);
                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement2 = (Element) node2;
                        AxPreqItem axPreqItem = new AxPreqItem();
                        String text = node2.getTextContent().trim();
                        axPreqItem.setText(text);
                        String id2 = eElement2.getAttribute("id").trim();
                        axPreqItem.setId(Integer.parseInt(id2));
                        axPreq.add(axPreqItem);
                    }

                }


                this.getAxPreqs().add(axPreq);
            }
        }
    }
    private void retrieveLTs() {
        NodeList nodeList = this.getDoc().getElementsByTagName("LT");

        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                LTemplate lTemplate = new LTemplate();
                String text = node.getTextContent().trim();
                lTemplate.setText(text);
                String id = eElement.getAttribute("id").trim();
                lTemplate.setId(Integer.parseInt(id));
                this.getLTemplates().add(lTemplate);
            }
        }
    }

    private void retrieveAnswers(){
        NodeList nodeList = this.getDoc().getElementsByTagName("A");

        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                Answer answer = new Answer();
                String text = node.getTextContent();
                answer.setText(text);
                String id = eElement.getAttribute("id").trim();
                answer.setId(Integer.parseInt(id));
                String type = eElement.getAttribute("type").trim();
                answer.setType(type);
                if (answer.getType().equals("LT")) {

                    NodeList nodeAnswerLTs = eElement.getElementsByTagName("LTA");
                    for (int itrLTs = 0; itrLTs < nodeAnswerLTs.getLength(); itrLTs++)
                    {
                        Node nodeAns = nodeAnswerLTs.item(itrLTs);
                        if (nodeAns.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElementAns = (Element) nodeAns;
                            LTemplate lTemplate = new LTemplate();
                            String textAns = node.getTextContent().trim();
                            lTemplate.setText(textAns);
                            String idAns = eElementAns.getAttribute("id").trim();
                            lTemplate.setId(Integer.parseInt(idAns));
                            answer.getLTemplates().add(lTemplate);
                        }
                    }
                }
                this.getAnswers().add(answer);
            }
        }
    }
    public String getTypeOfQuestions() {
        return typeOfQuestions;
    }

    public SlotSpecs getSlotSpecs() {
        return slotSpecs;
    }

    public AxPreqs getAxPreqs() {
        return aps;
    }

    public LTemplates getLTemplates() {
        return lTemplates;
    }

    public Answers getAnswers() {
        return answers;
    }


    public void print() {
        System.out.println("Type of questions "+ this.getTypeOfQuestions());
        this.getSlotSpecs().print();
        Tokens tokens = new Tokens(this.getSlotSpecs());
        tokens.print();
        this.getAxPreqs().print();
        this.getLTemplates().print();
        this.getAnswers().print();


    }

    public void printTest() {
        System.out.println("Type of questions "+ this.getTypeOfQuestions());
        this.getSlotSpecs().print();
        Tokens tokens = new Tokens(this.getSlotSpecs());
        tokens.print();
        this.getAxPreqs().print();
        /*AxPreq ax0 = this.getAxPreqs().get(0);
        AxPreq ax1 = this.getAxPreqs().get(1);
        AxPreqs axPreqs = new AxPreqs();
        axPreqs.add(ax0);
        axPreqs.add(ax1);*/

        TokensValidator validator = new TokensValidator(this.getAxPreqs(), tokens);
        //AxPreqInsts validAxPreq = validator.getValidAxPreq(ax);
        //validAxPreq.print();
        this.getLTemplates().print();
        this.getAnswers().print();
       // System.out.println(validAxPreq.size());
        //validAxPreq.print();
        //ResultAxs resultAxs = validator.initValidAxioms(ax);
        ListResultAxs listResultAxs = validator.initAllValidAxioms();


        Verbaliser verbaliser = null;
        try {
            verbaliser = new Verbaliser();
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        ArrayList<String> questions = new ArrayList<>();

        for (ResultAxs resultAxs: listResultAxs
             ) {
            ListAssociations listAssociations = resultAxs.getListAssociations();
            for (LTemplate lTemplate: this.getLTemplates()
            ) {
                System.out.println("template = "+lTemplate.getId()+" "+lTemplate.getText());
                for (Associations associations: listAssociations
                ) {
                    String output = verbaliser.render(lTemplate.getText(), associations);
                    if (output!= null) {
                        System.out.println(output);
                        questions.add(output);
                    }
                }
            }
        }
        System.out.println();
        System.out.println("----- Questions -----");
        for (String question: questions
             ) {
            System.out.println(question);
        }
        System.out.println();
    }
}
