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
 * This class demonstrates how you can write junit tests for JavaFX GUIs.  It
 * uses the TestFX library's FxRobot class to simluate inputs from a user while
 * your test runs.  Documentation for the TestFX library can be found here:
 * https://testfx.github.io/TestFX/docs/javadoc/testfx-core/javadoc/org.testfx/module-summary.html
 * This jar files for this library have been packaged into a junit5fx.jar file
 * that can be downloaded using the instructions below, and can be used in place
 * of our old junit5.jar file going forward.
 *
 * To create your own tests like this:
 * 1) define your test class to extend ApplicationTest.
 * 2) copy-paste the @BeforeEach public void setup... method defined below into
 *    your own test class, and change the SampleApp reference inside to the be
 *    the name of your own class that extends javafx.application.Application.
 * 3) set the ids for any controls that you'd like your tests to be able to
 *    access: either to interact with or to check the updated state within.
 * 4) use the FxRobot API to simulate interaction with your GUI in your tests:
 *    https://testfx.github.io/TestFX/docs/javadoc/testfx-core/javadoc/org.testfx/org/testfx/api/FxRobot.html
 *
 * You can find examples of #3 in the SampleApp class below, and examples of #4
 * in the sample test methods that follow below.
 *
 * To compile and run the sample tests below:
 * 0) If you don't already have a javafx folder with the appropriate version
 *    of javafx right outside of your assignment folder, follow the instructions
 *    in A07 to download/create that folder.
 * 1) Download and use junit5fx.jar instead of our old junit5.jar file by
 *    running the following command inside your assignment folder:
 *    wget -P .. https://pages.cs.wisc.edu/~cs400/junit5fx.jar
 * 2) To compile this file, you'll need both include the javafx modules, and
 *    include this new junit5fx.jar file in your classpath, as follows:
 *    javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar SampleFXTests.java
 * 3) Similar arguments are sent to java to run this sample application:
 *    java --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar SampleFXTests
 * 4) You'll also need these arguments to run the test runner, along with one
 *    additional argument (add-opens) to help our tests access javafx details:
 *    java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c SampleFXTests
 * 5) Sit back and watch the robot complete each of your tests.
 * You can update the above commands to work with your own tests by replacing
 * references to SampleFXTests with the name of your own test class.
 */
public class FrontendDeveloperTests extends ApplicationTest {

    /**
     * This method launches the JavaFX application to be tested
     * BeforeEach of the @Test methods are run.
     */
    @BeforeEach
    public void setup() throws Exception {
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
    }

    /**
     * This test chooses a start location and an end location then
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathBasic() {
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
     * This test chooses a start/end locations, a via location, and
     * checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathIncludeVia() {
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

    /**
     * This test chooses a start/end locations, checks the show travel times
     * box, and checks to make sure the correct path is displayed.
     */
    @Test
    public void testShortestPathShowWalkingTimes() {
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

    /**
     * This test clicks the about button and checks to make sure the correct
     * text is displayed on a new window.
     */
    @Test
    public void testAboutButton() {
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
}
