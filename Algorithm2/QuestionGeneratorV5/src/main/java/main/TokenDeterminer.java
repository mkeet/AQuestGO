/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simplenlg.framework.WordElement;

/**
 *
 * @author toky.raboanary
 */
public class TokenDeterminer extends TokenBase{
    
    public TokenDeterminer(String word, WordElement wElement) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        super(word, wElement);
    }
    
}
