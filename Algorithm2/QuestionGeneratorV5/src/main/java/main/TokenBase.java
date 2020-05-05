/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import simplenlg.framework.WordElement;

/**
 *
 * @author toky.raboanary
 */
public class TokenBase {
    private String word;
    private WordElement wElement;
    private String category;
    private String id;
    private String base;
    //private String 
    private NodeList nList;
    
    
    public TokenBase(String word, WordElement wElement) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        this.setWord(word);
        this.setWordElement(wElement);
        this.initByXML(wElement.toXML());
    }
    
    public boolean isPlurial() {
        return this.getWordElement().isPlural();
    }
    private void initByXML(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.parse(input);
        this.nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName()); // root element
         
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               //System.out.println("Student roll no : " 
                 // + eElement.getAttribute("rollno"));
               String base = eElement.getElementsByTagName("base").item(0).getTextContent();
               //System.out.println("Base : "+ base);
               this.setBase(base);
               String category = eElement.getElementsByTagName("category").item(0).getTextContent();
               this.setCategory(category);
               //System.out.println("category : "+ category);
               
               String id = eElement.getElementsByTagName("id").item(0).getTextContent();
               this.setId(id);
               //System.out.println("id =  "+ id);
               
             //  String default_infl = eElement.getElementsByTagName("default_infl").item(0).getTextContent();
             //  System.out.println("default_infl : "+default_infl);
            }
        }
    }
    
    private void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return this.word;
    }
    
    private void setBase(String base) {
        this.base = base;
    }
    public String getBase() {
        return this.base;
    }
    
    private void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    protected void setCategory( String category) {
        this.category = category;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    private void setWordElement(WordElement wElt) {
        this.wElement = wElt;
    }
    
    public WordElement getWordElement() {
        return this.wElement;
    }
    public NodeList getNodeList() {
        return this.nList;
    }
    
    public void print () {
        System.out.println("Word = "+this.getWord());        
        System.out.println("Category = "+this.getCategory());
        System.out.println("Base = "+this.getBase());
    }
    
}
