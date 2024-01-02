package controller;

import repository.LogsRepository;
import repository.OperacoesRepository;
import tables.Logs;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class OperacoesController {
    private OperacoesRepository operacoesRepository;

    public OperacoesController() {
        getOperacoesRepository();
    }

    private OperacoesRepository getOperacoesRepository() {
        if (Objects.isNull(operacoesRepository)) {
            operacoesRepository = new OperacoesRepository();
        }
        return operacoesRepository;
    }

    public void anularOperacao(int idQuinta, int idOperacao) throws SQLException {
        operacoesRepository.anularOperacao(idQuinta,idOperacao);
    }
}
