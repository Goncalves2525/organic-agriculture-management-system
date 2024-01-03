package ui;

import controller.RegasController;
import dto.FertirregasDTO;
import dto.RegaDTO;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

public class RegaFertiregaRegisterUI implements Runnable{

    private RegasController regasCtrl;

    public RegaFertiregaRegisterUI(){regasCtrl = new RegasController();}

    @Override
    public void run() {
        int type = regaOrFertirega();
        if (type == 1) {
            try {
                RegaDTO rega = new RegaDTO(); // Initialize the variable
                boolean option = false;

                do {
                    regasCtrl.getSetoresForRega();
                    int setor = Utils.readIntegerFromConsole("Escolha um algarismo de um dado SETOR, para proceder à REGA.");
                    rega.setSetorId(setor);
                    double quantidade = Utils.readDoubleFromConsole("Qual é a quantidade em minutos da rega?");
                    rega.setQuantidade(quantidade);
                    String horaInicio = Utils.readLineFromConsole("Qual é a hora de inicio (hh:mm)?");
                    rega.setHoraInicio(horaInicio);
                    Utils.showMessageColor("\nVou proceder ao registo da rega de: \n" + rega, AnsiColor.CYAN);

                    option = Utils.confirm("Confirma o registo com estes dados? (y/n)");
                    if (option) {
                        break;
                    }
                } while (Utils.confirm("Quer tentar de novo? (y/n)"));

                if (option) {
                    regasCtrl.insertRegas(rega);
                }

                Utils.readLineFromConsole("Press Enter to continue.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (type == 2) {
            try {
                FertirregasDTO fertirrega = new FertirregasDTO(); // Initialize the variable
                boolean option = false;

                do {
                    regasCtrl.getSetoresForRega();
                    int setor = Utils.readIntegerFromConsole("Escolha um algarismo de um dado SETOR, para proceder à REGA.");
                    fertirrega.setSetor(setor);
                    String dataInicio = Utils.readLineFromConsole("Indique a data de inicio da operação (YYYY/MM/DD): ");
                    fertirrega.setDataInicio(dataInicio);
                    double quantidade = Utils.readDoubleFromConsole("Qual é a quantidade em minutos da rega?");
                    fertirrega.setQuantidade(quantidade);
                    String horaInicio = Utils.readLineFromConsole("Qual é a hora de inicio (hh:mm)?");
                    fertirrega.setHoraInicio(horaInicio);
                    int receitaId = Utils.readIntegerFromConsole("Indique o id da receita:");
                    fertirrega.setReceitaId(receitaId);
                    Utils.showMessageColor("\nVou proceder ao registo da rega de: \n" + fertirrega, AnsiColor.CYAN);
                    option = Utils.confirm("Confirma o registo com estes dados? (y/n)");
                    if (option) {
                        break;
                    }
                } while (Utils.confirm("Quer tentar de novo? (y/n)"));

                if (option) {
                    regasCtrl.insertFertirregas(fertirrega);
                }

                Utils.readLineFromConsole("Press Enter to continue.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int regaOrFertirega(){
        int option = Utils.readIntegerFromConsole("Press one of the following options:\n" +
                "1. I want to insert REGA.\n" +
                "2. I want to insert Fertirrega.");
        if(option != 1 && option !=2){
            regaOrFertirega();
        }
        return option;
    }
}
