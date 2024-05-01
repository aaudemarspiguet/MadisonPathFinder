import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.concurrent.TimeUnit;

/**
 * This class tests the Frontend and integration for P213
 */
public class FrontendDeveloperTests extends ApplicationTest {
    /**
     * This test chooses a start location and an end location then
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathBasic() throws Exception {
        // Set backend and launch application
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
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
                "\tAtmospheric, Oceanic and Space Sciences", path.getText());
    }

    /**
     * This test chooses a start/end locations, a via location, and
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathIncludeVia() throws Exception {
        // Set backend and launch application
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
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
                "\tRadio Hall", path.getText());
    }

    /**
     * This test chooses a start/end locations, checks the show travel times
     * box, and checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathShowWalkingTimes() throws Exception {
        // Set backend and launch application
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
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
                "\t-> Computer Sciences and Statistics (176.0 seconds)\n" +
                "\t-> Atmospheric, Oceanic and Space Sciences (80.0 seconds)\n" +
                "\tTotal Time: 4.27 minutes", path.getText());
    }

    /**
     * This test clicks the about button and checks to make sure the correct
     * text is displayed on a new window.
     */
    @Test
    public void testAboutButton() throws Exception {
        // Set backend and launch application
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
        // Lookup GUI element
        Button about = lookup("#aboutButton").query();

        // Click the about button
        clickOn("#aboutButton");

        // Wait for the new window to open
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);

        // Lookup the new GUI elements
        Label aboutInfo = lookup("#aboutInfo").query();
        Button aboutCloseButton = lookup("#aboutCloseButton").query();

        // Check to make sure the correct text is displayed
        assertEquals("\tThis application is part of the second Comp Sci 400 Project.\n" +
                "\n" +
                "\tTo find the shortest path between two locations,\n" +
                "\tselect start/end locations and press 'Submit/Find Path'\n" +
                "\n" +
                "\tTo make sure a location is included on the path,\n" +
                "\tclick 'Include Above Location in Path'\n" +
                "\n" +
                "\tTo show the travel time between each location,\n" +
                "\tclick 'Show Walking Times'", aboutInfo.getText());

        // Click on the close button to close the about window
        clickOn("#aboutCloseButton");
    }


    /**
     * This test initiates the frontend with the backend and tests the
     * findShortestPath() method from the backend.
     */
        @Test
        public void testIntegrationShortestPath() throws Exception {
            // Set backend and launch application
            Backend backend = new Backend(new DijkstraGraph<String, Double>());
            Frontend.setBackend(backend);
            backend.loadGraphData("src/main/java/campus.dot");
            ApplicationTest.launch(Frontend.class);
            // Lookup GUI elements
            ComboBox startSelector = lookup("#startSelector").query();
            Button find = lookup("#submitButton").query();
            ComboBox endSelector = lookup("#endSelector").query();
            Label path = lookup("#display").query();

            // Select a start location
            clickOn("#startSelector");
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.ENTER).release(KeyCode.ENTER);

            // Select an end location
            clickOn("#endSelector");
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.DOWN).release(KeyCode.DOWN);
            press(KeyCode.ENTER).release(KeyCode.ENTER);

            // Press the submit button
            clickOn("#submitButton");

            // Check to make sure the correct path is displayed
            assertEquals("Results List:\n" +
                    "\tBrat Stand\n" +
                    "\tScience Hall\n" +
                    "\tWisconsin State Historical Society",path.getText());
        }

    /**
     * This test initiates the frontend with the backend and tests the
     * getTravelTimesOnPath() method from the backend.
     */
    @Test
    public void testIntegrationShowTimes() throws Exception {
        // Set backend and launch application
        Backend backend = new Backend(new DijkstraGraph<String, Double>());
        Frontend.setBackend(backend);
        backend.loadGraphData("src/main/java/campus.dot");
        ApplicationTest.launch(Frontend.class);
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
        for(int i = 0; i < 12; i++) {
            press(KeyCode.DOWN).release(KeyCode.DOWN);
        }
        press(KeyCode.ENTER).release(KeyCode.ENTER);


        // Select the "show travel times" box
        clickOn("#travelTimesBox");

        // Click the submit button
        clickOn("#submitButton");

        // Check to makes sure the correct path is displayed
        assertEquals("Results List:\n" +
                "\tMemorial Union\n" +
                "\t-> Radio Hall (176.7 seconds)\n" +
                "\t-> Education Building (113.0 seconds)\n" +
                "\t-> South Hall (187.6 seconds)\n" +
                "\t-> Law Building (112.8 seconds)\n" +
                "\t-> X01 (174.7 seconds)\n" +
                "\t-> Luther Memorial Church (65.5 seconds)\n" +
                "\t-> Noland Hall (183.5 seconds)\n" +
                "\t-> Meiklejohn House (124.2 seconds)\n" +
                "\t-> Computer Sciences and Statistics (164.2 seconds)\n" +
                "\t-> Atmospheric, Oceanic and Space Sciences (127.2 seconds)\n" +
                "\tTotal Time: 23.82 minutes", path.getText());
    }


}


