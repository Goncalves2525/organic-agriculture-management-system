package repository;

import oracle.jdbc.OracleTypes;
import tables.Mondas;
import utils.Utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MondasRepository {

    public MondasRepository(){
    }

    public List<Mondas> getMondas() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<Mondas> mondas = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getMondas() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            mondas = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return mondas;
    }

    public int insertMondas(int operacaoId, int quantidade, String unidadeMedida) throws SQLException {
        CallableStatement callStmt = null;
        int worked = -1;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call INSERTMONDAS(?, ?, ?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.setInt(2, operacaoId);
            callStmt.setInt(3, quantidade);
            callStmt.setString(4, unidadeMedida);

            callStmt.execute();
            worked = callStmt.getInt(1);
            connection.commit();
            System.out.println("Data inserted successfully!");
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }

        return worked;
    }

    private List<Mondas> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Mondas> mondasList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            Mondas aplicacao = new Mondas(
                    resultSet.getInt("OPERACAOID"),
                    resultSet.getInt("quantidade"),
                    resultSet.getString("UNIDADEMEDIDA")
            );
            mondasList.add(aplicacao);
        }
        return mondasList;
    }
}
