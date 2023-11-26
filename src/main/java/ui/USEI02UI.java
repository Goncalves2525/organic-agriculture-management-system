package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.Algorithms;
import graphs.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class USEI02UI implements Runnable {

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI02UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    @Override
    public void run() {
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        // Calcular a influência
        Map<Location, Integer> influenceMap = Algorithms.calculateInfluence(gfh);

        // Classificar os vértices com base nas métricas
        List<Location> sortedLocationsByInfluence = sortLocationsByMetric(influenceMap, Comparator.reverseOrder());

        System.out.println("Influence:");
        sortedLocationsByInfluence.forEach(location -> System.out.println(location + " - " + influenceMap.get(location)));

        // Choose a source vertex for proximity calculation
        Location sourceVertex = chooseSourceByInfluence(gfh);; // Choose a suitable source vertex

        // Calculate proximity (average distance) from the source vertex to all other vertices
        List<Double> proximities = calculateProximities(gfh, sourceVertex);

        // Display the proximities
        System.out.println("Proximities:");
        for (int i = 0; i < gfh.numVertices(); i++) {
            Location vertex = gfh.vertex(i);
            double proximity = proximities.get(i);
            System.out.println("Proximity of " + vertex + " to other vertices: " + proximity);
        }

        System.out.println("Centrality:");

        ArrayList<LinkedList<Location>> paths = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        Algorithms.shortestPaths(gfh, sourceVertex, Comparator.naturalOrder(), Integer::sum, 0, paths, distances);

//        for (Location vertex : gfh.vertices()) {
//            int index = gfh.key(vertex);
//            System.out.println("Vertex: " + vertex + ", Betweenness Centrality: " + distances.get(index));
//        }

        // Create a list of vertices with their centrality and influence measures
        List<LocationCentralityInfo> centralityInfoList = new ArrayList<>();
        for (Location vertex : gfh.vertices()) {
            int index = gfh.key(vertex);
            centralityInfoList.add(new LocationCentralityInfo(vertex, distances.get(index)));
        }

        // Order the list by centrality in descending order
        centralityInfoList.sort(Comparator.comparingInt(LocationCentralityInfo::getCentrality).reversed());

        // Now, you have the list ordered by decreasing centrality
        for (LocationCentralityInfo info : centralityInfoList) {
            System.out.println("Vertex: " + info.getVertex() + ", Centrality: " + info.getCentrality());
        }
    }

    private static class LocationCentralityInfo {
        private Location vertex;
        private int centrality;

        public LocationCentralityInfo(Location vertex, int centrality) {
            this.vertex = vertex;
            this.centrality = centrality;
        }

        public Location getVertex() {
            return vertex;
        }

        public int getCentrality() {
            return centrality;
        }
    }

    private Location chooseSourceByInfluence(Graph<Location, Integer> graph) {
        int maxDegree = -1;
        Location sourceVertex = null;

        for (Location vertex : graph.vertices()) {
            int degree = graph.outDegree(vertex); // Assuming outDegree represents influence
            if (degree > maxDegree) {
                maxDegree = degree;
                sourceVertex = vertex;
            }
        }

        return sourceVertex;
    }


    private List<Location> getDegreeSortedLocations(Graph<Location, Integer> graph) {
        return graph.vertices().stream()
                .sorted(Comparator.comparingInt(graph::outDegree).reversed())
                .collect(Collectors.toList());
    }

    private List<Location> getCentralitySortedLocations(Graph<Location, Integer> graph) {
        return graph.vertices().stream()
                .sorted(Comparator.comparingInt(v -> {
                    ArrayList<LinkedList<Location>> paths = Algorithms.allPaths(graph, (Location) v, null);
                    return paths.size();
                }).reversed())
                .collect(Collectors.toList());
    }

    private <T> List<Location> sortLocationsByMetric(Map<Location, T> metricMap, Comparator<? super T> comparator) {
        return metricMap.entrySet().stream()
                .sorted(Map.Entry.<Location, T>comparingByValue(comparator))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Double> calculateProximities(Graph<Location, Integer> graph, Location sourceVertex) {
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
