import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        OntologyFunctions ont = new OntologyFunctions();
        OntModel model = ModelFactory.createOntologyModel();
        String filename = "proj_rdf.owl";
        InputStream in = FileManager.get().open(filename);
        model.read(in, null);
        /**
         * Zad 1
         */
        System.out.println("Zadanie 1 (Lista klas):\n");
        List<OntClass> classes = ont.getOntologyClasses(model);
        for (OntClass classInst : classes) {
            if(classInst.getLocalName() != null) {
                System.out.println(classInst.getLocalName() + "\t");
            }
        }
        /**
         * Zad 2
         */
        System.out.println("\nZadanie 2 (Lista relacji obiektów):\n");
        List<ObjectProperty> objectProperties = ont.getOntologyObjectProperties(model);
        for (ObjectProperty propertyInst : objectProperties) {
            if(propertyInst.getLocalName() != null) {
                System.out.println(propertyInst.getLocalName() + "\t");
            }
        }

        System.out.println("\n(Lista relacji danych):\n");
        List<DatatypeProperty> datatypeProperties = ont.getOntologyDatatypeProperties(model);
        for (DatatypeProperty propertyInst : datatypeProperties) {
            if(propertyInst.getLocalName() != null) {
                System.out.println(propertyInst.getLocalName() + "\t");
            }
        }
        /**
         * Zad 3
         */
        System.out.println("\nZadanie 3 (Lista instancji):\n");
        List<Individual> individuals = ont.getOntologyIndividuals(model);
        for (Individual individualInst : individuals) {
            if(individualInst.getLocalName() != null) {
                System.out.println(individualInst.getLocalName() + "\t");
            }
        }
        /**
         * Zad 4
         */
        System.out.println("\nZadanie 4 (Lista instancji dla klas):\n");
        ont.showOntologyClassesWithInstances(model);
        /**
         * Zad 5
         */
        System.out.println("\nZadanie 5 (Lista klas z podklasami):\n");
        ont.showOntologyClassesWithSubclasses(model);
        /**
         * Zad 6
         */
        System.out.println("\nZadanie 6 (Lista zakresów i dziedzin dla relacji obiektów):\n");
        ont.showOntologyDomainsAndRangesOfObjectProperties(model);

        System.out.println("\n(Lista zakresów i dziedzin dla relacji danych):\n");
        ont.showOntologyDomainsAndRangesOfDatatypeProperties(model);
        /**
         * Zad 7
         */
        System.out.println("\nZadanie 7 (Zapytania SPARQL):\n");
        String sparql = "PREFIX onto:<http://www.semanticweb.org/kamil/ontologies/2017/10/untitled-ontology-9#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT (?NamedCharacters as ?Niskie_Postacie)"
                + "WHERE {"
                + "?NamedCharacters rdfs:subClassOf [ a owl:Restriction;"
                + "owl:onProperty onto:hasHeight; owl:someValuesFrom onto:Short ]"
                + "}";
        System.out.println("Zapytanie 1\n");
        ont.sparqlQuery(sparql, model);

        sparql = "PREFIX onto:<http://www.semanticweb.org/kamil/ontologies/2017/10/untitled-ontology-9#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT (?Specie as ?Dunedaini)"
                + "WHERE {"
                + "?Specie rdfs:subClassOf [ a owl:Restriction;"
                + "owl:onProperty onto:hasCreator; owl:someValuesFrom onto:Eru ] ."
                + "?Specie rdfs:subClassOf [ a owl:Restriction;"
                + "owl:onProperty onto:hasMortality; owl:someValuesFrom onto:LongLife ]"
                + "}";
        System.out.println("Zapytanie 2\n");
        ont.sparqlQuery(sparql, model);

        sparql = "PREFIX onto:<http://www.semanticweb.org/kamil/ontologies/2017/10/untitled-ontology-9#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT (?ElvesByClan as ?Calaquendi)"
                + "WHERE {"
                + "?ElvesByClan rdfs:subClassOf [ a owl:Restriction;"
                + "owl:onProperty onto:hasSpecialCharacteristic; owl:someValuesFrom onto:hasSeenLightOfAman ] "
                + "}";
        System.out.println("Zapytanie 3\n");
        ont.sparqlQuery(sparql, model);

        /**
         * Zad 8
         */
        System.out.println("\nZadanie 8 (Reguły)\n");

        String rules = "rule.txt";

        Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(rules));
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        OntModel newModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, infModel);
    }


}
