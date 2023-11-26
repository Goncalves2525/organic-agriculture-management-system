package controller;

import dto.ParcelaDTO;
import repository.MondasRepository;
import repository.ParcelasRepository;
import tables.Mondas;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MondasController {

    private MondasRepository mondasRepository;

    private ParcelasRepository parcelasRepository;

    public MondasController() {getMondasRepository(); getParcelasRepository();}


    private MondasRepository getMondasRepository() {
        if (Objects.isNull(mondasRepository)) {
            mondasRepository = new MondasRepository();
        }
        return mondasRepository;
    }

    private ParcelasRepository getParcelasRepository() {
        if (Objects.isNull(parcelasRepository)) {
            parcelasRepository = new ParcelasRepository();
        }
        return parcelasRepository;
    }


    public List<Mondas> getMondas() throws SQLException {
        return mondasRepository.getMondas();
    }

    public boolean insertMondas(ParcelaDTO mondas) throws SQLException {
        return mondasRepository.insertMondas(mondas);
    }

    public List<ParcelaDTO> getParcelas() throws SQLException {
        return parcelasRepository.getParcelas();
    }
}
