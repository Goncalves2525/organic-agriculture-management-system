package ui;

import controller.DatabaseConnectionTestController;
import repository.DatabaseConnection;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

/**
 * @author Nelson Freire, ISEP-IPP
 */
public class DatabaseConnectionTestUI implements Runnable {

    private DatabaseConnectionTestController controller;

    public DatabaseConnectionTestUI() {
        controller = new DatabaseConnectionTestController();
    }

    public void run() {
        int result = 0;
        try {
            result = controller.DatabaseConnectionTest();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (result == DatabaseConnection.CONNECTION_SUCCESS) {
            Utils.showMessageColor("\nConnected to the database.", AnsiColor.GREEN);
            Utils.readLineFromConsole("Press Enter to continue.");
        } else {
            Utils.showMessageColor("\nNot connected to the database!", AnsiColor.RED);
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }
}
