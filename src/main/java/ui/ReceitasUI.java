package ui;

import controller.ReceitasController;
import dto.FatProdDTO;
import dto.ReceitasDTO;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

public class ReceitasUI implements Runnable {

    private ReceitasController ctrlReceitas;

    public ReceitasUI() {
        this.ctrlReceitas = new ReceitasController();
    }

    @Override
    public void run() {
        int option = 0;
        do {
            Utils.showMessageColor("\nRECEITAS", AnsiColor.CYAN);
            System.out.println("1 - Listar Receitas");
            System.out.println("2 - Registar Receita");
            System.out.println("0 - Menu Principal");

            option = Utils.readIntegerFromConsole("Escolha uma opção: ");
            switch (option) {
                case 1:
                    getReceitas();
                    break;
                case 2:
                    registerReceita();
                    break;
                case 0:
                    break;
                default:
                    Utils.showMessageColor("Opção inválida!", AnsiColor.RED);
                    Utils.readLineFromConsole("Press Enter to continue.");
            }
        } while (option != 0);
    }

    private void getReceitas() {
        try {
            for (ReceitasDTO id : ctrlReceitas.getReceitas()) {
                Utils.showMessageColor("\nID Receita: " + id.getIdReceita(), AnsiColor.CYAN);
                for (FatProdDTO ing : id.getReceita()) {
                    System.out.println(ing.getFatProd() + ": "
                            + ing.getQuantidadePorHectar() + ing.getUnidadeMedida() + "/ha");
                }
            }
            Utils.readLineFromConsole("Press Enter to continue.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean registerReceita() {
        try {

            int idReceita = Utils.readIntegerFromConsole("Indique o ID da Receita que irá registar:");
            ReceitasDTO receita = new ReceitasDTO(idReceita);

            boolean receitaComIngrediente = false;
            do {
                Utils.showMessageColor("\nEscolha um fator de produção para incluir na receita.\n", AnsiColor.BLUE);
                FatProdDTO fatProd = (FatProdDTO) Utils.showAndSelectOneNoCancel(ctrlReceitas.getFatProd(), "FATORES PRODUÇÃO:");

                double quantidade = Utils.readDoubleFromConsole("Qual a quantidade por hectar a usar?");

                String unidade = Utils.readLineFromConsole("Qual a unidade de medida a considerar (ex: kg)?");

                Utils.showMessageColor("\nVou proceder ao registo do ingrediente, na receita ID " + idReceita + ": \n" +
                                fatProd.getFatProd() + " | Quantidade: " + quantidade + unidade + "/ha"
                        , AnsiColor.CYAN);

                if (Utils.confirm("Confirma o registo deste ingrediente? (y/n)")) {
                    receita.getReceita().add(new FatProdDTO(fatProd.getFatProd(), quantidade, unidade));
                    Utils.showMessageColor("\nIngrediente registado.", AnsiColor.GREEN);
                    receitaComIngrediente = true;
                }

            } while (Utils.confirm("Quer adicionar outro ingrediente? (y/n)"));

            if (receitaComIngrediente) {
                Utils.showMessageColor("\nVou proceder ao registo da seguinte receita:", AnsiColor.CYAN);
                Utils.showMessageColor("ID Receita " + receita.getIdReceita(), AnsiColor.CYAN);
                for (FatProdDTO ing : receita.getReceita()) {
                    Utils.showMessageColor(" » " + ing.getFatProd()
                            + " | Quantidade: " + ing.getQuantidadePorHectar() + ing.getUnidadeMedida() + "/ha", AnsiColor.CYAN);
                }

                if (Utils.confirm("Valida o registo da receita apresentada? (y/n)")) {
                    ctrlReceitas.registerReceita(receita);
                }
            } else {
                Utils.showMessageColor("\nNão há nenhuma receita a registar.\n", AnsiColor.YELLOW);
            }

            Utils.readLineFromConsole("Press Enter to continue.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
