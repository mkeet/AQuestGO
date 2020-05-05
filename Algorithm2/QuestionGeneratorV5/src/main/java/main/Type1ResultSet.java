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
public class Type1ResultSet {
        
    // Global Variables
    private  OWLClassExpression X;
    private  OWLClassExpression Y;
    private  OWLObjectPropertyExpression OP;
    
    /**
     * Construct a new result set to a null object
     */
    public Type1ResultSet(){
        this.X = null;
        this.Y = null;
        this.OP = null;
    }
    
    /**
     * Constructor for type 1 result set with the given parameters 
     * @param x
     * @param y
     * @param op 
     */
    public Type1ResultSet(OWLClassExpression x , OWLClassExpression y, OWLObjectPropertyExpression op){
        this.X = x;
        this.Y = y;
        this.OP = op;
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
     * Get OWLObjectProperty objectProperty
     * @return 
     */
    public OWLObjectPropertyExpression getOP(){
        return OP;
    }
    
    /**
     * Check if the result is null
     * @return 
     */
    public boolean isNull(){
        if ( X==null | Y==null | OP==null){
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
    public void setValues (OWLClassExpression x , OWLClassExpression y, OWLObjectPropertyExpression op){
        this.X = x;
        this.Y = y;
        this.OP = op;
    }
    
    /**
     * Override the to string method 
     * @return 
     */
    public String toString(){
        return "Class "+ X + " Relation " + OP + " Class "+ Y;
    }
}
