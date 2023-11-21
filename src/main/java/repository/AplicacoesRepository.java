package repository;

import oracle.jdbc.OracleTypes;
import tables.AplicacoesFatProd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean aplicacoesRegister(int operacaoId, int fatorProducaoId, int quantidade, String unidadeMedidaId) throws SQLException {

        CallableStatement callStmt = null;
        boolean worked = false;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call registarAplicacao(?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.BOOLEAN);
            callStmt.setInt(2, operacaoId);
            callStmt.setInt(3, fatorProducaoId);
            callStmt.setInt(4, quantidade);
            callStmt.setString(5, unidadeMedidaId);

            callStmt.execute();
            worked = callStmt.getBoolean(1);
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