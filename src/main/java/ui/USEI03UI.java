package ui;

import domain.Coordinate;
import domain.Distance;
import domain.Location;
import domain.USEI03_DTO;
import graphs.Algorithms;
import graphs.Edge;
import graphs.GFH;
import graphs.Graph;
import utils.AnsiColor;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.BinaryOperator;

public class USEI03UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI03UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    public double calcDistance(Coordinate coordinatesA, Coordinate coordinatesB) { // O(1)

        double latA = coordinatesA.getLatitude(); // O(1)
        double latB = coordinatesB.getLatitude(); // O(1)
        double lonA = coordinatesA.getLongitude(); // O(1)
        double lonB = coordinatesB.getLongitude(); // O(1)
        double R = 6371e3; // metres
        double φ1 = Math.toRadians(latA); // O(1)
        double φ2 = Math.toRadians(latB); // O(1)
        double Δφ = Math.toRadians(latB - latA); // O(1)
        double Δλ = Math.toRadians(lonB - lonA); // O(1)

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ / 2) * Math.sin(Δλ / 2); // O(1)

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // O(1)

        return R * c; // O(1)
    }


    public void run() {
        int autonomiaMax = Utils.readIntegerFromConsole("Qual é a autonomia do veículo? (em metros)");

        /*
        RESUMO:

        1. Obtenho todos os locais utilizando o algoritmo DepthFirstSearch
        2. Obtenho as coordenadas de todos os locais
        3. Obtenho as 2 coordenadas mais distantes
        4. Obtenho os locais correspondentes às 2 coordenadas (origem e destino)
        5. Obtenho o caminho mais curto entre a origem e o destino utilizando o algoritmo de Dijkstra e shortestPath
        6. Verifico em que locais é que o veículo tem de carregar (entre outros dados)
         */

        //1
        LinkedList<Location> locations = getAllLocations(gfh);

        //2
        ArrayList<Coordinate> coordinates = getAllCoordinates(locations);

        //3
        ArrayList<Coordinate> mostDistantCoordinates = getMostDistantCoordinates(coordinates);
        Coordinate originCoordinate = mostDistantCoordinates.get(0);
        Coordinate destinationCoordinate = mostDistantCoordinates.get(1);

        //4
        ArrayList<Location> mostDistantLocations = getMostDistantLocations(originCoordinate, destinationCoordinate, locations);
        Location origin = mostDistantLocations.get(0);
        Location destination = mostDistantLocations.get(1);

        //5
        USEI03_DTO dto = getShortestPathData(origin, destination, autonomiaMax);
        boolean tripIsPossible = dto.isTripIsPossible();
        LinkedList<Location> shortPath = dto.getShortPath();
        ArrayList<Location> chargeLocations = dto.getChargeLocations();
        Integer pathLength = dto.getPathLength();
        int numberOfCharges = dto.getNumberOfCharges();


        //6
        showShortestPath(tripIsPossible, shortPath, chargeLocations, pathLength, numberOfCharges, origin, destination);

    }


    public LinkedList<Location> getAllLocations(Graph<Location, Integer> gfh) {
        LinkedList<Location> locations = new LinkedList<>();
        for (Location location : gfh.vertices()) {
            locations.add(location);
        }
        return locations;
    }


    public ArrayList<Coordinate> getAllCoordinates(LinkedList<Location> locations) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (Location location : locations) {
            coordinates.add(location.getCoordinate());
        }
        return coordinates;
    }

    public ArrayList<Coordinate> getMostDistantCoordinates(ArrayList<Coordinate> coordinates) {
        double maxDistance = 0;
        Coordinate originCoordinate = null;
        Coordinate destinationCoordinate = null;


        for (int i = 0; i < coordinates.size(); i++) {
            for (int j = 0; j < coordinates.size(); j++) {
                double distance = calcDistance(coordinates.get(i), coordinates.get(j));
                if (distance > maxDistance) {
                    maxDistance = distance;
                    originCoordinate = coordinates.get(i);
                    destinationCoordinate = coordinates.get(j);
                }
            }
        }
        ArrayList<Coordinate> mostDistantCoordinates = new ArrayList<>();
        mostDistantCoordinates.add(originCoordinate);
        mostDistantCoordinates.add(destinationCoordinate);
        return mostDistantCoordinates;
    }

    public ArrayList<Location> getMostDistantLocations(Coordinate originCoordinate, Coordinate destinationCoordinate, LinkedList<Location> locations) {
        Location origin = null;
        Location destination = null;

        for (Location location : locations) {
            if (location.getCoordinate().equals(originCoordinate)) {
                origin = location;
            }
            if (location.getCoordinate().equals(destinationCoordinate)) {
                destination = location;
            }
        }
        ArrayList<Location> mostDistantLocations = new ArrayList<>();
        mostDistantLocations.add(origin);
        mostDistantLocations.add(destination);
        return mostDistantLocations;
    }


    public USEI03_DTO getShortestPathData(Location origin, Location destination, int autonomiaMax) {
        LinkedList<Location> shortPath = new LinkedList<>();
        Integer pathLength = Algorithms.shortestPath(gfh, origin, destination, Integer::compare, Integer::sum, 0, shortPath);
        int autonomiaAtual = autonomiaMax;
        int numberOfCharges = 0;
        int distance = 0;
        boolean tripIsPossible = true;
        ArrayList<Location> chargeLocations = new ArrayList<>();


        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location = shortPath.get(i);
            Location nextLocation = shortPath.get(i + 1);
            distance = gfh.edge(location, nextLocation).getWeight();
            if (distance > autonomiaMax) {
                tripIsPossible = false;
                break;
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
        USEI03_DTO dto = new USEI03_DTO(tripIsPossible, shortPath, chargeLocations, pathLength, numberOfCharges, autonomiaMax);
        return dto;
    }

    public LinkedList<Location> showShortestPath(boolean tripIsPossible, LinkedList<Location> shortPath, ArrayList<Location> chargeLocations, Integer pathLength, int numberOfCharges, Location origin, Location destination) {
        if (!tripIsPossible) {
            Utils.showMessageColor("\nO veículo não tem autonomia suficiente para efetuar a viagem", AnsiColor.RED);
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

        return shortPath;
    }

}

