import org.apache.jena.Jena;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kamil on 15.01.2018.
 */
public class OntologyFunctions {


    public OntologyFunctions() {
    }

    public List<OntClass> getOntologyClasses(OntModel model){
        List<OntClass> classesSet = new ArrayList<OntClass>();
        ExtendedIterator<OntClass> classes = model.listClasses();

        while (classes.hasNext())
        {
            OntClass thisClass = (OntClass) classes.next();
            classesSet.add(thisClass);
        }

        return classesSet;
    }

    public List<DatatypeProperty> getOntologyDatatypeProperties(OntModel model){
        List<DatatypeProperty> propertiesSet = new ArrayList<DatatypeProperty>();
        ExtendedIterator datatypeProperties = model.listDatatypeProperties();

        while (datatypeProperties.hasNext())
        {
            DatatypeProperty thisProperty = (DatatypeProperty) datatypeProperties.next();
            propertiesSet.add(thisProperty);
        }

        return propertiesSet;
    }

    public List<ObjectProperty> getOntologyObjectProperties(OntModel model){
        List<ObjectProperty> propertiesSet = new ArrayList<ObjectProperty>();
        ExtendedIterator objectProperties = model.listObjectProperties();

        while (objectProperties.hasNext())
        {
            ObjectProperty thisProperty = (ObjectProperty) objectProperties.next();
            propertiesSet.add(thisProperty);
        }

        return propertiesSet;
    }

    public List<Individual> getOntologyIndividuals(OntModel model){
        List<Individual> individualsSet = new ArrayList<Individual>();
        ExtendedIterator individuals = model.listIndividuals();

        while (individuals.hasNext())
        {
            Individual thisIndividual = (Individual) individuals.next();
            individualsSet.add(thisIndividual);
        }

        return individualsSet;
    }

    public void showOntologyClassesWithInstances(OntModel model){
        ExtendedIterator<OntClass> classes = model.listClasses();

        for (OntClass thisClass : classes.toList())
        {
            ExtendedIterator individuals = model.listIndividuals(thisClass);
            List<Individual> listIndividuals = individuals.toList();
            if (!listIndividuals.isEmpty()) {
                if (thisClass.getLocalName() == null){
                    continue;
                }
                System.out.println(thisClass.getLocalName() + "\t");
                for (Individual thisIndividual : listIndividuals) {
                    if (thisIndividual.getLocalName() != null) {
                        System.out.println("   " + thisIndividual.getLocalName() + "\t");
                    }
                }
            }

        }
    }

    public void showOntologyClassesWithSubclasses(OntModel model){
        ExtendedIterator<OntClass> classes = model.listClasses();

        for (OntClass thisClass : classes.toList())
        {
            if (thisClass.getLocalName() != null) {
                ExtendedIterator<OntClass> subClasses = thisClass.listSubClasses();
                List<OntClass> listSubclasses = subClasses.toList();
                if (!listSubclasses.isEmpty()){
                    System.out.println(thisClass.getLocalName() + "\t");
                    for (OntClass thisSubClass : listSubclasses) {
                        if (thisSubClass.getLocalName() != null) {
                            System.out.println("   " + thisSubClass.getLocalName() + "\t");
                        }
                    }
                }
            }

        }
    }

    public void showOntologyDomainsAndRangesOfDatatypeProperties(OntModel model){
        List<DatatypeProperty> datatypeProperties = getOntologyDatatypeProperties(model);

        for (DatatypeProperty thisProperty : datatypeProperties) {
            System.out.println(thisProperty.getLocalName() + "\t");
            OntResource domain = thisProperty.getDomain();
            OntResource range = thisProperty.getRange();
            if (domain != null) System.out.println("   Dziedzina: " + domain + "\t");
            if (range != null) System.out.println("   Zakres: " + range + "\t");
        }
    }

    public void showOntologyDomainsAndRangesOfObjectProperties(OntModel model){
        List<ObjectProperty> objectProperties = getOntologyObjectProperties(model);

        for (ObjectProperty thisProperty : objectProperties) {
            System.out.println(thisProperty.getLocalName() + "\t");
            OntResource domain = thisProperty.getDomain();
            OntResource range = thisProperty.getRange();
            if (domain != null) System.out.println("   Dziedzina: " + domain + "\t");
            if (range != null) System.out.println("   Zakres: " + range + "\t");
        }
    }

    public void sparqlQuery(String sparql, OntModel model) {
        Query query = QueryFactory.create(sparql);
        QueryExecution execution = QueryExecutionFactory.create(query, model);
        ResultSet results = execution.execSelect();

        ResultSetFormatter.out(System.out, results, query);
        execution.close();
    }

}
