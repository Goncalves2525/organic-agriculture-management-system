package controller;

import repository.SementeiraRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class SementeiraController {

    private SementeiraRepository sementeiraRepository;

    public SementeiraController(){
        getSementeiraRepository();
    }

    private SementeiraRepository getSementeiraRepository() {
        if (Objects.isNull(sementeiraRepository)) {
            sementeiraRepository = new SementeiraRepository();
        }
        return sementeiraRepository;
    }

    public void getSementeiras() throws SQLException {
        sementeiraRepository.getSementeiras();
    }

    public void sementeirasRegister(int idCultivo, int idParcela, int idOperador, Date dataInicio, Date dataFim, int qtd, String name) throws SQLException {
        //sementeiraRepository.sementeirasRegister(idCultivo, idParcela, idOperador, dataInicio, dataFim, qtd, name);
        sementeiraRepository.funcTeste();
    }
}

