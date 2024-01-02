package ui;

import controller.ImportDataCtrl;
import domain.Location;
import domain.Schedule;
import dto.USEI07_DTO;
import graphs.Edge;
import graphs.Graph;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class USEI07UI implements Runnable {

    private ImportDataCtrl importDataCtrl;

    private static String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI07UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        // Create a graph from imported data
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        // Find an optimized path using Circuit Euler
        List<Location> optimizedPath = findOptimizedPath(gfh);

        // Display the results
        System.out.println("Optimized Path:");
        for (Location location : optimizedPath) {
            System.out.println(location);
        }

        // Additional logic for USEI07_DTO if required
        List<USEI07_DTO> centralityInfoList = calculateCentrality(gfh);
        for (USEI07_DTO centralityInfo : centralityInfoList) {
            displayCentralityInfo(centralityInfo);
        }
    }

    private void displayCentralityInfo(USEI07_DTO centralityInfo) {
        USEI07_DTO.Hub hub = centralityInfo.getHub();

        // Converter String para LocalTime
        int openHour = hub.openHour;
        int closeHour = hub.closeHour;

        // Validar os horários
        if (isTimeWithinRange(String.valueOf(hub.arrivalTime), openHour, closeHour)) {
            System.out.println("Hub: " + hub.name);
            System.out.println("Arrival Time: " + hub.arrivalTime);
            System.out.println("Departure Time: " + hub.departureTime);
        } else {
            // Lógica para lidar com horário inválido
            System.out.println("Horário inválido para o hub " + hub.name);
        }
    }



    private boolean isTimeWithinRange(String time, int openHour, int closeHour) {
        try {
            // Parse a string no formato "hh:mm" para um objeto LocalTime
            LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

            // Converta os horários de abertura e fechamento para objetos LocalTime
            LocalTime openingTime = LocalTime.of(openHour, 0);
            LocalTime closingTime = LocalTime.of(closeHour, 0);

            // Verifique se o horário está no intervalo
            return !parsedTime.isBefore(openingTime) && !parsedTime.isAfter(closingTime);
        } catch (DateTimeParseException e) {
            // Lógica para lidar com horários em formato inválido
            return false;
        }
    }


    // Assuming USEI07_DTO class has a constructor that accepts a Hub object
    public List<USEI07_DTO> calculateCentrality(Graph<Location, Integer> graph) {
        List<USEI07_DTO> centralityList = new ArrayList<>();

        for (Location location : graph.vertices()) {
            int degreeCentrality = calculateDegreeCentrality(graph, location);

            LocalTime currentDepartureTime = LocalTime.of(9, 0);
            int travelTime = 30;
            int chargingTime = 60;

            USEI07_DTO.Hub hub = new USEI07_DTO.Hub(
                    location.getCode(),
                    degreeCentrality,
                    LocalTime.MIDNIGHT,
                    location.getSchedule().getOpenHour(),
                    location.getSchedule().getCloseHour(),
                    travelTime,
                    chargingTime
            );



            USEI07_DTO centralityInfo = new USEI07_DTO(hub);
            centralityList.add(centralityInfo);
        }

        return centralityList;
    }

    private LocalTime calculateArrivalTime(Location location, LocalTime departureTime, int travelTime) {
        // Obter o horário de funcionamento da localização
        Schedule schedule = location.getSchedule();
        LocalTime openingTime = LocalTime.ofSecondOfDay(schedule.getOpenHour());
        LocalTime closingTime = LocalTime.ofSecondOfDay(schedule.getCloseHour());

        // Calcular o horário de chegada
        LocalTime potentialArrivalTime = departureTime.plusMinutes(travelTime);

        // Verificar se o horário de chegada ultrapassa o horário de fechamento
        if (potentialArrivalTime.isAfter(closingTime)) {
            // Se ultrapassar, definir o horário de chegada como o horário de fechamento
            return closingTime;
        } else {
            // Caso contrário, manter o horário calculado
            return potentialArrivalTime;
        }
    }

    private LocalTime calculateDepartureTime(Location location, LocalTime arrivalTime, int chargingTime) {
        // Obter o horário de funcionamento da localização
        Schedule schedule = location.getSchedule();
        LocalTime openingTime = LocalTime.ofSecondOfDay(schedule.getOpenHour());
        LocalTime closingTime = LocalTime.ofSecondOfDay(schedule.getCloseHour());

        // Calcular o horário de partida
        LocalTime potentialDepartureTime = arrivalTime.plusMinutes(chargingTime);

        // Verificar se o horário de partida é anterior ao horário de abertura
        if (potentialDepartureTime.isBefore(openingTime)) {
            // Se for anterior, definir o horário de partida como o horário de abertura
            return openingTime;
        } else {
            // Caso contrário, manter o horário calculado
            return potentialDepartureTime;
        }
    }





    private int calculateDegreeCentrality(Graph<Location, Integer> graph, Location location) {
        // For directed graphs, degree centrality is the sum of in-degrees and out-degrees
        int inDegree = graph.inDegree(location);
        int outDegree = graph.outDegree(location);
        return inDegree + outDegree;
    }

    private List<Location> findOptimizedPath(Graph<Location, Integer> graph) {
        // Assuming your graph class has a method to get adjacent vertices of a vertex
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

    public static void main(String[] args) {
        USEI07UI usei07UI = new USEI07UI();
        usei07UI.run();
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
}
