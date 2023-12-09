package ui;

import controller.LogsController;
import tables.Logs;
import utils.AnsiColor;
import utils.Utils;

import java.util.List;

public class LogsUI implements Runnable{
    private LogsController controller;

    public LogsUI() {
        controller = new LogsController();
    }

    @Override
    public void run() {
        List<Logs> logs = null;
        Utils.showMessageColor("LOGS", AnsiColor.BLUE);

        try {
            logs = controller.getLogs();
        } catch (Exception e) {
            System.out.println("\nNão foi buscar os Logs.\n\n" + e.getMessage());
        }

        try {
            for (Logs log : logs) {
                System.out.println(log);
            }
        } catch (NullPointerException e) {
            System.out.println("\nNão existem Logs");
        }

        Utils.readLineFromConsole("Press Enter to continue.");
    }
}
