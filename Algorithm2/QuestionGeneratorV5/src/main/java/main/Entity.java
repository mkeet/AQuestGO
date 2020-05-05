/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Set;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 *
 * @author toky.raboanary
 */
public class Entity {
    private Ontology ontology;
    private OWLClass owlClass;
    
    public Entity(Ontology ontology, OWLClass owlClass) throws Exception {
        this.ontology = ontology;
        this.owlClass = owlClass;
    }
    
    public Entity(Ontology ontology, String className) throws Exception {
        this(ontology, ontology.getFactory().getOWLClass(className, ontology.getPrefixManager()));
    }
    
    public OWLClass getOWLClass() {
        return this.owlClass;
    }
    public Ontology getOntology() {
        return this.ontology;
    }
    public ArrayList<OWLClass> getSuperclasses() { // mety tsara ovaina Tree :) 
        
        ArrayList <OWLClass> results = new ArrayList<>();
        
        NodeSet<OWLClass> superClasses = this.ontology.getReasoner().getSuperClasses(this.getOWLClass(), false);
      //  System.out.println("count superclasse 3 :"+ superClasses.getFlattened().toArray().length);
       // Set<OWLClass> flattened = superClasses.getFlattened();
       // for (OWLClass superClass : flattened) {
         //   System.out.println(superClass);
        //}
        
        //NodeSet<OWLClass> subclasses = this.ontology.getReasoner().get(this.getOWLClass(), false);
        Set<Node<OWLClass>> subclassesNodes = superClasses.getNodes();
        subclassesNodes.remove(this.ontology.getReasoner().getTopClassNode());
        for (Node<OWLClass> subclass : subclassesNodes) {
            NodeSet <OWLClass> subsubclasses = this.ontology.getReasoner().getSuperClasses(this.getOWLClass(), true);
            results.add(subclass.getRepresentativeElement());
        }   
        return results;
    }
    
    
    public ArrayList<OWLClass> getSubclasses() { // mety tsara ovaina Tree :) 
        
        ArrayList <OWLClass> results = new ArrayList<>();
        
        NodeSet<OWLClass> subclasses = this.ontology.getReasoner().getSubClasses(this.getOWLClass(), false);
        
        
        
        Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
        subclassesNodes.remove(this.ontology.getReasoner().getBottomClassNode());
        for (Node<OWLClass> subclass : subclassesNodes) {
            NodeSet <OWLClass> subsubclasses = this.ontology.getReasoner().getSubClasses(this.getOWLClass(), true);
            results.add(subclass.getRepresentativeElement());
        }   
        return results;
    }
    
    public ArrayList<OWLClassAxiom> getSubclassAxioms() {
        OWLClass classX = this.getOWLClass();
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        Set<OWLClassAxiom> allAxioms = this.getOntology().getOWLOntology().getAxioms(classX);
        
        for (OWLClassAxiom axiom : allAxioms) {
            if (axiom.getAxiomType().equals(AxiomType.SUBCLASS_OF) && !axiom.getObjectPropertiesInSignature().isEmpty() ) {
                Set<OWLClass> classesInAxiom = axiom.getClassesInSignature();
                if (classesInAxiom.size()==2){
                    subclassAxioms.add(axiom);
                }
            }
        }
        return subclassAxioms;
    }
    public ArrayList<OWLClassAxiom> getAllClassAxioms() {
        OWLClass classX = this.getOWLClass();
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        Set<OWLClassAxiom> allAxioms = this.getOntology().getOWLOntology().getAxioms(classX);
        
        for (OWLClassAxiom axiom : allAxioms) {
            subclassAxioms.add(axiom);
        }
        return subclassAxioms;
    }
    
    private String getFormatted(String definition) throws Exception {
        Entity thing = this.getOntology().getEntity("Thing");
        String prefix = thing.getLabel().split("#")[0];
        prefix = prefix.substring(1);
        //System.out.println("PREFIX = ");
        return definition.replace(prefix+"#", "");
        
    }
    
    public ArrayList<OWLClassAxiom> getEquivalentAxioms() {
        OWLClass classX = this.getOWLClass();
        ArrayList<OWLClassAxiom> subclassAxioms = new ArrayList<OWLClassAxiom>();
        Set<OWLClassAxiom> allAxioms = this.getOntology().getOWLOntology().getAxioms(classX);
        
        for (OWLClassAxiom axiom : allAxioms) {
            if (axiom.getAxiomType().equals(AxiomType.EQUIVALENT_CLASSES) && !axiom.getObjectPropertiesInSignature().isEmpty() ) {
                Set<OWLClass> classesInAxiom = axiom.getClassesInSignature();
               // if (classesInAxiom.size()==2)
                {
                    subclassAxioms.add(axiom);
                }
            }
        }
        return subclassAxioms;
    }
    
