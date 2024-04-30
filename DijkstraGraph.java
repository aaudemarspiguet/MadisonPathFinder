// === CS400 File Header Information ===
// Name: Wyatt Federman
// Email: wfederman@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.PriorityQueue;
import java.util.List;
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
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // Create a new priority queue to add nodes to
        PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<SearchNode>();
        // Create a map to store and retrieve the shortest paths
        MapADT<NodeType, Node> visited = new HashtableMap<NodeType, Node>();
        // Add the beginning node to the priority queue (with a cost of 0)
        priorityQueue.add(new SearchNode(nodes.get(start), 0.0, null));

        // Check to see if nodes exist in graph, if not throw exceptions
        if (!nodes.containsKey(start)) {
            throw new NoSuchElementException("Start data doesn't contain key");
        }
        if (!nodes.containsKey(end)) {
            throw new NoSuchElementException("End data doesn't contain key");
        }

        // looping branch
        while (!priorityQueue.isEmpty()) {
            SearchNode curr = priorityQueue.poll();
            // If the end is reached, return the search node
            if (curr.node.equals(nodes.get(end))) {
                return curr;
                // Mark node as visited
            } else if (!visited.containsKey(curr.node.data)) {
                visited.put(curr.node.data, curr.node);
                // Store successor and cost, then add to priority queue
                for (Edge edge : curr.node.edgesLeaving) {
                    priorityQueue.add(new SearchNode(edge.successor, curr.cost + edge.data.doubleValue(), curr));
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
        // Call computeShortestPath() and get the final node for the shortest path
        SearchNode curr  = computeShortestPath(start, end);
        // Create new linked list to store the path
        List<NodeType> pathList = new LinkedList<>();
        // Add each node's data to the front of the list
        while (curr != null) {
            pathList.add(0, curr.node.data);
            curr = curr.predecessor;
        }
        return pathList;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // Call computeShortestPath() and get the final node for the shortest path
        SearchNode shortestPath = computeShortestPath(start, end);
        // Return the cost of the final node
        return shortestPath.cost;
    }

    /**
     * This test creates an undirected graph with five vertices and
     * seven edges. It checks to make sure shortestPathCost() outputs
     * 7 when called going from node A to E. It also checks to make sure
     * ShortestPathData() outputs [A, D, B, E] when called going from
     * node A to E.
     */
    @Test
    public void testDijkstraGraph() {
        // Create DijkstraGraph object
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        // Add vertices to the graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        // Add edges to the graph
        graph.insertEdge("A", "C", 1);
        graph.insertEdge("C", "A", 1);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("E", "C", 10);
        graph.insertEdge("E", "B", 1);
        graph.insertEdge("B", "E", 1);
        graph.insertEdge("E", "D", 10);
        graph.insertEdge("D", "E", 10);
        graph.insertEdge("D", "B", 2);
        graph.insertEdge("B", "D", 2);
        graph.insertEdge("A", "B", 15);
        graph.insertEdge("B", "A", 15);
        graph.insertEdge("A", "D", 4);
        graph.insertEdge("D", "A", 4);

        // Test if cost and sequence of data is correct
        Assertions.assertEquals(7, graph.shortestPathCost("A", "E"));
        Assertions.assertEquals("[A, D, B, E]", graph.shortestPathData("A", "E").toString());
    }

    /**
     * This test creates an undirected graph with five vertices and
     * seven edges. It checks to make sure shortestPathCost() outputs
     * 5 when called going from node C to D. It also checks to make sure
     * ShortestPathData() outputs [C, A, D] when called going from
     * node C to D.
     */
    @Test
    public void testDijkstraGraphAlternate() {
        // Create DijkstraGraph object
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        // Add vertices to graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        // Add edges to graph
        graph.insertEdge("A", "C", 1);
        graph.insertEdge("C", "A", 1);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("E", "C", 10);
        graph.insertEdge("E", "B", 1);
        graph.insertEdge("B", "E", 1);
        graph.insertEdge("E", "D", 10);
        graph.insertEdge("D", "E", 10);
        graph.insertEdge("D", "B", 2);
        graph.insertEdge("B", "D", 2);
        graph.insertEdge("A", "B", 15);
        graph.insertEdge("B", "A", 15);
        graph.insertEdge("A", "D", 4);
        graph.insertEdge("D", "A", 4);

        // Test if cost and sequence of data is correct
        Assertions.assertEquals(5, graph.shortestPathCost("C", "D"));
        Assertions.assertEquals("[C, A, D]", graph.shortestPathData("C", "D").toString());
    }

    /**
     * This test creates a weakly connected graph with five vertices and
     * four edges. It checks to make sure computeShortestPath() throws
     * NoSuchElementException when called from Node D to C.
     */
    @Test
    public void testDijkstraGraphNoPath() {
        // Create DijkstraGraph object
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        // Add vertices to graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        // Add edges to graph
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("B", "C", 10);
        graph.insertEdge("C", "D", 2);
        graph.insertEdge("D", "E", 15);

        // Check that NoSuchElementException is thrown
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("D", "C"));

    }

    /**
     * This test creates an undirected graph with 11 vertices and
     * 22 edges. It checks to make sure shortestPathCost() outputs
     * 16 when called going from node S to T. It also checks to make sure
     * ShortestPathData() outputs [S, B, F, H, T] when called going from
     * node S to T.
     */
    @Test
    public void testLargeDijkstraGraph() {
        // Create DijkstraGraph object
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        // Add vertices to graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertNode("I");
        graph.insertNode("S");
        graph.insertNode("T");

        // Add edges to graph
        graph.insertEdge("A", "S", 7);
        graph.insertEdge("S", "A", 7);
        graph.insertEdge("A", "D", 5);
        graph.insertEdge("D", "A", 5);
        graph.insertEdge("A", "E", 9);
        graph.insertEdge("E", "A", 9);
        graph.insertEdge("A", "B", 6);
        graph.insertEdge("B", "A", 6);
        graph.insertEdge("S", "B", 4);
        graph.insertEdge("B", "S", 4);
        graph.insertEdge("B", "E", 1);
        graph.insertEdge("E", "B", 1);
        graph.insertEdge("B", "F", 3);
        graph.insertEdge("F", "B", 3);
        graph.insertEdge("B", "C", 8);
        graph.insertEdge("C", "B", 8);
        graph.insertEdge("S", "C", 4);
        graph.insertEdge("C", "S", 4);
        graph.insertEdge("F", "C", 8);
        graph.insertEdge("C", "F", 8);
        graph.insertEdge("E", "F", 12);
        graph.insertEdge("F", "E", 12);
        graph.insertEdge("E", "D", 3);
        graph.insertEdge("D", "E", 3);
        graph.insertEdge("E", "G", 6);
        graph.insertEdge("G", "E", 6);
        graph.insertEdge("D", "G", 10);
        graph.insertEdge("G", "D", 10);
        graph.insertEdge("H", "G", 9);
        graph.insertEdge("G", "H", 9);
        graph.insertEdge("H", "E", 8);
        graph.insertEdge("E", "H", 8);
        graph.insertEdge("H", "F", 4);
        graph.insertEdge("F", "H", 4);
        graph.insertEdge("I", "F", 2);
        graph.insertEdge("F", "I", 2);
        graph.insertEdge("I", "H", 3);
        graph.insertEdge("H", "I", 3);
        graph.insertEdge("I", "T", 7);
        graph.insertEdge("T", "I", 7);
        graph.insertEdge("H", "T", 5);
        graph.insertEdge("T", "H", 5);
        graph.insertEdge("G", "T", 12);
        graph.insertEdge("T", "G", 12);

        // Test if cost and sequence of data is correct
        Assertions.assertEquals(16, graph.shortestPathCost("S", "T"));
        Assertions.assertEquals("[S, B, F, H, T]", graph.shortestPathData("S", "T").toString());
    }

}
