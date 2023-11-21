package repository;

import oracle.jdbc.OracleTypes;
import tables.Colheitas;

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
                    resultSet.getInt("quantidade"),
                    resultSet.getString("UNIDADEMEDIDA")
            );
            colheitasList.add(aplicacao);
        }
        return colheitasList;
    }
}
