package ui;

import java.util.List;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI09_DTO;
import graphs.*;

public class USEI09UI implements Runnable {

    private ImportDataCtrl importDataCtrl;

    private static String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI09UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

        List<USEI09_DTO.Cluster<Location>> clusters = USEI09_DTO.clusterize(gfh);

        for (USEI09_DTO.Cluster<Location> cluster : clusters) {
            System.out.println("Hub: " + cluster.getHub());
            System.out.println("Locations: " + cluster.getLocations());
            System.out.println();
        }
    }
}
