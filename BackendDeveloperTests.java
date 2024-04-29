import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BackendDeveloperTests {
    @Test
    public void testLoadGraphData() {
        BackendInterface backend = new Backend(new GraphPlaceholder());
        try {
            backend.loadGraphData("campus.dot");
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
}
