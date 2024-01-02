package ui;
import domain.Location;
import dto.USEI08_DTO;
import graphs.Graph;
import graphs.map.MapGraph;
import utils.Utils;
import java.util.*;

public class USEI08UI implements Runnable {
    private Graph<Location, Integer> gfh;
    public USEI08UI(Graph<Location, Integer> gfh) {this.gfh = gfh;}

    @Override
    public void run() {
        ArrayList<Location> vertices = new ArrayList<>(gfh.vertices());
        ArrayList<Location> hubs = new ArrayList<>();

        int autonomia = Utils.readIntegerFromConsole("Qual é a autonomia do veículo? (em metros)");

        // Find and collect hub locations
        for (Location location : vertices) {
            hubs.add(location);
        }

        //Nr of Employees
        for (Location hub : hubs) {
            int numberOfEmployees = extractNumberOfEmployees(hub.getCode()); // Assuming getCode() returns the location code
            hub.setNrOfEmployees(numberOfEmployees);
        }

        // Sort hubs based on the number of employees (descending order)
        Collections.sort(vertices, (hub1, hub2) -> Integer.compare(hub2.getNrOfEmployees(), hub1.getNrOfEmployees()));

        // Create a new graph for top hubs
        Graph<Location, Integer> topHubsGraph = new MapGraph<>(false);

        int numberOfTopHubs = 7; // Change this to 5, 6 or 7 as needed

        // Add vertices from top hubs to the new graph
        for (int i = 0; i < Math.min(numberOfTopHubs, vertices.size()); i++) {
            topHubsGraph.addVertex(vertices.get(i));
        }

        // Add edges from the original graph based on the top hubs
        for (int i = 0; i < Math.min(numberOfTopHubs, vertices.size()); i++) {
            Location hub = vertices.get(i);
            for (Location neighbor : gfh.adjVertices(hub)) {
                if (vertices.subList(0, Math.min(5, vertices.size())).contains(neighbor)) {
                    // Add the edge only if both vertices are in the top hubs
                    topHubsGraph.addEdge(hub, neighbor, gfh.edge(hub, neighbor).getWeight());
                }
            }
        }

        // Find the Hamiltonian path
        USEI08_DTO result = findShortestHamiltonianPath(topHubsGraph);

        // Print the results
        if (result.hasHamiltonianPath()) {
            LinkedList<Location> hamiltonianPath = result.getPath();
            int totalCost = result.getPathWeight();
            int remainingAutonomy = autonomia;
            double speedKmPerHour = 60.0; // Speed in km/h

            // Declare timeInHours outside the loop
            int chargingTimes = 0;

            System.out.println("Shortest Hamiltonian Path:");
            for (int i = 0; i < hamiltonianPath.size() - 1; i++) {
                Location currentLocation = hamiltonianPath.get(i);
                Location nextLocation = hamiltonianPath.get(i + 1);
                int cost = gfh.edge(currentLocation, nextLocation).getWeight();

                // Check if charging is needed
                if (remainingAutonomy <= cost) {
                    // Charge at the current location
                    System.out.println("Charging at " + currentLocation.getCode());
                    remainingAutonomy = autonomia;

                    // Add 30 minutes for charging
                    chargingTimes++;
                }

                // Deduct the cost from remaining autonomy
                remainingAutonomy -= cost;

                // Print the current path segment
                System.out.println(currentLocation.getCode() + " --> " + nextLocation.getCode() + " (Cost: " + cost + ")" + " Nr of employees : " + currentLocation.getNrOfEmployees());
            }

            // Calculate the time taken traveling
            double totalDistanceKm = totalCost / 1000.0; // meters
            double timeTravelingInHours = totalDistanceKm / speedKmPerHour;
            System.out.print("Time taken traveling: ");
            printTime(timeTravelingInHours);

            // Add 20 minutes for each vertex
            int vertexTimeMinutes = 20 * (hamiltonianPath.size() - 1);
            double timeInEachStopInHours = vertexTimeMinutes / 60.0;
            System.out.print("Time taken in each stop: ");
            printTime(timeInEachStopInHours);

            // Add 30 minutes for each charging
            int chargingTimeMinutes = 30 * chargingTimes;
            double timeInEachChargingStop = chargingTimeMinutes / 60.0;
            System.out.print("Time taken in each charging stoppage: ");
            printTime(timeInEachChargingStop);

            double timeInHours = timeTravelingInHours + timeInEachStopInHours + timeInEachChargingStop;
            System.out.print("Total time for the circuit: ");
            printTime(timeInHours);
            System.out.println("Total Cost: " + totalCost);
        } else {
            System.out.println("No feasible path found.");
        }
    }

    private void printTime (double timeInHours){
        int hours = (int) timeInHours;
        int minutes = (int) ((timeInHours - hours) * 60);
        System.out.println(hours + " hours " + minutes + " minutes");
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

    private int extractNumberOfEmployees(String locationCode) {
        try {
            // Assuming the location code follows the pattern "CTxx"
            String employeeNumberString = locationCode.substring(2);
            return Integer.parseInt(employeeNumberString);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Handle the case where the number cannot be extracted, or the location code is not in the expected format
            e.printStackTrace(); // Or log the error
            return 0; // Set a default value or handle the error accordingly
        }
    }
}