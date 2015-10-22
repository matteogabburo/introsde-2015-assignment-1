package assignment.evaluation.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import generated.ObjectFactory;
import generated.People;
import generated.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matteo Gabburo
 *
 * This class implements all methods required to exec the assignments.
 * In particular there are 4 attributes and 10 methods.
 *
 * DISCLAIMER
 * Following the assignment instructions, I have understand that there are some methods that they have only to print the
 * informations without return anything, these methods starting by "print".
 */
public class Assignment1SDE_functions
{
    //Attributes
    Document doc;
    XPath xpath;
    String xml_name;
    String xsd_name;


    //This is an aux method, used by work with ID parameters of xtml file.
    //it take an int parameters and return the string formatted how .xsd want (es. 4 -> "0004").
    private String parseId(int id)
    {
        String tmp = String.valueOf(id);
        String strId = "";
        for (int j = 4; j > tmp.length(); j--)
            strId += '0';
        strId += tmp;

        return strId;
    }

    //Load xml file specified by param
    public void loadXML(String xml_name) throws IOException, SAXException, ParserConfigurationException {
        if(xml_name.matches("[a-zA-Z0-9/_]+[.]xml"))
        {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            this.doc = builder.parse(xml_name);
            this.xml_name = xml_name;

            //creating xpath object
            getXPathObj();
        }
    }

    //This method take the xsd_name and store it in the xsd_name class attribute
    public void loadXSD(String xsd_name)
    {
        this.xsd_name = xsd_name;
    }

    //Initialize class attribute xpath and return it
    public XPath getXPathObj()
    {
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
        return this.xpath;
    }

    //Based on Lab3 point (1) Use xpath to implement methods like getWeight and getHeight
    public Node getHeight(int id) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("/people/person[@id = '"+this.parseId(id)+"']/healthprofile/height");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }
    public Node getWeight(int id) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("/people/person[@id = '"+this.parseId(id)+"']/healthprofile/weight");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }

    //Based on Lab3 point (2) Make a function that prints all people in the list with detail
    public void printPeopleWithDetails() throws XPathExpressionException {
        XPathExpression expr = xpath.compile("/people/person");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        for(int i = 0; i < nodes.getLength(); i++)
        {
            System.out.println("id("+nodes.item(i).getAttributes().getNamedItem("id").getTextContent()+")");
            System.out.println(nodes.item(i).getTextContent());
        }
    }

    //Based on Lab3 point (3) A function that accepts id as parameter and prints the HealthProfile of the person with
    // that id
    public NodeList getHealthProfile(int id) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("/people/person[@id = '"+this.parseId(id)+"']");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;
    }

    //Based on Lab3 point (4) A function which accepts a weight and an operator (=, > , <) as parameters and prints
    // people that fulfill that condition (i.e., >80Kg, =75Kg, etc.).
    public void printIfWeight(String operator, int weight) throws XPathExpressionException{
        XPathExpression expr = xpath.compile("/people/person[healthprofile/weight"+ operator+" "+weight+"]");

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        System.out.println(nodes.getLength());
        for(int i = 0; i < nodes.getLength(); i++) {
            System.out.println("id("+nodes.item(i).getAttributes().getNamedItem("id").getTextContent()+")");
            System.out.println(nodes.item(i).getTextContent());
        }
    }

    //Based on Lab4 point (2) Write a java application that does the marshalling and un-marshalling using classes
    // generated with JAXB XJC.
    public List<Person> unMarshall() {
        List<Person> personList = null;
        try
        {
            this.loadXML(this.xml_name);

            JAXBContext jaxbContext = JAXBContext.newInstance("generated");
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = schemaFactory.newSchema(new File(this.xsd_name));
            unMarshaller.setSchema(schema);
            //CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
            //unMarshaller.setEventHandler(validationEventHandler);
            //@SuppressWarnings("unchecked")
            People peopleElement = (People) unMarshaller.unmarshal(this.doc);

            personList = peopleElement.getPerson();
        }
        catch (SAXException e1) {
            e1.printStackTrace();
        }
        catch (JAXBException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personList;
    }
    //if .xml file already exist and have people, marshall append newPerson into .xml
    public void marshall(List<Person> newPerson) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("generated");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", new Boolean(true));

            ObjectFactory factory = new ObjectFactory();
            People people = factory.createPeople();

            List<Person> personList = people.getPerson();
            List<Person> personList_old = unMarshall();

            //Insert the new people into old file
            if(personList_old != null)
            {
                Iterator it = newPerson.iterator();
                Person tmpp;
                int size = personList_old.size();
                while (it.hasNext()) {
                    tmpp = (Person) it.next();
                    //Insert the missing id
                    tmpp.setId(parseId(++size));
                    personList.add(tmpp);
                }

                it = personList_old.iterator();
                while(it.hasNext())
                    personList.add((Person)it.next());
            }
            else//if there aren't Person into .xml file
            {
                Iterator it = newPerson.iterator();
                Person tmpp;
                while (it.hasNext()) {
                    tmpp = (Person) it.next();
                    //Insert the missing id
                    tmpp.setId(parseId(personList.size()+1));
                    personList.add(tmpp);
                }
            }
            marshaller.marshal(people, new FileOutputStream(this.xml_name));
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (JAXBException e) {
            System.out.println(e.toString());
        }
    }

    //Based on Lab4 point (3) Make your java application to convert also JSON
    public String marshallJSon(List<Person> personList, String jSonFileName) {
        String result = "";
        try
        {
        // Jackson Object Mapper
        ObjectMapper mapper = new ObjectMapper();

        // Adding the Jackson Module to process JAXB annotations
        JaxbAnnotationModule module = new JaxbAnnotationModule();

        // configure as necessary
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        Iterator it = personList.iterator();
        while(it.hasNext())
            result += mapper.writeValueAsString((Person)it.next());

            mapper.writeValue(new File(jSonFileName), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}