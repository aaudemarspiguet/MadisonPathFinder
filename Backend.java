import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Backend implements BackendInterface {
    GraphADT<String, Double> graph;
    List<String> locations = new ArrayList<String>();

    /**
     * Constructor
     * @param graph object to sture the backend's graph data
     */
    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
     }

    @Override
    /**
     * Loads graph data from a dot file.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was a problem reading in the specified file
     */
    public void loadGraphData(String filename) throws IOException {
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                String newLine = fileScanner.nextLine().trim();
                if (newLine.startsWith("\"") && newLine.contains("->")) {

                    // retrieve weight by:
                        // 1) split by "["
                        // 2) split by "="
                        // 3) split by "]"
                    double weight = Double.parseDouble(newLine.split("\\[")[1].split("=")[1].split("\\]")[0].trim());
                    
                    // retrieve nodes by:
                        // 1) split by "["
                        // 2) split by "->"
                    String[] nodes = newLine.split("\\[")[0].split("->");

                    // trim empty characters and clean up quotation marks
                    String source = nodes[0].trim().replaceAll("\"", "");
                    String destination = nodes[1].trim().replaceAll("\"", ""); 

                    // updating list of locations and adding node and edge to the graph
                  
                    if (!locations.contains(source)) {
                        locations.add(source);
                        graph.insertNode(source);
                    }
                    if (!locations.contains(destination)) {
                        locations.add(destination);
                        graph.insertNode(destination);
                    }

                    /*
                    if (!graph.containsNode(source)) {
                        graph.insertNode(source);
                    }

                    if (!graph.containsNode(destination)) {
                        graph.insertNode(destination);
                    }
                    */
                    graph.insertEdge(source, destination, weight);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Trouble reading in specified file: " + e.getMessage());
        }
    }

    @Override
    /**
     * Returns a list of all locations (nodes) available on the backend's graph.
     * @return list of all location names
     */
    public List<String> getListOfAllLocations() {
	    return locations;	
    }

    @Override
    /**
     * Returns the sequence of locations along the shortest path from startLocation to endLocation, or
     * en empty list if no such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to endLocation, or
     *         an empty list if no such path exists
     */
    public List<String> findShortestPath(String startLocation, String endLocation) {
	    try {
		    return graph.shortestPathData(startLocation, endLocation);
	    } catch (NoSuchElementException e) {
		    return new ArrayList<String>();
	    }
    }

    @Override
    /**
     * Returns the walking times in seconds between each two nodes on the shortest path from startLocation
     * to endLocation, or an empty list of no such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along the shortest path from
     *         startLocation to endLocation, or an empty list if no such path exists
     */
    public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
        List<String> path = findShortestPath(startLocation, endLocation);
        List<Double> intermediateCost = new ArrayList<Double>();

        for (int i = 0; i < path.size() - 1; i++) {
            intermediateCost.add(graph.getEdge(path.get(i), path.get(i + 1)));
        }
        return intermediateCost;
    }

    @Override
    /**
     * Returns the sequence of locations along the shortest path from startLocation to endLocation including
     * the third location viaLocation, or an empty list if no such path exists.
     * en empty list if no such path exists.
     * @param startLocation the start location of the path
     * @param viaLocation a location that the path show lead through
     * @param endLocation the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to endLocation including
     *         viaLocation, or an empty list if no such path exists
     */
    public List<String> findShortestPathVia(String startLocation, String viaLocation, String endLocation) {
        
        List<String> tempList = graph.shortestPathData(startLocation, viaLocation); 
        // make a deep copy for path one
        List<String> pathOne = new ArrayList<String>();
        for (int i = 0; i < tempList.size(); i++) {
            pathOne.add(tempList.get(i));
        }
        pathOne.remove(pathOne.size() - 1); // remove for redundant viaLocation

        try {
            List<String> pathTwo = new ArrayList<String>();
            pathTwo = graph.shortestPathData(viaLocation, endLocation);

            pathOne.addAll(pathTwo);
            return pathOne; // return path via if successfully added
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<String>(); // return empty list if path via fails to add
        }
    }

    @Override
    /**
     * Returns the walking times in seconds between each two nodes on the shortest path from startLocation
     * to endLocation through viaLocation, or an empty list of no such path exists.
     * @param startLocation the start location of the path
     * @param viaLocation a location that the path show lead through
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along the shortest path from
     *         startLocation to endLocationthrough viaLocation, or an empty list if no such path exists
     */
    public List<Double> getTravelTimesOnPathVia(String startLocation, String viaLocation, String endLocation) {

        List<String> path = findShortestPathVia(startLocation, viaLocation, endLocation);
        List<Double> intermediateCost = new ArrayList<Double>();

        if (!path.isEmpty()) {
            for (int i = 0; i < path.size() - 1; i++) {
                intermediateCost.add(graph.getEdge(path.get(i), path.get(i + 1)));
            }
        }
        return intermediateCost;
    }
    
}