    public ArrayList<Entity> getEntitiesSubClasses() throws Exception {
        ArrayList<Entity> entitiesResults = new ArrayList<>();
        ArrayList<OWLClass> owlClassesResults = this.getSubclasses();
        
        for (OWLClass owlClassIte: owlClassesResults) {
            entitiesResults.add(new Entity(this.getOntology(), owlClassIte));
        }
        return entitiesResults;
    }
    public ArrayList<Entity> getEntitiesSuperClasses() throws Exception {
        ArrayList<Entity> entitiesResults = new ArrayList<>();
        ArrayList<OWLClass> owlClassesResults = this.getSuperclasses();
        // System.out.println(this.getLabel()+" count superClasses2 = "+owlClassesResults.size());
        for (OWLClass owlClassIte: owlClassesResults) {
            entitiesResults.add(new Entity(this.getOntology(), owlClassIte));
        }
        return entitiesResults;
    }
    // equivalence class
    public ArrayList<OWLClass> getEquivalentClasses() { // mety tsara ovaina Tree :) 
        ArrayList <OWLClass> results = new ArrayList<>();
        Node<OWLClass> equivalentClasses = this.ontology.getReasoner().getEquivalentClasses(this.getOWLClass());  
        //this.ontology.getReasoner().
       // System.out.println("classe concernee : "+ this.getOWLClass());
        for (OWLClass equivalent : equivalentClasses) {           
            results.add(equivalent);
        }   
        return results;
    }
    public ArrayList<Entity> getEntitiesEquivalentClasses() throws Exception { // mety tsara ovaina Tree :) 
        ArrayList <Entity> results = new ArrayList<>();
        ArrayList<OWLClass> equivalentClasses = this.getEquivalentClasses();
        for (OWLClass equivalent : equivalentClasses) {           
            results.add(new Entity(this.getOntology(), equivalent));
        }   
        return results;
    }
    
    public String getLabel() { // tokony ao amin'ny annotations
        return this.getOWLClass().toString();
    }
    
    public ArrayList<OWLClass> getDisjointClasses() {
        ArrayList <OWLClass> results = new ArrayList<>();
        NodeSet<OWLClass> disjointClasses = this.ontology.getReasoner().getDisjointClasses(this.getOWLClass());
        
        Set<Node<OWLClass>> disjointClassesNodes = disjointClasses.getNodes();
        disjointClassesNodes.remove(this.ontology.getReasoner().getBottomClassNode());
        int i = 1;
        for (Node<OWLClass> disjointClassNodes : disjointClassesNodes) {
           // System.out.println(" disjoint classses hita : NODE = "+i);
            for (OWLClass disjointClass : disjointClassNodes) {
             //   System.out.println(disjointClass.toString());
                results.add(disjointClass);
            }
            i++;
        }   
        return results;
    }
   
    public ArrayList<Entity> getEntitiesDisjointClasses() throws Exception {
    ArrayList <Entity> results = new ArrayList<>();
        ArrayList<OWLClass> disjointClasses = this.getDisjointClasses();
        for (OWLClass equivalent : disjointClasses) {           
            results.add(new Entity(this.getOntology(), equivalent));
        }   
        return results;      
    }
    
    public ArrayList<OWLClass> getSiblingClasses() throws Exception {
        ArrayList <OWLClass> results = new ArrayList<>();

        NodeSet<OWLClass> superClasses = this.ontology.getReasoner().getSuperClasses(this.getOWLClass(), true);
        //NodeSet<OWLClass> subclasses = this.ontology.getReasoner().get(this.getOWLClass(), false);
        Set<Node<OWLClass>> superClassesNodes = superClasses.getNodes();
        superClassesNodes.remove(this.ontology.getReasoner().getTopClassNode());
        System.out.println("bbbbbbbbd kmfblgfmblfmmbkfmbls");
        int node = 0;
        for (Node<OWLClass> superclasses : superClassesNodes) {
            System.out.println(" disjoint classses hita : NODE = "+node);
            // NodeSet <OWLClass> subsubclasses = this.ontology.getReasoner().getSuperClasses(this.getOWLClass(), false);
            for (OWLClass cl : superclasses)   {
                System.out.println("\t\t"+cl.toString());
                results.add(cl);
            }       
            //results.add(subclass.getRepresentativeElement());
            node++;
        }   
        
        
        ArrayList <OWLClass> results2 = new ArrayList<>();
        
        for (OWLClass res : results) {
            NodeSet<OWLClass> subclasses = this.ontology.getReasoner().getSubClasses(res, true);
            Set<Node<OWLClass>> subclassesNodes = subclasses.getNodes();
            subclassesNodes.remove(this.ontology.getReasoner().getBottomClassNode());
            for (Node<OWLClass> subclass : subclassesNodes) {               
                for (OWLClass cl : subclass)   {
                    Entity ent = new Entity(this.getOntology(), cl);
                    
                    System.out.println("\t\t"+cl.toString());
                    boolean newElement = true;
                    if (this.theSameAs(ent)) continue;
                    if (this.isEquivalentTo(ent)) continue;
                    for (OWLClass cc : results2) {
                        if (cc.toString().compareTo(cl.toString())==0)
                            newElement = false;
                    }
                    if (newElement)
                        results2.add(cl);
                }
            }   
        }
        return results2;
    }
    public boolean isA(Entity entity) throws Exception {
        if (entity.getLabel().compareTo(this.getLabel()) == 0)
            return true;
        //System.out.println("count superClasses = "+this.getEntitiesSuperClasses().size());
        for(Entity superClass : this.getEntitiesSuperClasses()) {
            //superClass.Print();
            if (superClass.getLabel().compareTo(entity.getLabel())== 0) {
              //  System.out.println(superClass.getLabel()+" vs "+entity.getLabel());
                return true;
            }
        }
        return false;
    }
    
