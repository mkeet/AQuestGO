/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 *
 * @author toky.raboanary
 */
public class EntityDefinition {
    
    // une entity posede une definiton : (en tenant compte des ancestors anonyms )
    //  - superclasses
    //  - subcalss (Children)
    //  - axiom subclasses
    //  - Equivalences
    
    private ArrayList<Entity> equivalentClasses;
    private ArrayList<Entity> subClasses; // children
    private ArrayList<Entity> superClasses;
    private ArrayList<Entity> disjointClasses;
    private ArrayList<Entity> siblingClasses;
    
    private ArrayList<OWLClassAxiom> classAxioms; // subclass axioms and equivalent class axioms
    
    private Entity entity;
    
    public EntityDefinition(Entity entity) throws Exception {
        this.entity = entity;
        this.compute();
    }
    private void compute() throws Exception {
        this.equivalentClasses = entity.getEntitiesEquivalentClasses();
        
        System.out.println("nombre de classes equivalentes : "+ this.equivalentClasses.size());
        this.subClasses = entity.getEntitiesSubClasses();
        this.superClasses = entity.getEntitiesSuperClasses();
        this.classAxioms = entity.getSubclassAxioms();
        this.disjointClasses = entity.getEntitiesDisjointClasses();
        this.siblingClasses = entity.getEntitiesSiblingClasses();
        
        // update superclasses
        for (Entity equivalentClass : this.equivalentClasses){
            ArrayList<Entity> equiSuperclasses = equivalentClass.getEntitiesSuperClasses();
            for (Entity equiSuperclasse : equiSuperclasses) {
               // if (equiSuperclasse.getLabel() == this.getEntity().getLabel()) continue;
                boolean newElement = true;
                for (Entity superclasse : this.superClasses) {
                    
                   // System.out.println("----------");
                  //  System.out.println(equiSuperclasse.getLabel());
                  ///  System.out.println(superclasse.getLabel());
                   // System.out.println("----------");
                    if (equiSuperclasse.getLabel().compareTo(superclasse.getLabel())==0) {
                        
                       // System.out.println("Misy false oooo ");
                        newElement = false;
                    }
                }
                
                if (newElement)
                {
                    this.superClasses.add(equiSuperclasse);
                }
            }
        }
        
        // update classAxioms
        for (Entity equivalentClass : this.equivalentClasses){
            ArrayList<OWLClassAxiom> equiSubClassAxioms = equivalentClass.getSubclassAxioms();
            
            
            for (OWLClassAxiom equiSubClassAxiom : equiSubClassAxioms) {
                boolean newElement = true;
                for (OWLClassAxiom classAxiom : this.classAxioms) {
                    if (equiSubClassAxiom.toString().compareTo(classAxiom.toString())==0)
                        newElement = false;
                }
                
                if (newElement)
                {
                    this.classAxioms.add(equiSubClassAxiom);
                }
            }
        }
        
        // tenir compte axiom an subclasses
        //System.out.println(">>>>>>>>> subclasses consideration <<<<<<<<<<<");
        for(Entity superClass : this.superClasses) {       
            OWLClass classX = superClass.getOWLClass();
            //Set<OWLClassAxiom> equiSubClassAxioms = this.getEntity().getOntology().getOWLOntology().getAxioms(classX);
            ArrayList<OWLClassAxiom> equiSubClassAxioms = superClass.getEquivalentAxioms();
            
            System.out.println("      class -> "+superClass.getLabel() + " " +equiSubClassAxioms.size());
            
            for (OWLClassAxiom equiSubClassAxiom : equiSubClassAxioms) {
                boolean newElement = true;
                for (OWLClassAxiom classAxiom : this.classAxioms) {
                    
                    if (equiSubClassAxiom.toString().compareTo(classAxiom.toString())==0)
                        newElement = false;
                }
                if (newElement)
                {
                    this.classAxioms.add(equiSubClassAxiom);
                }
            }
        }
        
        // tenir compte axiom an equivalences an subclasses
        //System.out.println(">>>>>>>>> subclasses consideration <<<<<<<<<<<");
        for(Entity superClass : this.superClasses) {       
            //OWLClass classX = superClass.getOWLClass();
            //Set<OWLClassAxiom> equiSubClassAxioms = this.getEntity().getOntology().getOWLOntology().getAxioms(classX);
            ArrayList<OWLClassAxiom> equiSubClassAxioms = superClass.getSubclassAxioms();
            
            System.out.println("      class -> "+superClass.getLabel() + " " +equiSubClassAxioms.size());
            
            for (OWLClassAxiom equiSubClassAxiom : equiSubClassAxioms) {
                boolean newElement = true;
                for (OWLClassAxiom classAxiom : this.classAxioms) {
                    if (equiSubClassAxiom.toString().compareTo(classAxiom.toString())==0)
                        newElement = false;
                }
                if (newElement)
                {
                    this.classAxioms.add(equiSubClassAxiom);
                }
            }
        }
    }
    
    
    
