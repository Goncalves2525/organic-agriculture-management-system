package ui;

import controller.ColheitasController;
import tables.Colheitas;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

public class ColheitasUI implements Runnable {

    private ColheitasController ctrl;

    public ColheitasUI() { ctrl = new ColheitasController(); }

    public void run() {
        try {
            Utils.showMessageColor("\nTESTE: EXECUTAR METODO PRINT DA CLASSE COLHEITAS:", AnsiColor.BLUE);
            for (Colheitas colheitas : ctrl.getColheitas()){
                colheitas.print();
            }
            Utils.readLineFromConsole("Press Enter to continue.");
            ctrl.getColheitas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
