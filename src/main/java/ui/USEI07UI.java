package ui;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI07_DTO;
import graphs.Edge;
import graphs.Graph;
import utils.Utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class USEI07UI implements Runnable {

    private ImportDataCtrl importDataCtrl;

    private static String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI07UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    private Location chooseStartingLocation(Graph<Location, Integer> graph) {
        // local de origem
        System.out.println("Escolha o local de origem:");
        int option = 1;

        //  lista de vértices
        List<Location> vertices = graph.vertices();
        for (Location location : vertices) {
            System.out.println(option + ". " + location.getCode());
            option++;
        }

        int opcaoLocal = Utils.readIntegerFromConsole("Opção:");

        // Obter a localidade correspondente à opção
        Location startingLocation;
        try {
            startingLocation = vertices.get(opcaoLocal - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opção inválida. Usando valor padrão.");
            startingLocation = vertices.get(0); // Valor padrão, o primeiro da lista
        }

        System.out.println("Local de origem escolhido: " + startingLocation.getCode());

        return startingLocation;
    }


    public void run() {
        // Create a graph from imported data
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        Location startingLocation = chooseStartingLocation(gfh);

        // Find an optimized path using Circuit Euler
        //List<Location> optimizedPath = findOptimizedPath(gfh);
        List<Location> optimizedPath = findOptimizedPath(gfh, startingLocation);

        List<USEI07_DTO> centralityInfoList = calculateCentrality(gfh, optimizedPath);
        for (USEI07_DTO centralityInfo : centralityInfoList) {
            displayCentralityInfo(centralityInfo);
        }
    }

    private void displayCentralityInfo(USEI07_DTO centralityInfo) {
        USEI07_DTO.Hub hub = centralityInfo.getHub();

        System.out.println("Local: " + hub.name);

        if (hub.isHub()) {
            System.out.println("Hub de Partida");
            System.out.println("Hora de Chegada: " + centralityInfo.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            System.out.println("Hora de Partida: " + centralityInfo.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            System.out.println("Hora de Chegada: " + centralityInfo.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        System.out.println("Distância Total: " + centralityInfo.getTotalDistance() + "m");
        System.out.println("Número de Carregamentos: " + centralityInfo.getNumberOfLoadings());
        System.out.println("Tempo Total: " + centralityInfo.getTotalTime() + " minutos\n");
    }

    public List<USEI07_DTO> calculateCentrality(Graph<Location, Integer> graph, List<Location> optimizedPath) {
        List<USEI07_DTO> centralityList = new ArrayList<>();

        for (int i = 0; i < optimizedPath.size(); i++) {
            Location location = optimizedPath.get(i);

            // Create a Hub object for the location
            USEI07_DTO.Hub hub = new USEI07_DTO.Hub(location.getCode(), location.getSchedule().getOpenHour(), location.getSchedule().getCloseHour());

            // Calculate arrival and departure time for hubs
            if (hub.isHub()) {
                LocalTime arrivalTime = LocalTime.now();
                LocalTime departureTime = arrivalTime.plusMinutes(60);

                hub.setArrivalTime(arrivalTime);
                hub.setDepartureTime(departureTime);
            }

            // Create a USEI07_DTO object and add it to the list
            USEI07_DTO centralityInfo = new USEI07_DTO(hub);

            // Set additional information for hubs
            if (hub.isHub()) {
                centralityInfo.setArrivalTime(hub.getArrivalTime());
                centralityInfo.setDepartureTime(hub.getDepartureTime());
            }

            // Calculate total distance
            double totalDistance = calculateTotalDistance(graph, optimizedPath);
            centralityInfo.setTotalDistance(totalDistance);

            // Calculate number of loadings
            int numberOfLoadings = i;
            centralityInfo.setNumberOfLoadings(numberOfLoadings);

            // Calculate total time
            int totalTime = numberOfLoadings * 30 + (int) (totalDistance / 1000 / 30);
            centralityInfo.setTotalTime(totalTime);

            centralityList.add(centralityInfo);
        }

        return centralityList;
    }

    private double calculateTotalDistance(Graph<Location, Integer> graph, List<Location> optimizedPath) {
        double totalDistance = 0.0;

        for (int i = 0; i < optimizedPath.size() - 1; i++) {
            Location currentLocation = optimizedPath.get(i);
            Location nextLocation = optimizedPath.get(i + 1);

            Edge<Location, Integer> edge = graph.edge(currentLocation, nextLocation);
            if (edge != null) {
                totalDistance += edge.getWeight();
            }
        }

        return totalDistance;
    }

    public List<Location> findOptimizedPath2(Graph<Location, Integer> graph) {

        Map<Location, List<Location>> adjacencyList = getAdjacencyList(graph);

        // Initialize a stack to store the path
        Stack<Location> path = new Stack<>();
        // Select a starting vertex (replace with your logic)
        Location startLocation = graph.vertices().iterator().next();
        path.push(startLocation);

        List<Location> optimizedPath = new ArrayList<>();

        while (!path.isEmpty()) {
            Location current = path.peek();

            if (adjacencyList.get(current) != null && !adjacencyList.get(current).isEmpty()) {
                // If there are remaining edges from the current vertex
                Location next = adjacencyList.get(current).remove(0);
                path.push(next);
            } else {
                // If no more edges from the current vertex, add it to the final path
                optimizedPath.add(0, path.pop());
            }
        }

        return optimizedPath;
    }

    private List<Location> findOptimizedPath(Graph<Location, Integer> graph, Location startingLocation) {

        Map<Location, List<Location>> adjacencyList = getAdjacencyList(graph);

        // Initialize a stack to store the path
        Stack<Location> path = new Stack<>(); //O(1)
        path.push(startingLocation);

        List<Location> optimizedPath = new ArrayList<>();

        while (!path.isEmpty()) { //O(n)
            Location current = path.peek();

            if (adjacencyList.get(current) != null && !adjacencyList.get(current).isEmpty()) {
                // If there are remaining edges from the current vertex
                Location next = adjacencyList.get(current).remove(0);
                path.push(next); //O(1)
            } else {
                // If no more edges from the current vertex, add it to the final path
                optimizedPath.add(0, path.pop());//O(1)
            }
        }

        return optimizedPath;
    }


    private Map<Location, List<Location>> getAdjacencyList(Graph<Location, Integer> graph) {
        Map<Location, List<Location>> adjacencyList = new HashMap<>();

        // Iterate through each vertex in the graph
        for (Location vertex : graph.vertices()) {
            // Get the neighbors of the current vertex
            List<Location> neighbors = getNeighbors(graph, vertex);

            // Add the vertex and its neighbors to the adjacency list
            adjacencyList.put(vertex, neighbors);
        }

        return adjacencyList;
    }

    private List<Location> getNeighbors(Graph<Location, Integer> graph, Location vertex) {
        List<Location> neighbors = new ArrayList<>();

        // Iterate through all edges in the graph
        for (Edge<Location, Integer> edge : graph.edges()) {
            Location vOrig = edge.getVOrig();
            Location vDest = edge.getVDest();

            // Check if the current vertex is either the origin or destination of the edge
            if (vertex.equals(vOrig) || vertex.equals(vDest)) {
                Location neighbor = (vertex.equals(vOrig)) ? vDest : vOrig;
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

//    public static void main(String[] args) {
//        USEI07UI usei07UI = new USEI07UI();
//        usei07UI.run();
//    }
}
