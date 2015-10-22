package assignment.evaluation;

import assignment.evaluation.functions.Assignment1SDE_functions;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Created by matteo on 21/10/15.
 */
public class Evaluation1_Assignment1SDE {

    public final static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String fileXML = "res/HealthProfile_ev1235.xml";

        Assignment1SDE_functions test = new Assignment1SDE_functions();
        test.loadXML(fileXML);

        //Print all the people indented with details.
        test.printPeopleWithDetails();
    }

}
