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
                                                    boolean[] visited, V[] pathKeys, E[] dist) {

        Iterable<V> verticesIterator = g.vertices();

        for (V v : verticesIterator) {
            int index = g.key(v);
            visited[index] = false;
            pathKeys[index] = null;
            dist[index] = null;
        }

        dist[g.key(vOrig)] = zero;

        int vOrigAux = g.key(vOrig);
        while (vOrigAux != -1) {
            visited[vOrigAux] = true;
            vOrig = g.vertex(vOrigAux);
            verticesIterator = g.adjVertices(vOrig);
            for (V vertice : verticesIterator) {
                Edge<V, E> edge = g.edge(vOrig, vertice);
                int aux = g.key(vertice);

                if (!visited[aux] && (dist[aux] == null || ce.compare(dist[aux], sum.apply(dist[g.key(vOrig)], edge.getWeight())) > 0)) {
                    dist[aux] = sum.apply(dist[g.key(vOrig)], edge.getWeight());
                    pathKeys[aux] = vOrig;

                }
            }

            vOrigAux = getVertMinDist(dist, visited, ce);
        }
    }

    private static <E> int getVertMinDist(E[] dist, boolean[] visited, Comparator<E> ce) {
        E mindist = null;
        Integer indice = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && (dist[i] != null) && ((mindist == null) || ce.compare(dist[i], mindist) < 0)) {
                indice = i;
                mindist = dist[i];
            }
        }
        return indice;
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
                                        LinkedList<V> shortPath) {

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        if (vOrig == vDest) {
            shortPath.push(vOrig);
            return zero;
        }

        shortPath.clear();
        boolean[] visited = new boolean[g.numVertices()];

        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[g.numVertices()];
        @SuppressWarnings("unchecked")
        V[] pathKeys = (V[]) new Object[g.numVertices()];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        getPath(g, vOrig, vDest, pathKeys, shortPath);

        return shortPath.isEmpty() ? null : dist[g.key(vDest)];
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
                                       V[] pathKeys, LinkedList<V> path) {

        if (g.adjVertices(vDest).isEmpty()) {
            return;
        }

        if (vDest != null) {
            path.addFirst(vDest);

            if (g.key(vOrig) != g.key(vDest)) {
                int index = g.key(vDest);
                vDest = pathKeys[index];
                getPath(g, vOrig, vDest, pathKeys, path);
            }
        }

    }

    ///// FLOYD-WARSHALL ///////////////////////////////////////////////////////////////////////////////////////////////
    public static <V, E> MatrixGraph<V, E> minDistGraph1(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        int numVerts = g.numVertices();
        if (numVerts == 0) {
            return null;
        }

        Graph<V, E> g2 = g.clone();

        E[][] edges = (E[][]) new Object[numVerts][numVerts];

        for (int i = 0; i < numVerts; i++) {
            for (int j = 0; j < numVerts; j++) {
                Edge<V,E> edge = g2.edge(i, j);
                if (edge != null){
                    edges[i][j] = edge.getWeight();
                }
            }
        }

        for (int k = 0; k < numVerts; k++) {
            for (int i = 0; i < numVerts; i++) {
                if (i != k && edges[i][k] != null) {
                    for (int j = 0; j < numVerts; j++) {
                        if (j != i && j != k && edges[k][j] != null) {
                            E s = sum.apply(edges[i][k], edges[k][j]);
                            if ((edges[i][j] == null) || ce.compare(edges[i][j], s) > 0) {
                                edges[i][j] = s;
                            }
                        }
                    }
                }
            }
        }

        return new MatrixGraph<>(false, g.vertices(), edges);
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
        LinkedList<V> connectedVertices;

        for (V vertice : g.vertices()) {
            mst.addVertex(vertice);
        }

        for (Edge<V, E> edge : g.edges()) {
            lstEdges.add(edge);
        }

        //sort:
        lstEdges.sort(null);

        for (Edge e : lstEdges) {
            V verticeOrig = (V) e.getVOrig();
            V verticeDest = (V) e.getVDest();
            E edgeWeight = (E) e.getWeight();
            connectedVertices = DepthFirstSearch(mst, verticeOrig);
            if (!connectedVertices.contains(verticeDest)) {
                mst.addEdge(verticeOrig, verticeDest, edgeWeight);
            }
        }
        return mst;
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
}