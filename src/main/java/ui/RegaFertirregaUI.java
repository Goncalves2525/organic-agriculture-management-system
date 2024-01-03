package ui;
import repository.DatabaseConnection;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

public class RegaFertirregaUI implements Runnable {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        String civilYearStart = getUserInput();
        String civilYearEnd = getUserInput();
        try {
            callAndExportData(civilYearStart, civilYearEnd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getUserInput() {
        System.out.print("Enter the civil year (YYYY): ");
        return scanner.nextLine();
    }

    private void callAndExportData(String civilYearStart, String civilYearEnd) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            // Assuming you want to set the date to January 1st of the entered civil year
            String startDate = civilYearStart + "-01-01";
            String endDate = civilYearEnd + "-01-01";

            // Call the Oracle PL/SQL function
            String sql = "{ ? = call GetTopRankedOperations(?, ?, ?) }";
            try (CallableStatement stmt = connection.prepareCall(sql)) {
                stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                stmt.setDate(2, java.sql.Date.valueOf(startDate));
                stmt.setDate(3, java.sql.Date.valueOf(endDate));
                stmt.setString(4, "min");

                stmt.execute();

                // Get the result set
                try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                    // Print the result set to the console
                    printResultSetToConsole(rs);

                    // Export the result set to a .txt file
                    exportResultSetToFile(rs, "output.txt");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private static void printResultSetToConsole(ResultSet resultSet) throws SQLException {
        try {
            System.out.println("PARCELAID | PARCELANAME | CULTURAID | CULTURANAME | dataInicio | OperationType | quantidade | UNIDADEMEDIDA");
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                String row = String.format("%s | %s | %s | %s | %s | %s | %s | %s",
                        resultSet.getString("PARCELAID"),
                        resultSet.getString("PARCELANAME"),
                        resultSet.getString("CULTURAID"),
                        resultSet.getString("CULTURANAME"),
                        resultSet.getDate("dataInicio"),
                        resultSet.getString("OperationType"),
                        resultSet.getDouble("quantidade"),
                        resultSet.getString("UNIDADEMEDIDA"));

                System.out.println(row);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private static void exportResultSetToFile(ResultSet resultSet, String filePath) throws SQLException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            while (resultSet.next()) {
                String row = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                        resultSet.getString("PARCELAID"),
                        resultSet.getString("PARCELANAME"),
                        resultSet.getString("CULTURAID"),
                        resultSet.getString("CULTURANAME"),
                        resultSet.getDate("dataInicio"),
                        resultSet.getString("OperationType"),
                        resultSet.getDouble("quantidade"),
                        resultSet.getString("UNIDADEMEDIDA"));

                writer.println(row);
            }
            System.out.println("Data exported to " + filePath);
        } catch (SQLException | java.io.IOException e) {
            throw new SQLException("Error exporting data to file", e);
        }
    }
}