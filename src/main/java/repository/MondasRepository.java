package repository;


import dto.CultivosDTO;
import dto.ParcelaDTO;
import oracle.jdbc.OracleTypes;
import tables.Mondas;
import utils.AnsiColor;
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

    public boolean insertMondas(CultivosDTO cultivo) throws SQLException {

        CallableStatement callStmt = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call insertMondas(?,?,?,?,?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);

            callStmt.setInt(2, 1);
            callStmt.setInt(3, cultivo.getParcelaid());
            callStmt.setInt(4, cultivo.getCulturaid());
            callStmt.setInt(5,0);
            callStmt.setString(6, cultivo.getDataColheita());
            callStmt.setDouble(7, cultivo.getQuantidade());
            callStmt.setString(8, cultivo.getUnidadeMonda());
            callStmt.setString(9, cultivo.getProduto());

            callStmt.execute();
            int outcome = callStmt.getInt(1);
            connection.commit();

            boolean sucesso = false;
            if (outcome == 1) {
                sucesso = true;
                Utils.showMessageColor("Monda registada com sucesso!", AnsiColor.GREEN);
                return sucesso;
            } else {
                Utils.showMessageColor("Monda não resgistada.", AnsiColor.RED);
                return sucesso;
            }

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
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
