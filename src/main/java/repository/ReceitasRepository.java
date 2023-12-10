package repository;

import dto.CultivosDTO;
import dto.FatProdDTO;
import dto.ReceitasDTO;
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

public class ReceitasRepository {

    public ReceitasRepository() {
    }

    /**
     * Get list of all "Receitas" records within database.
     *
     * @return a list of all "Receitas"
     * @throws SQLException
     */
    public List<ReceitasDTO> getReceitas() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<ReceitasDTO> lstReceitas = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getListaReceitas() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            lstReceitas = resultReceitasToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return lstReceitas;
    }

    /**
     * Converts the result set from the database to app object.
     *
     * @param resultSet
     * @return list of "Receitas".
     * @throws SQLException
     */
    private List<ReceitasDTO> resultReceitasToList(ResultSet resultSet) throws SQLException {
        List<ReceitasDTO> lstReceitas = new ArrayList<>();
        while (resultSet.next()) {

            int idReceita = resultSet.getInt("RECEITAID");
            String fatorProd = resultSet.getString("FATORPRODID");
            double quantidadePorHecatar = resultSet.getDouble("QUANTIDADEPORHECTAR");
            String unidadeMedida = resultSet.getString("UNIDADEMEDIDA");

            boolean exists = false;
            for (ReceitasDTO obj : lstReceitas) {
                if (obj.getIdReceita() == idReceita) {
                    obj.getReceita().add(new FatProdDTO(fatorProd, quantidadePorHecatar, unidadeMedida));
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                ReceitasDTO receita = new ReceitasDTO(
                        idReceita);
                receita.getReceita().add(new FatProdDTO(fatorProd, quantidadePorHecatar, unidadeMedida));
                lstReceitas.add(receita);
            }

        }
        return lstReceitas;
    }

    /**
     * Get list of all "FatProd" records within database.
     *
     * @return a list of all "FatProd"
     * @throws SQLException
     */
    public List<FatProdDTO> getFatProd() throws SQLException {

        CallableStatement callStmt = null;
        ResultSet resultSet = null;
        List<FatProdDTO> lstFatProd = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            callStmt = connection.prepareCall("{ ? = call getListaFatProd() }");
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.execute();
            resultSet = (ResultSet) callStmt.getObject(1);
            lstFatProd = resultFatProdToList(resultSet);
        } finally {
            if (!Objects.isNull(callStmt)) {
                callStmt.close();
            }
            if (!Objects.isNull(resultSet)) {
                resultSet.close();
            }
        }

        return lstFatProd;
    }

    /**
     * Converts the result set from the database to app object.
     *
     * @param resultSet
     * @return list of "FatProd".
     * @throws SQLException
     */
    private List<FatProdDTO> resultFatProdToList(ResultSet resultSet) throws SQLException {
        List<FatProdDTO> lstFatProd = new ArrayList<>();
        while (resultSet.next()) {
            FatProdDTO fatProd = new FatProdDTO(
                    resultSet.getString("idfatorproducao"),
                    resultSet.getString("fabricante"),
                    resultSet.getString("formato"),
                    resultSet.getString("tipo"),
                    resultSet.getString("aplicacao")
            );
            lstFatProd.add(fatProd);
        }
        return lstFatProd;
    }

    /**
     * Register RECEITA
     * @param receita
     * @return true for success and false for register with incidents
     * @throws SQLException
     */
    public boolean registerReceita(ReceitasDTO receita) throws SQLException {

        CallableStatement callStmt = null;

        boolean sucessoReceita = true;
        for (FatProdDTO ing : receita.getReceita()) {

            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();

                // Enable DBMS_OUTPUT
                try (CallableStatement enableStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.ENABLE(NULL); END;")) {
                    enableStmt.execute();
                }

                // Call function
                callStmt = connection.prepareCall("{ ? = call registarReceita(?,?,?,?) }");

                callStmt.registerOutParameter(1, OracleTypes.INTEGER);

                callStmt.setInt(2, receita.getIdReceita());
                callStmt.setString(3, ing.getFatProd());
                callStmt.setDouble(4, ing.getQuantidadePorHectar());
                callStmt.setString(5, ing.getUnidadeMedida());

                callStmt.execute();

                String errorMessage = "";
                try (CallableStatement readStmt = connection.prepareCall("BEGIN DBMS_OUTPUT.GET_LINE(?, ?); END;")) {
                    readStmt.registerOutParameter(1, OracleTypes.VARCHAR);
                    readStmt.registerOutParameter(2, OracleTypes.NUMERIC);

                    // Loop to retrieve messages until no more available
                    while (true) {
                        readStmt.setInt(2, 32000);  // Maximum line length
                        readStmt.execute();
                        String message = readStmt.getString(1);
                        if (message == null) {
                            break;  // No more messages
                        }
                        errorMessage = message;
                    }
                }

                int outcome = callStmt.getInt(1);
                connection.commit();

                if (outcome == 1) {
                    Utils.showMessageColor("Ingrediente registado com sucesso!", AnsiColor.GREEN);
                } else {
                    Utils.showMessageColor("Ingrediente não registado.\n" + errorMessage, AnsiColor.RED);
                    sucessoReceita = false;
                }

            } finally {
                if (!Objects.isNull(callStmt)) {
                    callStmt.close();
                }
            }
        }

        if (sucessoReceita) {
            Utils.showMessageColor("Receita registada com sucesso!", AnsiColor.GREEN);
        } else {
            Utils.showMessageColor("O registo da Receita encontrou incidentes.", AnsiColor.RED);
        }

        return sucessoReceita;
    }
}
