package graphs;

import graphs.map.MapGraph;
import graphs.matrix.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * @author DEI-ISEP, adapted thourghout ESINF classes
 */
public class Algorithms {


    ///// GRAPH TRAVERSALS /////////////////////////////////////////////////////////////////////////////////////////////

    /** OK & TESTED **/
    /**
     * Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g    Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        if (g.key(vert) < 0) {
            return null;
        }

        LinkedList<V> qbfs = new LinkedList<>();
        LinkedList<V> qaux = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        qbfs.add(vert);
        qaux.add(vert);
        visited[g.key(vert)] = true;

        while (!qaux.isEmpty()) {
            vert = qaux.removeFirst();
            for (V vAdj : g.adjVertices(vert)) {
                if (!visited[g.key(vAdj)]) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
                    visited[g.key(vAdj)] = true;
                }
            }
        }

        return qbfs;
    }

    /** OK & TESTED **/
    /**
     * Performs depth-first search starting in a vertex
     *
     * @param g       Graph instance
     * @param vOrig   vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs    return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        if (visited[g.key(vOrig)]) {
            return;
        }

        qdfs.add(vOrig);
        visited[g.key(vOrig)] = true;

        for (V vAdj : g.adjVertices(vOrig)) {
            DepthFirstSearch(g, vAdj, visited, qdfs);
        }
    }

    /** OK & TESTED **/
    /**
     * Performs depth-first search starting in a vertex
     *
     * @param g    Graph instance
     * @param vert vertex of graph g that will be the source of the search
     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {

        if (g.numVertices() <= 0 || !g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> qdfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, qdfs);

        return qdfs;
    }


    ///// PATHS ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** OK **/
    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        path.push(vOrig);
        visited[g.key(vOrig)] = true;
        for (V vAdj : g.adjVertices(vOrig)) {
            if (vAdj == vDest) {
                path.push(vDest);
                paths.add(path);
                path.pop();
            } else {
                if (visited[g.key(vAdj)]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }
            }
            path.pop();
        }
    }

    /** OK **/
    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        if (g.numVertices() <= 0) {
            return null;
        }

        boolean[] visited = new boolean[g.numVertices()];
        LinkedList<V> path = new LinkedList<>();
        ArrayList<LinkedList<V>> paths = new ArrayList<>();

        allPaths(g, vOrig, vDest, visited, path, paths);

        return paths;
    }

    /** OK **/
    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V[] pathKeys, E[] dist) { // O(V^2)

        Iterable<V> verticesIterator = g.vertices(); // O(V)

        for (V v : verticesIterator) { // O(V)
            int index = g.key(v); // O(1)
            visited[index] = false; // O(1)
            pathKeys[index] = null; // O(1)
            dist[index] = null; // O(1)
        }

        dist[g.key(vOrig)] = zero; // O(1)

        int vOrigAux = g.key(vOrig); // O(1)
        while (vOrigAux != -1) { // O(V^2)
            visited[vOrigAux] = true; // O(1)
            vOrig = g.vertex(vOrigAux); // O(1)
            verticesIterator = g.adjVertices(vOrig); // O(V)
            for (V vertice : verticesIterator) {  // O(V)
                Edge<V, E> edge = g.edge(vOrig, vertice); // O(1)
                int aux = g.key(vertice); // O(1)

                if (!visited[aux] && (dist[aux] == null || ce.compare(dist[aux], sum.apply(dist[g.key(vOrig)], edge.getWeight())) > 0)) { // O(1)
                    dist[aux] = sum.apply(dist[g.key(vOrig)], edge.getWeight()); // O(1)
                    pathKeys[aux] = vOrig; // O(1)

                }
            }

            vOrigAux = getVertMinDist(dist, visited, ce); // O(V)
        }
    }

    private static <E> int getVertMinDist(E[] dist, boolean[] visited, Comparator<E> ce) { // O(V)
        E mindist = null; // O(1)
        Integer indice = -1; // O(1)
        for (int i = 0; i < dist.length; i++) { // O(V)
            if (!visited[i] && (dist[i] != null) && ((mindist == null) || ce.compare(dist[i], mindist) < 0)) { // O(1)
                indice = i; // O(1)
                mindist = dist[i]; // O(1)
            }
        }
        return indice; // O(1)
    }

    /** OK **/
    /**
     * Auxiliary method to shortestPathDijkstra
     *
     * @param g
     * @param vOrig
     * @param dist
     * @param visited
     * @param ce
     * @param zero
     * @param <V>
     * @param <E>
     * @return indice for the vertex of minimum distance
     */
    private static <V, E> Integer getVertMinDist(Graph<V, E> g, V vOrig, E[] dist, boolean[] visited, Comparator<E> ce, E zero) {
        E minDist = zero;
        Integer indice = -1;

        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && (dist[i] != null) && ((minDist == null) ||
                    ce.compare(dist[i], minDist) > 0)) {
                indice = i;
                minDist = dist[i];
            }
        }

        return indice;
    }

    /** NOT-OK **/ //TODO: corrigir!

    /**
     * Shortest-path between two vertices
     *
     * @param g         graph
     * @param vOrig     origin vertex
     * @param vDest     destination vertex
     * @param ce        comparator between elements of type E
     * @param sum       sum two elements of type E
     * @param zero      neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) { // O(V^2)

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) { // O(1)
            return null; // O(1)
        }

        if (vOrig == vDest) { // O(1)
            shortPath.push(vOrig); // O(1)
            return zero; // O(1)
        }

        shortPath.clear(); // O(1)
        boolean[] visited = new boolean[g.numVertices()]; // O(1)


        E[] dist = (E[]) new Object[g.numVertices()]; // O(1)

        V[] pathKeys = (V[]) new Object[g.numVertices()]; // O(1)

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist); // O(V^2)

        getPath(g, vOrig, vDest, pathKeys, shortPath); // O(V)

        return shortPath.isEmpty() ? null : dist[g.key(vDest)]; // O(1)
    }

    /** OK **/
    /**
     * Shortest-path between a vertex and all other vertices
     *
     * @param g     graph
     * @param vOrig start vertex
     * @param ce    comparator between elements of type E
     * @param sum   sum two elements of type E
     * @param zero  neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        int numVerts = g.numVertices();
        boolean[] visited = new boolean[g.numVertices()];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[g.numVertices()];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[g.numVertices()];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        dists.clear();
        paths.clear();

        for (int i = 0; i < numVerts; i++) {
            paths.add(null);
            dists.add(null);
        }

        for (V vertDest : g.vertices()) {
            int i = g.key(vertDest);
            if (dist[i] != null) {
                LinkedList<V> shortPath = new LinkedList<>();
                getPath(g, vOrig, vertDest, pathKeys, shortPath);
                paths.set(i, shortPath);
                dists.set(i, dist[i]);
            }
        }

        return true;
    }

    /** OK **/
    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V[] pathKeys, LinkedList<V> path) { // O(V)

        if (g.adjVertices(vDest).isEmpty()) { // O(1)
            return; // O(1)
        }

        if (vDest != null) { // O(1)
            path.addFirst(vDest); // O(1)

            if (g.key(vOrig) != g.key(vDest)) { // O(1)
                int index = g.key(vDest); // O(1)
                vDest = pathKeys[index]; // O(1)
                getPath(g, vOrig, vDest, pathKeys, path); // O(V)
            }
        }

    }

    ///// FLOYD-WARSHALL ///////////////////////////////////////////////////////////////////////////////////////////////
    public static <V, E> MatrixGraph<V, E> minDistGraph1(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum) { // O(V^3)
        int numVerts = g.numVertices(); // O(1)
        if (numVerts == 0) { // O(1)
            return null; // O(1)
        }

        Graph<V, E> g2 = g.clone(); // O(V^2)

        E[][] edges = (E[][]) new Object[numVerts][numVerts]; // O(1)

        for (int i = 0; i < numVerts; i++) { // O(V^2)
            for (int j = 0; j < numVerts; j++) { // O(V)
                Edge<V,E> edge = g2.edge(i, j); // O(1)
                if (edge != null){ // O(1)
                    edges[i][j] = edge.getWeight(); // O(1)
                }
            }
        }

        for (int k = 0; k < numVerts; k++) { // O(V^3)
            for (int i = 0; i < numVerts; i++) { // O(V)
                if (i != k && edges[i][k] != null) { // O(1)
                    for (int j = 0; j < numVerts; j++) { // O(V)
                        if (j != i && j != k && edges[k][j] != null) { // O(1)
                            E s = sum.apply(edges[i][k], edges[k][j]); // O(1)
                            if ((edges[i][j] == null) || ce.compare(edges[i][j], s) > 0) { // O(1)
                                edges[i][j] = s; // O(1)
                            }
                        }
                    }
                }
            }
        }

        return new MatrixGraph<>(false, g.vertices(), edges); // O(V^3)
    }


    public static <V, E> MatrixGraph<V, E> minDistGraph2(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        int numVerts = g.numVertices();
        if (numVerts == 0) {
            return null;
        }

        MatrixGraph<V, E> g2 = new MatrixGraph<>(g.isDirected());

        // Initialize the matrix graph with vertices
        for (V vertex : g.vertices()) {
            g2.addVertex(vertex);
        }

        // Initialize the matrix graph with edges and weights
        for (Edge<V, E> edge : g.edges()) {
            V vOrig = edge.getVOrig();
            V vDest = edge.getVDest();
            E weight = edge.getWeight();

            if (weight != null) {
                g2.addEdge(vOrig, vDest, weight);
            }
        }

        // Apply the Floyd-Warshall algorithm
        for (int k = 0; k < numVerts; k++) {
            for (int i = 0; i < numVerts; i++) {
                if (i != k && g2.edge(i, k) != null) {
                    for (int j = 0; j < numVerts; j++) {
                        if (j != i && j != k && g2.edge(k, j) != null) {
                            E s = sum.apply(g2.edge(i, k).getWeight(), g2.edge(k, j).getWeight());
                            Edge<V, E> edgeIJ = g2.edge(i, j);

                            // Check if the edgeIJ is null or the new weight is smaller
                            if (edgeIJ == null || ce.compare(edgeIJ.getWeight(), s) > 0) {
                                g2.addEdge(g2.vertex(i), g2.vertex(j), s);
                            }
                        }
                    }
                }
            }
        }

        return g2;
    }


    ///// CIRCUIT EULER: HIERHOLZER

    // todo: circuit euler


    ///// MINIMUM SPANNING TREE ////////////////////////////////////////////////////////////////////////////////////////

    /** OK **/
    /**
     * Minimum spanning tree - Kruskal Algorithm
     *
     * @return minimum spanning tree
     */
    public static <V, E extends Comparable<E>> Graph<V, E> kruskalAlgorithm(Graph<V, E> g) {
        Graph<V, E> mst = new MapGraph<>(false);
        List<Edge<V, E>> lstEdges = new ArrayList<>();

        for (V vertex : g.vertices()) {
            mst.addVertex(vertex);
        }

        for (Edge<V, E> edge : g.edges()) {
            lstEdges.add(edge);
        }

        // Sort edges using a comparator
        lstEdges.sort(Comparator.comparing(Edge::getWeight));

        for (Edge<V, E> edge : lstEdges) {
            V verticeOrig = edge.getVOrig();
            V verticeDest = edge.getVDest();
            E edgeWeight = edge.getWeight();

            // Check if adding the edge creates a cycle
            if (!createsCycle(mst, verticeOrig, verticeDest)) {
                mst.addEdge(verticeOrig, verticeDest, edgeWeight);
            }
        }

        return mst;
    }

    private static <V, E> boolean createsCycle(Graph<V, E> graph, V vOrig, V vDest) {
        LinkedList<V> visitedVertices = new LinkedList<>();
        visitedVertices.add(vOrig);

        return createsCycleHelper(graph, vOrig, vDest, visitedVertices);
    }

    private static <V, E> boolean createsCycleHelper(Graph<V, E> graph, V currentVertex, V targetVertex, LinkedList<V> visitedVertices) {
        if (currentVertex.equals(targetVertex)) {
            return true; // Found a cycle
        }

        for (V adjacentVertex : graph.adjVertices(currentVertex)) {
            if (!visitedVertices.contains(adjacentVertex)) {
                visitedVertices.add(adjacentVertex);

                if (createsCycleHelper(graph, adjacentVertex, targetVertex, visitedVertices)) {
                    return true; // Found a cycle
                }

                visitedVertices.removeLast();
            }
        }

        return false;
    }

    /**
     * Minimum spanning tree - PRIM Algorithm
     *
     * @return minimum spanning tree
     */
