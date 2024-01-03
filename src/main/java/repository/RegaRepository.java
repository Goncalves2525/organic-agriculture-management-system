package repository;

import dto.FertirregasDTO;
import dto.RegaDTO;
import oracle.jdbc.OracleTypes;
import tables.Regas;
import tables.SetoresRega;
import utils.AnsiColor;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegaRepository {

    public RegaRepository(){}
    public boolean insertRegas(RegaDTO rega) throws SQLException{
        CallableStatement callStmt = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call insertRegas(?,?,?,?,?,?,?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);

            callStmt.setInt(2, 1);
            callStmt.setInt(3, rega.getParcelaid());
            callStmt.setInt(4, rega.getCulturaid());
            callStmt.setInt(5,0);
            callStmt.setString(6, rega.getDataColheita());
            callStmt.setDouble(7, rega.getQuantidade());
            callStmt.setString(8, rega.getUnidadeRega());
            callStmt.setInt(9, rega.getSetorId());
            callStmt.setString(10, rega.getHoraInicio());

            callStmt.execute();
            int outcome = callStmt.getInt(1);
            connection.commit();

            boolean sucesso = false;
            if (outcome == 1) {
                sucesso = true;
                Utils.showMessageColor("Rega registada com sucesso!", AnsiColor.GREEN);
                return sucesso;
            } else {
                Utils.showMessageColor("Rega não resgistada.", AnsiColor.RED);
                return sucesso;
            }

        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
    }

    public boolean insertFertirregas(FertirregasDTO fertirregas) throws SQLException {
        CallableStatement callStmt = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call insertFertirregas(?,?,?,?,?,?,?,?,?,?) }");

            callStmt.registerOutParameter(1, OracleTypes.INTEGER);

            callStmt.setInt(2, 1);
            callStmt.setInt(3, fertirregas.getParcelaid());
            callStmt.setInt(4, fertirregas.getCulturaid());
            callStmt.setInt(5,0);
            callStmt.setString(6, fertirregas.getDataInicio());
            callStmt.setDouble(7, fertirregas.getQuantidade());
            callStmt.setString(8, fertirregas.getUnidadeMedida());
            callStmt.setInt(9, fertirregas.getSetor());
            callStmt.setInt(10, fertirregas.getReceitaId());
            callStmt.setString(11, fertirregas.getHoraInicio());

            callStmt.execute();
            int outcome = callStmt.getInt(1);
            connection.commit();

            boolean sucesso = false;
            if (outcome == 1) {
                sucesso = true;
                Utils.showMessageColor("Fertirrega registada com sucesso!", AnsiColor.GREEN);
                return sucesso;
            } else {
                Utils.showMessageColor("Fertirrega não resgistada.", AnsiColor.RED);
                return sucesso;
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }
        return false;
    }

    public String registarFertiRega(int p_id_Quinta, int p_id_Parcela, int p_id_Operador, int p_quantidade, String p_unMedida, int p_Setor, int p_receitaID, String dataInicio, String p_horaInicio) throws SQLException {

        String result = "";
        CallableStatement callStmt = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call FUNC_REGISTAR_FERTIREGA(?,?,?,?,?,?,?,?,?) }");
            callStmt.registerOutParameter(1, OracleTypes.VARCHAR);

            callStmt.setInt(2, p_id_Quinta);
            callStmt.setInt(3, p_id_Parcela);
            callStmt.setInt(4, p_id_Operador);
            callStmt.setInt(5, p_quantidade);
            callStmt.setString(6, p_unMedida);
            callStmt.setInt(7, p_Setor);
            callStmt.setInt(8, p_receitaID);
            callStmt.setString(9,p_horaInicio );
            callStmt.setString(10, dataInicio);

            callStmt.execute();

            result = callStmt.getString(1);
            callStmt.close();
            connection.commit();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
        }

        return result;
    }
}
