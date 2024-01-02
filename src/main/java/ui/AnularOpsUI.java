package ui;

import controller.AplicacoesController;
import controller.OperacoesController;
import jdk.jshell.execution.Util;
import utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class AnularOpsUI implements Runnable {

    private OperacoesController controller;

    public AnularOpsUI() {
        controller = new OperacoesController();
    }
    @Override
    public void run() {
        try {
            int idQuinta = Utils.readIntegerFromConsole("Selecione o ID da Quinta:");
            int idOperacao = Utils.readIntegerFromConsole("Selecione o ID da Operação a anular:");
            controller.anularOperacao(idQuinta,idOperacao);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
