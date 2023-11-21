package ui;

import controller.AplicacoesController;
import tables.AplicacoesFatProd;

import java.sql.Date;
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Registar Aplicação");
        System.out.println("------------------\n");
        System.out.println("Operação ID: ");
        int operacaoID = sc.nextInt();
        int quintaID = 1;
        System.out.println("Parcela ID: ");
        int parcelaID = sc.nextInt();
        System.out.println("Cultura ID: ");
        int culturaID = sc.nextInt();
        System.out.println("Operador ID: ");
        int opradorID = sc.nextInt();
        System.out.println("Data de Inicio: (AAAA-MM-DD)");
        Date dataInicio = Date.valueOf(sc.next());
        System.out.println("Fator de Produção: ");
        String fatorProducaoID = sc.next();
        System.out.println("Quantidade: ");
        int quantidade = sc.nextInt();
        System.out.println("Unidade de Medida: ");
        String unidadeMedidaID = sc.next();

        int worked = controller.aplicacoesRegister(operacaoID, quintaID, parcelaID, culturaID, opradorID, dataInicio, fatorProducaoID, quantidade, unidadeMedidaID);

        if (worked == 1) {
            System.out.println("\nAplicação registada com sucesso!");
        } else if (worked == 0){
            System.out.println("\nOperação já existe.");
        }
    }
}