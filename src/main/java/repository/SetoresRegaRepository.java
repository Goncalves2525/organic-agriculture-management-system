package repository;

import oracle.jdbc.OracleTypes;
import tables.SetoresRega;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetoresRegaRepository {
    public SetoresRegaRepository(){}

    public List<SetoresRega> getSetoresForRega() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<SetoresRega> setores = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getSetores() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            setores = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }
        return setores;
    }

    private List<SetoresRega> resultSetToList(ResultSet resultSet) throws SQLException {
        List<SetoresRega> setoresList = new ArrayList<>();

        while (resultSet.next()) {
            SetoresRega setor = new SetoresRega(
                    resultSet.getInt("IDSETOR"),
                    resultSet.getInt("QUINTAID"),
                    resultSet.getInt("EDIFICIOID"),
                    resultSet.getString("DATAINICIO"),
                    resultSet.getString("DATAFIM"),
                    resultSet.getDouble("CAUDALMAXIMO"),
                    resultSet.getString("UNIDADEMEDIDA")
            );
            setoresList.add(setor);
        }

        printSetoresList(setoresList);
        return setoresList;
    }

    public void printSetoresList(List<SetoresRega> setoresList) {
        if (setoresList == null || setoresList.isEmpty()) {
            System.out.println("No setores available.");
            return;
        }

        System.out.println("Setores:");
        for (SetoresRega setor : setoresList) {
            setor.print(); // Assuming you have a print method in your SetoresRega class
        }
    }
}
