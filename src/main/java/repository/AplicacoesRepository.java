package repository;

import oracle.jdbc.OracleTypes;
import tables.AplicacoesFatProd;

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

    public int aplicacoesRegister(int operacaoID, int quintaID, int parcelaID, int culturaID, int operadorID, Date dataInicio, String fatorProducaoID, int quantidade, String unidadeMedidaID) throws SQLException {

        CallableStatement callStmt = null;
        int worked = -1;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call registarAplicacao(?,?,?,?,?,?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.setInt(2, operacaoID);
            callStmt.setInt(3, quintaID);
            callStmt.setInt(4, parcelaID);
            callStmt.setInt(5, culturaID);
            callStmt.setInt(6, operadorID);
            callStmt.setDate(7, dataInicio);
            callStmt.setString(8, fatorProducaoID);
            callStmt.setInt(9, quantidade);
            callStmt.setString(10, unidadeMedidaID);

            callStmt.execute();
            worked = callStmt.getInt(1);
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