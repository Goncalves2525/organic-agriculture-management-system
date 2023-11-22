package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.GFH;
import graphs.Graph;
import utils.AnsiColor;
import utils.Utils;

public class ImportGFHDataUI implements Runnable{

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public ImportGFHDataUI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        int result = 0;
        try {
            Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

            Utils.showMessageColor("Building graph!", AnsiColor.BLUE);
            System.out.println(gfh.toString());
            Utils.readLineFromConsole("Press Enter to continue.");

            // TODO: add ESINF US

        } catch (Exception e) {
            Utils.showMessageColor("There is an issue with ImportGFHDataUI." +
                    "\n Please contact admin team.", AnsiColor.YELLOW);
            e.printStackTrace();
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }
}
