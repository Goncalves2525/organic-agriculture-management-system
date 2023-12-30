package controller;

import domain.Location;
import graphs.Graph;
import utils.ImportFarmOperationsLegacy;
import utils.ImportGFHData;

public class ImportDataCtrl {

    private ImportFarmOperationsLegacy importFarmOperationsLegacy = new ImportFarmOperationsLegacy();
    private ImportGFHData importGFHData = new ImportGFHData();

    public ImportDataCtrl() {
    }

    public void runImportFarmOperationsLegacyData (String location){
        importFarmOperationsLegacy.run(location);
    }

    public void exportDataToSqlScript(){
        importFarmOperationsLegacy.exportToSqlScript();
    }

    public void printDataToTerminal(){
        importFarmOperationsLegacy.print();
    }

    public void addExtraDataSprint2() {
        importFarmOperationsLegacy.addExtraDataSprint2();
    }

    public void addExtraDataSprint3() {
        importFarmOperationsLegacy.addExtraDataSprint3();
    }

    public Graph<Location, Integer> runImportGFHData(String locaisPath, String distanciasPath) { return importGFHData.run(locaisPath, distanciasPath);}

    public void printGFHData() { importGFHData.toString(); }
}