//    public static <V, E> MatrixGraph<V, E> mstPRIM(Graph<V, E> g) {
//
//        boolean[] visited = new boolean[g.numVertices()];
//        ArrayList<LinkedList<V>> path =  new ArrayList<>();
//        ArrayList<E> dist = new ArrayList<>();
//
//        for (V vertice : g.vertices()) {
//            dist.set(g.key(vertice), null);
//            path.set(g.key(vertice), null);
//            visited[g.key(vertice)] = false;
//        }
//
//        g.vertex(0);
//        vOrig
//    }

    ///// TOPOLOGICAL SORT /////////////////////////////////////////////////////////////////////////////////////////////

    // todo: kahns algorithm


    ///// GRAPH COLORING ///////////////////////////////////////////////////////////////////////////////////////////////

    // todo: welsh-powell

    // todo: d-satur


    ///// MAXIMUM NETWORK FLOW /////////////////////////////////////////////////////////////////////////////////////////

    // todo: ford-fulkerson

    public static <V, E> int calculatePathWeight(Graph<V, E> g, LinkedList<V> path) {
        int weight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            V currentVertex = path.get(i);
            V nextVertex = path.get(i + 1);
            Edge<V, E> edge = g.edge(currentVertex, nextVertex);
            if (edge != null) {
                weight += (int) edge.getWeight();
            }
        }
        return weight;
    }

    public static <V, E> boolean hamiltonianPathUtil(Graph<V, E> g, boolean[] visited, LinkedList<V> path, int pos) {
        if (pos == g.numVertices()) {
            return true;
        }

        V lastVertex = path.getLast();

        for (V v : g.vertices()) {
            int key = g.key(v);
            if (!visited[key]) {
                if (pos == 0 || g.edge(lastVertex, v) != null) {
                    visited[key] = true;
                    path.addLast(v);

                    if (hamiltonianPathUtil(g, visited, path, pos + 1)) {
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