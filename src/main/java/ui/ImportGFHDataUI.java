package ui;

import main.lapr3.controller.ImportDataCtrl;
import main.lapr3.graphs.GFH;
import main.lapr3.utils.AnsiColor;
import main.lapr3.utils.Utils;

public class ImportGFHDataUI implements Runnable{

    private ImportDataCtrl importDataCtrl;

    private String locaisPath = "files/locais_small.xlsx";
    private String distanciasPath = "files/distancias_small.xlsx";

    public ImportGFHDataUI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        int result = 0;
        try {
            importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

            Utils.showMessageColor("DONE!", AnsiColor.GREEN);
            Utils.readLineFromConsole("Press Enter to continue.");

            GFH gfh = new GFH();
            System.out.println(gfh.getGfhMatrix().toString());
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
