package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI09_DTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.USEI07UI;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class USEI09Test {

    private ImportDataCtrl importDataCtrlSmall;
    private String locaisPathSmall;
    private String distanciasPathSmall;

    @BeforeEach
    public void setUp() {
        importDataCtrlSmall = new ImportDataCtrl();
        locaisPathSmall = "files/locais_small.csv";
        distanciasPathSmall = "files/distancias_small.csv";
    }

    @Test
    public void testEmptyClusterize() {
        USEI09_DTO ui = new USEI09_DTO();
        Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);

        List<USEI09_DTO.Cluster<Location>> clusters = ui.clusterize(gfh);

        assertFalse(clusters.isEmpty());
    }

    @Test
    public void testExploreCluster() {
        USEI09_DTO ui = new USEI09_DTO();
        Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);

        Location startVertex = gfh.vertices().get(0);

        USEI09_DTO.Cluster<Location> cluster = ui.exploreCluster(gfh, startVertex);

        assertFalse(cluster.getLocations().isEmpty());
    }


}
