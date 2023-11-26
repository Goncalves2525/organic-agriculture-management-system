package controller;

import dto.CultivosDTO;
import dto.ParcelaDTO;
import repository.CultivosRepository;
import repository.MondasRepository;
import repository.ParcelasRepository;
import tables.Mondas;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MondasController {

    private MondasRepository mondasRepository;

    private ParcelasRepository parcelasRepository;

    private CultivosRepository cultivosRepository;

    public MondasController() {getMondasRepository(); getParcelasRepository(); getCultivosRepository();}


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

    private CultivosRepository getCultivosRepository() {
        if (Objects.isNull(cultivosRepository)) {
            cultivosRepository = new CultivosRepository();
        }
        return cultivosRepository;
    }

    public List<Mondas> getMondas() throws SQLException {
        return mondasRepository.getMondas();
    }

    public boolean insertMondas(CultivosDTO mondas) throws SQLException {
        return mondasRepository.insertMondas(mondas);
    }

    public List<CultivosDTO> getCultivosForMondas() throws SQLException {
        return cultivosRepository.getCultivosForMondas();
    }
}
