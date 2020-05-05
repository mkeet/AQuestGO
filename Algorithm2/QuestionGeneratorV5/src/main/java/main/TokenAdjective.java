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

/**
 *
 * @author toky.raboanary
 */
public class TokenAdjective extends TokenBase{
    private String comparative;
    private String superlative;
    
    public TokenAdjective(String word, WordElement wElement) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        super(word, wElement);
        this.initTokenAdjective();
    }
    
    private void initTokenAdjective() {
        NodeList nList = this.getNodeList();
         
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
              
               String comp = eElement.getElementsByTagName("comparative").item(0).getTextContent();
               this.setComparative(comp);
               
               String sup = eElement.getElementsByTagName("superlative").item(0).getTextContent();
               this.setSuperlative(superlative);
            }
        }  
    }
        
    private void setComparative(String comp) {
        this.comparative = comp;
    }
    public String getComparative() {
        return this.comparative;
    }
    private void setSuperlative(String sup) {
        this.superlative = sup;
    }
    public String getSuperlative() {
        return this.superlative;
    } 
}
