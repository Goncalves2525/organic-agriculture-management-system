package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.Algorithms;
import graphs.Graph;
import graphs.matrix.MatrixGraph;

import java.util.Comparator;

public class USEI04UI implements Runnable{

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI04UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    @Override
    public void run() {
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        // Print the original graph
        System.out.println("Original Graph:");
        System.out.println(gfh.toString());

        ResultInfo resultInfo = minConnectionNetwork(gfh);

        // Print the minimum connection network
        System.out.println("\nMinimum Connection Network:");
        System.out.println(resultInfo);
    }

    private ResultInfo minConnectionNetwork(Graph<Location, Integer> gfh) {
        // Use the minDistGraph method from Algorithms class
        MatrixGraph<Location, Integer> minDistGraph = Algorithms.minDistGraph2(gfh, Comparator.naturalOrder(), Integer::sum);

        // Calculate the total distance
        int totalDistance = minDistGraph.edges().stream().mapToInt(edge -> edge.getWeight()).sum();

        // Create ResultInfo object to hold the result
        return new ResultInfo(minDistGraph, totalDistance);
    }

    private static class ResultInfo {
        private final MatrixGraph<Location, Integer> minDistGraph;
        private final int totalDistance;

        public ResultInfo(MatrixGraph<Location, Integer> minDistGraph, int totalDistance) {
            this.minDistGraph = minDistGraph;
            this.totalDistance = totalDistance;
        }

        @Override
        public String toString() {
            return "Locations: " + minDistGraph.vertices() +
                    "\nDistances: " + minDistGraph.edges() +
                    "\nTotal Distance: " + totalDistance;
        }
    }
}
