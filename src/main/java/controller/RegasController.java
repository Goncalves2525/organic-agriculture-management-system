package controller;

import dto.CultivosDTO;
import dto.FertirregasDTO;
import dto.RegaDTO;
import repository.CultivosRepository;
import repository.ParcelasRepository;
import repository.RegaRepository;
import repository.SetoresRegaRepository;
import tables.Regas;
import tables.SetoresRega;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class RegasController {

    private RegaRepository regaRepository;

    private ParcelasRepository parcelasRepository;

    private CultivosRepository cultivosRepository;

    private SetoresRegaRepository setoresRegaRepository;

    public RegasController() {
        getRegaController();
        getParcelasRepository();
        getCultivosRepository();
        getSetoresRegaRepository();
    }

    private SetoresRegaRepository getSetoresRegaRepository() {
        if (Objects.isNull(setoresRegaRepository)) {
            setoresRegaRepository = new SetoresRegaRepository();
        }
        return setoresRegaRepository;
    }


    private RegaRepository getRegaController() {
        if (Objects.isNull(regaRepository)) {
            regaRepository = new RegaRepository();
        }
        return regaRepository;
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

//    public List<Regas> getRegas() throws SQLException {
//        return regaRepository.getRegas();
//    }

    public boolean insertRegas(RegaDTO rega) throws SQLException {
        return regaRepository.insertRegas(rega);
    }

    public boolean insertFertirregas(FertirregasDTO fertirregas) throws  SQLException{
        return regaRepository.insertFertirregas(fertirregas);
    }
    public List<SetoresRega> getSetoresForRega() throws SQLException {
        return setoresRegaRepository.getSetoresForRega();
    }

}
