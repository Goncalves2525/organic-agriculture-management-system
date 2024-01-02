package ui;

import domain.Location;
import dto.USEI08_DTO;
import graphs.Graph;
import java.util.*;

public class USEI04UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI04UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    @Override
    public void run() {
        // List of every vertice
        List<Location> allLocations = new ArrayList<>(gfh.vertices()); // O(|V|), where |V| is the number of vertices in the graph

        // No vertice, ends
        if (allLocations.isEmpty()) {
            System.out.println("Graph is empty. Cannot find path.");
            return;
        }

        // Find the shortest path
        USEI08_DTO result = findShortestHamiltonianPath(gfh);

        // Prints the path
        if (result.hasHamiltonianPath()) {
            LinkedList<Location> hamiltonianPath = result.getPath();
            int totalCost = result.getPathWeight();

            System.out.println("Shortest Hamiltonian Path:");
            for (int i = 0; i < hamiltonianPath.size() - 1; i++) {
                Location currentLocation = hamiltonianPath.get(i);
                Location nextLocation = hamiltonianPath.get(i + 1);
                int cost = gfh.edge(currentLocation, nextLocation).getWeight();

                System.out.println(currentLocation.getCode() + " --> " + nextLocation.getCode() + " (Cost: " + cost + ")");
            }

            System.out.println("Total Cost: " + totalCost);
        } else {
            System.out.println("No feasible path found.");
        }
    }

    public USEI08_DTO findShortestHamiltonianPath(Graph<Location, Integer> g) {
        //Get the nr of vertices in the graph
        int numVerts = g.numVertices(); // O(1)

        // If 0, returns a null list
        if (numVerts == 0) {
            return new USEI08_DTO(false, new LinkedList<>(), 0); // O(1)
        }

        // Array to check if the vertices have been visited
        boolean[] visited = new boolean[numVerts]; // O(|V|)

        //Array to save the min path cost
        int[] minPathWeight = { Integer.MAX_VALUE }; // O(1)

        // List to save the shortest path found
        LinkedList<Location> shortestPath = new LinkedList<>(); // O(1)

        // For loop to go thorugh all the vertices in the graph to find a valid path
        for (Location vertex : g.vertices()) { //O(|V|)
            LinkedList<Location> path = new LinkedList<>(); // O(1)
            path.addLast(vertex); // O(1)
            visited[g.key(vertex)] = true; // O(1)

            // If a path is found, calculate its cost
            if (shortestHamiltonianPathUtil(g, visited, path, 1, minPathWeight)) {
                int pathWeight = calculatePathWeight(g, path);
                if (pathWeight < minPathWeight[0]) {
                    minPathWeight[0] = pathWeight; // O(1)
                    shortestPath = new LinkedList<>(path); //O(|V|)
                }
            }

            //Resets for next loop
            Arrays.fill(visited, false);
        }

        return new USEI08_DTO(!shortestPath.isEmpty(), shortestPath, minPathWeight[0]);
    }

    private boolean shortestHamiltonianPathUtil(Graph<Location, Integer> g, boolean[] visited,
                                                LinkedList<Location> path, int pos, int[] minPathWeight) {

        //If all vertices have been visited ends - the path has been found
        if (pos == g.numVertices()) { // O(1)
            return true;
        }

        // Get the last vertice of the current path
        Location lastVertex = path.getLast();

        for (Location v : g.vertices()) { //O(|V|)
            int key = g.key(v);

            //If the vertice is not visited yet, and there is a valid edge, goes thorugh that path
            if (!visited[key]) { // O(1)
                if (pos == 0 || g.edge(lastVertex, v) != null) {
                    visited[key] = true;
                    path.addLast(v);

                    // Recursive to explore the path
                    if (shortestHamiltonianPathUtil(g, visited, path, pos + 1, minPathWeight)) {
                        return true;
                    }

                    // Goes back if the current path is not valid
                    visited[key] = false;
                    path.removeLast();
                }
            }
        }
        return false;
    }

    public int calculatePathWeight(Graph<Location, Integer> g, LinkedList<Location> path) {
        int weight = 0;
        // for loop that goes thorugh the path to calculte it's cost
        for (int i = 0; i < path.size() - 1; i++) { //O(n)
            Location currentLocation = path.get(i);
            Location nextLocation = path.get(i + 1);
            weight += g.edge(currentLocation, nextLocation).getWeight();
        }
        return weight;
    }
}