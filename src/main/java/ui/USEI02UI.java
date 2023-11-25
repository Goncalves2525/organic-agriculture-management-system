package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.Algorithms;
import graphs.Graph;
import graphs.matrix.MatrixGraph;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class USEI02UI implements Runnable{

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public USEI02UI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    @Override
    public void run() {}

}
