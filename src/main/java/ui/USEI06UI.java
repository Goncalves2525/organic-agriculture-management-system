package ui;

import domain.Location;
import graphs.Algorithms;
import graphs.Edge;
import graphs.Graph;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

public class USEI06UI implements Runnable {
    private Graph<Location, Integer> gfh;

    public USEI06UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    @Override
    public void run() {
        ArrayList<Location> vertices = gfh.vertices();
        Location origem = gfh.vertices().get(0);
        Location hubDestino = null;
        String hubDestinoCode = "CT10";
        for (Location destino : vertices) {
            if (destino.getCode().equals(hubDestinoCode)) {
                hubDestino = destino;
            }
        }

        int autonomia = Utils.readIntegerFromConsole("Insira a autonomia do veículo: ");

        ArrayList<LinkedList<Location>> allPathsAutonomy = Algorithms.allPathsAutonomy(gfh, origem, hubDestino, autonomia);

        if (allPathsAutonomy == null) {
            System.out.println("O vetor está null.");
        } else if (allPathsAutonomy.isEmpty()) {
            System.out.println("Não há caminhos possíveis.");
        } else {
            System.out.println("Caminhos possíveis:");
            for (LinkedList<Location> path : allPathsAutonomy) {

                Integer distance = 0;

                for (int i = 0; i < path.size() - 1; i++) {

                    Edge edge = gfh.edge(path.get(i), path.get(i + 1));
                    Integer weight = (Integer) edge.getWeight();
                    distance += weight;
                    if (i == 0)
                        System.out.print(path.get(i).getCode() + " -> " + "(" + weight + "m) " + path.get(i + 1).getCode());
                    else
                        System.out.print(" -> " + "(" + weight + "m) " + path.get(i + 1).getCode());
                }
                System.out.println("\nDistância Total: " + distance + "m\n");
                System.out.println();
            }
        }

    }

}
