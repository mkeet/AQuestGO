/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Set;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 *
 * @author toky.raboanary
 */
public class QuestionDraft {
    private Entity entity;
    private OWLClassAxiom axiom;
    
    public QuestionDraft(Entity entity) {
        this.entity = entity;
    }
    public QuestionDraft(Entity entity, OWLClassAxiom axiom) throws Exception {
        this(entity);
        this.setAxiomClass(axiom);
    }
    
    public void setAxiomClass(OWLClassAxiom axiom) throws Exception {
    //    System.out.println("TEST QUESTION DRAFT : "+ Singleton.getFormatted(axiom.toString()));
        
        OWLClass range = this.OWLClassInSig(axiom);
        OWLObjectPropertyExpression property = this.OWLObjectProperyInSig(axiom);
        
        String rangeStr = Singleton.getFormatted(range.toString());
        String propertyStr = Singleton.getFormatted(property.toString());
        String domaineStr = Singleton.getFormatted(this.getEntity().getLabel());
        String result = domaineStr+ " "+propertyStr+ " "+rangeStr;
        
     //   System.out.println(" ----> RESULT : "+ result);
        /*
        Set<OWLObjectProperty> props = axiom.getObjectPropertiesInSignature();
        for (OWLObjectProperty prop : props) {
            Set<OWLClassExpression> range = prop.getRanges(this.getOntology().getOWLOntology());
        }*/
        
    }
    private OWLClass OWLClassInSig(OWLClassAxiom axiom /*, OWLClass classX*/) {
        OWLClass output = null;
        Set<OWLClass> classes= axiom.getClassesInSignature();
        //classes.remove(classX);
        for(OWLClass x : classes){
            output = x;
        }
        return output;
    }

    private OWLObjectPropertyExpression OWLObjectProperyInSig(OWLClassAxiom axiom) {
        OWLObjectPropertyExpression output = null;
        Set<OWLObjectProperty> ops = axiom.getObjectPropertiesInSignature();
        for(OWLObjectProperty x : ops){
            output = x;
        }
        return output;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public Ontology getOntology() {
        return this.getEntity().getOntology();
    }
}
