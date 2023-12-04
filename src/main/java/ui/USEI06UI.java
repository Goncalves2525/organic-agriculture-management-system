package ui;

import domain.Location;
import graphs.Algorithms;
import graphs.Graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class USEI06UI implements Runnable{
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
        int autonomia = 300000;

        ArrayList<LinkedList<Location>> allPaths = Algorithms.allPathsAutonomy(gfh, origem, hubDestino, autonomia);
        ArrayList<LinkedList<Location>> allPaths2 = Algorithms.allPaths(gfh, origem, hubDestino);

        /*
        if (allPaths == null) {
            System.out.println("O vetor está null.");
        }else if (allPaths.isEmpty()) {
            System.out.println("Não há caminhos possíveis.");
        }
        else {
            System.out.println("Caminhos possíveis:");
            for (LinkedList<Location> path : allPaths) {
                System.out.println(path);
            }
        }

         */

        if (allPaths2 == null) {
            System.out.println("O vetor está null.");
        }else if (allPaths2.isEmpty()) {
            System.out.println("Não há caminhos possíveis.");
        }
        else {
            System.out.println("Caminhos possíveis:");
            for (LinkedList<Location> path : allPaths2) {
                System.out.println(path);
            }
        }


    }

}
