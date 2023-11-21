package controller;

import repository.DatabaseConnection;

import java.sql.SQLException;

/**
 * @author Nelson Freire, ISEP-IPP
 */
public class DatabaseConnectionTestController {

    public DatabaseConnectionTestController() {
    }

    public int DatabaseConnectionTest() throws SQLException {
        int testResult = DatabaseConnection.getInstance().testConnection();
        return testResult;
    }
}
