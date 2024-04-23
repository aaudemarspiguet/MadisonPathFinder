import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import java.util.List;

/**
 * This class functions as the Frontend for an application that computes
 * the shortest distance between two point ins Madison
 */
public class Frontend extends Application implements FrontendInterface {
    private static BackendPlaceholder back; // Reference to backend
    private ComboBox<String> startSelector; // Dropdown for start location
    private ComboBox<String> endSelector; // Dropdown for end location
    private ComboBox<String> includeSelector; // Dropdown for via location
    private Label path; // Displays the path information
    private boolean useVia = false; // Indicates if the via location will be used
    private boolean includeTravelTimes = false; // Indicates if travel times will be shown

    /**
     * This method sets the backend for the frontend.
     * @param back the backend to be set
     */
    public static void setBackend(BackendPlaceholder back) {
        Frontend.back = back;
    }

    /**
     * This method sets up the JavaFx application and calls
     * another method to create all the controls
     * @param stage the stage for the application
     */
    public void start(Stage stage) {
        Pane root = new Pane();
        // Sets the background to light blue
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTYELLOW,
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        root.setBackground(background);
        // Calls method to create controls
        createAllControls(root);
        // Configures the stage and sets the scene
        Scene scene = new Scene(root, 800, 600);;
        stage.setScene(scene);
        stage.setTitle("P2: Prototype");
        stage.show();
    }

    /**
     * Creates all controls in the GUI.
     * @param parent the parent pane that contains all controls
     */
    public void createAllControls(Pane parent) {
        createShortestPathControls(parent);
        createPathListDisplay(parent);
        createAdditionalFeatureControls(parent);
        createAboutAndQuitControls(parent);
    }

