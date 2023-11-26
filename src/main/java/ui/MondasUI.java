package ui;

import controller.MondasController;
import dto.CultivosDTO;
import dto.ParcelaDTO;
import tables.Mondas;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

public class MondasUI implements Runnable {

    private MondasController ctrlMondas;

    public MondasUI() {
        ctrlMondas = new MondasController();
    }

    @Override
    public void run() {

        printTable();

        try {
        CultivosDTO mondas;

        boolean option = false;

        do {
            Utils.showMessageColor("Escolha um produto de um dado cultivo, para proceder à MONDA.", AnsiColor.BLUE);
            mondas = (CultivosDTO) Utils.showAndSelectOneNoCancel(ctrlMondas.getCultivosForMondas(), "CULTIVOS:");

            //area
            double quantidade = Utils.readDoubleFromConsole("Qual é a área em 'ha' em que deseja aplicar a operação MONDA?");
            mondas.setQuantidade(quantidade);

            String dataMonda = Utils.readLineFromConsole("Qual a data em que foi realizada a monda? (aaaa-mm-dd)");
            mondas.setDataColheita(dataMonda);

            Utils.showMessageColor("\nVou proceder ao registo da monda de: \n" +
                            mondas + "\n Data: " + dataMonda +
                            " | Área:: " + mondas.getQuantidade()
                    , AnsiColor.CYAN);

            option = Utils.confirm("Confirma o registo com estes dados? (y/n)");
            if (option) {
                break;
            }

        } while (Utils.confirm("Quer tentar de novo? (y/n)"));

        if (option) {
            ctrlMondas.insertMondas(mondas);
        }

        Utils.readLineFromConsole("Press Enter to continue.");

        } catch(
            SQLException e)
            {
        e.printStackTrace();
    }

        printTable();
}

    private void printTable(){
        try {
            Utils.showMessageColor("\nTESTE: EXECUTAR METODO PRINT DA CLASSE MONDAS:", AnsiColor.BLUE);
            for (Mondas mondas : ctrlMondas.getMondas()){
                mondas.print();
            }
            Utils.readLineFromConsole("Press Enter to continue.");
            ctrlMondas.getMondas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
