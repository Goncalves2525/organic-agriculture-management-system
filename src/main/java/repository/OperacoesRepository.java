package repository;

import oracle.jdbc.OracleTypes;
import tables.AplicacoesFatProd;
import utils.AnsiColor;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacoesRepository {

    public OperacoesRepository() {
    }

    public void anularOperacao(int p_id_Quinta, int idOperacao) throws SQLException {

        CallableStatement callStmt = null;
        boolean result = false;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Enable DBMS_OUTPUT
            try (CallableStatement enableStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.ENABLE(NULL); END;")) {
                enableStmt.execute();
            }

            callStmt = connection.prepareCall("{ ? = call Anular_Operacao(?) }");
            callStmt.registerOutParameter(1, OracleTypes.BOOLEAN);
            callStmt.setInt(2, idOperacao);
            //callStmt.setString(3, parcela);


            callStmt.execute();
            result = callStmt.getBoolean(1);
            String errorMessage = "";
            try (CallableStatement readStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.GET_LINE(?, ?); END;")) {
                readStmt.registerOutParameter(1, OracleTypes.VARCHAR);
                readStmt.registerOutParameter(2, OracleTypes.NUMERIC);

                // Loop to retrieve messages until no more available
                while (true) {
                    readStmt.setInt(2, 32000);  // Maximum line length
                    readStmt.execute();
                    String message = readStmt.getString(1);
                    if (message == null) {
                        break;  // No more messages
                    }
                    errorMessage = message;
                }
            }

            if(result)
                Utils.showMessageColor("\nOperação anulada com sucesso!", AnsiColor.GREEN);
            else{
                Utils.showMessageColor("\nOperação não anulada, com o seguinte alerta: " + errorMessage, AnsiColor.RED);
            }

            //System.out.println(result);
            callStmt.close();
            connection.commit();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
    }

}