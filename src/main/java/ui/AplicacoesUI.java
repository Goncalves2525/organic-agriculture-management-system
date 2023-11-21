package ui;

import controller.AplicacoesController;
import tables.AplicacoesFatProd;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AplicacoesUI implements Runnable {
    private AplicacoesController controller;

    public AplicacoesUI() {
        controller = new AplicacoesController();
    }

    public void run() {
        System.out.println("APLICAÇÕES");
        Scanner sc = new Scanner(System.in);
        System.out.println("1 - Ver todas as Aplicações");
        System.out.println("2 - Registar Aplicação");

        int opcao = sc.nextInt();
        switch (opcao) {
            case 1:
                try {
                    getAplicacoes();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try {
                    aplicacoesRegister();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }


    public void getAplicacoes() throws SQLException{
        List<AplicacoesFatProd> aplicacoesList = null;

        try {
            aplicacoesList = controller.getAplicacoes();
        } catch (SQLException e) {
            System.out.println("\nNão foi buscar as Aplicações.\n\n" + e.getMessage());
        }

        try {
            for (AplicacoesFatProd aplicacoes : aplicacoesList) {
                System.out.println(aplicacoes);
            }
        } catch (NullPointerException e) {
            System.out.println("\nNão existem aplicações");
        }
    }

    public void aplicacoesRegister() throws SQLException {
        boolean worked = controller.aplicacoesRegister(2, 1, 10, "m2");

        if (worked) {
            System.out.println("\nAplicação registada com sucesso!");
        } else {
            System.out.println("\nNão foi possível registar a aplicação!");
        }
    }
}