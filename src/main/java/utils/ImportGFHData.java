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
    private boolean importExcel(String locaisPath, String distanciasPath) { // O(n)
        try {
            // Auxiliary path initializer
            File currentDirFile = new File("."); // O(1)
            String pathSufix = currentDirFile.getAbsolutePath(); // O(1)
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1); // O(1)

            // Path to locais Excel file
            String excelLocaisPath = pathSufix + locaisPath; // O(1)

            // Run method for Excel file
            importLocais(excelLocaisPath); // O(n)

            // Auxiliary path initializer
            currentDirFile = new File("."); // O(1)
            pathSufix = currentDirFile.getAbsolutePath(); // O(1)
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1); // O(1)

            // Path to distancias Excel file
            String excelDistanciasPath = pathSufix + distanciasPath; // O(1)

            // Run method for Excel file
            importDistancias(excelDistanciasPath); // O(n)

            return true;
        } catch (Exception e) {
            e.printStackTrace(); // O(1)
            return false;
        }        
    }

    private void importLocais(String excelLocaisPath){ // O(n)
        try {
            // load the data from file
            Scanner sc = new Scanner(new FileReader(excelLocaisPath)).useDelimiter("\n"); // O(1)

            // checking each line, adding it to string and each of this to arraylist
            sc.next(); // O(1)
            while (sc.hasNext()) { // O(n)
                String[] array = sc.next().split(","); // O(n)

                // get data to variables
                String code = array[0]; // O(1)
                int numberOfEmployees = Integer.parseInt(array[0].substring(2)); // O(1)
                double latitude = Double.parseDouble(array[1]); // O(1)
                double longitude = Double.parseDouble(array[2]); // O(1)
                int openHour; // O(1)
                int closeHour; // O(1)
                if (numberOfEmployees <= 105) { // O(1)
                    openHour = 9; // O(1)
                    closeHour = 14; // O(1)
                } else if (numberOfEmployees <= 215) { // O(1)
                    openHour = 11; // O(1)
                    closeHour = 16; // O(1)
                } else { // O(1)
                    openHour = 12; // O(1)
                    closeHour = 17; // O(1)
                }

                Location location = new Location(code, numberOfEmployees, latitude, longitude, openHour, closeHour); // O(1)

                // add to matrix
                gfh.addLocation(location); // O(1)
            }
        } catch (IOException e) {
            e.printStackTrace(); // O(1)
        }
    }

    private void importDistancias(String excelDistanciasPath){ // O(n)
        try {
            // load the data from file
            Scanner sc = new Scanner(new FileReader(excelDistanciasPath)).useDelimiter("\n"); // O(1)

            // checking each line, adding it to string and each of this to arraylist
            sc.next(); // O(1)
            while (sc.hasNext()) { // O(n)
                String[] array = sc.next().split(","); // O(1)

                // get data to variables
                String codeOrig = array[0]; // O(1)
                String codeDest = array[1]; // O(1)
                Integer distance = Integer.parseInt(array[2].split("\r")[0]); // O(1)

                // add to matrix
                gfh.addDistance(codeOrig, codeDest, distance); // O(1)
            }
        } catch (IOException e) {
            e.printStackTrace(); // O(1)
        }
    }
}