    /**
     * Creates the controls for the shortest path search.
     * @param parent the parent pane that contains all controls
     */
    public void createShortestPathControls(Pane parent) {
        // Create start selector and start label
        Label startText = new Label("Path Start:");
        startSelector = new ComboBox<>();
        List<String> startLocations = back.getListOfAllLocations();
        startSelector.getItems().addAll(startLocations);
        startSelector.setId("startSelector");
        // Set layout for start selector and start label
        startText.setLayoutX(16);
        startText.setLayoutY(18);
        startSelector.setLayoutX(79);
        startSelector.setLayoutY(16);
        // Add start selector and start label to parent pane
        parent.getChildren().add(startText);
        parent.getChildren().add(startSelector);
        // Create end selector and end label
        Label endText = new Label("Path End:");
        endSelector = new ComboBox<>();
        List<String> endLocations = Frontend.back.getListOfAllLocations();
        endSelector.getItems().addAll(endLocations);
        endSelector.setId("endSelector");
        // Set layout for end selector and end label
        endText.setLayoutX(16);
        endText.setLayoutY(50);
        endSelector.setLayoutX(79);
        endSelector.setLayoutY(48);
        // Add end selector and end label to parent pane
        parent.getChildren().add(endText);
        parent.getChildren().add(endSelector);
        // Create find button
        Button find = new Button("Submit/Find Button");
        find.setId("submitButton");
        find.setOnAction(e -> {
            // Create string builders for the path display
            StringBuilder shortestPathResults = new StringBuilder("Results List:");
            StringBuilder shortestPathSeconds = new StringBuilder("\n\nResults List (With Travel Times): \n\t");
            // Create list to keep track of travel times
            List<Double> travelTimes = null;
            Double totalTime = 0.0;
            //Checks if a start and end location have been selected
            if(startSelector.getValue() != null && endSelector.getValue() != null) {
                // Checks to see if an include via location has been selected
                if (includeSelector.getValue() != null && useVia) {
                    List<String> viaShortestPath = back.findShortestPathVia(startSelector.getValue(),
                            includeSelector.getValue(), endSelector.getValue());
                    // Checks to see if include travel times has been selected
                    if(includeTravelTimes) {
                        travelTimes = back.getTravelTimesOnPathVia(startSelector.getValue(),
                                includeSelector.getValue(), endSelector.getValue());
                    }
                    // Builds the display for the shortest path with travel times
                    for (int i = 0; i < viaShortestPath.size(); i++)  {
                        shortestPathResults.append("\n\t");
                        shortestPathResults.append(viaShortestPath.get(i));
                        if(includeTravelTimes) {
                            shortestPathSeconds.append(viaShortestPath.get(i));
                            if(i < viaShortestPath.size() - 1 && includeTravelTimes) {
                                shortestPathSeconds.append("\n\t-(");
                                shortestPathSeconds.append(travelTimes.get(i).toString());
                                shortestPathSeconds.append(" seconds)->");
                            }
                        }
                    }
                    // Calculates the total travel time
                    if(includeTravelTimes) {
                        for (Double times : travelTimes) {
                            totalTime += times;
                        }
                        shortestPathSeconds.append("\n\tTotal Time: ");
                        shortestPathSeconds.append(totalTime/60);
                        shortestPathSeconds.append(" minutes");
                    }
                    // If no locations are returned, display that no paths were found
                    if(viaShortestPath.isEmpty()) {
                        shortestPathResults.append("\n\nNo Paths Found");
                    }
                    // If no via location is selected, compute the shortest path using start and end locations
                } else {
                    List<String> shortestPath = back.findShortestPath(startSelector.getValue(), endSelector.getValue());
                    if(includeTravelTimes) {
                        travelTimes = back.getTravelTimesOnPath(startSelector.getValue(), endSelector.getValue());
                    }
                    // Builds the display for the shortest path (without travel times)
                    for (int i = 0; i < shortestPath.size(); i++)  {
                        shortestPathResults.append("\n\t");
                        shortestPathResults.append(shortestPath.get(i));
                        if(includeTravelTimes) {
                            shortestPathSeconds.append(shortestPath.get(i));
                            if(i < shortestPath.size() - 1 && includeTravelTimes) {
                                shortestPathSeconds.append("\n\t-(");
                                shortestPathSeconds.append(travelTimes.get(i).toString());
                                shortestPathSeconds.append(" seconds)->");
                            }
                        }
                    }
                    // Calculates the total travel times
                    if(includeTravelTimes) {
                        for (Double times : travelTimes) {
                            totalTime += times;
                        }
                        shortestPathSeconds.append("\n\tTotal Time: ");
                        shortestPathSeconds.append(totalTime/60);
                        shortestPathSeconds.append(" minutes");
                    }
                    // If no locations are returned, display that no paths were found
                    if(shortestPath.isEmpty()) {
                        shortestPathResults.append("\n\n No Paths Found");
                    }
                }
                shortestPathResults.append(shortestPathSeconds);
                path.setText(shortestPathResults.toString());
            }
        });
        // Set layout for the submit button
        find.setLayoutX(79);
        find.setLayoutY(80);
        // Add submit button to parent pane
        parent.getChildren().add(find);
    }

    /**
     * Creates the controls for displaying the shortest path returned by the search.
     * @param parent the parent pane that contains all controls
     */
    public void createPathListDisplay(Pane parent) {
        // Create a label to display the shortest path
        path = new Label("Results List: \n\n\n\n Results List(With Travel Times):");
        // Set a layout for the path label
        path.setLayoutX(16);
        path.setLayoutY(112);
        path.setId("display");
        // A the path label to the parent pane
        parent.getChildren().add(path);
    }


    /**
     * Creates controls for the two features in addition to the shortest path search.
     * @param parent parent pane that contains all controls
     */
    public void createAdditionalFeatureControls(Pane parent) {
        this.createTravelTimesBox(parent);
        this.createOptionalLocationControls(parent);
    }

    /**
     * Creates the check box to add travel times in the result display.
     * @param parent parent pane that contains all controls
     */
    public void createTravelTimesBox(Pane parent) {
        // Create check box to toggle travel times
        CheckBox travelTimesBox = new CheckBox("Show Walking Times");

        // Toggle global variable for showing travel times when clicked
        travelTimesBox.setOnAction(e -> {
            if(travelTimesBox.isSelected()) {
                includeTravelTimes = true;
            } else {
                includeTravelTimes = false;
            }
        });
        travelTimesBox.setId("travelTimesBox");
        // Sets layout for the checkbox
        travelTimesBox.setLayoutX(220);
        travelTimesBox.setLayoutY(80);
        // Adds the checkbox to the parent pane
        parent.getChildren().add(travelTimesBox);
    }

