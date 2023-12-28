package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SensoresUI implements Runnable {
    @Override
    public void run() {
        try {
            readCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean readCSVFile() throws IOException{
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        helper = helper.substring(0, helper.length() - 1);
        String outputPath = helper + "sensores/saidaDeDados/output/outputFile.csv";
        String outputInfo = "";
        FileInputStream fis = new FileInputStream(outputPath);
        int content;
        while ((content = fis.read()) != -1) {
            outputInfo += (char) content;
        }
        System.out.println(outputInfo);
        return true;
    }
}
