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
                "\tAtmospheric, Oceanic and Space Sciences\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\t", path.getText());
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
                "\tRadio Hall\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\t", path.getText());
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
                "\tComputer Sciences and Statistics\n" +
                "\tAtmospheric, Oceanic and Space Sciences\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\tUnion South\n" +
                "\t-(176.0 seconds)->Computer Sciences and Statistics\n" +
                "\t-(80.0 seconds)->Atmospheric, Oceanic and Space Sciences\n" +
                "\tTotal Time: 4.266666666666667 minutes", path.getText());
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
        public void testBackendShortestPath() throws Exception {
            // Set backend and launch application
            Frontend.setBackend(new Backend(new DijkstraGraph<String, Double>()));
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
                    "\tAtmospheric, Oceanic and Space Sciences\n" +
                    "\n" +
                    "Results List (With Travel Times): \n" +
                    "\t",path.getText());
        }

    /**
     * This test initiates the frontend with the backend and tests the 
     * getTravelTimesOnPath() method from the backend.
     */
    @Test
    public void testBackendShowTimes() throws Exception {
        // Set backend and launch application
        Frontend.setBackend(new Backend(new DijkstraGraph<String, Double>()));
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
                "\tComputer Sciences and Statistics\n" +
                "\tAtmospheric, Oceanic and Space Sciences\n" +
                "\n" +
                "Results List (With Travel Times): \n" +
                "\tUnion South\n" +
                "\t-(176.0 seconds)->Computer Sciences and Statistics\n" +
                "\t-(80.0 seconds)->Atmospheric, Oceanic and Space Sciences\n" +
                "\tTotal Time: 4.266666666666667 minutes", path.getText());
    }
        
        
}


