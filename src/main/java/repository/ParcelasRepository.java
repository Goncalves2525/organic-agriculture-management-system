package repository;

import dto.CultivosDTO;
import dto.ParcelaDTO;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParcelasRepository {

    public ParcelasRepository() {
    }

    public List<ParcelaDTO> getParcelas() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<ParcelaDTO> parcelas = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getParcelasData() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            parcelas = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return parcelas;
    }

    /**
     * Converts the result set from the database to app object.
     *
     * @param resultSet
     * @return list of "Cultivos".
     * @throws SQLException
     */
    private List<ParcelaDTO> resultSetToList(ResultSet resultSet) throws SQLException {
        List<ParcelaDTO> parcelaDTOList = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
            ParcelaDTO aplicacao = new ParcelaDTO(
                    resultSet.getInt("QUINTAID"),
                    resultSet.getInt("idParcela"),
                    resultSet.getString("nome"),
                    resultSet.getInt("area"),
                    resultSet.getString("unidademedida")
            );
            parcelaDTOList.add(aplicacao);
        }
        return parcelaDTOList;
    }
}