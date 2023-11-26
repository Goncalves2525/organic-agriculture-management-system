package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI04_DTO;
import graphs.map.MapGraph;
import org.junit.jupiter.api.Test;
import ui.USEI03UI;
import ui.USEI04UI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class USEI04Test {
    private ImportDataCtrl importDataCtrlBig = new ImportDataCtrl();
    private ImportDataCtrl importDataCtrlSmall = new ImportDataCtrl();
    private String locaisPathBig = "files/locais_big.csv";
    private String distanciasPathBig = "files/distancias_big.csv";
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";
    Graph<Location, Integer> gfhBig = importDataCtrlBig.runImportGFHData(locaisPathBig, distanciasPathBig);
    Graph<Location, Integer> gfhSmall = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);
    USEI04UI usei04uiBig = new USEI04UI(gfhBig);
    USEI04UI usei04uiSmall = new USEI04UI(gfhSmall);

    @Test
    public void testFindShortestHamiltonianPathEmptyGraph() {
        Graph<Location, Integer> emptyGraph = new MapGraph<>(true);

        USEI04_DTO result = usei04uiBig.findShortestHamiltonianPath(emptyGraph);
        assertTrue(result.getPath().isEmpty());
    }

    @Test
    public void testFindShortestHamiltonianPathNonEmptyGraph() {
        USEI04_DTO result = usei04uiBig.findShortestHamiltonianPath(gfhBig);

        assertFalse(result.getPath().isEmpty());
        assertTrue(result.getPathWeight() >= 0);
    }

    @Test
    public void testCalculatePathWeightEmptyPath() {
        LinkedList<Location> emptyPath = new LinkedList<>();

        int result = usei04uiBig.calculatePathWeight(gfhBig, emptyPath);
        assertEquals(0, result);
    }

    @Test
    public void testFindShortestHamiltonianPathSingleVertexGraph() {
        Graph<Location, Integer> singleVertexGraph = new MapGraph<>(true);
        Location vertexA = new Location("A", 10, 0, 0, 8, 17); // Example parameters
        singleVertexGraph.addVertex(vertexA);

        USEI04_DTO result = usei04uiBig.findShortestHamiltonianPath(singleVertexGraph);
        assertEquals( 0, result.getPathWeight());
    }
}
