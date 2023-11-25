package controller;

import dto.CultivosDTO;
import repository.CultivosRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CultivosController {
    private CultivosRepository cultivosRepository;

    public CultivosController() { getCultivosRepository(); };

    private CultivosRepository getCultivosRepository() {
        if (Objects.isNull(cultivosRepository)) {
            cultivosRepository = new CultivosRepository();
        }
        return cultivosRepository;
    }

    public List<CultivosDTO> getCultivos() throws SQLException {
        return cultivosRepository.getCultivos();
    }

}
