package repository;

import oracle.jdbc.OracleTypes;
import tables.AplicacoesFatProd;
import tables.Logs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogsRepository {

    public LogsRepository() {
    }

    public List<Logs> getLogs() throws SQLException {
        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<Logs> logs = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getLogs() }");

            callStmt.registerOutParameter(1, OracleTypes.CURSOR);

            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);

            logs = resultSetToList(resultSet);

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return logs;
    }

    private List<Logs> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Logs> logsList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            Logs log = new Logs(
                    resultSet.getInt("IDLOG"),
                    resultSet.getInt("OPERACAOID"),
                    resultSet.getTimestamp("TIMESTAMP"),
                    resultSet.getString("DESCRICAO")
            );
            logsList.add(log);
        }
        return logsList;
    }

}
