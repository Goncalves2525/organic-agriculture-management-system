package ui;

import main.lapr3.utils.AnsiColor;
import main.lapr3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainMenuUI implements Runnable {

    public MainMenuUI() {
    }

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Database Connection Test", new DatabaseConnectionTestUI()));
        options.add(new MenuItem("Import legacy data of \"Gestão de Operações da Quinta.\"", new ImportFarmOperationsLegacyUI()));
        options.add(new MenuItem("Menu Sementeiras", new SementeiraUI()));
        options.add(new MenuItem("Menu Aplicações (Fatores de Produção)", new AplicacoesUI()));
        options.add(new MenuItem("Run GFH Manager", new ImportGFHDataUI()));
        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\nMain Menu", "Exit");

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
    }
}
