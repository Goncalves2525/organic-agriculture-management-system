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

    public void sementeirasRegister(int p_id_Quinta, String parcela, String cultura, int p_id_Operador, String dataInicio, String dataFim, double p_qtdSementeira, String p_unMedidaSementeira, double p_qtdArea, String p_unMedidaArea) throws SQLException {
        sementeiraRepository.sementeirasRegister(p_id_Quinta,parcela, cultura, p_id_Operador, dataInicio, dataFim, p_qtdSementeira, p_unMedidaSementeira, p_qtdArea, p_unMedidaArea);
        //sementeiraRepository.funcTeste();
    }
}

