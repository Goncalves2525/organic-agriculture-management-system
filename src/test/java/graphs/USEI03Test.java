package graphs;

import controller.ImportDataCtrl;
import domain.Coordinate;
import domain.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.USEI03UI;
import utils.Utils;

public class USEI03Test {
    private ImportDataCtrl importDataCtrl = new ImportDataCtrl();
    private String locaisPathBig = "files/locais_big.csv";
    private String distanciasPathBig = "files/distancias_big.csv";
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";
    Graph<Location, Integer> gfhBig = importDataCtrl.runImportGFHData(locaisPathBig, distanciasPathBig);
    Graph<Location, Integer> gfhSmall = importDataCtrl.runImportGFHData(locaisPathSmall, distanciasPathSmall);
    USEI03UI usei03uiBig = new USEI03UI(gfhBig);
    USEI03UI usei03uiSmall = new USEI03UI(gfhSmall);

    @Test
    void testRun() {
       // Assertions.assertNull(Algorithms.BreadthFirstSearch(completeMap, "LX"), "Should be null if vertex does not exist");




    }

}
