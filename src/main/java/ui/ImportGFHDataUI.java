package ui;

import controller.ImportDataCtrl;
import domain.Location;
import graphs.GFH;
import graphs.Graph;
import utils.AnsiColor;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImportGFHDataUI implements Runnable{

    private ImportDataCtrl importDataCtrl;
    private String locaisPath = "files/locais_small.csv";
    private String distanciasPath = "files/distancias_small.csv";

    public ImportGFHDataUI() {
        this.importDataCtrl = new ImportDataCtrl();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        int result = 0;
        try {
            Graph<Location, Integer> gfh = importDataCtrl.runImportGFHData(locaisPath, distanciasPath);

            Utils.showMessageColor("Building graph!", AnsiColor.BLUE);
            System.out.println(gfh.toString());
            Utils.readLineFromConsole("Press Enter to continue.");

            List<MenuItem> options = new ArrayList<MenuItem>();
            //options.add(new MenuItem("USEI01", new USEI01UI()));
            options.add(new MenuItem("USEI02", new USEI02UI()));
            options.add(new MenuItem("USEI03", new USEI03UI(gfh)));
            options.add(new MenuItem("USEI04", new USEI04UI(gfh)));
            //options.add(new MenuItem("USEI05", new USEI05UI()));
            int option = 0;
            do {
                option = Utils.showAndSelectIndex(options, "\n\nESINF User Stories", "Exit");

                if ((option >= 0) && (option < options.size())) {
                    options.get(option).run();
                } else if (option == -1) {
                    ExitUI exit = new ExitUI();
                    exit.run();
                } else {
                    System.out.println();
                    Utils.showMessageColor("Invalid option!", AnsiColor.RED);
                    Utils.readLineFromConsole("Press Enter to continue.");
                }
            } while (option > -1);


        } catch (Exception e) {
            Utils.showMessageColor("There is an issue with ImportGFHDataUI." +
                    "\n Please contact admin team.", AnsiColor.YELLOW);
            e.printStackTrace();
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }
}
