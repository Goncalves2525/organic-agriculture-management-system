package ui;

import controller.ExitController;
import utils.AnsiColor;
import utils.Utils;

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
            Utils.showMessageColor("\nUnsuccessful termination!", AnsiColor.RED);
            System.exit(-1);
        }
    }

}
