import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BackendDeveloperTests extends ApplicationTest {

    /**
     * This method launches the JavaFX application to be tested
     * BeforeEach of the Integration Tests are run.
     */
    @BeforeEach
    public void setup() throws Exception {
        Frontend.setBackend(new Backend(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
    }


    @Test
    public void IntegrationShortestPathBasic() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        Label path = lookup("#display").query();

        // Select a start location
        clickOn("#startSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select an end location
        clickOn("#endSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Press the submit button
        clickOn("#submitButton");

        // Check to make sure the correct path is displayed
        assertEquals("Results List:\n" +
                "\tUnion South\n" +
                "\tComputer Sciences and Statistics\n" +
                "\tAtmospheric, Oceanic and Space Sciences\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\t",path.getText());
        
    }

    @Test
    public void IntegrationShortestPathVia() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        ComboBox includeSelector = lookup("#includeSelector").query();
        CheckBox useViaBox = lookup("#viaBox").query();
        Label path = lookup("#display").query();

        // Select a start location
        clickOn("#startSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select an end location
        clickOn("#endSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select an include via location
        clickOn("#includeSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Enable the via checkbox
        clickOn("#viaBox");

        // Click the submit button
        clickOn("#submitButton");

        // Check to make sure the correct results are displayed
        assertEquals("Results List:\n" +
                "\tMemorial Union\n" +
                "\tScience Hall\n" +
                "\tRadio Hall\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\t",path.getText());
    }

    @Test
    public void IntegrationShortestPathShowWalkingTimes() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        CheckBox travelTimesBox = lookup("#travelTimesBox").query();
        Label path = lookup("#display").query();

        // Select a start location
        clickOn("#startSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select an end location
        clickOn("#endSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select the "show travel times" box
        clickOn("#travelTimesBox");

        // Click the submit button
        clickOn("#submitButton");

        // Check to makes sure the correct path is displayed
        assertEquals("Results List:\n" +
                "\tUnion South\n" +
                "\tComputer Sciences and Statistics\n" +
                "\tAtmospheric, Oceanic and Space Sciences\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\tUnion South\n" +
                "\t-(176.0 seconds)->Computer Sciences and Statistics\n" +
                "\t-(80.0 seconds)->Atmospheric, Oceanic and Space Sciences\n" +
                "\tTotal Time: 4.266666666666667 minutes",path.getText());
    }

    @Test
    public void testLoadGraphData() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        try {
            backend.loadGraphData("src/campus.dot");
            // assuming no exceptions are thrown, the test passes
            assertTrue(true);
        } catch (IOException e) {
           fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testGetListOfAllLocations() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        try {
            backend.loadGraphData("src/campus.dot");
            List<String> locations = backend.getListOfAllLocations();
            assertNotNull(locations);
            // assuming there are three locations in the placeholder
            assertTrue(locations.containsAll(Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences")));
 
        } catch (IOException e) {
           fail("Exception not expected: " + e.getMessage()); // test fails if exception thrown when unexpected
        }
        
    }

    @Test
    public void testFindShortestPath() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        List<String> shortestPath = backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(shortestPath);
        // assuming the shortest shortestPath between two locations is as specified in GraphPlaceholder
        assertEquals(3, shortestPath.size());
        // assuming the returned list is {"Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"}
        assertEquals("Union South", shortestPath.get(0));
        assertEquals("Computer Sciences and Statistics", shortestPath.get(1));
        assertEquals("Atmospheric, Oceanic and Space Sciences", shortestPath.get(2));
    }

    @Test
    public void testGetTravelTimesOnPath() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        List<Double> travelTimes = backend.getTravelTimesOnPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(travelTimes);
        // Assuming there are 2 edges on the shortestPath
        assertEquals(2, travelTimes.size());
        // Assuming the travel time between the two edges is 176.0 and 127.2 seconds
        assertEquals(Double.valueOf(176.0), travelTimes.get(0));
        assertEquals(Double.valueOf(127.2), travelTimes.get(1));
    }

    @Test
    public void testFindShortestPathVia() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        List<String> shortestPath = backend.findShortestPathVia("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(shortestPath);
        // assuming the shortest shortestPath with via location is direct
        assertEquals(5, shortestPath.size());
        // assuming the first location is "Memorial Union" when via location is "Computer Sciences and Statistics"
        assertEquals("Union South", shortestPath.get(0));
    }

    @Test
    public void testGetTravelTimesOnPathVia() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        List<Double> travelTimes = backend.getTravelTimesOnPathVia("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(travelTimes);
        // Assuming there are four edges on the shortestPath via the specified location
        assertEquals(4, travelTimes.size());
        // Assuming the travel time between the four edges via the specified location is 176.0, 146.0, 176.0, 127.2 seconds
        assertEquals(Double.valueOf(176.0), travelTimes.get(0));
        assertEquals(Double.valueOf(146.0), travelTimes.get(1));
        assertEquals(Double.valueOf(176.0), travelTimes.get(2));
        assertEquals(Double.valueOf(127.2), travelTimes.get(3));
    }
}
