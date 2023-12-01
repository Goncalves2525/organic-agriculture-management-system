package repository;

import oracle.jdbc.OracleTypes;
import tables.AplicacoesFatProd;
import utils.AnsiColor;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AplicacoesRepository {

    public AplicacoesRepository() {
    }

    public List<AplicacoesFatProd> getAplicacoes() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<AplicacoesFatProd> aplicacoes = null;


        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getAplicacoes() }");

            callStmt.registerOutParameter(1, OracleTypes.CURSOR);

            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);

            aplicacoes = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return aplicacoes;
    }

    public int aplicacoesRegister(int quintaID, String parcelaNome, int culturaID, int operadorID, Date dataInicio, String fatorProducaoID, int quantidade, String unidadeMedidaID, float area) throws SQLException {

        CallableStatement callStmt = null;
        int worked = -1;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            try (CallableStatement enableStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.ENABLE(NULL); END;")) {
                enableStmt.execute();
            }

            callStmt = connection.prepareCall("{ ? = call registarAplicacao(?,?,?,?,?,?,?,?,?) }");



            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.setInt(2, quintaID);
            callStmt.setString(3, parcelaNome);
            callStmt.setInt(4, culturaID);
            callStmt.setInt(5, operadorID);
            callStmt.setDate(6, dataInicio);
            callStmt.setString(7, fatorProducaoID);
            callStmt.setInt(8, quantidade);
            callStmt.setString(9, unidadeMedidaID);
            callStmt.setFloat(10, area);

            callStmt.execute();
            worked = callStmt.getInt(1);

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

            connection.commit();


        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }

        return worked;
    }

    // TODO: corrigir método para aplicacoes
//    public int aplicacoesDelete(int sailorId) throws SQLException {
//
//        CallableStatement callStmt = null;
//        int deletedRows = 0;
//        try {
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            callStmt = connection.prepareCall("{call fncSailorDelete(?) }");
//
//            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
//            callStmt.setInt(2, sailorId);
//
//            callStmt.execute();
//            deletedRows = callStmt.getInt(1);
//
//            connection.commit();
//
//        } finally {
//            if (!Objects.isNull(callStmt)) {
//                callStmt.close();
//            }
//        }
//        return deletedRows;
//    }

    private List<AplicacoesFatProd> resultSetToList(ResultSet resultSet) throws SQLException {
        List<AplicacoesFatProd> aplicacoesList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            AplicacoesFatProd aplicacao = new AplicacoesFatProd(
                    resultSet.getInt("OPERACAOID"),
                    resultSet.getString("FATORPRODID"),
                    resultSet.getInt("quantidade"),
                    resultSet.getString("UNIDADEMEDIDA")
            );
            aplicacoesList.add(aplicacao);
        }
        return aplicacoesList;
    }

}