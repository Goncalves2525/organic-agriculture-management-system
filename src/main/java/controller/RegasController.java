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
import java.util.Date;
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

    public boolean insertFertirregas(FertirregasDTO fertirregas) throws SQLException {
        return regaRepository.insertFertirregas(fertirregas);
    }

    public List<SetoresRega> getSetoresForRega() throws SQLException {
        return setoresRegaRepository.getSetoresForRega();
    }

    public String registarRega(int p_id_Quinta, int p_id_Parcela, int p_id_Operador, int p_quantidade, String p_unMedida, int p_Setor, String dataInicio, String horaInicio) throws SQLException {
        return regaRepository.registarRega(p_id_Quinta, p_id_Parcela, p_id_Operador, p_quantidade, p_unMedida, p_Setor, dataInicio, horaInicio);
    }

    public String registarFertiRega(int p_id_Quinta, int p_id_Parcela, int p_id_Operador, int p_quantidade, String p_unMedida, int p_Setor, int p_receitaID, String dataInicio, String horaInicio) throws SQLException {
        return regaRepository.registarFertiRega(p_id_Quinta, p_id_Parcela, p_id_Operador, p_quantidade, p_unMedida, p_Setor, p_receitaID, dataInicio, horaInicio);
    }
}
