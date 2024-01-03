package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import dto.USEI07_DTO;
import graphs.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.USEI07UI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class USEI07Test {

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
    public void testChooseStartingLocation() {
        USEI07UI ui = new USEI07UI();
        Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);
        List<Location> vertices = gfh.vertices();
        //1st vert
        Location startingLocation = vertices.get(0);

        assertEquals("CT1", startingLocation.getCode());
    }

    @Test
    public void testCreateOptimizedPathForTest() {
        USEI07UI ui = new USEI07UI();
        Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);

        List<Location> optimizedPath = ui.findOptimizedPath2(gfh);

        assertNotNull(optimizedPath);
        assertFalse(optimizedPath.isEmpty());
        assertTrue(isValidPath(optimizedPath, gfh));
    }

    private boolean isValidPath(List<Location> path, Graph<Location, Integer> graph) {
        for (int i = 0; i < path.size() - 1; i++) {
            Location currentLocation = path.get(i);
            Location nextLocation = path.get(i + 1);

            Edge<Location, Integer> edge = graph.edge(currentLocation, nextLocation);

            if (edge == null) {
                return false;
            }
        }

        return true;
    }

}
