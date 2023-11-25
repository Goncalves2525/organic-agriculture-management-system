package ui;

import controller.ColheitasController;
import controller.CultivosController;
import dto.CultivosDTO;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;
import java.util.Date;

public class ColheitasUI implements Runnable {

    private ColheitasController ctrlColheitas;
    private CultivosController ctrlCultivos;

    public ColheitasUI() {
        ctrlColheitas = new ColheitasController();
        ctrlCultivos = new CultivosController();
    }

    public void run() {
        try {

            CultivosDTO cultivoPicked;

            boolean option = false;
            do {
                Utils.showMessageColor("Escolha um produto de um dado cultivo, para proceder à COLHEITA.", AnsiColor.BLUE);
                cultivoPicked = (CultivosDTO) Utils.showAndSelectOneNoCancel(ctrlCultivos.getCultivos(), "CULTIVOS:");

                int quantidade = Utils.readIntegerFromConsole("Quantas unidades foram colhidas?");
                cultivoPicked.setQuantidade(quantidade);

                String dataColheita = Utils.readLineFromConsole("Qual a data em que foi realizada a colheita? (aaaa-mm-dd)");
                cultivoPicked.setDataColheita(dataColheita);

                Utils.showMessageColor("\nVou proceder ao registo da colheita de: \n" +
                                cultivoPicked + "\n Data: " + dataColheita +
                                " | Quantidade: " + cultivoPicked.getQuantidade()
                        , AnsiColor.CYAN);

                option = Utils.confirm("Confirma o registo com estes dados? (y/n)");
                if (option) { break; }

            } while (Utils.confirm("Quer tentar de novo? (y/n)"));

            if (option) {
                ctrlColheitas.registerColheitas(cultivoPicked);
            }

            Utils.readLineFromConsole("Press Enter to continue.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
