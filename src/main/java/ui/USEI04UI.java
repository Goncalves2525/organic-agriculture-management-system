package ui;

import controller.ImportDataCtrl;
import domain.Coordinate;
import domain.Location;
import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;
import graphs.map.MapGraph;
import graphs.matrix.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;

public class USEI04UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI04UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    public double calcDistance(Coordinate coordinatesA, Coordinate coordinatesB) { // O(1)

        double latA = coordinatesA.getLatitude(); // O(1)
        double latB = coordinatesB.getLatitude(); // O(1)
        double lonA = coordinatesA.getLongitude(); // O(1)
        double lonB = coordinatesB.getLongitude(); // O(1)
        double R = 6371e3; // metres
        double φ1 = Math.toRadians(latA); // O(1)
        double φ2 = Math.toRadians(latB); // O(1)
        double Δφ = Math.toRadians(latB - latA); // O(1)
        double Δλ = Math.toRadians(lonB - lonA); // O(1)

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ / 2) * Math.sin(Δλ / 2); // O(1)

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // O(1)

        return R * c; // O(1)
    }

    @Override
    public void run() {
        System.out.println("\nShortest Path that Visits Every Vertex:");

        // Get all vertices in the graph
        List<Location> vertices = new ArrayList<>(gfh.vertices());

        LinkedList<Location> minConnectionPath = null;
        Integer minConnectionDistance = Integer.MAX_VALUE;

        // Loop through all possible combinations of vertices
        for (Location origin : vertices) {
            for (Location destination : vertices) {
                if (!origin.equals(destination)) {
                    LinkedList<Location> currentPath = new LinkedList<>();
                    Integer currentDistance = Algorithms.shortestPath(
                            gfh, origin, destination, Integer::compare, Integer::sum, 0, currentPath);

                    // Update the minimum connection network if a shorter path is found
                    if (currentDistance < minConnectionDistance) {
                        minConnectionDistance = currentDistance;
                        minConnectionPath = currentPath;
                    }
                }
            }
        }

        // Display the minimum connection network
        //displayMinimumConnectionNetwork(minConnectionPath, minConnectionDistance);
    }

    }
