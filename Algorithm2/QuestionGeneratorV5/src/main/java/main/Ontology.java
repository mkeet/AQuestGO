/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 *
 * @author toky.raboanary
 */
public class Ontology {
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private OWLReasoner reasoner;
    private OWLOntologyFormat format; 
    private PrefixOWLOntologyFormat prefixFormat;
    private PrefixManager prefixManager;
    private Boolean consistent;
    
    public void loadOntology(File OntologyFile) throws OWLOntologyCreationException, FileNotFoundException  {
        this.manager = OWLManager.createOWLOntologyManager();
        this.ontology = manager.loadOntologyFromOntologyDocument(OntologyFile);
        this.factory = manager.getOWLDataFactory();
        this.reasoner = new Reasoner(ontology);
        this.format = manager.getOntologyFormat(ontology);
        this.prefixFormat = format.asPrefixOWLOntologyFormat();
        this.prefixManager = new DefaultPrefixManager(prefixFormat);
        this.reasoner.precomputeInferences();
        this.consistent = this.reasoner.isConsistent();
    }
    
    public void loadOntology(String filename) throws OWLOntologyCreationException, FileNotFoundException {
        this.loadOntology(new File(filename));
    }
    
    public OWLOntology getOWLOntology() {
        return this.ontology;
    }
    public OWLOntologyManager getManager() {
        return this.manager;
    }
    public OWLDataFactory getFactory() {
        return this.factory;
    }
    public OWLReasoner getReasoner() {
        return this.reasoner;
    }
    public OWLOntologyFormat getFormat() {
        return this.format;
    }
    public PrefixOWLOntologyFormat getPrefixFormat(){
        return this.prefixFormat;
    }
    public PrefixManager getPrefixManager() {
        return this.prefixManager;
    }
    public boolean isConsistent() {
        return this.consistent;
    } 
    
    public Entity getEntity(String className) throws Exception {
        return new Entity(this, className);
    }
    
    public Entity getEntity(OWLClass owlClass) throws Exception {
        return new Entity(this, owlClass);
    }
}