    public void Print() throws Exception {
           
        System.out.println("********************** Equivalent classes Of "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (Entity entityIte: this.getEquivalentClasses()) {
            System.out.println("\t"+getFormatted(entityIte.getLabel()));
        }
        
        System.out.println("********************** Superclasses Of "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (Entity entityIte: this.getSuperClasses()) {
            System.out.println("\t"+getFormatted(entityIte.getLabel()));
        }
        System.out.println("********************** Subclass Of "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (Entity entityIte: this.getSubclasses()) {
            System.out.println("\t"+getFormatted(entityIte.getLabel()));
        }
        
        System.out.println("********************** Disjoint Classes "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (Entity entityIte: this.getDisjointClasses()) {
            System.out.println("\t"+getFormatted(entityIte.getLabel()));
        }
        
        System.out.println("********************** Sibling Classes "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (Entity entityIte: this.getSiblingClasses()) {
            System.out.println("\t"+getFormatted(entityIte.getLabel()));
        }
        
        System.out.println("********************** Subclass AXIOMS Of "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        System.out.println(">>>>>>   subClassOf and EquivalenceClass   <<<<<<");
        for (OWLClassAxiom owlClassAxIte: this.getClassAxioms()) {
            System.out.println("\t"+getFormatted(owlClassAxIte.toString()));
            
            //QuestionDraft q1 = new QuestionDraft(this.getEntity(), owlClassAxIte);
            
          /*  Set<OWLEntity> oEnts = owlClassAxIte.getSignature(); // entity can be a class[domain]/ObjectProperty/DataProperty/class[Range]
            for (OWLEntity oEnt : oEnts)
            {
                System.out.println("\t\t - "+getFormatted(oEnt.toString()));
            } */      
        }

        System.out.println("********************** ALLL CLASS-AXIOMS Of "+this.getFormatted(this.getEntity().getLabel())+ " **********************");
        for (OWLClassAxiom owlClassAxIte: this.entity.getAllClassAxioms()) {
            
            System.out.println("\t"+this.getFormatted(owlClassAxIte.toString()));
         //   QuestionDraft q2 = new QuestionDraft(this.getEntity(), owlClassAxIte);
            
            /*
            Set<OWLEntity> oEnts = owlClassAxIte.getSignature();
            for (OWLEntity oEnt : oEnts)
            {
                System.out.println("\t\t - "+getFormatted(oEnt.toString()));
            }       
            */
        }        
        String s = "toto";
        String ss = s.replaceAll("o", "a");
        System.out.println(ss);
        
       /* Entity liona = this.getEntity().getOntology().getEntity("Liona");
        String definition = liona.getLabel();
        Entity thing = this.getEntity().getOntology().getEntity("Thing");
        String prefix = thing.getLabel().split("#")[0];
        prefix = prefix.substring(1);
        System.out.println("PREFIX = "+prefix);
         System.out.println(definition.replace(prefix, ""));*/
        
    }
    private String getFormatted(String definition) throws Exception {
        Entity thing = this.getEntity().getOntology().getEntity("Thing");
        String prefix = thing.getLabel().split("#")[0];
        prefix = prefix.substring(1);
        //System.out.println("PREFIX = ");
        return definition.replace(prefix+"#", "");
        
    }
    
    public ArrayList<Entity> getSuperClasses() {
        return this.superClasses;
    }
    
    public ArrayList<Entity> getSubclasses() {
        return this.subClasses;
    }
    
    public ArrayList<Entity> getEquivalentClasses() {
        return this.equivalentClasses;
    }
    
    public ArrayList<OWLClassAxiom> getClassAxioms() {
        return this.classAxioms;
    }
    
    public ArrayList<Entity> getDisjointClasses() {
        return this.disjointClasses;
    }
  
    public ArrayList<Entity> getSiblingClasses() {
        return this.siblingClasses;
    }
    public Entity getEntity() {
        return this.entity;
    }
}
