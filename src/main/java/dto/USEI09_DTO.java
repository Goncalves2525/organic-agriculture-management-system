package dto;

import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;

import java.util.*;
import java.util.function.BinaryOperator;

public class USEI09_DTO {

    public static class Cluster<V> {
        private V hub;
        private Set<V> locations;

        public Cluster(V hub, Set<V> locations) {
            this.hub = hub;
            this.locations = locations;
        }

        public V getHub() {
            return hub;
        }

        public Set<V> getLocations() {
            return locations;
        }

        public void addLocation(V location) {
            locations.add(location);
        }
    }

    public static <V, E> List<Cluster<V>> clusterize(Graph<V, E> graph) {
        List<Cluster<V>> clusters = new ArrayList<>();

        while (graph.numEdges() > 0) { //O(E)
            Edge<V, Integer> edgeToRemove = findEdgeToRemove((Graph<V, Integer>) graph);
            if (edgeToRemove != null) {
                V vOrig = edgeToRemove.getVOrig();
                V vDest = edgeToRemove.getVDest();
                graph.removeEdge(vOrig, vDest);
            } else {
                break; // No more edges to remove
            }
        }

        // Identify clusters
        Set<V> visitedLocations = new HashSet<>(); // O(1)
        for (V vertex : graph.vertices()) { // O(V)
            if (!visitedLocations.contains(vertex)) {
                Cluster<V> cluster = exploreCluster(graph, vertex); // O(V + E)
                if (cluster != null) {
                    clusters.add(cluster);
                }
            }
        }

        return clusters;
    }


    private static <V> Edge<V, Integer> findEdgeToRemove(Graph<V, Integer> graph) {
        Edge<V, Integer> edgeToRemove = null;
        int maxShortestPaths = -1;

        // Iterate over the edges in the graph
        for (Edge<V, Integer> edge : graph.edges()) {
            // Create a modified graph without the current edge
            Graph<V, Integer> modifiedGraph = createModifiedGraph(graph, edge);

            // Find the vertices of the edge
            V vOrig = edge.getVOrig();
            V vDest = edge.getVDest();

            // Calculate the number of shortest paths in the modified graph
            int currentShortestPaths = calculateNumberOfShortestPaths(modifiedGraph, vOrig, vDest);

            // Check if the current edge provides shortest paths
            if (currentShortestPaths > maxShortestPaths) {
                maxShortestPaths = currentShortestPaths;
                edgeToRemove = edge;
            }
        }

        return edgeToRemove;
    }

    private static <V> Graph<V, Integer> createModifiedGraph(Graph<V, Integer> graph, Edge<V, Integer> edgeToRemove) {
        // Find the vertices of the edge
        V vOrig = edgeToRemove.getVOrig();
        V vDest = edgeToRemove.getVDest();

        // copy of the original graph
        Graph<V, Integer> modifiedGraph = graph.clone();

        // Remove the edge from the modified graph
        modifiedGraph.removeEdge(vOrig, vDest);

        return modifiedGraph;
    }


    private static <V, E> int calculateNumberOfShortestPaths(Graph<V, Integer> graph, V vOrig, V vDest) {
        List<V> shortPath = new LinkedList<>();
        Integer distance = shortestPath(graph, vOrig, vDest, shortPath);

        if (distance != null) {
            double distanceDouble = distance.doubleValue();

            if (!shortPath.isEmpty()) {
                // Process the shortest path or perform additional actions as needed
                System.out.println("Shortest Path: " + shortPath);
                System.out.println("Shortest Distance: " + distanceDouble);
            }

            // Return the calculated number of shortest paths
            return 0; // Replace with your logic
        }

        // Handle the case when distance is null (e.g., no path found)
        return -1; // Or another appropriate value indicating no valid path
    }

    public static <V> Integer shortestPath(Graph<V, Integer> g, V vOrig, V vDest, List<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        if (vOrig.equals(vDest)) {
            shortPath.add(vOrig);
            return 0; // Assuming the graph deals with Integer weights
        }

        shortPath.clear();
        boolean[] visited = new boolean[g.numVertices()];
        V[] pathKeys = (V[]) new Object[g.numVertices()];
        Integer[] dist = new Integer[g.numVertices()];  // Change to Integer

        Algorithms.shortestPathDijkstra(
                g,
                vOrig,
                (d1, d2) -> {
                    int index1 = g.key((V) d1);
                    int index2 = g.key((V) d2);

                    // Check if either vertex is not found
                    if (index1 == -1 || index2 == -1) {
                        return 0; // or another appropriate value for vertices not found
                    }

                    return Integer.compare(dist[index1], dist[index2]);
                },
                Integer::sum,
                0,
                visited,
                pathKeys,
                dist
        );


        getPath(g, vOrig, vDest, pathKeys, shortPath);

        return dist[g.key(vDest)];
    }




    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] pathKeys, List<V> path) {
        Collection<V> adjVertices = g.adjVertices(vDest);

        if (adjVertices == null || adjVertices.isEmpty()) {
            return;
        }

        if (vDest != null) {
            path.add(0, vDest);

            if (g.key(vOrig) != g.key(vDest)) {
                int index = g.key(vDest);
                vDest = pathKeys[index];
                getPath(g, vOrig, vDest, pathKeys, path);
            }
        }
    }




    public static <V, E> Cluster<V> exploreCluster(Graph<V, E> graph, V startVertex) {
        Set<V> visitedLocations = new HashSet<>();
        LinkedList<V> queue = new LinkedList<>();
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            V currentVertex = queue.poll();

            // Skip if already visited
            if (visitedLocations.contains(currentVertex)) {
                continue;
            }

            visitedLocations.add(currentVertex);

            // Process the current vertex (e.g., add it to the cluster)

            // Add adjacent vertices to the queue for further exploration
            for (V neighbor : graph.adjVertices(currentVertex)) {
                if (!visitedLocations.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        // Create and return a Cluster object based on the visited locations
        return new Cluster<>(startVertex, visitedLocations);
    }
}
