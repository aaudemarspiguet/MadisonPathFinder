// === CS400 File Header Information ===
// Name: Amogh Paruvelli
// Email: paruvelli@wisc.edu
// Lecturer: Dahl
// Notes to Grader: None

import java.util.PriorityQueue;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementExeption when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3
        MapADT<NodeType, Node> visited = new HashtableMap<NodeType, Node>();
        PriorityQueue<SearchNode> pQueue = new PriorityQueue<SearchNode>();
        pQueue.add(new SearchNode(nodes.get(start), 0.0, null));

        // throw exception when nodes don't exist in the graph
        if (!nodes.containsKey(start)) {
            throw new NoSuchElementException("Start data doesn't correspond to a graph node");
        }
            
        if (!nodes.containsKey(end)) {
            throw new NoSuchElementException("End data doesn't correspond to a graph node");
        }

        // looping branch
        while (!pQueue.isEmpty()) {
            SearchNode x = pQueue.poll();   

            if (x.node.equals(nodes.get(end))) { // if end node is reached, terminate
                return x;
            } else if (!visited.containsKey(x.node.data)) { // if unvisited mark destination node as visited,
                visited.put(x.node.data, x.node);
                for (Edge e : x.node.edgesLeaving) {
                    pQueue.add(new SearchNode(e.successor, x.cost + e.data.doubleValue(), x)); // store predecessor and cost
                }
            }            
        }

        // throw exception if it reaches this point
        throw new NoSuchElementException();
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // implement in step 5.4

        SearchNode endNode = computeShortestPath(start, end);
        LinkedList<NodeType> pathData = new LinkedList<>(); // store the intermediary values along the shortest path
        
        // traverse from the end node to the start node
        while (endNode != null) {
            pathData.addFirst(endNode.node.data);
            endNode = endNode.predecessor;
        }        
        return pathData;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return computeShortestPath(start, end).cost;
    }

    // MIDWEEK SUBMISSION

    /** 
     * Test case for verifying the shortest path and cost between two nodes based
     * on a manually traced example.
     */
    @Test
    public void testShortestPath() {
        // Construct the graph
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        graph.insertEdge("A", "D", 4.0);

        graph.insertEdge("B", "A", 4.0);
        graph.insertEdge("B", "C", 3.0);
        graph.insertEdge("B", "E", 4.0);

        graph.insertEdge("D", "B", 5.0);

        graph.insertEdge("C", "A", 1.0);

        graph.insertEdge("E", "D", 2.0);
        graph.insertEdge("E", "C", 3.0);
  
    
        // Expected result for the shortest path: A -> D
        List<String> expectedPath = List.of("A", "D");
        double expectedCost = 4.0;

        // Test the shortest path and cost
        assertEquals(expectedPath, graph.shortestPathData("A", "D"));
        assertEquals(expectedCost, graph.shortestPathCost("A", "D"));
    }

    /**
     * Test case for checking the shortest path and cost between different start
     * and end nodes in the graph.
     */
    @Test
    public void testDifferentStartEndNodes() {
        
        // Construct the graph
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        graph.insertEdge("A", "D", 4.0);

        graph.insertEdge("B", "A", 4.0);
        graph.insertEdge("B", "C", 3.0);
        graph.insertEdge("B", "E", 4.0);

        graph.insertEdge("D", "B", 5.0);

        graph.insertEdge("C", "A", 1.0);

        graph.insertEdge("E", "D", 2.0);
        graph.insertEdge("E", "C", 3.0);

        // Expected result for the shortest path: B -> E -> D
        List<String> expectedPath = List.of("B", "E", "D");
        double expectedCost = 6.0;

        // Test the shortest path and cost
        assertEquals(expectedPath, graph.shortestPathData("B", "D"));
        assertEquals(expectedCost, graph.shortestPathCost("B", "D"));
    }

    /**
     * Test case for verifying the behavior when no path exists between start and
     * end nodes.
     */
    @Test
    public void testNoPathExists() {
        // Construct the graph
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        // Test the behavior when no path exists between nodes
        assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("A", "C"));
        assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("A", "C"));
    }
}
