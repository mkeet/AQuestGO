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
class Type3ResultSet {
        // Private Global Variables
    private  OWLClassExpression X;
    private  OWLClassExpression Y;
    private  OWLObjectPropertyExpression OP;
    private  String quantifier;
    
    /**
     * Public constructor of the Type 3 result set with the given parameters
     * @param x
     * @param y
     * @param op
     * @param q 
     */
    public Type3ResultSet(OWLClassExpression x, OWLClassExpression y, OWLObjectPropertyExpression op, String q){
        this.X = x;
        this.Y = y;
        this.OP = op;
        this.quantifier = q;
    }
    
    /**
     * Public constructor for an null type 3 result set
     */
    public Type3ResultSet(){
        this.X = null;
        this.Y = null;
        this.OP = null;
        this.quantifier = "";
    }
    
    /**
     * Check if the object is null
     * @return 
     */
    public boolean isNull(){
        if (X==null || Y==null || OP==null || quantifier.equals("")){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Getter for OWLClass X
     * @return 
     */
    public OWLClassExpression getX(){
        return X;
    }
    
    /**
     * Getter for OWLClass Y
     * @return 
     */
    public OWLClassExpression getY(){
        return Y;
    }
    
    /**
     * Getter for ObjectProperty op
     * @return 
     */
    public OWLObjectPropertyExpression getOP(){
        return OP;
    }
    
    /**
     * Getter for String quantifier
     * @return 
     */
    public String getQuantifier(){
        return quantifier;
    }
    
    /**
     * Override toString method ,giving a string representation to the Type 3 object property
     * @return 
     */
    public String toString(){
        return "X: " +LiguisticHandler.stringProcess(getX().toString()) + " Y: "+LiguisticHandler.stringProcess(getY().toString()) + " objectProperty: "+LiguisticHandler.stringProcess(getOP().toString())+ " Quantifier: "+getQuantifier();
    }
}
