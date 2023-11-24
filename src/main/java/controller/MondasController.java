package controller;

import repository.MondasRepository;
import tables.Mondas;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MondasController {

    private MondasRepository mondasRepository;

    public MondasController() {getMondasRepository();}

    private MondasRepository getMondasRepository() {
        if (Objects.isNull(mondasRepository)) {
            mondasRepository = new MondasRepository();
        }
        return mondasRepository;
    }

    public List<Mondas> getMondas() throws SQLException {
        return mondasRepository.getMondas();
    }

    public int insertMondas(int operacaoId, int quantidade, String unidadeMedida) throws SQLException {
        int worked = mondasRepository.insertMondas(operacaoId, quantidade, unidadeMedida);
        return worked;
    }
}
