package repository;

import dto.CultivosDTO;
import oracle.jdbc.OracleTypes;
import tables.Colheitas;
import utils.AnsiColor;
import utils.Utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColheitasRepository {

    public ColheitasRepository() {
    }

    /**
     * Get list of all "Colheitas" records within database.
     *
     * @return a list of all "Colheitas".
     * @throws SQLException
     */
    public List<Colheitas> getColheitas() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<Colheitas> colheitas = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getColheitas() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            colheitas = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return colheitas;
    }

    /**
     * Register an operation and updates parameters within COLHEITAS and OPERACOES and PRODUTOS
     * @param cultivo
     * @return true for success and false for rollback
     * @throws SQLException
     */
    public boolean registerColheitas(CultivosDTO cultivo) throws SQLException {

        CallableStatement callStmt = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call registerColheita(?,?,?,?,?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);

            callStmt.setInt(2, 1);
            callStmt.setInt(3, cultivo.getParcelaid());
            callStmt.setInt(4, cultivo.getCulturaid());
            callStmt.setInt(5,0);
            callStmt.setString(6, cultivo.getDataColheita());
            callStmt.setDouble(7, cultivo.getQuantidade());
            callStmt.setString(8, cultivo.getUnidadeColheita());
            callStmt.setString(9, cultivo.getProduto());

            callStmt.execute();
            int outcome = callStmt.getInt(1);
            connection.commit();

            boolean sucesso = false;
            if (outcome == 1) {
                sucesso = true;
                Utils.showMessageColor("Colheita registada com sucesso!", AnsiColor.GREEN);
                return sucesso;
            } else {
                Utils.showMessageColor("Colheita não resgistada.", AnsiColor.RED);
                return sucesso;
            }

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
    }

    /**
     * Converts the result set from the database to app object.
     *
     * @param resultSet
     * @return list of "Colheitas".
     * @throws SQLException
     */
    private List<Colheitas> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Colheitas> colheitasList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            Colheitas aplicacao = new Colheitas(
                    resultSet.getInt("OPERACAOID"),
                    resultSet.getDouble("quantidade"),
                    resultSet.getString("UNIDADEMEDIDA")
            );
            colheitasList.add(aplicacao);
        }
        return colheitasList;
    }
}