    /**
     * Creates controls to allow users to add a third location for the path to go through.
     * @param parent parent pane that contains all controls
     */
    public void createOptionalLocationControls(Pane parent) {
        // Creates the label and selector for including a via location
        Label includeText = new Label("Via Location (optional):");
        includeSelector = new ComboBox();
        List<String> includeLocations = Frontend.back.getListOfAllLocations();
        includeSelector.getItems().addAll(includeLocations);
        includeSelector.setId("includeSelector");
        // Sets the layout for the "include via location" label and selector
        includeSelector.setLayoutX(500);
        includeSelector.setLayoutY(16);
        includeText.setLayoutX(368);
        includeText.setLayoutY(18);
        // Adds the "include via location" label and selector to the parent pane
        parent.getChildren().add(includeText);
        parent.getChildren().add(includeSelector);
        // Creates a new checkbox to toggle using a via location in the path
        CheckBox useViaBox = new CheckBox("Use Above Location in Path");
        useViaBox.setId("viaBox");
        // Toggle global variable for including via location when clicked
        useViaBox.setOnAction(e -> {
            if(useViaBox.isSelected()) {
                useVia = true;
            } else {
                useVia = false;
            }
        });
        // Set layout of the via location checkbox
        useViaBox.setLayoutX(500);
        useViaBox.setLayoutY(48);

        // Add via location checkbox to parent pane
        parent.getChildren().add(useViaBox);
    }

    /**
     * Creates an about and quit button.
     * @param parent parent pane that contains all controls
     */
    public void createAboutAndQuitControls(Pane parent) {
        // Creates an about button
        Button about = new Button("About");
        about.setId("aboutButton");
        // Sets layout of the about button
        about.setLayoutX(32);
        about.setLayoutY(560);
        about.setOnAction(e -> {
            // Create a new stage for the about information
            Stage aboutStage = new Stage();
            aboutStage.setTitle("About This Application");
            Pane aboutRoot = new Pane();
            BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTYELLOW,
                    CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            aboutRoot.setBackground(background);
            // Creates a close button for the about stage
            Button aboutCloseButton = new Button("Close");
            aboutCloseButton.setId("aboutCloseButton");
            aboutCloseButton.setOnAction(event -> aboutStage.close());
            // Sets layout of the close button
            aboutCloseButton.setLayoutX(180);
            aboutCloseButton.setLayoutY(210);
            aboutRoot.getChildren().add(aboutCloseButton);
            // Sets the text to be displayed in the about stage
            Label aboutInfo = new Label("\tThis application is part of the second Comp Sci 400 Project." +
                    "\n\n\tTo find the shortest path between two locations," +
                    "\n\tselect start/end locations and press 'Submit/Find Path'" +
                    "\n\n\tTo make sure a location is included on the path," +
                    "\n\tclick 'Include Above Location in Path'" +
                    "\n\n\tTo show the travel time between each location," +
                    "\n\tclick 'Show Walking Times'");
            // Sets the layout for the about text
            aboutInfo.setLayoutX(18);
            aboutInfo.setLayoutY(22);
            aboutInfo.setId("aboutInfo");
            aboutRoot.getChildren().add(aboutInfo);
            // Configures the new scene for the about window
            Scene aboutScene = new Scene(aboutRoot,400 , 260);
            aboutStage.setScene(aboutScene);
            aboutStage.show();
        });
        parent.getChildren().add(about);
        // Creates quit button
        Button quit = new Button("Quit");
        quit.setOnAction(e ->System.exit(0));
        // Sets layout of the quit button
        quit.setLayoutX(96);
        quit.setLayoutY(560);
        // Add the quit button to the parent pane
        parent.getChildren().add(quit);

    }
}