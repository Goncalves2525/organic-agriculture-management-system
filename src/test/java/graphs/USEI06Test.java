package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.map.MapGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class USEI06Test {
    private ImportDataCtrl importDataCtrlSmall = new ImportDataCtrl();
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";
    private Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);
    Location ct1, ct2, ct3, ct4, ct5, ct6, ct7, ct8, ct9, ct10, ct11, ct12, ct13, ct14, ct15, ct16, ct17;

    @BeforeEach
    public void setUp() {
        ArrayList<Location> vertices = gfh.vertices();
        ct1 = vertices.get(0);
        ct2 = vertices.get(1);
        ct3 = vertices.get(2);
        ct15 = vertices.get(3);
        ct16 = vertices.get(4);
        ct12 = vertices.get(5);
        ct7 = vertices.get(6);
        ct8 = vertices.get(7);
        ct13 = vertices.get(8);
        ct14 = vertices.get(9);
        ct11 = vertices.get(10);
        ct5 = vertices.get(11);
        ct9 = vertices.get(12);
        ct4 = vertices.get(13);
        ct17 = vertices.get(14);
        ct6 = vertices.get(15);
        ct10 = vertices.get(16);
    }

    @Test
    public void testAllPathsAutonomy_Success() {
        Location origem = ct1;
        Location destino = ct17;
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(gfh, origem, destino, 200000);
        ArrayList<LinkedList<Location>> expectedPaths = new ArrayList<>();
        LinkedList<Location> path1 = new LinkedList<>();
        LinkedList<Location> path2 = new LinkedList<>();

        path1.add(ct1);
        path1.add(ct17);
        expectedPaths.add(path1);

        path2.add(ct1);
        path2.add(ct6);
        path2.add(ct17);
        expectedPaths.add(path2);

        assertEquals(expectedPaths, resultPaths);
    }

    @Test
    public void testAllPathsAutonomy_Success2() {
        Location origem = ct10;
        Location destino = ct3;
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(gfh, origem, destino, 300000);
        ArrayList<LinkedList<Location>> expectedPaths = new ArrayList<>();
        LinkedList<Location> path1 = new LinkedList<>();
        LinkedList<Location> path2 = new LinkedList<>();
        LinkedList<Location> path3 = new LinkedList<>();
        LinkedList<Location> path4 = new LinkedList<>();

        path1.add(ct10);
        path1.add(ct1);
        path1.add(ct12);
        path1.add(ct3);
        expectedPaths.add(path1);

        path2.add(ct10);
        path2.add(ct1);
        path2.add(ct12);
        path2.add(ct15);
        path2.add(ct3);
        expectedPaths.add(path2);

        path3.add(ct10);
        path3.add(ct6);
        path3.add(ct1);
        path3.add(ct12);
        path3.add(ct3);
        expectedPaths.add(path3);

        path4.add(ct10);
        path4.add(ct6);
        path4.add(ct17);
        path4.add(ct16);
        path4.add(ct3);
        expectedPaths.add(path4);

        assertEquals(expectedPaths, resultPaths);
    }

    @Test
    public void testAllPathsAutonomy_Empty() {
        Location origem = ct1;
        Location destino = ct17;
        MapGraph<Location, Integer> emptyGraph = new MapGraph<>(true);
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(emptyGraph, origem, destino, 200000);
        ArrayList<LinkedList<Location>> expectedPaths = null;

        assertEquals(expectedPaths, resultPaths);
    }

    @Test
    public void testAllPaths_NotEnoughAutonomy() {
        Location origem = ct1;
        Location destino = ct17;
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(gfh, origem, destino, 0);
        ArrayList<LinkedList<Location>> expectedPaths = new ArrayList<>(); //empty

        assertEquals(expectedPaths, resultPaths);
    }

    @Test
    public void testAllPathsAutonomy_NoPossiblePaths() {
        Location origem = ct10;
        Location destino = ct3;
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(gfh, origem, destino, 100000);
        ArrayList<LinkedList<Location>> expectedPaths = new ArrayList<>(); //empty

        assertEquals(expectedPaths, resultPaths);
    }

    @Test
    public void testAllPathsAutonomy_NegativeAutonomy() {
        Location origem = ct1;
        Location destino = ct17;
        ArrayList<LinkedList<Location>> resultPaths = Algorithms.allPathsAutonomy(gfh, origem, destino, -1);
        ArrayList<LinkedList<Location>> expectedPaths = null;

        assertEquals(expectedPaths, resultPaths);
    }

}
