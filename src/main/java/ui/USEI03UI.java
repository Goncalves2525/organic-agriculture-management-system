package ui;

import domain.Location;
import domain.USEI03_DTO;
import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;
import graphs.matrix.MatrixGraph;
import utils.AnsiColor;
import utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;


public class USEI03UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI03UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }


    public void run() {
        int autonomiaMax = Utils.readIntegerFromConsole("Qual é a autonomia do veículo? (em metros)");

        USEI03_DTO dto = getBiggestShortestPathData(autonomiaMax);

        boolean tripIsPossible = dto.isTripIsPossible();
        LinkedList<Location> shortPath = dto.getShortPath();
        ArrayList<Location> chargeLocations = dto.getChargeLocations();
        Integer pathLength = dto.getPathLength();
        int numberOfCharges = dto.getNumberOfCharges();
        int minimumAutonomy = dto.getMinimumAutonomy();
        Location origin = null;
        Location destination = null;


        if (shortPath != null){
            origin = shortPath.get(0);
            destination = shortPath.get(shortPath.size() - 1);
        }

        showShortestPath(tripIsPossible, shortPath, chargeLocations, pathLength, numberOfCharges, origin, destination, minimumAutonomy);
    }

    public USEI03_DTO getBiggestShortestPathData(Integer autonomiaMax) {
        MatrixGraph<Location, Integer> matrixGraphConverted = new MatrixGraph<>(gfh);
        MatrixGraph<Location, Integer> matrixGraph = Algorithms.minDistGraph1(matrixGraphConverted, Integer::compare, Integer::sum);

        Integer maxDistance = 0;
        Location origin = null;
        Location destination = null;
        Collection<Edge<Location, Integer>> allDistances = matrixGraph.edges();
        Utils.showMessageColor("matrix:", AnsiColor.BLUE);
        System.out.println(matrixGraph);
        for (Edge<Location, Integer> distance : allDistances) {
            if (distance.getWeight() > maxDistance) {
                maxDistance = distance.getWeight();
                origin = distance.getVOrig();
                destination = distance.getVDest();
            }
        }
        Utils.showMessageColor("Distância: ", AnsiColor.BLUE);
        System.out.println(maxDistance + "m");

        USEI03_DTO dto = getShortestPathData(origin, destination, autonomiaMax);

        return dto;
    }

    public USEI03_DTO getShortestPathData(Location origin, Location destination, int autonomiaMax) {
        LinkedList<Location> shortPath = new LinkedList<>();
        Integer pathLength = Algorithms.shortestPath(gfh, origin, destination, Integer::compare, Integer::sum, 0, shortPath);
        int autonomiaAtual = autonomiaMax;
        int numberOfCharges = 0;
        int distance = 0;
        int minimumAutonomy = 0;
        boolean tripIsPossible = true;
        ArrayList<Location> chargeLocations = new ArrayList<>();


        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location = shortPath.get(i);
            Location nextLocation = shortPath.get(i + 1);
            distance = gfh.edge(location, nextLocation).getWeight();
            if (distance > autonomiaMax) {
                tripIsPossible = false;
                if (distance > minimumAutonomy) {
                    minimumAutonomy = distance;
                }

            } else {
                if (autonomiaAtual - distance <= 0) {
                    chargeLocations.add(location);
                    numberOfCharges++;
                    autonomiaAtual = autonomiaMax;
                } else {
                    autonomiaAtual -= distance;
                }
            }
        }
        USEI03_DTO dto = new USEI03_DTO(tripIsPossible, shortPath, chargeLocations, pathLength, numberOfCharges, autonomiaMax, minimumAutonomy);
        return dto;
    }

    public LinkedList<Location> showShortestPath(boolean tripIsPossible, LinkedList<Location> shortPath, ArrayList<Location> chargeLocations, Integer pathLength, int numberOfCharges, Location origin, Location destination, int minimumAutonomy) {
        if (!tripIsPossible) {
            Utils.showMessageColor("\nO veículo não tem autonomia suficiente para efetuar a viagem. Precisava de ter pelo menos " + minimumAutonomy + "m de autonomia.", AnsiColor.RED);
            shortPath = null;
            return shortPath;
        }
        Utils.showMessageColor("Local de origem: ", AnsiColor.BLUE);
        System.out.println(origin.getCode());
        Utils.showMessageColor("\nLocal de destino: ", AnsiColor.BLUE);
        System.out.println(destination.getCode());
        Utils.showMessageColor("\nTrajeto: ", AnsiColor.BLUE);

        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location = shortPath.get(i);
            Location nextLocation = shortPath.get(i + 1);
            int weight = gfh.edge(location, nextLocation).getWeight();
            if (chargeLocations.contains(location)) {
                if (i == shortPath.size() - 2)
                    System.out.print(location.getCode() + " (Efetuou carregamento) --- " + weight + " ---> " + nextLocation.getCode());
                else
                    System.out.print(location.getCode() + " (Efetuou carregamento) --- " + weight + " ---> ");
            } else if (i == shortPath.size() - 2) {
                System.out.print(location.getCode() + " --- " + weight + " ---> " + nextLocation.getCode());
            } else {
                System.out.print(location.getCode() + " --- " + weight + " ---> ");
            }


        }
        Utils.showMessageColor("\n\nDistância total: ", AnsiColor.BLUE);
        System.out.println(pathLength + "m");
        Utils.showMessageColor("\nNúmero de carregamentos: ", AnsiColor.BLUE);
        System.out.println(numberOfCharges);
        Utils.showMessageColor("\nLocais de carregamento: ", AnsiColor.BLUE);
        for (Location location : chargeLocations) {
            System.out.println(location.getCode());
        }

        return shortPath;
    }
}

