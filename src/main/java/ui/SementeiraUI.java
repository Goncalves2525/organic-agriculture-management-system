package ui;

import controller.SementeiraController;
import jdk.jshell.execution.Util;
import utils.Utils;

import java.sql.Date;
import java.sql.SQLException;

public class SementeiraUI implements Runnable{

    private SementeiraController controller;

    public SementeiraUI() {
        controller = new SementeiraController();
    }

    public void run() {
        try {
            System.out.println("Execute func_Registar_Semeadura:");

            int p_id_Quinta = 1;
            String parcela = Utils.readLineFromConsole("Parcela: ");
            int p_id_Operador = 123456789;
            String dataInicio = Utils.readLineFromConsole("Data de início, no formato yyyy-MM-dd:");
            //String dataFim = Utils.readLineFromConsole("Data de fim, no formato dd-MM-yyyy:");
            String dataFim = dataInicio;
            String produto = Utils.readLineFromConsole("Produto a semear:");
            double hectares = Utils.readDoubleFromConsole("Área a semear:");
            String p_unMedidaArea = Utils.readLineFromConsole("Unidade de medida da área:");
            double p_qtd = Utils.readDoubleFromConsole("Quantidade a semear:");
            String p_unMedida = Utils.readLineFromConsole("Unidade de medida da quantidade:");


            //controller.sementeirasRegister(1,101,0,123456789, Date.valueOf("2021-01-01"), Date.valueOf("2021-01-01"), 1, "kg");
            controller.sementeirasRegister(p_id_Quinta, parcela, produto, p_id_Operador, dataInicio, dataFim, p_qtd, p_unMedida, hectares, p_unMedidaArea);
        } catch (SQLException e ) {
            System.out.println("\nOperação de semeadura não registada, com o seguinte erro:\n" + e.getMessage());
        }
    }

}
