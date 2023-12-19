package ui;

import domain.Location;
import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLOutput;
import java.util.*;

public class USEI06UI implements Runnable {
    private Graph<Location, Integer> gfh;

    public USEI06UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    @Override
    public void run() {
        ArrayList<Location> vertices = gfh.vertices();
        ArrayList<Location> hubs = new ArrayList<>();
        Location hubDestino = null;
        int nHubs = Utils.readIntegerFromConsole("Insira o número de hubs: ");
        int autonomia = Utils.readIntegerFromConsole("Insira a autonomia do veículo: ");
        int velocidade = Utils.readIntegerFromConsole("Insira a velocidade (km/h) média do veículo: ");


        Map<Location, Integer> influenceMap = Algorithms.calculateInfluence(gfh);
        List<Location> sortedLocationsByInfluence = USEI02UI.sortLocationsByMetric(influenceMap, Comparator.reverseOrder());
        for (Location location : sortedLocationsByInfluence) {
            if (hubs.size() < nHubs) {
                location.setHub(true);
                hubs.add(location);
            }
        }


        Location origem = null;
        boolean found = false;
        do {
            String localOrigemCode = Utils.readLineFromConsole("Insira o código do local de origem: ");
            for (Location location : vertices) {
                if (location.getCode().equals(localOrigemCode)) {
                    origem = location;
                    found = true;
                    break;
                }
            }
            if (!found)
                System.out.println("Código inválido.");
        } while (!found);

        System.out.println();
        Utils.showMessageColor("HUBS", AnsiColor.BLUE);
        for (Location hub : hubs) {
            System.out.println(hub.getCode());
        }

        found = false;
        do {
            String hubDestinoCode = Utils.readLineFromConsole("Insira o código do local do hub de destino: ");
            for (Location hub : hubs) {
                if (hub.getCode().equals(hubDestinoCode)) {
                    hubDestino = hub;
                    found = true;
                    break;
                }
            }
            if (!found)
                System.out.println("Código inválido.");
        }while (!found);



        ArrayList<LinkedList<Location>> allPathsAutonomy = Algorithms.allPathsAutonomy(gfh, origem, hubDestino, autonomia);

        if (allPathsAutonomy == null) {
            System.out.println("O vetor está null.");
        } else if (allPathsAutonomy.isEmpty()) {
            System.out.println("Não há caminhos possíveis.");
        } else {
            Utils.showMessageColor("Caminhos possíveis: ", AnsiColor.BLUE);
            for (LinkedList<Location> path : allPathsAutonomy) {

                Integer distance = 0;

                for (int i = 0; i < path.size() - 1; i++) {

                    Edge edge = gfh.edge(path.get(i), path.get(i + 1));
                    Integer weight = (Integer) edge.getWeight();
                    distance += weight;

                    if (i == 0)
                        System.out.print(path.get(i).getCode() + printIfHub(path.get(i)) + " -> " + "(" + weight + "m) " + path.get(i + 1).getCode() + printIfHub(path.get(i)));
                    else
                        System.out.print(" -> " + "(" + weight + "m) " + path.get(i + 1).getCode() + printIfHub(path.get(i + 1)));
                }

                int distanciaEmKm = distance / 1000;
                double tempo = (double) distanciaEmKm / velocidade;
                System.out.printf("\nTempo Total: %.2fh\n", tempo);
                System.out.println("Distância Total: " + distance + "m | " + distanciaEmKm + "km\n");
                System.out.println();
                System.out.println();
            }
        }

    }

    public String printIfHub(Location location) {
        if (location.isHub())
            return " (hub)";
        else
            return "";
    }

}
