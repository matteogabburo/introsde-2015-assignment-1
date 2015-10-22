package assignment.evaluation;

import assignment.evaluation.functions.Assignment1SDE_functions;
import generated.Healthprofile;
import generated.Person;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo on 21/10/15.
 */
public class Evaluation4_Assignment1SDE
{
    public final static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String fileXML = "res/HealthProfile_ev4.xml";
        String fileXSD = "res/HealthProfile.xsd";

        Assignment1SDE_functions test = new Assignment1SDE_functions();
        test.loadXML(fileXML);
        test.loadXSD(fileXSD);

        //Create the 3 people, and put them in a List<Person> called personList
        List<Person> personList;

        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();

        Healthprofile healthprofile;

        p1.setFirstname("Giacomo");
        p1.setLastname("Leopardi");
        p1.setBirthdate("1968-02-14T18:00:00.000+02:00");
        healthprofile = new Healthprofile();
        healthprofile.setLastupdate("2005-04-07T18:00:00.000+02:00");
        healthprofile.setWeight(new BigInteger("70"));
        healthprofile.setHeight((float) 1.75);
        healthprofile.setBmi((float)(70 / (1.75 * 1.75)));
        p1.setHealthprofile(healthprofile);

        p2.setFirstname("Maurizio");
        p2.setLastname("Castagna");
        p2.setBirthdate("1998-04-10T18:00:00.000+02:00");
        healthprofile = new Healthprofile();
        healthprofile.setLastupdate("2007-14-07T18:00:00.000+02:00");
        healthprofile.setWeight(new BigInteger("80"));
        healthprofile.setHeight((float)1.83);
        healthprofile.setBmi((float)(80 / (1.83 * 1.83)));
        p2.setHealthprofile(healthprofile);

        p3.setFirstname("Dante");
        p3.setLastname("Giacomelli");
        p3.setBirthdate("1960-12-09T18:00:00.000+02:00");
        healthprofile = new Healthprofile();
        healthprofile.setLastupdate("2014-11-11T18:00:00.000+02:00");
        healthprofile.setWeight(new BigInteger("90"));
        healthprofile.setHeight((float) 1.95);
        healthprofile.setBmi((float)(90 / (1.95 * 1.95)));
        p3.setHealthprofile(healthprofile);

        personList = new ArrayList();
        personList.add(p1);
        personList.add(p2);
        personList.add(p3);

        //Marshall
        //This method take a list of all the people that I want to marshall into an .xml file specified before
        test.marshall(personList);

        //update the function class with modified doc and print all
        test.loadXML(fileXML);
        test.printPeopleWithDetails();
    }

}
