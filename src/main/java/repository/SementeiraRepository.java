package repository;

import oracle.jdbc.OracleTypes;
import tables.Sementeiras;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SementeiraRepository {

    public SementeiraRepository() {
    }

    public void funcTeste () throws SQLException {
        CallableStatement callStmt = null;
        //ResultSet resultSet = null;
        boolean result = false;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call func_Check_Operador(?) }");
            callStmt.registerOutParameter(1, OracleTypes.BOOLEAN);
            callStmt.setInt(2, 1);

            callStmt.execute();
            result = callStmt.getBoolean(1);
            callStmt.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
    }

    public List<Sementeiras> getSementeiras() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<Sementeiras> sementeiras = null;

        // TODO: fazer USBD11
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call FUNC_REGISTAR_SEMEADURA(?,?,?,?,'') }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.registerOutParameter(2, OracleTypes.INTEGER);
            callStmt.registerOutParameter(3, OracleTypes.INTEGER);

            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);

            sementeiras = resultSetToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return sementeiras;
    }

    // TODO: alterar parâmetros de entrada
    public void sementeirasRegister(int idCultivo, int idParcela, int idOperador, Date dataInicio, Date dataFim, int qtd, String name) throws SQLException {

        CallableStatement callStmt = null;
        boolean result = false;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call FUNC_REGISTAR_SEMEADURA(?,?,?,?,?,?,?) }");
            callStmt.registerOutParameter(1, OracleTypes.BOOLEAN);
            int p_id_Cultivo = 1;
            int p_id_Parcela = 1;
            int p_id_Operador = 1;
            String p_unMedida = "ha";
            int p_qtd = 5;
            long millis=System.currentTimeMillis();
            long millis2=System.currentTimeMillis();
            java.sql.Date p_dataInicio = new java.sql.Date(millis);
            java.sql.Date p_dataFim = new java.sql.Date(millis2);
            callStmt.setInt(2, p_id_Cultivo);
            callStmt.setInt(3, p_id_Parcela);
            callStmt.setInt(4, p_id_Operador);
            callStmt.setDate(5, p_dataInicio);
            callStmt.setDate(6, p_dataFim);
            callStmt.setInt(7, p_qtd);
            callStmt.setString(8, p_unMedida);

//            callStmt.setInt(1, sailorId);
//            callStmt.setString(2, name);
//            callStmt.setInt(3, rating);
//            java.sql.Date sqlDate = new java.sql.Date(birthday.getTime());
//            callStmt.setDate(4, sqlDate);

            callStmt.execute();
            result = callStmt.getBoolean(1);
            System.out.println(result);
            //connection.commit();
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
    }

    // TODO: corrigir método para sementeiras
    public int sementeirasDelete(int sailorId) throws SQLException {

        CallableStatement callStmt = null;
        int deletedRows = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call fncSailorDelete(?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);
            callStmt.setInt(2, sailorId);

            callStmt.execute();
            deletedRows = callStmt.getInt(1);

            connection.commit();

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
        return deletedRows;
    }

    private List<Sementeiras> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Sementeiras> sementeiras = new ArrayList<>();
        while (true) {
            if (!resultSet.next()) break;
//            Sementeiras sailor = new Sementeiras(
//                    resultSet.getInt("operacaoId"),
//                    resultSet.getInt("tipoOperacaoId")
//            );
            //sementeiras.add(sailor);
        }
        return sementeiras;
    }
}
