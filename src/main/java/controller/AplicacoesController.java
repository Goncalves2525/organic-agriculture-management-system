package controller;

import repository.AplicacoesRepository;
import tables.AplicacoesFatProd;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AplicacoesController {
    private AplicacoesRepository aplicacoesRepository;

    public AplicacoesController(){
        getAplicacoesRepository();
    }

    private AplicacoesRepository getAplicacoesRepository() {
        if (Objects.isNull(aplicacoesRepository)) {
            aplicacoesRepository = new AplicacoesRepository();
        }
        return aplicacoesRepository;
    }

    public List<AplicacoesFatProd> getAplicacoes() throws SQLException {
        List<AplicacoesFatProd> aplicacoesList = aplicacoesRepository.getAplicacoes();
        return aplicacoesList;
    }

    public int aplicacoesRegister(int operacaoID, int quintaID, int parcelaID, int culturaID, int operadorID, Date dataInicio, String fatorProducaoID, int quantidade, String unidadeMedidaID) throws SQLException {
        int worked = aplicacoesRepository.aplicacoesRegister(operacaoID, quintaID, parcelaID, culturaID, operadorID, dataInicio, fatorProducaoID, quantidade, unidadeMedidaID);
        return worked;
    }
}