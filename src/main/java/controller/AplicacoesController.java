package controller;

import repository.AplicacoesRepository;
import tables.AplicacoesFatProd;

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

    public boolean aplicacoesRegister(int operacaoId, int fatorProducaoId, int quantidade, String unidadeMedidaId) throws SQLException {
        boolean worked = aplicacoesRepository.aplicacoesRegister(operacaoId, fatorProducaoId, quantidade, unidadeMedidaId);
        return worked;
    }
}