package ui;

import main.lapr3.controller.ImportDataCtrl;
import main.lapr3.utils.AnsiColor;
import main.lapr3.utils.Utils;

public class ImportFarmOperationsLegacyUI implements Runnable {

    private ImportDataCtrl importDataCtrl;
    private String locationFarmOperationsLegacy = "files/Legacy_Data_v2a.xlsx";

    public ImportFarmOperationsLegacyUI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        int result = 0;
        try {
            importDataCtrl.runImportFarmOperationsLegacyData(locationFarmOperationsLegacy);

            // export to sql?
            boolean toExport = Utils.confirm("Do you want to export, the imported data, as INSERTS to .sql file? (y/n)");
            if (toExport) {
                importDataCtrl.exportDataToSqlScript();
            }

            // print to terminal?
            boolean toPrint = Utils.confirm("Do you want to print the imported values to the terminal? (y/n)");
            System.out.println();
            if (toPrint) {
                importDataCtrl.printDataToTerminal();
            }

            Utils.showMessageColor("DONE!", AnsiColor.GREEN);
            Utils.readLineFromConsole("Press Enter to continue.");
        } catch (Exception e) {
            Utils.showMessageColor("Exception raised on ImportFarmOperationsLegacyUI." +
                    "\n Please contact admin team.", AnsiColor.YELLOW);
            e.printStackTrace();
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }
}
