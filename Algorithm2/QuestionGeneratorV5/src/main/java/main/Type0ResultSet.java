/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.   
 */
package main;

import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 *
 * @author stevewang
 */
class Type0ResultSet {
    // Global Variables
    private  OWLClassExpression X;
    private  OWLClassExpression Y;
    
    /**
     * Construct a new result set to a null object
     */
    public Type0ResultSet(){
        this.X = null;
        this.Y = null;
    }
    
    /**
     * Constructor for type 1 result set with the given parameters 
     * @param x
     * @param y
     * @param op 
     */
    public Type0ResultSet(OWLClassExpression x , OWLClassExpression y){
        this.X = x;
        this.Y = y;
    }
    
    /**
     * Get OWLClassExpression X
     * @return 
     */
    public OWLClassExpression getX(){
        return X;
    }
    
    /**
     * Get OWLClassExpression Y
     * @return 
     */
    public OWLClassExpression getY(){
        return Y;
    }
    
    /**
     * Check if the result is null
     * @return 
     */
    public boolean isNull(){
        if ( X==null | Y==null ){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Set the values of a result set with the given value 
     * @param x
     * @param y
     * @param op 
     */
    public void setValues (OWLClassExpression x , OWLClassExpression y){
        this.X = x;
        this.Y = y;
    }
    
    /**
     * Override the to string method 
     * @return 
     */
    public String toString(){
        return "Class x "+ X +  " Class y "+ Y;
    }
}
