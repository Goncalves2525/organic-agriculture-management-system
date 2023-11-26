package repository;

import oracle.jdbc.OracleTypes;
import tables.Sementeiras;
import utils.AnsiColor;
import utils.Utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SementeiraRepository {

    public SementeiraRepository() {
    }



    public List<Sementeiras> getSementeiras() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<Sementeiras> sementeiras = null;

        // TODO: fazer USBD11
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call FUNC_REGISTAR_SEMEADURA(?,?,?,?,'') }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.registerOutParameter(2, OracleTypes.INTEGER);
            callStmt.registerOutParameter(3, OracleTypes.INTEGER);

            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);

            sementeiras = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return sementeiras;
    }

    // TODO: alterar parâmetros de entrada
    public void sementeirasRegister(int p_id_Quinta, String parcela, String cultura, int p_id_Operador, String dataInicio, String dataFim, double p_qtdSementeira, String p_unMedidaSementeira, double p_qtdArea, String p_unMedidaArea) throws SQLException {

        CallableStatement callStmt = null;
        boolean result = false;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Enable DBMS_OUTPUT
            try (CallableStatement enableStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.ENABLE(NULL); END;")) {
                enableStmt.execute();
            }

            callStmt = connection.prepareCall("{ ? = call FUNC_REGISTAR_SEMEADURA(?,?,?,?,?,?,?,?,?,?) }");
            callStmt.registerOutParameter(1, OracleTypes.BOOLEAN);
            callStmt.setInt(2, p_id_Quinta);
            callStmt.setString(3, parcela);
            callStmt.setString(4, cultura);
            callStmt.setInt(5, p_id_Operador);
            callStmt.setString(6, dataInicio);
            callStmt.setString(7, dataFim);
            callStmt.setDouble(8,p_qtdSementeira );
            callStmt.setString(9, p_unMedidaSementeira);
            callStmt.setDouble(10,p_qtdArea );
            callStmt.setString(11, p_unMedidaArea);

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
                Utils.showMessageColor("\nOperação de semeadura registada com sucesso!", AnsiColor.GREEN);
            else{
                Utils.showMessageColor("\nOperação de semeadura não registada, com o seguinte erro: " + errorMessage, AnsiColor.RED);
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

    // TODO: corrigir método para sementeiras
    public int sementeirasDelete(int sailorId) throws SQLException {

        CallableStatement callStmt = null;
        int deletedRows = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call fncSailorDelete(?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.setInt(2, sailorId);

            callStmt.execute();
            deletedRows = callStmt.getInt(1);

            connection.commit();

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
        return deletedRows;
    }

    private List<Sementeiras> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Sementeiras> sementeiras = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
//            Sementeiras sailor = new Sementeiras(
//                    resultSet.getInt("operacaoId"),
//                    resultSet.getInt("tipoOperacaoId")
//            );
            //sementeiras.add(sailor);
        }
        return sementeiras;
    }


}
