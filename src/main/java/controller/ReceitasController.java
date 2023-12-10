package controller;

import dto.FatProdDTO;
import dto.ReceitasDTO;
import repository.ReceitasRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReceitasController {

    private ReceitasRepository receitasRepository;

    public ReceitasController() { getReceitasRepository(); };

    private ReceitasRepository getReceitasRepository() {
        if (Objects.isNull(receitasRepository)) {
            receitasRepository = new ReceitasRepository();
        }
        return receitasRepository;
    }

    public List<ReceitasDTO> getReceitas() throws SQLException {
        return receitasRepository.getReceitas();
    }

    public List<FatProdDTO> getFatProd() throws SQLException {
        return receitasRepository.getFatProd();
    }

    public boolean registerReceita(ReceitasDTO receita) throws SQLException {
        return receitasRepository.registerReceita(receita);
    }
}
