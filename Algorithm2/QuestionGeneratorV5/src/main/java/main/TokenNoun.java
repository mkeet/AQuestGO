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
public class TokenNoun extends TokenBase{
    
    // ampiaana eto ny momba ny noun :) 
    // eo isika izao :) 
    
    
    public TokenNoun(String word, WordElement wElement) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        super(word, wElement);
    }
    
   
}
