package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI03_DTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.USEI02UI;
import ui.USEI03UI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class USEI02Test {
    private ImportDataCtrl importDataCtrlSmall = new ImportDataCtrl();
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";

    private Graph<Location, Integer> graphSmall;

    @BeforeEach
    public void setUp() {
        graphSmall = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);
    }

    @Test
    public void testRunImportGFHData() {
        Graph<Location, Integer> graph = graphSmall;

        assertNotNull(graph);
        assertEquals(17, graph.numVertices());
        assertEquals(66, graph.numEdges());
    }

    @Test
    public void testCalcInfluence() {

        Graph<Location, Integer> graph = graphSmall;

        Map<Location, Integer> influenceMap = Algorithms.calculateInfluence(graph);
        assertEquals(3, influenceMap.get(new Location("CT7", 7, 38.5667, -7.9, 9, 14)));
    }

    @Test
    public void testCalcInfluence2() {
        Graph<Location, Integer> graph = graphSmall;

        Map<Location, Integer> influenceMap = Algorithms.calculateInfluence(graph);
        ArrayList<Location> sortedLocationsByInfluence = (ArrayList<Location>) USEI02UI.sortLocationsByMetric(influenceMap, Comparator.reverseOrder());

        assertEquals(5, influenceMap.get(sortedLocationsByInfluence.get(0)));
    }

    @Test
    public void testCalcProximity(){
        Graph<Location, Integer> graph = graphSmall;

        Location sourceVertex = USEI02UI.chooseSourceByInfluence(graph);

        List<Double> proximities = USEI02UI.calculateProximities(graph, sourceVertex);

        assertEquals(145873.0, proximities.get(0));
    }

    @Test
    public void testCalcCentrality(){
        Graph<Location, Integer> graph = graphSmall;

        Location sourceVertex = USEI02UI.chooseSourceByInfluence(graph);

        ArrayList<LinkedList<Location>> paths = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        Algorithms.shortestPaths(graph, sourceVertex, Comparator.naturalOrder(), Integer::sum, 0, paths, distances);

        assertEquals(145873, distances.get(0));

    }

}
