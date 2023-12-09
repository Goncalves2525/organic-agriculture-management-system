package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.Algorithms;
import graphs.Graph;

import java.util.*;
import java.util.stream.Collectors;
import dto.USEI02_DTO;

public class USEI02UI implements Runnable {

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI02UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        // Calculate and display influence
        displayInfluence(gfh);

        // Choose a source vertex for proximity calculation
        Location sourceVertex = chooseSourceByInfluence(gfh);

        // Calculate and display proximities
        displayProximities(gfh, sourceVertex);

        // Calculate and display centrality
        displayCentrality(gfh, sourceVertex);
    }

    public void displayInfluence(Graph<Location, Integer> graph) {
        Map<Location, Integer> influenceMap = Algorithms.calculateInfluence(graph);
        List<Location> sortedLocationsByInfluence = sortLocationsByMetric(influenceMap, Comparator.reverseOrder());

        System.out.println("\nInfluence:\n");
        sortedLocationsByInfluence.forEach(location -> System.out.println("Vertex: " + location + " - influence: " + influenceMap.get(location)));
    }

    public void displayProximities(Graph<Location, Integer> graph, Location sourceVertex) {
        List<Double> proximities = calculateProximities(graph, sourceVertex);

        System.out.println("\nProximities:\n");
        List<Map.Entry<Location, Double>> sortedProximities = sortProximities(graph, proximities);

        // Display the sorted proximities
        for (Map.Entry<Location, Double> entry : sortedProximities) {
            Location vertex = entry.getKey();
            double proximity = entry.getValue();
            System.out.println("Vertex: " + vertex +  " - proximity to other vertices: " + proximity);
        }
    }

    public void displayCentrality(Graph<Location, Integer> graph, Location sourceVertex) {
        ArrayList<LinkedList<Location>> paths = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        Algorithms.shortestPaths(graph, sourceVertex, Comparator.naturalOrder(), Integer::sum, 0, paths, distances);

        // Create and display a list of vertices with their centrality and influence measures
        List<USEI02_DTO> centralityInfoList = buildCentralityInfoList(graph, distances);

        System.out.println("\nCentrality:\n");
        centralityInfoList.forEach(info -> System.out.println("Vertex: " + info.getVertex() + " - centrality: " + info.getCentrality()));
    }

    public List<Map.Entry<Location, Double>> sortProximities(Graph<Location, Integer> graph, List<Double> proximities) { //O(n log n)
        List<Map.Entry<Location, Double>> sortedProximities = new ArrayList<>();

        for (int i = 0; i < graph.numVertices(); i++) {
            Location vertex = graph.vertex(i);
            double proximity = proximities.get(i);
            sortedProximities.add(new AbstractMap.SimpleEntry<>(vertex, proximity));
        }

        // Sort the list based on proximity in descending order
        sortedProximities.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

        return sortedProximities;
    }

    public List<USEI02_DTO> buildCentralityInfoList(Graph<Location, Integer> graph, ArrayList<Integer> distances) { //O(n)
        List<USEI02_DTO> centralityInfoList = new ArrayList<>();
        for (Location vertex : graph.vertices()) {
            int index = graph.key(vertex);
            centralityInfoList.add(new USEI02_DTO(vertex, distances.get(index)));
        }

        // Order the list by centrality in descending order
        centralityInfoList.sort(Comparator.comparingInt(USEI02_DTO::getCentrality).reversed());

        return centralityInfoList;
    }

    public static Location chooseSourceByInfluence(Graph<Location, Integer> graph) { //O(n)
        int maxDegree = -1;
        Location sourceVertex = null;

        for (Location vertex : graph.vertices()) {
            int degree = graph.outDegree(vertex);
            if (degree > maxDegree) {
                maxDegree = degree;
                sourceVertex = vertex;
            }
        }

        return sourceVertex;
    }



    public static <T> List<Location> sortLocationsByMetric(Map<Location, T> metricMap, Comparator<? super T> comparator) { //O(n log n)
        return metricMap.entrySet().stream()
                .sorted(Map.Entry.<Location, T>comparingByValue(comparator))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Double> calculateProximities(Graph<Location, Integer> graph, Location sourceVertex) { // O(n^2)
        List<Double> proximities = new LinkedList<>();

        for (Location vertex : graph.vertices()) {
            if (!vertex.equals(sourceVertex)) {
                double proximity = Algorithms.shortestPath(graph, sourceVertex, vertex, Comparator.naturalOrder(), Integer::sum, 0, new LinkedList<>());
                proximities.add(proximity);
            } else {
                // Proximity from the vertex to itself is 0
                proximities.add(0.0);
            }
        }

        return proximities;
    }

}
