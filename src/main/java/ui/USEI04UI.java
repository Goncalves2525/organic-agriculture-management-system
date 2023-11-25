package ui;


import domain.Location;
import dto.USEI04_DTO;
import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;
import java.util.*;

import static graphs.Algorithms.calculatePathWeight;
import static graphs.Algorithms.hamiltonianPathUtil;

public class USEI04UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI04UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    @Override
    public void run() {
        List<Location> allLocations = new ArrayList<>(gfh.vertices());

        // If empty
        if (allLocations.isEmpty()) {
            System.out.println("Graph is empty. Cannot find path.");
            return;
        }

        USEI04_DTO result = findShortestHamiltonianPath(gfh);

        // Print
        if (result.hasHamiltonianPath()) {
            LinkedList<Location> hamiltonianPath = result.getPath();
            int totalCost = result.getPathWeight();

            for (int i = 0; i < hamiltonianPath.size() - 1; i++) {
                Location currentLocation = hamiltonianPath.get(i);
                Location nextLocation = hamiltonianPath.get(i + 1);
                int cost = gfh.edge(currentLocation, nextLocation).getWeight();
                totalCost += cost;

                System.out.println(currentLocation.getCode() + " --> " + nextLocation.getCode() + " (Cost: " + cost + ")");
            }

            System.out.println("Total Cost: " + totalCost);
        } else {
            System.out.println("No feasible path found.");
        }
    }

    private USEI04_DTO findShortestHamiltonianPath(Graph<Location, Integer> g) {
        int numVerts = g.numVertices();
        if (numVerts == 0) {
            return new USEI04_DTO(false, new LinkedList<>(), 0);
        }

        boolean[] visited = new boolean[numVerts];

        for (Location vertex : g.vertices()) {
            LinkedList<Location> path = new LinkedList<>();
            path.addLast(vertex);
            visited[g.key(vertex)] = true;

            if (shortestHamiltonianPathUtil(g, visited, path, 1)) {
                int pathWeight = calculatePathWeight(g, path);
                return new USEI04_DTO(true, path, pathWeight);
            }

            // Reset visited array for the next iteration
            Arrays.fill(visited, false);
        }

        return new USEI04_DTO(false, new LinkedList<>(), 0);
    }

    private boolean shortestHamiltonianPathUtil(Graph<Location, Integer> g, boolean[] visited, LinkedList<Location> path, int pos) {
        if (pos == g.numVertices()) {
            return true;  // All vertices are visited
        }

        Location lastVertex = path.getLast();

        for (Location v : g.vertices()) {
            int key = g.key(v);
            if (!visited[key]) {
                if (pos == 0 || g.edge(lastVertex, v) != null) {
                    visited[key] = true;
                    path.addLast(v);

                    if (shortestHamiltonianPathUtil(g, visited, path, pos + 1)) {
                        return true;
                    }

                    // Backtrack
                    visited[key] = false;
                    path.removeLast();
                }
            }
        }
        return false;
    }
}