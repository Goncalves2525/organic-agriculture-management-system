package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import domain.USEI03_DTO;
import org.junit.jupiter.api.Test;
import ui.USEI03UI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class USEI03Test {
    private ImportDataCtrl importDataCtrlBig = new ImportDataCtrl();
    private ImportDataCtrl importDataCtrlSmall = new ImportDataCtrl();
    private String locaisPathBig = "files/locais_big.csv";
    private String distanciasPathBig = "files/distancias_big.csv";
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";
    Graph<Location, Integer> gfhBig = importDataCtrlBig.runImportGFHData(locaisPathBig, distanciasPathBig);
    Graph<Location, Integer> gfhSmall = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);
    USEI03UI usei03uiBig = new USEI03UI(gfhBig);
    USEI03UI usei03uiSmall = new USEI03UI(gfhSmall);



    @Test
    public void testGetBiggestShortestPathData() {
        USEI03_DTO dto = usei03uiBig.getBiggestShortestPathData(null);

        assertNull(dto);

        USEI03_DTO dto2 = usei03uiBig.getBiggestShortestPathData(200000);
        LinkedList<Location> path1 = dto2.getShortPath();
        Location origin1 = path1.get(0);
        String originName1 = origin1.getCode();
        Location destination1 = path1.get(path1.size() - 1);
        String destinationName1 = destination1.getCode();
        int numberOfCharges1 = dto2.getNumberOfCharges();

        assertEquals("CT162", originName1);
        assertEquals("CT194", destinationName1);
        assertEquals(3, numberOfCharges1);

        USEI03_DTO dto3 = usei03uiSmall.getBiggestShortestPathData(200000);
        LinkedList<Location> path2 = dto3.getShortPath();
        Location origin2 = path2.get(0);
        String originName2 = origin2.getCode();
        Location destination2 = path2.get(path2.size() - 1);
        String destinationName2 = destination2.getCode();
        int numberOfCharges2 = dto3.getNumberOfCharges();

        assertEquals("CT15", originName2);
        assertEquals("CT8", destinationName2);
        assertEquals(1, numberOfCharges2);
    }

    @Test
    public void testGetShortestPathData() {
        USEI03_DTO dto = usei03uiBig.getShortestPathData(null, null, 200000);

        assertNull(dto);

        USEI03_DTO dto1 = usei03uiBig.getBiggestShortestPathData(200000);
        LinkedList<Location> path = dto1.getShortPath();
        Location origin = path.get(0);
        Location destination = path.get(path.size() - 1);
        USEI03_DTO dto2 = usei03uiBig.getShortestPathData(origin, destination, 200000);
        LinkedList<Location> path1 = dto2.getShortPath();
        Location location1 = path1.get(1);
        String locationName1 = location1.getCode();

        assertEquals("CT34", locationName1);

        USEI03_DTO dto3 = usei03uiSmall.getBiggestShortestPathData(200000);
        LinkedList<Location> path2 = dto3.getShortPath();
        Location origin2 = path2.get(0);
        Location destination2 = path2.get(path2.size() - 1);
        USEI03_DTO dto4 = usei03uiSmall.getShortestPathData(origin2, destination2, 200000);
        LinkedList<Location> path3 = dto4.getShortPath();
        Location location2 = path3.get(1);
        String locationName2 = location2.getCode();

        assertEquals("CT12", locationName2);
    }

    @Test
    public void testShowShortestPath() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        USEI03_DTO dto = usei03uiBig.getBiggestShortestPathData(1);
        LinkedList<Location> path = dto.getShortPath();
        ArrayList<Location> chargeLocations = dto.getChargeLocations();
        Integer pathLength = dto.getPathLength();
        int numberOfCharges = dto.getNumberOfCharges();
        int minimumAutonomy = dto.getMinimumAutonomy();
        Location origin = null;
        Location destination = null;

        usei03uiBig.showShortestPath(false, path, chargeLocations, pathLength, numberOfCharges, origin, destination, minimumAutonomy);

        System.setOut(System.out);

        String consoleOutput = outContent.toString();

        assertTrue(consoleOutput.contains("\nO veículo não tem autonomia suficiente para efetuar a viagem. Precisava de ter pelo menos " + minimumAutonomy + "m de autonomia."));

    }
}
