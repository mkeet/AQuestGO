/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 *
 * @author toky.raboanary
 */
public class TokenVerb extends TokenBase{
    
    private String past;
    private String pastParticiple;
    private String present3s;
    private String presentParticiple;
    
    
    public TokenVerb(String word, WordElement wElement) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        super(word, wElement);
        
        this.initTokenVerb();
        
        this.setCategory("verb");
    }
    
    private void initTokenVerb() {
        NodeList nList = this.getNodeList();
         
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
//            VPPhraseSpec wElt = LinguisticClass.getNLGFactory().createVerbPhrase(word);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
              
               try {
                    String past = eElement.getElementsByTagName("past").item(0).getTextContent();
                    this.setPast(past);
               }
               catch (Exception e) {
                    this.setPast(this.getDefaultPast());
               }
               
               try {
                    String pastParticiple = eElement.getElementsByTagName("pastParticiple").item(0).getTextContent();
                    this.setPastParticiple(pastParticiple);
               }
               catch (Exception e) {
                    this.setPastParticiple(this.getDefaultPast());
                }
               try {
                    String present3s = eElement.getElementsByTagName("present3s").item(0).getTextContent();
                    this.setPresent3s(present3s);
               }
               catch (Exception e) {
                   this.setPresent3s(this.getWord()+"s");
               }
               try {
                    String presentParticiple = eElement.getElementsByTagName("presentParticiple").item(0).getTextContent();
                    this.setPresentParticiple(presentParticiple);
               }
               catch (Exception e) {
                   this.setPresentParticiple(this.getDefaultPresentParticiple());
               }
            }
        }
    }
    private String getDefaultPast() {
        String input = this.getWord().toLowerCase();
        String lastLetter = input.substring(input.length()-1);
        if (lastLetter.equals("e"))
        {
            return input+"d";
        }
        else return input+"ed";
    }
    
    private String getDefaultPresentParticiple() {
        String input = this.getWord().toLowerCase();
        String lastLetter = input.substring(input.length()-1);
        if (lastLetter.equals("e"))
        {
            return input.subSequence(0, input.length()-1)  +"ing";
        }
        else return input+"ing";
    }
    private void setPast(String past) {
        this.past = past;
    }
    public String getPast() {
        return this.past;
    }
    
    private void setPastParticiple(String pastParticiple) {
        this.pastParticiple = pastParticiple;
    }
    
    public String getPastParticiple() {
        return this.pastParticiple;
    }
    private void setPresent3s(String present3s) {
        this.present3s = present3s;
    }
    public String getPresent3s() {
        return this.present3s;
    }
    private void setPresentParticiple(String presentParticiple) {
        this.presentParticiple = presentParticiple;
    }
    public String getPresentParticiple() {
        return this.presentParticiple;
    }
    
    public String getGerund() {
        return this.getPresentParticiple();
    }
    
    public String getInfinitive() {
        return this.getBase();
    }
    
    @Override
    public void print() {
       super.print();
       System.out.println("Past = "+this.getPast());
       System.out.println("Past Participle = "+this.getPastParticiple());
       System.out.println("Present 3s = "+this.getPresent3s());
       System.out.println("Present Participle = "+this.getPresentParticiple());
    }
    public VerbFormType getVerbFormType() {
        if (this.getWord().compareTo(this.getInfinitive())==0 ) {
            return VerbFormType.INFINITIVE;
        } else if (this.getWord().compareTo(this.getPast())==0 ) {
            return VerbFormType.PAST;
        } else if (this.getWord().compareTo(this.getPastParticiple())==0 ) {
            return VerbFormType.PAST_PARTICIPLE;
        } if (this.getWord().compareTo(this.getPresent3s())==0 ) {
            return VerbFormType.PRESENT3S;
        } else if (this.getWord().compareTo(this.getPresentParticiple())==0 ) {
            return VerbFormType.PRESENT_PARTICIPLE;
        } else return VerbFormType.UNKNOWN;
    }
}
