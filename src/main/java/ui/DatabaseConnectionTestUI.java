package ui;

import main.lapr3.controller.DatabaseConnectionTestController;
import main.lapr3.data_access.DatabaseConnection;
import main.lapr3.utils.AnsiColor;
import main.lapr3.utils.Utils;

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
            System.out.println();
            Utils.showMessageColor("Connected to the database.", AnsiColor.GREEN);
            Utils.readLineFromConsole("Press Enter to continue.");
        } else {
            System.out.println();
            Utils.showMessageColor("Not connected to the database!", AnsiColor.RED);
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }
}
