import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class BackendDeveloperTests {
    @Test
    public void testLoadGraphData() {
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
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
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
        List<String> locations = backend.getListOfAllLocations();
        assertNotNull(locations);
        // assuming there are three locations in the placeholder
        assertEquals(3, locations.size());
        // assuming the first location is "Union South"
        assertEquals("Union South", locations.get(0));
    }

    @Test
    public void testFindShortestPath() {
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
        List<String> path = backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(path);
        // assuming the shortest path between two locations is direct
        assertEquals(3, path.size());
        // assuming the first location is "Union South"
        assertEquals("Union South", path.get(0));
    }

    @Test
    public void testGetTravelTimesOnPath() {
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
        List<Double> travelTimes = backend.getTravelTimesOnPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(travelTimes);
        // Assuming there are two nodes on the path
        assertEquals(2, travelTimes.size());
        // Assuming the travel time between the two nodes is 176.0 seconds
        assertEquals(Double.valueOf(176.0), travelTimes.get(0));
    }

    @Test
    public void testFindShortestPathVia() {
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
        List<String> path = backend.findShortestPathVia("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(path);
        // assuming the shortest path with via location is direct
        assertEquals(3, path.size());
        // assuming the first location is "Memorial Union" when via location is "Computer Sciences and Statistics"
        assertEquals("Memorial Union", path.get(0));
    }

    @Test
    public void testGetTravelTimesOnPathVia() {
        BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
        List<Double> travelTimes = backend.getTravelTimesOnPathVia("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
        assertNotNull(travelTimes);
        // Assuming there are two nodes on the path via the specified location
        assertEquals(2, travelTimes.size());
        // Assuming the travel time between the two nodes via the specified location is 146.0 seconds
        assertEquals(Double.valueOf(146.0), travelTimes.get(0));
    }
}
