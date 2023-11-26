package repository;

import dto.CultivosDTO;
import oracle.jdbc.OracleTypes;
import utils.AnsiColor;
import utils.Utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CultivosRepository {

    public CultivosRepository() {
    }

    /**
     * Get list of all "Cultivos" records within database.
     *
     * @return a list of all "Cultivos".
     * @throws SQLException
     */
    public List<CultivosDTO> getCultivos() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<CultivosDTO> cultivos = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getCultivosData() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            cultivos = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return cultivos;
    }

    /**
     * Converts the result set from the database to app object.
     *
     * @param resultSet
     * @return list of "Cultivos".
     * @throws SQLException
     */
    private List<CultivosDTO> resultSetToList(ResultSet resultSet) throws SQLException {
        List<CultivosDTO> cultivosList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            CultivosDTO aplicacao = new CultivosDTO(
                    resultSet.getInt("PARCELAID"),
                    resultSet.getString("NOME"),
                    resultSet.getInt("CULTURAID"),
                    resultSet.getString("NOMECOMPLETO"),
                    resultSet.getString("PRODUTO")
            );
            cultivosList.add(aplicacao);
        }
        return cultivosList;
    }

    public List<CultivosDTO> getCultivosForMondas() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<CultivosDTO> cultivos = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getCultivosForMondasData() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            cultivos = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return cultivos;
    }
}
