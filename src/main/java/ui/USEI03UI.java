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

    public USEI03_DTO getBiggestShortestPathData(Integer autonomiaMax) { //O(V^3)
        if (autonomiaMax == null) { //O(1)
            return null; //O(1)
        }
        MatrixGraph<Location, Integer> matrixGraphConverted = new MatrixGraph<>(gfh); //O(1)
        MatrixGraph<Location, Integer> matrixGraph = Algorithms.minDistGraph1(matrixGraphConverted, Integer::compare, Integer::sum); //O(V^3)

        Integer maxDistance = 0; //O(1)
        Location origin = null; //O(1)
        Location destination = null; //O(1)
        Collection<Edge<Location, Integer>> allDistances = matrixGraph.edges(); //O(V^2)
        for (Edge<Location, Integer> distance : allDistances) { //O(V^2)
            if (distance.getWeight() > maxDistance) { //O(1)
                maxDistance = distance.getWeight(); //O(1)
                origin = distance.getVOrig(); //O(1)
                destination = distance.getVDest(); //O(1)
            }
        }

        USEI03_DTO dto = getShortestPathData(origin, destination, autonomiaMax); //O(V^2)

        return dto; //O(1)
    }

    public USEI03_DTO getShortestPathData(Location origin, Location destination, int autonomiaMax) { //O(V^2)
        if (origin == null || destination == null) { //O(1)
            return null; //O(1)
        }
        LinkedList<Location> shortPath = new LinkedList<>(); //O(1)
        Integer pathLength = Algorithms.shortestPath(gfh, origin, destination, Integer::compare, Integer::sum, 0, shortPath); //O(V^2)
        int autonomiaAtual = autonomiaMax; //O(1)
        int numberOfCharges = 0; //O(1)
        int distance = 0; //O(1)
        int minimumAutonomy = 0; //O(1)
        boolean tripIsPossible = true; //O(1)
        ArrayList<Location> chargeLocations = new ArrayList<>(); //O(1)


        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location = shortPath.get(i); //O(1)
            Location nextLocation = shortPath.get(i + 1); //O(1)
            distance = gfh.edge(location, nextLocation).getWeight(); //O(1)
            if (distance > autonomiaMax) { //O(1)
                tripIsPossible = false; //O(1)
                if (distance > minimumAutonomy) { //O(1)
                    minimumAutonomy = distance; //O(1)
                }

            } else {
                if (autonomiaAtual - distance <= 0) { //O(1)
                    chargeLocations.add(location); //O(1)
                    numberOfCharges++; //O(1)
                    autonomiaAtual = autonomiaMax; //O(1)
                } else {
                    autonomiaAtual -= distance; //O(1)
                }
            }
        }
        USEI03_DTO dto = new USEI03_DTO(tripIsPossible, shortPath, chargeLocations, pathLength, numberOfCharges, autonomiaMax, minimumAutonomy); //O(1)
        return dto; //O(1)
    }

    public LinkedList<Location> showShortestPath(boolean tripIsPossible, LinkedList<Location> shortPath, ArrayList<Location> chargeLocations, Integer pathLength, int numberOfCharges, Location origin, Location destination, int minimumAutonomy) { //O(V)
        if (!tripIsPossible) { //O(1)
            Utils.showMessageColor("\nO veículo não tem autonomia suficiente para efetuar a viagem. Precisava de ter pelo menos " + minimumAutonomy + "m de autonomia.", AnsiColor.RED); //O(1)
            shortPath = null; //O(1)
            return shortPath; //O(1)
        }
        Utils.showMessageColor("Local de origem: ", AnsiColor.BLUE); //O(1)
        System.out.println(origin.getCode()); //O(1)
        Utils.showMessageColor("\nLocal de destino: ", AnsiColor.BLUE); //O(1)
        System.out.println(destination.getCode()); //O(1)
        Utils.showMessageColor("\nTrajeto: ", AnsiColor.BLUE); //O(1)

        for (int i = 0; i < shortPath.size() - 1; i++) { //O(V)
            Location location = shortPath.get(i); //O(1)
            Location nextLocation = shortPath.get(i + 1); //O(1)
            int weight = gfh.edge(location, nextLocation).getWeight(); //O(1)
            if (chargeLocations.contains(location)) { //O(1)
                if (i == shortPath.size() - 2) //O(1)
                    System.out.print(location.getCode() + " (Efetuou carregamento) --- " + weight + " ---> " + nextLocation.getCode()); //O(1)
                else
                    System.out.print(location.getCode() + " (Efetuou carregamento) --- " + weight + " ---> "); //O(1)
            } else if (i == shortPath.size() - 2) { //O(1)
                System.out.print(location.getCode() + " --- " + weight + " ---> " + nextLocation.getCode()); //O(1)
            } else {
                System.out.print(location.getCode() + " --- " + weight + " ---> "); //O(1)
            }


        }
        Utils.showMessageColor("\n\nDistância total: ", AnsiColor.BLUE); //O(1)
        System.out.println(pathLength + "m"); //O(1)
        Utils.showMessageColor("\nNúmero de carregamentos: ", AnsiColor.BLUE); //O(1)
        System.out.println(numberOfCharges); //O(1)
        Utils.showMessageColor("\nLocais de carregamento: ", AnsiColor.BLUE); //O(1)
        for (Location location : chargeLocations) { //O(V)
            System.out.println(location.getCode()); //O(1)
        }

        return shortPath; //O(1)
    }
}

