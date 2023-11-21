package ui;

import main.lapr3.controller.SementeiraController;

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
            controller.sementeirasRegister(1,1,1,new Date(1),new Date(1),1,"");
            System.out.println("\nDone.");

        } catch (SQLException e ) {
            System.out.println("\nNot executed!\n" + e.getMessage());
        }
    }
}
