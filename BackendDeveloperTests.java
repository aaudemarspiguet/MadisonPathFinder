import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.layout.Pane;

import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.application.Application;
import javafx.scene.control.CheckBox;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BackendDeveloperTests extends ApplicationTest {

    /**
     * This method launches the JavaFX application to be tested
     * BeforeEach of the @Test methods are run.
     */
    @BeforeEach
    public void setup() throws Exception {
        Backend backend = new Backend(new DijkstraGraph<String, Double>());
        backend.loadGraphData("campus.dot");
        Frontend.setBackend(backend);
        ApplicationTest.launch(Frontend.class);
    }

    /**
     * This test chooses a start location and an end location then
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testIntegrationShortestPathBasic() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        CheckBox travelTimesBox = lookup("#travelTimesBox").query();
        Label path = lookup("#display").query();

        // start location as Science Hall
        clickOn("#startSelector");
        for (int i = 0; i < 2; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // end location as Robert M. Lafollete School of Public Affairs
        clickOn("#endSelector");
        for (int i = 0; i < 26; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        // Click the submit button
        clickOn("#submitButton");

        // Check to makes sure the correct path is displayed
        assertEquals(Arrays.toString(new String[]{
		"Results List:",
        "\tScience Hall",
		"\tRadio Hall",
		"\tEducation Building",
		"\tNorth Hall",
		"\tCarillon Tower",
        "\tVan Hise Hall",
        "\tRobert M. Lafollette School of Public Affairs"
	    }), Arrays.toString(path.getText().split("\n")));
    }

    /**
     * This test chooses a start/end locations, checks the show travel times
     * box, and checks to make sure the correct path is displayed.
     */
    @Test
    public void testIntegrationShowWalkingTimes() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        CheckBox travelTimesBox = lookup("#travelTimesBox").query();
        Label path = lookup("#display").query();

        // start location as Science Hall
        clickOn("#startSelector");
        for (int i = 0; i < 2; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // end location as Robert M. Lafollete School of Public Affairs
        clickOn("#endSelector");
        for (int i = 0; i < 26; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Click on Show Walking Times button
        clickOn("#travelTimesBox");
        
        // Click the submit button
        clickOn("#submitButton");
        
        // Check to makes sure the correct path is displayed
        assertEquals(Arrays.toString(new String[]{
            "Results List:",
            "\tScience Hall",
            "\t-> Radio Hall (108.8 seconds)",
            "\t-> Education Building (113.0 seconds)",
            "\t-> North Hall (99.2 seconds)",
            "\t-> Carillon Tower (243.5 seconds)",
            "\t-> Van Hise Hall (171.9 seconds)",
            "\t-> Robert M. Lafollette School of Public Affairs (92.6 seconds)",
            "\tTotal Time: 13.82 minutes"
            }), Arrays.toString(path.getText().split("\n")));
    }

    /**
     * This test chooses a start/end locations, a via location, and
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testIntegrationShortestPathIncludeVia() {
        // Lookup GUI elements
        ComboBox startSelector = lookup("#startSelector").query();
        Button find = lookup("#submitButton").query();
        ComboBox endSelector = lookup("#endSelector").query();
        CheckBox travelTimesBox = lookup("#travelTimesBox").query();
        Label path = lookup("#display").query();

        // start location as Wendt Hall
        clickOn("#startSelector");
        for (int i = 0; i < 7; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // end location as Wendt Hall
        clickOn("#endSelector");
        for (int i = 0; i < 7; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Select an include Atmospheric, Oceanic and Space Sciences
        clickOn("#includeSelector");
        for (int i = 0; i < 12; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);

        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Click on the via button
        clickOn("#viaBox");
        
        // Click the submit button
        clickOn("#submitButton");

        // Check to makes sure the correct path is displayed
        assertEquals(Arrays.toString(new String[]{
		"Results List:",
        "\tWendt Commons",
		"\tAtmospheric, Oceanic and Space Sciences",
		"\tUnion South",
		"\tWendt Commons",
	    }), Arrays.toString(path.getText().split("\n")));

    }

    @Test
    public void testLoadGraphData() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        try {
            backend.loadGraphData("campus.dot");
            System.out.println(backend.getListOfAllLocations().size());
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
            backend.loadGraphData("campus.dot");
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

    // Test case if createAllControls method creates all necessary GUI controls
    @Test
    public void testPartnerCreateAllControls() {
        Pane parent = new Pane();
        Frontend frontend = new Frontend();
        assertTrue(parent.getChildren().size() == 0, "Returned non-zero # of controls!");
        frontend.createAllControls(parent);
        assertTrue(parent.getChildren().size() > 0, "Controls are not created!");
    }

    // Test case to simulate the scenario where one start location selected but no end locations are selected
    public void testPartnerNoLocationsSelected() {
        //  select start
        clickOn("#startSelector");
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        Label path = lookup("#display").query();
        // Simulate clicking the submit button
        clickOn("#submitButton");
        // Assert that the correct message is displayed
        assertEquals("Results List:", path.getText());
    }
}
