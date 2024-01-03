package graphs;

import domain.Location;
import dto.USEI08_DTO;
import graphs.map.MapGraph;
import org.junit.jupiter.api.Test;
import ui.USEI08UI;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class USEI08Test {

    // Helper method to create a sample graph
    private Graph<Location, Integer> createSampleGraph() {
        Graph<Location, Integer> graph = new MapGraph<>(false);

        Location location1 = new Location("CT01", 10, 0.0, 0.0, 8, 18);
        Location location2 = new Location("CT02", 15, 1.0, 1.0, 9, 19);
        Location location3 = new Location("CT03", 20, 2.0, 2.0, 10, 20);
        Location location4 = new Location("CT04", 25, 3.0, 3.0, 11, 21);
        Location location5 = new Location("CT05", 30, 4.0, 4.0, 12, 22);
        Location location6 = new Location("CT06", 35, 5.0, 5.0, 13, 23);

        graph.addVertex(location1);
        graph.addVertex(location2);
        graph.addVertex(location3);
        graph.addVertex(location4);
        graph.addVertex(location5);
        graph.addVertex(location6);

        graph.addEdge(location1, location2, 300);
        graph.addEdge(location2, location3, 400);
        graph.addEdge(location3, location4, 200);
        graph.addEdge(location4, location5, 500);
        graph.addEdge(location5, location6, 300);
        graph.addEdge(location6, location1, 100);

        return graph;
    }
    @Test
    public void testFindShortestHamiltonianPath() {
        // Create a sample graph for testing
        Graph<Location, Integer> sampleGraph = createSampleGraph();

        // Create USEI08UI instance and run the method
        USEI08UI usei08ui = new USEI08UI(sampleGraph);
        USEI08_DTO result = usei08ui.findShortestHamiltonianPath(sampleGraph);

        // Check if the result is not null and has a valid path
        assertNotNull(result);
        assertTrue(result.hasHamiltonianPath());

        // Check if the path is valid and has the correct weight
        LinkedList<Location> path = result.getPath();
        assertEquals(6, path.size());
        assertEquals(1300, result.getPathWeight());
    }

    @Test
    public void testCalculatePathWeight() {
        // Create a sample graph for testing
        Graph<Location, Integer> sampleGraph = createSampleGraph();

        // Create USEI08UI instance
        USEI08UI usei08ui = new USEI08UI(sampleGraph);

        // Create a sample path
        LinkedList<Location> samplePath = new LinkedList<>();
        samplePath.add(new Location("CT01", 10, 0.0, 0.0, 8, 18));
        samplePath.add(new Location("CT02", 15, 1.0, 1.0, 9, 19));
        samplePath.add(new Location("CT03", 20, 2.0, 2.0, 10, 20));

        sampleGraph.addEdge(samplePath.get(0), samplePath.get(1), 300);
        sampleGraph.addEdge(samplePath.get(1), samplePath.get(2), 400);

        // Calculate the path weight
        int pathWeight = usei08ui.calculatePathWeight(sampleGraph, samplePath);

        // Adjust the expected weight based on your sample graph
        assertEquals(700, pathWeight);
    }

    @Test
    public void testNoFeasiblePath() {
        Graph<Location, Integer> emptyGraph = new MapGraph<>(false);

        USEI08UI usei08ui = new USEI08UI(emptyGraph);
        USEI08_DTO result = usei08ui.findShortestHamiltonianPath(emptyGraph);

        assertNotNull(result);
        assertFalse(result.hasHamiltonianPath());
        assertEquals(0, result.getPath().size());
        assertEquals(0, result.getPathWeight());
    }

    @Test
    public void testSingleLocationGraph() {
        Graph<Location, Integer> singleLocationGraph = new MapGraph<>(false);
        Location location = new Location("CT01", 10, 0.0, 0.0, 8, 18);
        singleLocationGraph.addVertex(location);

        USEI08UI usei08ui = new USEI08UI(singleLocationGraph);
        USEI08_DTO result = usei08ui.findShortestHamiltonianPath(singleLocationGraph);

        assertNotNull(result);
        assertTrue(result.hasHamiltonianPath());
        assertEquals(1, result.getPath().size());
        assertEquals(0, result.getPathWeight());
    }
}
