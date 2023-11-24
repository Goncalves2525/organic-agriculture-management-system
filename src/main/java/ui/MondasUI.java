package ui;

import controller.MondasController;
import tables.Mondas;
import utils.AnsiColor;
import utils.Utils;

import java.sql.SQLException;

public class MondasUI implements Runnable{

    private MondasController ctrl;

    public MondasUI() {ctrl = new MondasController();}

    private int OPERACAOID;
    private int quantidade;
    private String UNIDADEMEDIDA;

    @Override
    public void run() {
        printTable();

        int operacaoId = Utils.readIntegerFromConsole("Enter OPERACAOID: ");

        int quantidade = Utils.readIntegerFromConsole("Enter quantidade: ");

        String unidadeMedida = Utils.readLineFromConsole("Enter UNIDADEMEDIDA: ");

        int worked = 0;
        try {
            worked = ctrl.insertMondas(operacaoId, quantidade, unidadeMedida);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        printTable();

        if (worked == 1) {
            System.out.println("\nMonda registada com sucesso!");
        } else if (worked == 0){
            System.out.println("\nMonda já existe.");
        }
    }


    private void printTable(){
        try {
            Utils.showMessageColor("\nTESTE: EXECUTAR METODO PRINT DA CLASSE MONDAS:", AnsiColor.BLUE);
            for (Mondas mondas : ctrl.getMondas()){
                mondas.print();
            }
            Utils.readLineFromConsole("Press Enter to continue.");
            ctrl.getMondas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
