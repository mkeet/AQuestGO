/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.  
 */
package main;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 *
 * @author stevewang
 */
class Type5ResultSet {
    // Private static global variables
    private  OWLClassExpression X;
    private  OWLObjectPropertyExpression OP;
    
    /**
     * Constructor for null Type 5 result set 
     * All attributes are initiated to null 
     */
    public Type5ResultSet(){
        this.X = null;
        this.OP = null;
    }
    
    /**
     * Constructor for type 5 result set with the given attributes
     * @param x
     * @param op 
     */
    public Type5ResultSet(OWLClassExpression x , OWLObjectPropertyExpression op){
        this.X = x;
        this.OP = op;
    }
    
    /**
     * Getter method for OWLClass X
     * @return 
     */
    public OWLClassExpression getX(){
        return X;
    }
    
    /**
     * Getter method for OWlObjectProperty OP
     * @return 
     */
    public OWLObjectPropertyExpression getOP(){
        return OP;
    }
    
    /**
     * Check if the given type 5 result set is null
     * @return 
     */
    public boolean isNull(){
        if ( X==null | OP==null){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Sets the value of the result set with the given parameters
     * @param x
     * @param y
     * @param op 
     */
    public void setValues (OWLClassExpression x , OWLClassExpression y, OWLObjectPropertyExpression op){
        this.X = x;
        this.OP = op;
    }
    
    /**
     * Override the toString method to a string representation of the result set
     * @return 
     */
    public String toString(){
        return "Class "+ X + " Relation " + OP ;
    }
}
