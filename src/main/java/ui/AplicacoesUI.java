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
        int quintaID = 1;
        System.out.println("Nome da Parcela: ");
        String parcelaNome = sc.nextLine();
        System.out.println("Cultura ID: ");
        int culturaID = sc.nextInt();
        sc.nextLine();
        System.out.println("Operador ID: ");
        int opradorID = sc.nextInt();
        sc.nextLine();
        System.out.println("Data de Inicio: (AAAA-MM-DD)");
        Date dataInicio = Date.valueOf(sc.next());
        sc.nextLine();
        System.out.println("Fator de Produção: ");
        String fatorProducaoID = sc.nextLine();
        System.out.println("Quantidade: ");
        int quantidade = sc.nextInt();
        sc.nextLine();
        System.out.println("Unidade de Medida: ");
        String unidadeMedidaID = sc.nextLine();
        System.out.println("Area: ");
        float area = sc.nextFloat();

        int worked = controller.aplicacoesRegister(quintaID, parcelaNome, culturaID, opradorID, dataInicio, fatorProducaoID, quantidade, unidadeMedidaID, area);

        if (worked == 1) {
            System.out.println("\nAplicação registada com sucesso!");
        } else if (worked == 0){
            System.out.println("\nErro! Não foi possível registar a Aplicação");
        } else if (worked == -2){
            System.out.println("\nA área da parcela é inferior à área da aplicação");
        }
    }
}