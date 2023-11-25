package controller;

import dto.CultivosDTO;
import repository.ColheitasRepository;
import tables.Colheitas;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ColheitasController {

    private ColheitasRepository colheitasRepository;

    public ColheitasController() { getColheitasRepository(); };

    private ColheitasRepository getColheitasRepository() {
        if (Objects.isNull(colheitasRepository)) {
            colheitasRepository = new ColheitasRepository();
        }
        return colheitasRepository;
    }

    public List<Colheitas> getColheitas() throws SQLException {
        return colheitasRepository.getColheitas();
    }

    public boolean registerColheitas (CultivosDTO cultivos) throws SQLException {
        return colheitasRepository.registerColheitas(cultivos);
    }
}
