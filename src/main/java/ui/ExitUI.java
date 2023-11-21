package ui;

import main.lapr3.controller.ExitController;
import main.lapr3.utils.AnsiColor;
import main.lapr3.utils.Utils;

import java.sql.SQLException;

public class ExitUI implements Runnable {

    private ExitController controller;

    public ExitUI() {
        controller = new ExitController();
    }

    public void run() {
        try {
            controller.exit();
            Utils.showMessageColor("\nSuccessful termination.\n" +
                    "Thank you!\nWish you a great day!", AnsiColor.GREEN);
            System.exit(0);
        } catch (SQLException e) {
            Utils.showMessageColor("Unsuccessful termination!", AnsiColor.RED);
            System.exit(-1);
        }
    }

}
