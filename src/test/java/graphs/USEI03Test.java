package graphs;

import controller.ImportDataCtrl;
import domain.Coordinate;
import domain.Location;
import domain.USEI03_DTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.USEI03UI;
import utils.ImportGFHData;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void testGetAllLocations() {

        LinkedList<Location> result1 = usei03uiBig.getAllLocations(null);
        assertNull(result1, "If graph is null, result should be null");

        LinkedList<Location> result2 = usei03uiBig.getAllLocations(gfhBig);
        assertEquals(323, result2.size(), "If graph is not null, result should be a list of all locations");

        LinkedList<Location> result3 = usei03uiSmall.getAllLocations(gfhSmall);
        assertEquals(17, result3.size(), "If graph is not null, result should be a list of all locations");

    }

    @Test
    void testGetAllCoordinates(){
        ArrayList<Coordinate> result1 = usei03uiBig.getAllCoordinates(null);
        assertNull(result1, "If list of locations is null, result should be null");

        ArrayList<Coordinate> result2 = usei03uiBig.getAllCoordinates(usei03uiBig.getAllLocations(gfhBig));
        assertEquals(323, result2.size(), "If list of locations is not null, result should be a list of all coordinates");

        ArrayList<Coordinate> result3 = usei03uiSmall.getAllCoordinates(usei03uiSmall.getAllLocations(gfhSmall));
        assertEquals(17, result3.size(), "If list of locations is not null, result should be a list of all coordinates");
    }

    @Test
    void testGetMostDistantCoordinates(){
        ArrayList<Coordinate> result1 = usei03uiBig.getMostDistantCoordinates(null);
        assertNull(result1, "If list of coordinates is null, result should be null");

        ArrayList<Coordinate> result2 = usei03uiBig.getMostDistantCoordinates(usei03uiBig.getAllCoordinates(usei03uiBig.getAllLocations(gfhBig)));
        Coordinate originCoordinate = result2.get(0);
        Coordinate destinationCoordinate = result2.get(1);
        Coordinate originCoordinateExpected = new Coordinate(42.116700, -8.266700);
        Coordinate destinationCoordinateExpected = new Coordinate(37.016100, -7.935000);

        assertEquals(originCoordinateExpected, originCoordinate, "Same origins.");
        assertEquals(destinationCoordinateExpected, destinationCoordinate, "Same destinations.");

        ArrayList<Coordinate> result3 = usei03uiSmall.getMostDistantCoordinates(usei03uiSmall.getAllCoordinates(usei03uiSmall.getAllLocations(gfhSmall)));
        Coordinate originCoordinate2 = result3.get(0);
        Coordinate destinationCoordinate2 = result3.get(1);
        Coordinate originCoordinateExpected2 = new Coordinate(37.016100, -7.935000);
        Coordinate destinationCoordinateExpected2 = new Coordinate(41.800000, -6.750000);

        assertEquals(originCoordinateExpected2, originCoordinate2, "Same origins.");
        assertEquals(destinationCoordinateExpected2, destinationCoordinate2, "Same destinations.");
    }

    @Test
    void testGetMostDistantLocations(){
        ArrayList<Coordinate> coordinates = usei03uiBig.getMostDistantCoordinates(usei03uiBig.getAllCoordinates(usei03uiBig.getAllLocations(gfhBig)));
        ArrayList<Location> result1 = usei03uiBig.getMostDistantLocations(coordinates.get(0), coordinates.get(1), usei03uiBig.getAllLocations(gfhBig));
        Location origin = result1.get(0);
        Location destination = result1.get(1);
        Location originExpected = new Location("origin1", 0, coordinates.get(0).getLatitude(), coordinates.get(0).getLongitude(), 0, 0);
        Location destinationExpected = new Location("destination1", 0, coordinates.get(1).getLatitude(), coordinates.get(1).getLongitude(), 0, 0);

        ArrayList<Coordinate> coordinates2 = usei03uiSmall.getMostDistantCoordinates(usei03uiSmall.getAllCoordinates(usei03uiSmall.getAllLocations(gfhSmall)));
        ArrayList<Location> result2 = usei03uiSmall.getMostDistantLocations(coordinates2.get(0), coordinates2.get(1), usei03uiSmall.getAllLocations(gfhSmall));
        Location origin2 = result2.get(0);
        Location destination2 = result2.get(1);
        Location originExpected2 = new Location("origin2", 0, coordinates2.get(0).getLatitude(), coordinates2.get(0).getLongitude(), 0, 0);
        Location destinationExpected2 = new Location("destination2", 0, coordinates2.get(1).getLatitude(), coordinates2.get(1).getLongitude(), 0, 0);
    }

    @Test
    void testGetShortestPathData(){
        ArrayList<Coordinate> coordinates = usei03uiBig.getMostDistantCoordinates(usei03uiBig.getAllCoordinates(usei03uiBig.getAllLocations(gfhBig)));
        ArrayList<Location> locations = usei03uiBig.getMostDistantLocations(coordinates.get(0), coordinates.get(1), usei03uiBig.getAllLocations(gfhBig));
        USEI03_DTO dto = usei03uiBig.getShortestPathData(locations.get(0), locations.get(1), 1);
        boolean tripIsPossible = dto.isTripIsPossible();
        boolean tripIsPossibleExpected = false;
        assertEquals(tripIsPossibleExpected, tripIsPossible, "Trip is not possible because autonomy is too low.");

    }

    @Test
    void testShowShortestPath(){
        ArrayList<Coordinate> coordinates = usei03uiBig.getMostDistantCoordinates(usei03uiBig.getAllCoordinates(usei03uiBig.getAllLocations(gfhBig)));
        ArrayList<Location> locations = usei03uiBig.getMostDistantLocations(coordinates.get(0), coordinates.get(1), usei03uiBig.getAllLocations(gfhBig));
        USEI03_DTO dto = usei03uiBig.getShortestPathData(locations.get(0), locations.get(1), 200000);
        boolean tripIsPossible = dto.isTripIsPossible();
        LinkedList<Location> shortPath = dto.getShortPath();
        ArrayList<Location> chargeLocations = dto.getChargeLocations();
        Integer pathLength = dto.getPathLength();
        int numberOfCharges = dto.getNumberOfCharges();

        boolean expectedTripIsPossible = true;
        Integer expectedPathLength = 679976;
        int expectedNumberOfCharges = 3;

        assertEquals(expectedTripIsPossible, tripIsPossible, "Trip is possible because autonomy is high enough.");
        assertEquals(expectedPathLength, pathLength, "Path length is 0 because there is no path.");
        assertEquals(expectedNumberOfCharges, numberOfCharges, "Number of charges is 0 because there is no path.");

        ArrayList<Coordinate> coordinates2 = usei03uiSmall.getMostDistantCoordinates(usei03uiSmall.getAllCoordinates(usei03uiSmall.getAllLocations(gfhSmall)));
        ArrayList<Location> locations2 = usei03uiSmall.getMostDistantLocations(coordinates2.get(0), coordinates2.get(1), usei03uiSmall.getAllLocations(gfhSmall));
        USEI03_DTO dto2 = usei03uiSmall.getShortestPathData(locations2.get(0), locations2.get(1), 200000);
        boolean tripIsPossible2 = dto2.isTripIsPossible();
        LinkedList<Location> shortPath2 = dto2.getShortPath();
        ArrayList<Location> chargeLocations2 = dto2.getChargeLocations();
        Integer pathLength2 = dto2.getPathLength();
        int numberOfCharges2 = dto2.getNumberOfCharges();

        boolean expectedTripIsPossible2 = true;
        Integer expectedPathLength2 = 604469;
        int expectedNumberOfCharges2 = 2;

        assertEquals(expectedTripIsPossible2, tripIsPossible2, "Trip is possible because autonomy is high enough.");
        assertEquals(expectedPathLength2, pathLength2, "Path length is 0 because there is no path.");
        assertEquals(expectedNumberOfCharges2, numberOfCharges2, "Number of charges is 0 because there is no path.");

    }

}
