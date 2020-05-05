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
class Type2ResultSet {
        
    // Global Variables
    private  OWLClassExpression X;
    private  OWLClassExpression Y;
    
    /**
     * Constructor for type 2 result set with given variables
     * @param x
     * @param y 
     */
    public Type2ResultSet(OWLClassExpression x, OWLClassExpression y){
        
        this.X = x;
        this.Y = y;
    }
    
    /**
     * Construct a Type 2 results set with null value
     */
    public Type2ResultSet(){
        this.X = null;
        this.Y = null;
    }
    
    /**
     * Check if the result is null
     * @return 
     */
    public boolean isNull(){
        if (X==null || Y==null){
            return true;
        }else{
            return false;
        }
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
     * Override the to string method 
     * @return 
     */
    public String toString(){
        return "Class "+ X + " Class "+ Y;
    }
}
