package graphs;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.map.MapGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class USEI07Test {
    private ImportDataCtrl importDataCtrlSmall = new ImportDataCtrl();
    private String locaisPathSmall = "files/locais_small.csv";
    private String distanciasPathSmall = "files/distancias_small.csv";
    private Graph<Location, Integer> gfh = importDataCtrlSmall.runImportGFHData(locaisPathSmall, distanciasPathSmall);

}
