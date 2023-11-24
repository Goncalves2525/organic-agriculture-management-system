package utils;

import domain.Location;
import graphs.GFH;
import graphs.Graph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ImportGFHData {

    private GFH gfh = new GFH();

    /**
     * Runs the import data from Excels files.
     */
    public Graph<Location, Integer> run(String locaisPath, String distanciasPath) {
        Utils.showMessageColor("\nFiles import started.", AnsiColor.BLUE);
        if (importExcel(locaisPath, distanciasPath)) {
            Utils.showMessageColor("\nFiles imported successfuly.", AnsiColor.GREEN);
            //Utils.readLineFromConsole("Press Enter to continue.");
            return gfh.getGfh();
        } else {
            Utils.showMessageColor("\nFiles import failed.\nPlease, review file location or name.", AnsiColor.RED);
            //Utils.readLineFromConsole("Press Enter to continue.");
            return null;
        }
    }

    /**
     * Imports files with "locais" and "distancias" data.
     *
     * @param locaisPath
     * @param distanciasPath
     * @return true if successful and false if not.
     */
    private boolean importExcel(String locaisPath, String distanciasPath) {
        try {
            // Auxiliary path initializer
            File currentDirFile = new File(".");
            String pathSufix = currentDirFile.getAbsolutePath();
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1);

            // Path to locais Excel file
            String excelLocaisPath = pathSufix + locaisPath;

            // Run method for Excel file
            importLocais(excelLocaisPath);

            // Auxiliary path initializer
            currentDirFile = new File(".");
            pathSufix = currentDirFile.getAbsolutePath();
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1);

            // Path to distancias Excel file
            String excelDistanciasPath = pathSufix + distanciasPath;

            // Run method for Excel file
            importDistancias(excelDistanciasPath);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }        
    }

    private void importLocais(String excelLocaisPath){
        try {
            // load the data from file
            Scanner sc = new Scanner(new FileReader(excelLocaisPath)).useDelimiter("\n");

            // checking each line, adding it to string and each of this to arraylist
            sc.next();
            while (sc.hasNext()) {
                String[] array = sc.next().split(",");

                // get data to variables
                String code = array[0];
                int numberOfEmployees = Integer.parseInt(array[0].substring(2));
                double latitude = Double.parseDouble(array[1]);
                double longitude = Double.parseDouble(array[2]);
                int openHour;
                int closeHour;
                if (numberOfEmployees <= 105) {
                    openHour = 9;
                    closeHour = 14;
                } else if (numberOfEmployees <= 215) {
                    openHour = 11;
                    closeHour = 16;
                } else {
                    openHour = 12;
                    closeHour = 17;
                }

                Location location = new Location(code, numberOfEmployees, latitude, longitude, openHour, closeHour);

                // add to matrix
                gfh.addLocation(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importDistancias(String excelDistanciasPath){
        try {
            // load the data from file
            Scanner sc = new Scanner(new FileReader(excelDistanciasPath)).useDelimiter("\n");

            // checking each line, adding it to string and each of this to arraylist
            sc.next();
            while (sc.hasNext()) {
                String[] array = sc.next().split(",");

                // get data to variables
                String codeOrig = array[0];
                String codeDest = array[1];
                Integer distance = Integer.parseInt(array[2].split("\r")[0]);

                // add to matrix
                gfh.addDistance(codeOrig, codeDest, distance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
