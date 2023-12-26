package ui;

import domain.Location;
import graphs.Graph;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class USEI08UI implements Runnable {

    private Graph<Location, Integer> gfh;

    public USEI08UI(Graph<Location, Integer> gfh) {
        this.gfh = gfh;
    }

    @Override
    public void run() {
        ArrayList<Location> vertices = gfh.vertices();
        ArrayList<Location> hubs = new ArrayList<>();

        // Find and collect hub locations
        for (Location location : vertices) {
            hubs.add(location);
        }
        // Generate random number of employees for each hub
        Random random = new Random();
        for (Location hub : hubs) {
            int numberOfEmployees = random.nextInt(100) + 1; // Assuming a range of 1 to 100 employees
            hub.setNrOfEmployees(numberOfEmployees);
        }

        // Sort hubs based on the number of employees (descending order)
        Collections.sort(hubs, (hub1, hub2) -> Integer.compare(hub2.getNrOfEmployees(), hub1.getNrOfEmployees()));

        int nHubs = Utils.readIntegerFromConsole("Insira o número de hubs do circuito: ");


    }
}