    public boolean isEquivalentTo(Entity entity) throws Exception {
        ArrayList<Entity> equivalentClasses = this.getEntitiesEquivalentClasses();
        for (Entity ent : equivalentClasses) {
            if (ent.getLabel().compareTo(entity.getLabel())== 0)
                return true;
        }
        return false;
    }
 
    public boolean theSameAs(Entity entity) throws Exception {
        if (this.getLabel().compareTo(entity.getLabel())== 0)
            return true;       
        return false;
    }
    
    public ArrayList<String> getAllObjectPropertiesStirng() {
        Set<OWLObjectProperty> props;
        ArrayList<String> list = new ArrayList<String>();
        
        props = this.getOWLClass().getObjectPropertiesInSignature();
        System.out.println("name = "+this.getLabel()+ " "+props.size());
        for(OWLObjectProperty owlObjectProperty : props) {
            String prop = owlObjectProperty.toString();
            list.add(prop);
        }
        
        return list;
    }
    
    public void printAllObjectProperties() {
        ArrayList<String> elts = this.getAllObjectPropertiesStirng();
        for (String elt: elts) {
            System.out.println(elt);
        }
    }
    
    public ArrayList<Entity> getEntitiesSiblingClasses() throws Exception {
        ArrayList <Entity> results = new ArrayList<>();
        ArrayList<OWLClass> siblingClasses = this.getSiblingClasses();
        for (OWLClass cl : siblingClasses) {           
            results.add(new Entity(this.getOntology(), cl));
        }   
        return results;      
    }
    
    
    // une entity posede une definiton : (en tenant compte des ancestors anonyms )
    //  - superclasses
    //  - subcalss (Children)
    //  - axiom subclasses
    //  - Equivalences
    
    public EntityDefinition getEntityDefinition() throws Exception {
        return new EntityDefinition(this);
    }
    
    
    /* private ArrayList<OWLObjectPropertyExpression> getValidObjectProperty(OWLObjectPropertyExpression owlop) {
        ArrayList <OWLObjectPropertyExpression> results = new ArrayList<>();
        OWLReasoner reasoner = this.getOntology().getReasoner();
         
        NodeSet<OWLObjectPropertyExpression> subObjectProperty = reasoner.getSubObjectProperties(this.getOWLClass(), true);
        Set<Node<OWLObjectPropertyExpression>> subclassesNodes = subObjectProperty.getNodes();
        subclassesNodes.remove(reasoner.getBottomObjectPropertyNode());
        
        for (Node<OWLObjectPropertyExpression> subclass : subclassesNodes) {
            results.add(subclass.getRepresentativeElement());
        }
        
        return results;
    }*/ 
    
    public void Print() {
        System.out.println("********************** Superclasses Of "+this.getLabel()+ " **********************");
        for (OWLClass owlClassIte: this.getSuperclasses()) {
            System.out.println("\t"+owlClassIte.toString());
        }
        System.out.println("********************** Subclass Of "+this.getLabel()+ " **********************");
        for (OWLClass owlClassIte: this.getSubclasses()) {
            System.out.println("\t"+owlClassIte.toString());
        }
        
        System.out.println("********************** Subclass AXIOMS Of "+this.getLabel()+ " **********************");
        for (OWLClassAxiom owlClassAxIte: this.getSubclassAxioms()) {
            System.out.println("\t"+owlClassAxIte.toString());
        }
    }
    
}
