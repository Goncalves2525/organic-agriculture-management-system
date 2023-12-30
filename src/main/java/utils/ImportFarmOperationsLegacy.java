package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import repository.TablesRepository;
import tables.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportFarmOperationsLegacy {

    private TablesRepository tblRepo = new TablesRepository();

    /**
     * Constructor
     */
    public ImportFarmOperationsLegacy() {
    }

    /**
     * Runs the import legacy data from Excel.
     */
    public void run(String location) {
        Utils.showMessageColor("\nFiles import started.", AnsiColor.BLUE);
        if (importExcel(location)) {
            Utils.showMessageColor("\nFiles imported successfuly.", AnsiColor.GREEN);
            Utils.readLineFromConsole("Press Enter to continue.");
        } else {
            Utils.showMessageColor("\nFiles import failed.\nPlease, review file location or name.", AnsiColor.RED);
            Utils.readLineFromConsole("Press Enter to continue.");
        }
    }

    /**
     * Imports all data within each sheet of the Excel file.
     *
     * @return true if all was successfuly imported and false if any error occurs.
     */
    private boolean importExcel(String location) {
        try {
            // Pre-Import : manual added data
            createManualData();

            // Auxiliary path initializer
            File currentDirFile = new File(".");
            String pathSufix = currentDirFile.getAbsolutePath();
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1);

            // Path to Excel file
            String excelFilePath = pathSufix + location;

            // Run method for each Excel Sheet
            importSheetPlantas(excelFilePath);
            importSheetFatorProducao(excelFilePath);
            importSheetExploracaoAgricola(excelFilePath);
            importSheetCulturas(excelFilePath);
            importSheetOperacoes(excelFilePath);

            // Run method to add extra data provided for Sprint2
            addExtraDataSprint2();

            // Run method to add extra data provided for Sprint3
            addExtraDataSprint3();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //////// INDIVIDUAL SHEET IMPORT ////////////////////////////////////////////////////////////////////////

    private void importSheetPlantas(String excelFilePath) throws IOException {

        // Load the Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Get the sheet
        Sheet sheet = workbook.getSheet("Plantas");

        // Iterate through rows and cells to read data
        for (Row row : sheet) {

            // Setup columns variables
            String especie = null;
            String nomeComum = null;
            String nomeComumOrig = null;
            String variedade = null;
            String tipoPlantacao = null;
            String tempoSementeiraPlantacao = null;
            String tempoPoda = null;
            String tempoFloracao = null;
            String tempoColheita = null;

            // Skips header row and goes through each row of the sheet!
            if (row.getRowNum() != 0) {
                // Goes through each cell of the row to alocate data to column variables
                if (row.getCell(0) != null) {
                    especie = row.getCell(0).getStringCellValue().toUpperCase();
                }
                if (row.getCell(1) != null) {
                    nomeComumOrig = row.getCell(1).getStringCellValue().toUpperCase();
                    nomeComum = nomeComumOrig.replace(" ", "_").replace("Ç", "C");
                }
                if (row.getCell(2) != null) {
                    variedade = row.getCell(2).getStringCellValue().toUpperCase();
                }
                if (row.getCell(3) != null) {
                    tipoPlantacao = row.getCell(3).getStringCellValue().toUpperCase();
                }
                if (row.getCell(4) != null) {
                    tempoSementeiraPlantacao = row.getCell(4).getStringCellValue();
                }
                if (row.getCell(5) != null) {
                    tempoPoda = row.getCell(5).getStringCellValue();
                }
                if (row.getCell(6) != null) {
                    tempoFloracao = row.getCell(6).getStringCellValue();
                }
                if (row.getCell(7) != null) {
                    tempoColheita = row.getCell(7).getStringCellValue();
                }
            }

            //// Creates objects for tables that require this data:
            // TiposCultura
            if (tipoPlantacao != null) {
                tblRepo.addToTableTiposCultura(tipoPlantacao);
            }
            // EspeciesVegetais
            if (especie != null) {
                tblRepo.addToTableEspeciesVegetais(especie, nomeComum);
            }
            // Culturas
            if (variedade != null) {
                String nomeCompleto = nomeComumOrig + " " + variedade;
                tblRepo.addToTableCulturas(tipoPlantacao, especie, variedade, nomeCompleto, nomeComum);
            }
            // Produtos
            if (nomeComumOrig != null) {
                String nomeCompleto = nomeComum + " " + variedade;
                tblRepo.addToTableProdutos(nomeComum, nomeCompleto);
            }
            // Ciclos
            if (tempoSementeiraPlantacao != null) {
                String operacao = "SEMENTEIRA";
                if (tipoPlantacao.equals("PERMANENTE")) {
                    operacao = "PLANTAÇÃO";
                }
                tblRepo.addToTableCiclos(nomeComumOrig, tempoSementeiraPlantacao, operacao);
            }

            if (tempoPoda != null) {
                String operacao = "PODA";
                tblRepo.addToTableCiclos(nomeComumOrig, tempoPoda, operacao);
            }

            if (tempoFloracao != null) {
                String operacao = "FLORAÇÃO";
                tblRepo.addToTableCiclos(nomeComumOrig, tempoFloracao, operacao);
            }

            if (tempoColheita != null) {
                String operacao = "COLHEITA";
                tblRepo.addToTableCiclos(nomeComumOrig, tempoColheita, operacao);
            }
        }

        // Close the FileInputStream and workbook
        fis.close();
        workbook.close();
    }

    private void importSheetFatorProducao(String excelFilePath) throws IOException {

        // Load the Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Set sheet name
        String sheetName = "Fator Produção";

        // Get the sheet
        Sheet sheet = workbook.getSheet(sheetName);

        // Iterate through rows and cells to read data
        for (Row row : sheet) {

            // Setup columns variables
            String designacao = null;
            String fabricante = null;
            String formato = null;
            String tipo = null;
            String aplicacao = null;
            String c1 = null;
            double c1Perc = 0;
            String c2 = null;
            double c2Perc = 0;
            String c3 = null;
            double c3Perc = 0;
            String c4 = null;
            double c4Perc = 0;
            int idFichaTecnica = 0;

            // Skips header row and goes through each row of the sheet!
            if (row.getRowNum() != 0) {
                // Goes through each cell of the row to alocate data to column variables
                if (row.getCell(0) != null) {
                    designacao = row.getCell(0).getStringCellValue().toUpperCase();
                }
                if (row.getCell(1) != null) {
                    fabricante = row.getCell(1).getStringCellValue().toUpperCase();
                }
                if (row.getCell(2) != null) {
                    formato = row.getCell(2).getStringCellValue().toUpperCase();
                }
                if (row.getCell(3) != null) {
                    tipo = row.getCell(3).getStringCellValue().toUpperCase();
                }
                if (row.getCell(4) != null) {
                    aplicacao = row.getCell(4).getStringCellValue().toUpperCase();
                }
                if (row.getCell(5) != null) {
                    c1 = row.getCell(5).getStringCellValue();
                }
                if (row.getCell(6) != null) {
                    c1Perc = row.getCell(6).getNumericCellValue();
                }
                if (row.getCell(7) != null) {
                    c2 = row.getCell(7).getStringCellValue();
                }
                if (row.getCell(8) != null) {
                    c2Perc = row.getCell(8).getNumericCellValue();
                }
                if (row.getCell(9) != null) {
                    c3 = row.getCell(9).getStringCellValue();
                }
                if (row.getCell(10) != null) {
                    c3Perc = row.getCell(10).getNumericCellValue();
                }
                if (row.getCell(11) != null) {
                    c4 = row.getCell(11).getStringCellValue();
                }
                if (row.getCell(12) != null) {
                    c4Perc = row.getCell(12).getNumericCellValue();
                }
            }

            //// Creates objects for tables that require this data:
            // Formulacoes
            if (formato != null) {
                tblRepo.addToTableFormulacoes(formato, tipo, aplicacao);
            }

            // FichasTecnicas
            if (designacao != null) {
                idFichaTecnica = tblRepo.addToTableFichasTecnicas(designacao);
            }

            // Substancias e ComponentesFT
            if (c1 != null) {
                int subsId = tblRepo.addToTableSubstancias(c1, c1Perc, "%");
                tblRepo.addToTableComponentesFT(idFichaTecnica, 1, subsId);
            }
            if (c2 != null) {
                int subsId = tblRepo.addToTableSubstancias(c2, c2Perc, "%");
                tblRepo.addToTableComponentesFT(idFichaTecnica, 2, subsId);
            }
            if (c3 != null) {
                int subsId = tblRepo.addToTableSubstancias(c3, c3Perc, "%");
                tblRepo.addToTableComponentesFT(idFichaTecnica, 3, subsId);
            }
            if (c4 != null) {
                int subsId = tblRepo.addToTableSubstancias(c4, c4Perc, "%");
                tblRepo.addToTableComponentesFT(idFichaTecnica, 4, subsId);
            }

            // FatoresProducao
            if (designacao != null) {
                tblRepo.addToTableFatoresProducao(designacao, fabricante, idFichaTecnica, formato, tipo, aplicacao);
            }
        }

        // Close the FileInputStream and workbook
        fis.close();
        workbook.close();
    }

    private void importSheetExploracaoAgricola(String excelFilePath) throws IOException {

        // Load the Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Set sheet name
        String sheetName = "Exploração Agrícola";

        // Get the sheet
        Sheet sheet = workbook.getSheet(sheetName);

        // Iterate through rows and cells to read data
        for (Row row : sheet) {

            // Setup columns variables
            int quintaId = 1;
            int idEdificio = 0;
            String tipoEdif = null;
            String nome = null;
            double area = 0;
            String unidadeMedida = "un";

            // Skips header row and goes through each row of the sheet!
            if (row.getRowNum() != 0) {
                // Goes through each cell of the row to alocate data to column variables
                if (row.getCell(0) != null) {
                    idEdificio = (int) row.getCell(0).getNumericCellValue();
                }
                if (row.getCell(1) != null) {
                    tipoEdif = row.getCell(1).getStringCellValue().toUpperCase();
                }
                if (row.getCell(2) != null) {
                    nome = row.getCell(2).getStringCellValue().toUpperCase();
                }
                if (row.getCell(3) != null) {
                    area = row.getCell(3).getNumericCellValue();
                }
                if (row.getCell(4) != null) {
                    unidadeMedida = row.getCell(4).getStringCellValue();
                }
            }

            //// Creates objects for tables that require this data:
            // EDIFICIOS
            if (idEdificio != 0 && !tipoEdif.equals("PARCELA")) {
                tblRepo.addToTableEdificios(quintaId, idEdificio);
            }

            // PARCELAS e todos os tipos de EDIFICIOS
            if (tipoEdif != null) {
                switch (tipoEdif) {
                    case "PARCELA":
                        tblRepo.addToTableParcelas(quintaId, idEdificio, nome, area, unidadeMedida);
                        break;
                    case "ARMAZÉM":
                        tblRepo.addToTableArmazens(quintaId, idEdificio, nome, area, unidadeMedida);
                        break;
                    case "GARAGEM":
                        tblRepo.addToTableGaragens(quintaId, idEdificio, nome, area, unidadeMedida);
                        break;
                    case "MOINHO":
                        tblRepo.addToTableMoinhos(quintaId, idEdificio, nome, area, unidadeMedida);
                        break;
                    case "REGA":
                        tblRepo.addToTableSistemasRega(quintaId, idEdificio, nome, area, unidadeMedida);
                        break;
                }
            }
        }

        // Close the FileInputStream and workbook
        fis.close();
        workbook.close();
    }

    private void importSheetCulturas(String excelFilePath) throws IOException {

        // Load the Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Set sheet name
        String sheetName = "Culturas";

        // Get the sheet
        Sheet sheet = workbook.getSheet(sheetName);

        // Iterate through rows and cells to read data
        for (Row row : sheet) {

            // Setup columns variables
            int quintaId = 1;
            int parcelaId = 0;
            String cultura = null;
            String dataInicio = null;
            String dataFim = null;
            double quantidade = 0;
            String unidadeMedida = "un";

            // Skips header row and goes through each row of the sheet!
            if (row.getRowNum() != 0) {
                // Goes through each cell of the row to alocate data to column variables
                if (row.getCell(0) != null) {
                    parcelaId = (int) row.getCell(0).getNumericCellValue();
                }
                if (row.getCell(2) != null) {
                    cultura = row.getCell(2).getStringCellValue().toUpperCase();
                }
                if (row.getCell(4) != null) {
                    dataInicio = String.valueOf(row.getCell(4).getLocalDateTimeCellValue());
                }
                if (row.getCell(5) != null) {
                    dataFim = String.valueOf(row.getCell(5).getLocalDateTimeCellValue());
                }
                if (row.getCell(6) != null) {
                    quantidade = row.getCell(6).getNumericCellValue();
                }
                if (row.getCell(7) != null) {
                    unidadeMedida = row.getCell(7).getStringCellValue();
                }
            }

            //// Creates objects for tables that require this data:
            // CULTIVOS
            if (parcelaId != 0 && cultura != null) {
                if (unidadeMedida.equals("un") && cultura.contains("OLIVEIRA")) {
                    tblRepo.addToTableCultivos(quintaId, parcelaId, cultura, dataInicio, dataFim, quantidade, 6, 6, 0);
                } else if (unidadeMedida.equals("un") && cultura.contains("MACIEIRA")) {
                    tblRepo.addToTableCultivos(quintaId, parcelaId, cultura, dataInicio, dataFim, quantidade, 5, 5, 0);
                } else if (unidadeMedida.equals("un") && cultura.contains("VIDEIRA")) {
                    tblRepo.addToTableCultivos(quintaId, parcelaId, cultura, dataInicio, dataFim, quantidade, 3, 3, 0);
                } else {
                    tblRepo.addToTableCultivos(quintaId, parcelaId, cultura, dataInicio, dataFim, quantidade, unidadeMedida, 0);
                }
            }
        }

        // Close the FileInputStream and workbook
        fis.close();
        workbook.close();
    }

    private void importSheetOperacoes(String excelFilePath) throws IOException {

        // Load the Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Set sheet name
        String sheetName = "Operações";

        // Get the sheet
        Sheet sheet = workbook.getSheet(sheetName);

        // Iterate through rows and cells to read data
        for (Row row : sheet) {

            // Setup columns variables
            int quintaId = 1;
            int parcelaId = 0;
            String operacao = null;
            String cultura = null;
            String dataInicio = null;
            double quantidade = 0;
            String unidadeMedida = "un";
            String fatorProd = null;

            // Skips header row and goes through each row of the sheet!
            if (row.getRowNum() != 0) {
                // Goes through each cell of the row to alocate data to column variables
                if (row.getCell(0) != null) {
                    parcelaId = (int) row.getCell(0).getNumericCellValue();
                }
                if (row.getCell(2) != null) {
                    operacao = row.getCell(2).getStringCellValue().toUpperCase();
                }
                if (row.getCell(4) != null) {
                    cultura = row.getCell(4).getStringCellValue().toUpperCase();
                }
                if (row.getCell(5) != null) {
                    dataInicio = String.valueOf(row.getCell(5).getLocalDateTimeCellValue());
                }
                if (row.getCell(6) != null) {
                    quantidade = row.getCell(6).getNumericCellValue();
                }
                if (row.getCell(7) != null) {
                    unidadeMedida = row.getCell(7).getStringCellValue();
                }
                if (row.getCell(8) != null) {
                    fatorProd = row.getCell(8).getStringCellValue().toUpperCase();
                }

                //// Creates objects for tables that require this data:
                // OPERACOES
                int operacaoId = 0;
                if (parcelaId != 0) {
                    operacaoId = tblRepo.addToTableOperacoes(quintaId, parcelaId, cultura, 0, dataInicio);
                }

                // APLICACOES_FATPROD
                if (fatorProd != null && operacaoId != 0) {
                    tblRepo.addToTableAplicacoesFatProd(operacaoId, fatorProd, quantidade, unidadeMedida);
                }

                // SEMENTEIRAS
                if (operacao.equals("SEMENTEIRA") && operacaoId != 0) {
                    tblRepo.addToTableSementeiras(operacaoId, quantidade, unidadeMedida);
                }

                // PLANTACOES
                if (operacao.equals("PLANTAÇÃO") && operacaoId != 0) {
                    if (cultura.contains("MACIEIRA")) {
                        tblRepo.addToTablePlantacoes(operacaoId, quantidade, 5, 5);
                    } else if (cultura.contains("OLIVEIRA")) {
                        tblRepo.addToTablePlantacoes(operacaoId, quantidade, 6, 6);
                    } else if (cultura.contains("VIDEIRA")) {
                        tblRepo.addToTablePlantacoes(operacaoId, quantidade, 3, 3);
                    } else {
                        tblRepo.addToTablePlantacoes(operacaoId, quantidade, unidadeMedida);
                    }
                }

                // REGAS
                if (operacao.equals("REGA") && operacaoId != 0) {
                    tblRepo.addToTableRegas(operacaoId, quantidade, unidadeMedida, 0);
                }

                // PODAS
                if (operacao.equals("PODA") && operacaoId != 0) {
                    tblRepo.addToTablePodas(operacaoId, quantidade, unidadeMedida);
                }

                // INCORPS_SOLO
                if (operacao.equals("INCORPORAÇÃO NO SOLO") && operacaoId != 0) {
                    tblRepo.addToTableIncorpsSolo(operacaoId, quantidade, unidadeMedida);
                }

                // COLHEITAS
                if (operacao.equals("COLHEITA") && operacaoId != 0) {
                    tblRepo.addToTableColheitas(operacaoId, quantidade, unidadeMedida);
                }
            }
        }

        // Close the FileInputStream and workbook
        fis.close();
        workbook.close();
    }

    //////// EXPORTING OPTIONS ///////////////////////////////////////////////////////////////////////////

    /**
     * Export to a SQL script file, all SQL INSERT lines, ordered by level of insertion.
     */
    public void exportToSqlScript() {
        try {
            // Auxiliary path initializer
            File currentDirFile = new File(".");
            String pathSufix = currentDirFile.getAbsolutePath();
            pathSufix = pathSufix.substring(0, pathSufix.length() - 1);

            // Get the current timestamp
            LocalDateTime currentTimestamp = LocalDateTime.now();

            // Format the timestamp as a string
            DateTimeFormatter formattedTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss");
            String timestamp = currentTimestamp.format(formattedTimestamp);

            // Create Sql file (if it doesn't exist or populate if it does)
            String sqlFilePath = pathSufix + "sql_files/INSERTS_auto_" + timestamp + ".sql";
            Path sqlFile = Paths.get(sqlFilePath);
            if (!Files.exists(sqlFile)) {
                Files.createFile(sqlFile);
            }

            // Append ALL TABLES INSERTS AVAILABLE to .sql file
            for (Quintas obj : tblRepo.getLstQuintas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (UnidadesMedida obj : tblRepo.getLstUnidadesMedida()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Operadores obj : tblRepo.getLstOperadores()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (EspeciesVegetais obj : tblRepo.getLstEspeciesVegetais()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (TiposConsumo obj : tblRepo.getLstTiposConsumo()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (TiposCultura obj : tblRepo.getLstTiposCultura()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Culturas obj : tblRepo.getLstCulturas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Ciclos obj : tblRepo.getLstCiclos()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Produtos obj : tblRepo.getLstProdutos()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Substancias obj : tblRepo.getLstSubstancias()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Formulacoes obj : tblRepo.getLstFormulacoes()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (FichasTecnicas obj : tblRepo.getLstFichasTecnicas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (ComponentesFT obj : tblRepo.getLstComponentesFT()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (FatoresProducao obj : tblRepo.getLstFatoresProducao()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Edificios obj : tblRepo.getLstEdificios()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Parcelas obj : tblRepo.getLstParcelas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Armazens obj : tblRepo.getLstArmazens()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Garagens obj : tblRepo.getLstGaragens()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Moinhos obj : tblRepo.getLstMoinhos()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (SistemasRega obj : tblRepo.getLstSistemasRega()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (SetoresRega obj : tblRepo.getLstSetoresRega()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Cultivos obj : tblRepo.getLstCultivos()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Operacoes obj : tblRepo.getLstOperacoes()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (AplicacoesFatProd obj : tblRepo.getLstAplicacoes()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Sementeiras obj : tblRepo.getLstSementeiras()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Plantacoes obj : tblRepo.getLstPlantacoes()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Mix obj : tblRepo.getLstMix()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Receitas obj : tblRepo.getLstReceitas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Regas obj : tblRepo.getLstRegas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Fertirregas obj : tblRepo.getLstFertirregas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Podas obj : tblRepo.getLstPodas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (IncorpsSolo obj : tblRepo.getLstIncorpsSolo()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Colheitas obj : tblRepo.getLstColheitas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (Mondas obj : tblRepo.getLstMondas()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

            Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            for (MobilizacoesSolo obj : tblRepo.getLstMobilizacoesSolo()) {
                Files.write(sqlFile, obj.append().getBytes(), StandardOpenOption.APPEND);
                Files.write(sqlFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            Utils.showMessageColor("Error writing to .sql file!", AnsiColor.RED);
            e.printStackTrace();
        }
    }

    /**
     * Print to the terminal, all SQL INSERT lines, ordered by level of insertion.
     */
    public void print() {

        System.out.println("----- QUINTAS");
        for (Quintas obj : tblRepo.getLstQuintas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- UNIDADES MEDIDA");
        for (UnidadesMedida obj : tblRepo.getLstUnidadesMedida()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- OPERADORES");
        for (Operadores obj : tblRepo.getLstOperadores()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- ESPECIES VEGETAIS");
        for (EspeciesVegetais obj : tblRepo.getLstEspeciesVegetais()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- TIPOS DE CONSUMO");
        for (TiposConsumo obj : tblRepo.getLstTiposConsumo()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- TIPOS DE CULTURA");
        for (TiposCultura obj : tblRepo.getLstTiposCultura()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- CULTURAS");
        for (Culturas obj : tblRepo.getLstCulturas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- CICLOS");
        for (Ciclos obj : tblRepo.getLstCiclos()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- PRODUTOS");
        for (Produtos obj : tblRepo.getLstProdutos()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- SUBSTANCIAS");
        for (Substancias obj : tblRepo.getLstSubstancias()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- FORMULACOES");
        for (Formulacoes obj : tblRepo.getLstFormulacoes()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- FICHAS_TECNICAS");
        for (FichasTecnicas obj : tblRepo.getLstFichasTecnicas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- COMPONENTES");
        for (ComponentesFT obj : tblRepo.getLstComponentesFT()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- FATORES_PRODUCAO");
        for (FatoresProducao obj : tblRepo.getLstFatoresProducao()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- EDIFICIOS");
        for (Edificios obj : tblRepo.getLstEdificios()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- PARCELAS");
        for (Parcelas obj : tblRepo.getLstParcelas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- ARMAZENS");
        for (Armazens obj : tblRepo.getLstArmazens()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- GARAGENS");
        for (Garagens obj : tblRepo.getLstGaragens()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- MOINHOS");
        for (Moinhos obj : tblRepo.getLstMoinhos()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- SISTEMAS_REGA");
        for (SistemasRega obj : tblRepo.getLstSistemasRega()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- SETORES_REGA");
        for (SetoresRega obj : tblRepo.getLstSetoresRega()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- CULTIVOS");
        for (Cultivos obj : tblRepo.getLstCultivos()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- OPERACOES");
        for (Operacoes obj : tblRepo.getLstOperacoes()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- APLICACOES FATOR PROD");
        for (AplicacoesFatProd obj : tblRepo.getLstAplicacoes()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- SEMENTEIRAS");
        for (Sementeiras obj : tblRepo.getLstSementeiras()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- PLANTACOES");
        for (Plantacoes obj : tblRepo.getLstPlantacoes()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- MIX");
        for (Mix obj : tblRepo.getLstMix()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- RECEITAS");
        for (Receitas obj : tblRepo.getLstReceitas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- REGAS");
        for (Regas obj : tblRepo.getLstRegas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- FERTIRREGAS");
        for (Fertirregas obj : tblRepo.getLstFertirregas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- PODAS");
        for (Podas obj : tblRepo.getLstPodas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- INCORPS SOLO");
        for (IncorpsSolo obj : tblRepo.getLstIncorpsSolo()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- COLHEITAS");
        for (Colheitas obj : tblRepo.getLstColheitas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- MONDAS");
        for (Mondas obj : tblRepo.getLstMondas()) {
            obj.print();
        }
        System.out.println();

        System.out.println("----- MOBILIZACOES SOLO");
        for (MobilizacoesSolo obj : tblRepo.getLstMobilizacoesSolo()) {
            obj.print();
        }
        System.out.println();
    }

    //////// MANUAL INSERTED DATA //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Data added manually to repository.
     */
    private void createManualData() {

        // INSERT UNIDADES_MEDIDA
        tblRepo.addToTableUnidadesMedida("ha", "hectares");
        tblRepo.addToTableUnidadesMedida("m2", "metros quadrados");
        tblRepo.addToTableUnidadesMedida("m3", "metros cubicos");
        tblRepo.addToTableUnidadesMedida("un", "unidades");
        tblRepo.addToTableUnidadesMedida("kg", "kilogramas");
        tblRepo.addToTableUnidadesMedida("m", "metros");
        tblRepo.addToTableUnidadesMedida("g", "gramas");
        tblRepo.addToTableUnidadesMedida("mes", "mês");
        tblRepo.addToTableUnidadesMedida("dia", "dia");
        tblRepo.addToTableUnidadesMedida("min", "minutos");
        tblRepo.addToTableUnidadesMedida("l/h", "litros/hora");
        tblRepo.addToTableUnidadesMedida("pH", "pH");
        tblRepo.addToTableUnidadesMedida("%", "percentagem");
        tblRepo.addToTableUnidadesMedida("l", "litros");
        tblRepo.addToTableUnidadesMedida("kg/l", "kilogramas/litro");

        // INSERT QUINTAS
        tblRepo.addToTableQuintas(1, "QUINTA");

        // INSERT EDIFICIO & SISTEMA REGA
        tblRepo.addToTableEdificios(1, 303);
        tblRepo.addToTableSistemasRega(1, 303, "Sist-Rega Novo S2", 50, "m3");
        tblRepo.addToTableSetoresRega(0, 1, 303, "2017-05-01", null, 200000, "l/h");

        // INSERT OPERADORES
        tblRepo.addToTableOperadores(0, "default");
        tblRepo.addToTableOperadores(123456789, "GESTOR");
        tblRepo.addToTableOperadores(987654321, "FUNCIONÁRIO");

        // INSERT TIPOS_CONSUMO
        tblRepo.addToTableTiposConsumo("HUMANO");
        tblRepo.addToTableTiposConsumo("ANIMAL");
        tblRepo.addToTableTiposConsumo("VEGETAL");

        // INSERT TIPO_CULTURA default
        tblRepo.addToTableTiposCulturaDefault(new TiposCultura(0, "default"));

        // INSERT ESPECIES_VEGETAIS default
        tblRepo.addToTableEspeciesVegetaisDefault(new EspeciesVegetais(0, "default", "detault"));

        // INSERT CULTURA default
        tblRepo.addToTableCulturasDefault(new Culturas(0, 0, 0, "default", "default", "default"));

    }

    public void addExtraDataSprint2() {
//        Inserir as seguintes plantas:
//        Abóbora manteiga, designada por Cucurbita moschata var 'Butternut'
        tblRepo.addToTableEspeciesVegetais("CUCURBITA MOSCHATA VAR BUTTERNUT", "ABOBORA_MANTEIGA");
        tblRepo.addToTableCulturas("TEMPORÁRIA", "CUCURBITA MOSCHATA VAR BUTTERNUT", "ABOBORA MANTEIGA", "ABOBORA MANTEIGA", "ABOBORA MANTEIGA");

//        Inserir a seguinte parcela:
//        Em 01/12/2022, Campo Novo, 1.1 ha
        tblRepo.addToTableParcelas(1, 108, "CAMPO NOVO", 1.1, "ha");

//        Inserir os seguintes fatores de produção:
//        Adubo para aplicação no solo, Fertimax Extrume de Cavalo, da Nutrofertil, granulado, Matéria Orgânica 50%, pH 6.7, Azoto orgânico (N) 3%, Fósforo total (P2O5) 0.8%, Potássio total (K2O) 0.4%, Cálcio total (Ca) 1.6%, Magnésio total (MgO) 0.3%, Boro total (B) 0.004%
        int ft1 = tblRepo.addToTableFichasTecnicas("FERTIMAX EXTRUME DE CAVALO");
        tblRepo.addToTableFormulacoes("GRANULADO", "ADUBO", "ADUBO SOLO");
        tblRepo.addToTableFatoresProducao("FERTIMAX EXTRUME DE CAVALO", "NUTROFERTIL", ft1, "GRANULADO", "ADUBO", "ADUBO SOLO");
        int subsId1 = tblRepo.addToTableSubstancias("Matéria Orgânica", 0.5, "%");
        tblRepo.addToTableComponentesFT(ft1, 1, subsId1);
        int subsId2 = tblRepo.addToTableSubstancias("pH", 6.7, "pH");
        tblRepo.addToTableComponentesFT(ft1, 2, subsId2);
        int subsId3 = tblRepo.addToTableSubstancias("N", 0.03, "%");
        tblRepo.addToTableComponentesFT(ft1, 3, subsId3);
        int subsId4 = tblRepo.addToTableSubstancias("P2O5", 0.008, "%");
        tblRepo.addToTableComponentesFT(ft1, 4, subsId4);
        int subId5 = tblRepo.addToTableSubstancias("K2O", 0.004, "%");
        tblRepo.addToTableComponentesFT(ft1, 5, subId5);
        int subsId6 = tblRepo.addToTableSubstancias("Ca", 0.016, "%");
        tblRepo.addToTableComponentesFT(ft1, 6, subsId6);
        int subsId7 = tblRepo.addToTableSubstancias("MgO", 0.003, "%");
        tblRepo.addToTableComponentesFT(ft1, 7, subsId7);
        int subsId8 = tblRepo.addToTableSubstancias("B", 0.00004, "%");
        tblRepo.addToTableComponentesFT(ft1, 8, subsId8);

//        Adubo para aplicação no solo, BIOFERTIL N6, da Nutrofertil, granulado, Matéria Orgânica 53%, pH 6.5, Azoto orgânico (N) 6.4%, Fósforo total (P2O5) 2.5%, Potássio total (K2O) 2.4%, Cálcio total (Ca) 6%, Magnésio total (MgO) 0.3%, Boro total (B) 0.0020%
        int ft2 = tblRepo.addToTableFichasTecnicas("BIOFERTIL N6");
        tblRepo.addToTableFormulacoes("GRANULADO", "ADUBO", "ADUBO SOLO");
        tblRepo.addToTableFatoresProducao("BIOFERTIL N6", "NUTROFERTIL", ft2, "GRANULADO", "ADUBO", "ADUBO SOLO");
        int subsId9 = tblRepo.addToTableSubstancias("Matéria Orgânica", 0.53, "%");
        tblRepo.addToTableComponentesFT(ft2, 1, subsId9);
        int subsId10 = tblRepo.addToTableSubstancias("pH", 6.5, "pH");
        tblRepo.addToTableComponentesFT(ft2, 2, subsId10);
        int subsId11 = tblRepo.addToTableSubstancias("N", 0.064, "%");
        tblRepo.addToTableComponentesFT(ft2, 3, subsId11);
        int subsId12 = tblRepo.addToTableSubstancias("P2O5", 0.025, "%");
        tblRepo.addToTableComponentesFT(ft2, 4, subsId12);
        int subsId13 = tblRepo.addToTableSubstancias("K2O", 0.024, "%");
        tblRepo.addToTableComponentesFT(ft2, 5, subsId13);
        int subsId14 = tblRepo.addToTableSubstancias("Ca", 0.06, "%");
        tblRepo.addToTableComponentesFT(ft2, 6, subsId14);
        int subsId15 = tblRepo.addToTableSubstancias("MgO", 0.03, "%");
        tblRepo.addToTableComponentesFT(ft2, 7, subsId15);
        int subsId16 = tblRepo.addToTableSubstancias("B", 0.00002, "%");
        tblRepo.addToTableComponentesFT(ft2, 8, subsId16);

//        Inserir os seguintes setores de rega:
//
//        Setor 10
//        Início a 01/05/2017, sem data fim
//        Caudal máximo: 2500 l/h
//        Parcelas: Campo grande
//        Culturas:
//        - Oliveira Galega, 30 un, 01/05/2017, sem data fim
//        - Oliveira Picual, 20 un, 01/05/2017, sem data fim
        tblRepo.addToTableSetoresRega(10, 1, 303, "2017/05/01", null, 2500, "l/h");
        tblRepo.setSetorRegaToCultivo(10, "CAMPO GRANDE", "OLIVEIRA GALEGA");
        tblRepo.setSetorRegaToCultivo(10, "CAMPO GRANDE", "OLIVEIRA PICUAL");

//        Setor 11
//        Início a 01/05/2017, sem data fim
//        Caudal máximo: 1500 l/h
//        Parcelas: Campo grande
//        Culturas:
//        - Oliveira Arbquina, 40 un, 01/05/2017, sem data fim
        tblRepo.addToTableSetoresRega(11, 1, 303, "2017/05/01", null, 1500, "l/h");
        tblRepo.setSetorRegaToCultivo(11, "CAMPO GRANDE", "OLIVEIRA ARBEQUINA");

//        Setor 21
//        Início a 01/05/2017, sem data fim
//        Caudal máximo: 3500 l/h
//        Parcelas: Lameiro da ponte, Lameiro do Moinho
//        Culturas:
//        - Macieira Jonagored, 90 un, 01/05/2017, sem data fim
//        - Macieira Fuji, 60 un, 01/05/2017, sem data fim
//        - Macieira Royal Gala, 60 un, 01/05/2017, sem data fim
//        - Macieira Royal Gala, 40 un, 01/05/2019, sem data fim
//        - Macieira Pipo de Basto, 40 un, 01/05/2019, sem data fim
        tblRepo.addToTableSetoresRega(21, 1, 303, "2017/05/01", null, 3500, "l/h");
        tblRepo.setSetorRegaToCultivo(21, "LAMEIRO DA PONTE", "MACIEIRA JONAGORED");
        tblRepo.setSetorRegaToCultivo(21, "LAMEIRO DA PONTE", "MACIEIRA FUJI");
        tblRepo.setSetorRegaToCultivo(21, "LAMEIRO DA PONTE", "MACIEIRA ROYAL GALA");
        tblRepo.setSetorRegaToCultivo(21, "LAMEIRO DA PONTE", "MACIEIRA ROYAL GALA");
        tblRepo.setSetorRegaToCultivo(21, "LAMEIRO DA PONTE", "MACIEIRA PIPO DE BASTO");


//        Setor 22
//        Início a 01/05/2019, sem data fim
//        Caudal máximo: 3500 l/h
//        Parcelas: Lameiro do Moinho
//        Culturas:
//        - Macieira Porta da Loja, 50 un, 01/05/2019, sem data fim
//        - Macieira Malápio, 20 un, 01/05/2019, sem data fim
//        - Macieira Canada, 30 un, 01/05/2019, sem data fim
//        - Macieira Grand Fay, 40 un, 01/05/2019, sem data fim
//        - Macieira Gronho Doce, 50 un, 01/05/2019, sem data fim
        tblRepo.addToTableSetoresRega(22, 1, 303, "2019/05/01", null, 3500, "l/h");
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA PORTA DA LOJA", "2019/05/01", null, 50, 5, 5, 22);
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA MALÁPIO", "2019/05/01", null, 20, 5, 5, 22);
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA REINETTE OU CANADA", "2019/05/01", null, 30, 5, 5, 22);
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA REINETTE OU GRAND FAY", "2019/05/01", null, 40, 5, 5, 22);
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA GRONHO DOCE", "2019/05/01", null, 50, 5, 5, 22);


//        Setor 41
//        Início a 01/04/2023, 10/10/2023
//        Caudal máximo: 2500 l/h
//        Parcelas: Campo Novo
//        Culturas:
//        - Cenoura Sugarsnax Hybrid, 05/04/2023 a 31/05/2023
//        - Cenoura Danvers Half Long, 05/07/2023 a 08/10/2023
        tblRepo.addToTableSetoresRega(41, 1, 303, "2023/04/01", "2023/10/10", 2500, "l/h");
        tblRepo.addToTableCultivos(1, 108, "CENOURA SUGARSNAX HYBRID", "2023/04/05", "2023/05/31", 0.5, "ha", 41);
        tblRepo.addToTableCultivos(1, 108, "CENOURA DANVERS HALF LONG", "2023/07/05", "2023/10/08", 0.5, "ha", 41);

//        Setor 42
//        Início a 01/04/2023, 10/10/2023
//        Caudal máximo: 3500 l/h
//        Parcelas: Campo Novo
//        Culturas:
//        - Abóbora Manteiga, 06/04/2023 a 10/09/2023
        tblRepo.addToTableSetoresRega(42, 1, 303, "2023/04/01", "2023/10/10", 3500, "l/h");
        tblRepo.addToTableCultivos(1, 108, "ABOBORA MANTEIGA", "2023/04/06", "2023/09/10", 0.6, "ha", 42);

//        Inserir as seguintes operações realizadas na quinta:
//
//        Lameiro da Ponte (id: 104)
//
//        14/05/2023 operação de rega, setor 21, 120 min, 07:00
        int op1 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/05/14");
        tblRepo.addToTableRegas(op1, 120, "min", 21, "07:00");

//        01/06/2023 operação de rega, setor 21, 120 min, 07:00
        int op2 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/06/01");
        tblRepo.addToTableRegas(op2, 120, "min", 21, "07:00");

//        15/06/2023 operação de rega, setor 21, 120 min, "07:00"
        int op3 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/06/15");
        tblRepo.addToTableRegas(op3, 120, "min", 21, "07:00");

//        30/06/2023 operação de rega, setor 21, 120 min, "07:00"
        int op4 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/06/30");
        tblRepo.addToTableRegas(op4, 120, "min", 21, "07:00");

//        07/07/2023 operação de rega, setor 21, 180 min, "07:00"
        int op5 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/07/07");
        tblRepo.addToTableRegas(op5, 180, "min", 21, "07:00");

//        14/07/2023 operação de rega, setor 21, 180 min, "22:00"
        int op6 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/07/14");
        tblRepo.addToTableRegas(op6, 180, "min", 21, "22:00");

//        21/07/2023 operação de rega, setor 21, 180 min, "22:00"
        int op7 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/07/21");
        tblRepo.addToTableRegas(op7, 180, "min", 21, "22:00");

//        28/07/2023 operação de rega, setor 21, 180 min, "22:00"
        int op8 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/07/28");
        tblRepo.addToTableRegas(op8, 180, "min", 21, "22:00");

//        04/08/2023 operação de rega, setor 21, 150 min, "22:00"
        int op9 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/08/04");
        tblRepo.addToTableRegas(op9, 150, "min", 21, "22:00");

//        11/08/2023 operação de rega, setor 21, 150 min, "22:00"
        int op10 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/08/11");
        tblRepo.addToTableRegas(op10, 150, "min", 21, "22:00");

//        18/08/2023 operação de rega, setor 21, 150 min, "22:00"
        int op11 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/08/18");
        tblRepo.addToTableRegas(op11, 150, "min", 21, "22:00");

//        25/08/2023 operação de rega, setor 21, 120 min, "22:00"
        int op12 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/08/25");
        tblRepo.addToTableRegas(op12, 120, "min", 21, "22:00");

//        01/09/2023 operação de rega, setor 21, 120 min, "22:00"
        int op13 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/09/01");
        tblRepo.addToTableRegas(op13, 120, "min", 21, "22:00");

//        08/09/2023 operação de rega, setor 21, 120 min, "22:00"
        int op14 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/09/08");
        tblRepo.addToTableRegas(op14, 120, "min", 21, "22:00");

//        15/09/2023 operação de rega, setor 21, 120 min, "22:00"
        int op15 = tblRepo.addToTableOperacoes(1, 104, null, 0, "2023/09/15");
        tblRepo.addToTableRegas(op15, 120, "min", 21, "22:00");

//        18/08/2023 operação de colheita de maçã Royal Gala, 700 kg
        int op16 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ ROYAL GALA", 0, "2023/08/18");
        tblRepo.addToTableColheitas(op16, 700, "kg");

//        30/08/2023 operação de colheita de maçã Royal Gala, 900 kg
        int op17 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ ROYAL GALA", 0, "2023/08/30");
        tblRepo.addToTableColheitas(op17, 900, "kg");

//        05/09/2023 operação de colheita de maçã Jonagored, 900 kg
        int op18 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ JONAGORED", 0, "2023/09/05");
        tblRepo.addToTableColheitas(op18, 900, "kg");

//        08/09/2023 operação de colheita de maçã Jonagored, 1050 kg
        int op19 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ JONAGORED", 0, "2023/09/08");
        tblRepo.addToTableColheitas(op19, 1050, "kg");

//        28/09/2023 operação de colheita de maçã Fuji, 950 kg
        int op20 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ FUJI", 0, "2023/09/28");
        tblRepo.addToTableColheitas(op20, 950, "kg");

//        03/10/2023 operação de colheita de maçã Fuji, 800 kg
        int op21 = tblRepo.addToTableOperacoes(1, 104, "MAÇÃ FUJI", 0, "2023/10/03");
        tblRepo.addToTableColheitas(op21, 800, "kg");

//
//        Lameiro do Moinho: (105)
//
//        04/01/2019 operação de aplicação de fator de produção BIOFERTIL N6, no solo, 1.1 ha, 3200 kg
        int op22 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2019/01/04");
        tblRepo.addToTableAplicacoesFatProd(op22, "BIOFERTIL N6", 3200, "kg");

//        09/01/2019 operação de plantação de Macieira Porta da Loja, 50 un, compasso de 5 m, distancia entre filas de 5 m
        int op23 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PORTA DA LOJA", 0, "2019/01/09");
        tblRepo.addToTablePlantacoes(op23, 50, 5, 5);

//        09/01/2019 operação de plantação de Macieira Malápio, 20 un, compasso de 5 m, distancia entre filas de 5 m
        int op24 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA MALÁPIO", 0, "2019/01/09");
        tblRepo.addToTablePlantacoes(op24, 20, 5, 5);

//        10/01/2019 operação de plantação de Macieira Pipo de Basto, 40 un, compasso de 5 m, distancia entre filas de 5 m
        tblRepo.addToTableCultivos(1, 105, "MACIEIRA PIPO DE BASTO", "2019/01/10", null, 40, 5, 5, 0);
        int op25 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PIPO DE BASTO", 0, "2019/01/10");
        tblRepo.addToTablePlantacoes(op25, 40, 5, 5);

//        10/01/2019 operação de plantação de Macieira Canada, 30 un, compasso de 5 m, distancia entre filas de 5 m
        int op26 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA REINETTE OU CANADA", 0, "2019/01/10");
        tblRepo.addToTablePlantacoes(op26, 30, 5, 5);

//        11/01/2019 operação de plantação de Macieira Grand Fay, 40 un, compasso de 5 m, distancia entre filas de 5 m
        int op27 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA REINETTE OU GRAND FAY", 0, "2019/01/11");
        tblRepo.addToTablePlantacoes(op27, 40, 5, 5);

//        11/01/2019 operação de plantação de Macieira Gronho Doce, 50 un, compasso de 5 m, distancia entre filas de 5 m
        int op28 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA GRONHO DOCE", 0, "2019/01/11");
        tblRepo.addToTablePlantacoes(op28, 50, 5, 5);

//        06/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Porta da Loja, 100 kg
        int op29 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/06");
        tblRepo.addToTableAplicacoesFatProd(op29, "FERTIMAX EXTRUME DE CAVALO", 100, "kg");

//        06/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Malápio, 40 kg
        int op30 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/06");
        tblRepo.addToTableAplicacoesFatProd(op30, "FERTIMAX EXTRUME DE CAVALO", 40, "kg");

//        06/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Pipo de Basto, 80 kg
        int op31 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/06");
        tblRepo.addToTableAplicacoesFatProd(op31, "FERTIMAX EXTRUME DE CAVALO", 80, "kg");

//        06/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Canada, 60 kg
        int op32 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/06");
        tblRepo.addToTableAplicacoesFatProd(op32, "FERTIMAX EXTRUME DE CAVALO", 60, "kg");

//        07/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Grand Fay, 80 kg
        int op33 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/07");
        tblRepo.addToTableAplicacoesFatProd(op33, "FERTIMAX EXTRUME DE CAVALO", 80, "kg");

//        07/01/2020 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Gronho Doce, 100 kg
        int op34 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2020/01/07");
        tblRepo.addToTableAplicacoesFatProd(op34, "FERTIMAX EXTRUME DE CAVALO", 100, "kg");

//        07/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Porta da Loja, 150 kg
        int op35 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/07");
        tblRepo.addToTableAplicacoesFatProd(op35, "FERTIMAX EXTRUME DE CAVALO", 150, "kg");

//        07/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Malápio, 60 kg
        int op36 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/07");
        tblRepo.addToTableAplicacoesFatProd(op36, "FERTIMAX EXTRUME DE CAVALO", 60, "kg");

//        08/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Pipo de Basto, 120 kg
        int op37 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/08");
        tblRepo.addToTableAplicacoesFatProd(op37, "FERTIMAX EXTRUME DE CAVALO", 120, "kg");

//        07/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Canada, 90 kg
        int op38 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/07");
        tblRepo.addToTableAplicacoesFatProd(op38, "FERTIMAX EXTRUME DE CAVALO", 90, "kg");

//        07/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Grand Fay, 120 kg
        int op39 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/07");
        tblRepo.addToTableAplicacoesFatProd(op39, "FERTIMAX EXTRUME DE CAVALO", 120, "kg");

//        08/01/2021 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, Macieira Gronho Doce, 150 kg
        int op40 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2021/01/08");
        tblRepo.addToTableAplicacoesFatProd(op40, "FERTIMAX EXTRUME DE CAVALO", 150, "kg");

//        15/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Porta da Loja, 150 kg
        int op41 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/15");
        tblRepo.addToTableAplicacoesFatProd(op41, "BIOFERTIL N6", 150, "kg");

//        15/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Malápio, 60 kg
        int op42 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/15");
        tblRepo.addToTableAplicacoesFatProd(op42, "BIOFERTIL N6", 60, "kg");

//        15/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Pipo de Basto, 120 kg
        int op43 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/15");
        tblRepo.addToTableAplicacoesFatProd(op43, "BIOFERTIL N6", 120, "kg");

//        16/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Canada, 90 kg
        int op44 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/16");
        tblRepo.addToTableAplicacoesFatProd(op44, "BIOFERTIL N6", 90, "kg");

//        16/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Grand Fay, 120 kg
        int op45 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/16");
        tblRepo.addToTableAplicacoesFatProd(op45, "BIOFERTIL N6", 120, "kg");

//        16/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, no solo, Macieira Gronho Doce, 150 kg
        int op46 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2022/01/16");
        tblRepo.addToTableAplicacoesFatProd(op46, "BIOFERTIL N6", 150, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Porta da Loja, 5 kg
        int op47 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op47, "EPSO MICROTOP", 5, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Malápio, 2 kg
        int op48 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op48, "EPSO MICROTOP", 2, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Pipo de Basto, 4 kg
        int op49 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op49, "EPSO MICROTOP", 4, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Canada, 3 kg
        int op50 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op50, "EPSO MICROTOP", 3, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Grand Fay, 4 kg
        int op51 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op51, "EPSO MICROTOP", 4, "kg");

//        15/05/2023 operação de aplicação de fator de produção EPSO Microtop, foliar, Macieira Gronho Doce, 5 kg
        int op52 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/15");
        tblRepo.addToTableAplicacoesFatProd(op52, "EPSO MICROTOP", 5, "kg");
//
//        15/09/2023 operação de colheita de maçã Canada, 700 kg
        int op53 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA REINETTE OU CANADA", 0, "2023/09/15");
        tblRepo.addToTableColheitas(op53, 700, "kg");

//        16/09/2023 operação de colheita de maçã Grand Fay, 600 kg
        int op54 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA REINETTE OU GRAND FAY", 0, "2023/09/16");
        tblRepo.addToTableColheitas(op54, 600, "kg");

//        20/09/2023 operação de colheita de maçã Grand Fay, 700 kg
        int op55 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA REINETTE OU GRAND FAY", 0, "2023/09/20");
        tblRepo.addToTableColheitas(op55, 700, "kg");

//        27/09/2023 operação de colheita de maçã Pipo de Basto, 600 kg
        int op56 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PIPO DE BASTO", 0, "2023/09/27");
        tblRepo.addToTableColheitas(op56, 600, "kg");

//        05/10/2023 operação de colheita de maçã Pipo de Basto, 700 kg
        int op57 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PIPO DE BASTO", 0, "2023/10/05");
        tblRepo.addToTableColheitas(op57, 700, "kg");

//        15/10/2023 operação de colheita de maçã Gronho Doce, 1200 kg
        int op58 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA GRONHO DOCE", 0, "2023/10/15");
        tblRepo.addToTableColheitas(op58, 1200, "kg");

//        15/10/2023 operação de colheita de maçã Malápio, 700 kg
        int op59 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA MALÁPIO", 0, "2023/10/15");
        tblRepo.addToTableColheitas(op59, 700, "kg");

//        12/11/2023 operação de colheita de maçã Porta da Loja, 700 kg
        int op60 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PORTA DA LOJA", 0, "2023/11/12");
        tblRepo.addToTableColheitas(op60, 700, "kg");

//        15/11/2023 operação de colheita de maçã Porta da Loja, 800 kg
        int op61 = tblRepo.addToTableOperacoes(1, 105, "MACIEIRA PORTA DA LOJA", 0, "2023/11/15");
        tblRepo.addToTableColheitas(op61, 800, "kg");

//
//        13/05/2023 operação de rega, setor 22, 120 min
        int op62 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/05/13");
        tblRepo.addToTableRegas(op62, 120, "min", 22);

//        02/06/2023 operação de rega, setor 22, 120 min
        int op63 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/06/02");
        tblRepo.addToTableRegas(op63, 120, "min", 22);

//        16/06/2023 operação de rega, setor 22, 120 min
        int op64 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/06/16");
        tblRepo.addToTableRegas(op64, 120, "min", 22);

//        01/07/2023 operação de rega, setor 22, 120 min
        int op65 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/07/01");
        tblRepo.addToTableRegas(op65, 120, "min", 22);

//        08/07/2023 operação de rega, setor 22, 180 min
        int op66 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/07/08");
        tblRepo.addToTableRegas(op66, 180, "min", 22);

//        15/07/2023 operação de rega, setor 22, 180 min
        int op67 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/07/15");
        tblRepo.addToTableRegas(op67, 180, "min", 22);

//        22/07/2023 operação de rega, setor 22, 180 min
        int op68 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/07/22");
        tblRepo.addToTableRegas(op68, 180, "min", 22);

//        29/07/2023 operação de rega, setor 22, 180 min
        int op69 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/07/29");
        tblRepo.addToTableRegas(op69, 180, "min", 22);

//        05/08/2023 operação de rega, setor 22, 150 min
        int op70 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/08/05");
        tblRepo.addToTableRegas(op70, 150, "min", 22);

//        10/08/2023 operação de rega, setor 22, 150 min
        int op71 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/08/10");
        tblRepo.addToTableRegas(op71, 150, "min", 22);

//        17/08/2023 operação de rega, setor 22, 150 min
        int op72 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/08/17");
        tblRepo.addToTableRegas(op72, 150, "min", 22);

//        24/08/2023 operação de rega, setor 22, 120 min
        int op73 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/08/24");
        tblRepo.addToTableRegas(op73, 120, "min", 22);

//        02/09/2023 operação de rega, setor 22, 120 min
        int op74 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/09/02");
        tblRepo.addToTableRegas(op74, 120, "min", 22);

//        09/09/2023 operação de rega, setor 22, 120 min
        int op75 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/09/09");
        tblRepo.addToTableRegas(op75, 120, "min", 22);

//        18/09/2023 operação de rega, setor 22, 120 min
        int op76 = tblRepo.addToTableOperacoes(1, 105, null, 0, "2023/09/18");
        tblRepo.addToTableRegas(op76, 120, "min", 22);
//
//        Campo Grande: (102)
//
//        12/10/2016 operação de plantação de Oliveira Arbequina, 40 un, compasso de 6 m, distancia entre filas de 6 m
        int op77 = tblRepo.addToTableOperacoes(1, 102, "OLIVEIRA ARBEQUINA", 0, "2016/10/12");
        tblRepo.addToTablePlantacoes(op77, 40, 6, 6);

//        13/01/2021 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Picual, 120 kg
        int op78 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2021/01/13");
        tblRepo.addToTableAplicacoesFatProd(op78, "BIOFERTIL N6", 120, "kg");

//        12/01/2021 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Galega, 180 kg
        int op79 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2021/01/12");
        tblRepo.addToTableAplicacoesFatProd(op79, "BIOFERTIL N6", 180, "kg");

//        12/01/2021 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Arbquina, 240 kg
        int op80 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2021/01/12");
        tblRepo.addToTableAplicacoesFatProd(op80, "BIOFERTIL N6", 240, "kg");

//        12/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Picual, 120 kg
        int op81 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2022/01/12");
        tblRepo.addToTableAplicacoesFatProd(op81, "BIOFERTIL N6", 120, "kg");

//        12/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Galega, 180 kg
        int op82 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2022/01/12");
        tblRepo.addToTableAplicacoesFatProd(op82, "BIOFERTIL N6", 180, "kg");

//        13/01/2022 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Arbquina, 240 kg
        int op83 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2022/01/13");
        tblRepo.addToTableAplicacoesFatProd(op83, "BIOFERTIL N6", 240, "kg");

//        12/01/2023 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Picual, 120 kg
        int op84 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/01/12");
        tblRepo.addToTableAplicacoesFatProd(op84, "BIOFERTIL N6", 120, "kg");

//        12/01/2023 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Galega, 180 kg
        int op85 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/01/12");
        tblRepo.addToTableAplicacoesFatProd(op85, "BIOFERTIL N6", 180, "kg");

//        12/01/2023 operação de aplicação de fator de produção BIOFERTIL N6, Oliveira Arbquina, 240 kg
        int op86 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/01/12");
        tblRepo.addToTableAplicacoesFatProd(op86, "BIOFERTIL N6", 240, "kg");

//        02/11/2023 operação de colheita de azeitona Arbquina, 400 kg
        int op87 = tblRepo.addToTableOperacoes(1, 102, "OLIVEIRA ARBEQUINA", 0, "2023/11/02");
        tblRepo.addToTableColheitas(op87, 400, "kg");

//        05/11/2023 operação de colheita de azeitona Picual, 300 kg
        int op88 = tblRepo.addToTableOperacoes(1, 102, "OLIVEIRA PICUAL", 0, "2023/11/05");
        tblRepo.addToTableColheitas(op88, 300, "kg");

//        08/11/2023 operação de colheita de azeitona Galega, 350 kg
        int op89 = tblRepo.addToTableOperacoes(1, 102, "OLIVEIRA GALEGA", 0, "2023/11/08");
        tblRepo.addToTableColheitas(op89, 350, "kg");

//        02/06/2023 operação de rega, setor 10, 60 min
        int op90 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/06/02");
        tblRepo.addToTableRegas(op90, 60, "min", 10);

//        02/07/2023 operação de rega, setor 10, 120 min
        int op91 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/07/02");
        tblRepo.addToTableRegas(op91, 120, "min", 10);

//        02/08/2023 operação de rega, setor 10, 180 min
        int op92 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/08/02");
        tblRepo.addToTableRegas(op92, 180, "min", 10);

//        04/09/2023 operação de rega, setor 10, 120 min
        int op93 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/09/04");
        tblRepo.addToTableRegas(op93, 120, "min", 10);

//        02/10/2023 operação de rega, setor 10, 60 min
        int op94 = tblRepo.addToTableOperacoes(1, 102, null, 0, "2023/10/02");
        tblRepo.addToTableRegas(op94, 60, "min", 10);

//
//        Campo Novo: (108)
//
//        01/04/2023 operação de aplicação de fator de produção Biocal Composto, no solo, 1.1 ha, 500 kg
        int op95 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/04/01");
        tblRepo.addToTableAplicacoesFatProd(op95, "BIOCAL COMPOSTO", 500, "kg");

//        05/04/2023 operação de semeadura de cenouras Sugarsnax Hybrid, 0.5 ha, 1.2 kg
        int op96 = tblRepo.addToTableOperacoes(1, 108, "CENOURA SUGARSNAX HYBRID", 0, "2023/04/05");
        tblRepo.addToTableSementeiras(op96, 1.2, "kg");

//        06/04/2023 operação de semeadura de abóbora manteiga, 0.6 ha, 1.5 kg
        int op97 = tblRepo.addToTableOperacoes(1, 108, "ABOBORA MANTEIGA", 0, "2023/04/06");
        tblRepo.addToTableSementeiras(op97, 1.5, "kg");

//        08/05/2023 operação de monda de plantação de cenouras Sugarsnax Hybrid, 0.5 ha
        int op98 = tblRepo.addToTableOperacoes(1, 108, "CENOURA SUGARSNAX HYBRID", 0, "2023/05/08");
        tblRepo.addToTableMondas(op98, 0.5, "ha");

//        20/05/2023 operação de monda de plantação de abóbora manteiga, 0.6 ha
        int op136 = tblRepo.addToTableOperacoes(1, 108, "ABOBORA MANTEIGA", 0, "2023/05/20");
        tblRepo.addToTableMondas(op136, 0.6, "ha");

//        14/06/2023 operação de colheita de cenouras Sugarsnax Hybrid, 1500 kg
        int op99 = tblRepo.addToTableOperacoes(1, 108, "CENOURA SUGARSNAX HYBRID", 0, "2023/06/14");
        tblRepo.addToTableColheitas(op99, 1500, "kg");

//        20/06/2023 operação de monda de plantação de abóbora manteiga, 0.6 ha
        int op137 = tblRepo.addToTableOperacoes(1, 108, "ABOBORA MANTEIGA", 0, "2023/06/20");
        tblRepo.addToTableMondas(op137, 0.6, "ha");

//        28/06/2023 operação de colheita de cenouras Sugarsnax Hybrid, 2500 kg
        int op100 = tblRepo.addToTableOperacoes(1, 108, "CENOURA SUGARSNAX HYBRID", 0, "2023/06/28");
        tblRepo.addToTableColheitas(op100, 2500, "kg");

//        03/07/2023 operação de aplicação de fator de produção Fertimax Extrume de Cavalo, no solo, 0.5 ha, 1800 kg
        int op101 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/03");
        tblRepo.addToTableAplicacoesFatProd(op101, "FERTIMAX EXTRUME DE CAVALO", 1800, "kg");

//        04/07/2023 operação de mobilização do solo, 0.5 ha
        int op138 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/04");
        tblRepo.addToTableMobilizacesSolo(op138, 0.5, "ha");

//        05/07/2023 operação de semeadura de cenouras Danvers Half Long, 0.5 ha, 1.2 kg
        int op102 = tblRepo.addToTableOperacoes(1, 108, "CENOURA DANVERS HALF LONG", 0, "2023/07/05");
        tblRepo.addToTableSementeiras(op102, 1.2, "kg");

//        08/08/2023 operação de monda de plantação de cenouras Danvers Half Long, 0.5 ha
        int op139 = tblRepo.addToTableOperacoes(1, 108, "CENOURA DANVERS HALF LONG", 0, "2023/08/08");
        tblRepo.addToTableMondas(op139, 0.5, "ha");

//        15/09/2023 operação de colheita de abóbora manteiga, 8000 kg
        int op103 = tblRepo.addToTableOperacoes(1, 108, "ABOBORA MANTEIGA", 0, "2023/09/15");
        tblRepo.addToTableColheitas(op103, 8000, "kg");

//        25/09/2023 operação de colheita de abóbora manteiga, 5000 kg
        int op104 = tblRepo.addToTableOperacoes(1, 108, "ABOBORA MANTEIGA", 0, "2023/09/25");
        tblRepo.addToTableColheitas(op104, 5000, "kg");

//        18/09/2023 operação de colheita de cenouras Danvers Half Long, 900 kg
        int op105 = tblRepo.addToTableOperacoes(1, 108, "CENOURA DANVERS HALF LONG", 0, "2023/09/18");
        tblRepo.addToTableColheitas(op105, 900, "kg");

//        22/09/2023 operação de colheita de cenouras Danvers Half Long, 1500 kg
        int op106 = tblRepo.addToTableOperacoes(1, 108, "CENOURA DANVERS HALF LONG", 0, "2023/09/22");
        tblRepo.addToTableColheitas(op106, 1500, "kg");

//        05/10/2023 operação de colheita de cenouras Danvers Half Long, 1200 kg
        int op107 = tblRepo.addToTableOperacoes(1, 108, "CENOURA DANVERS HALF LONG", 0, "2023/10/05");
        tblRepo.addToTableColheitas(op107, 1200, "kg");

//        10/10/2023 operação de mobilização do solo, 1.1 ha
        int op140 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/10/10");
        tblRepo.addToTableMobilizacesSolo(op140, 1.1, "ha");

//        12/10/2023 operação de semeadura de Tremoço Amarelo, 1.1 ha, 32 kg
        int op108 = tblRepo.addToTableOperacoes(1, 108, "TREMOÇO AMARELO", 0, "2023/10/12");
        tblRepo.addToTableCultivos(1, 108, "TREMOÇO AMARELO", "2023/10/12", null, 1.1, "ha", 0);
        tblRepo.addToTableSementeiras(op108, 32, "kg");
//
//        12/06/2023 operação de rega, setor 42, 60 min, "06:00"
        int op109 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/12");
        tblRepo.addToTableRegas(op109, 60, "min", 42, "06:00");

//        19/06/2023 operação de rega, setor 42, 60 min, "06:00"
        int op110 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/19");
        tblRepo.addToTableRegas(op110, 60, "min", 42, "06:00");

//        08/07/2023 operação de rega, setor 42, 120 min, "04:00"
        int op112 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/08");
        tblRepo.addToTableRegas(op112, 120, "min", 42, "04:00");

//        22/07/2023 operação de rega, setor 42, 150 min, "04:00"
        int op114 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/22");
        tblRepo.addToTableRegas(op114, 150, "min", 42, "04:00");

//        05/08/2023 operação de rega, setor 42, 120 min, "21:30"
        int op116 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/05");
        tblRepo.addToTableRegas(op116, 120, "min", 42, "21:30");

//        19/08/2023 operação de rega, setor 42, 120 min, "21:30"
        int op118 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/19");
        tblRepo.addToTableRegas(op118, 120, "min", 42, "21:30");

//        26/08/2023 operação de rega, setor 42, 120 min, "21:30"
        int op119 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/26");
        tblRepo.addToTableRegas(op119, 120, "min", 42, "21:30");

//        31/08/2023 operação de rega, setor 42, 120 min, "21:30"
        int op120 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/31");
        tblRepo.addToTableRegas(op120, 120, "min", 42, "21:30");

//        05/09/2023 operação de rega, setor 42, 120 min, "21:30"
        int op121 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/05");
        tblRepo.addToTableRegas(op121, 120, "min", 42, "21:30");

//
//        02/06/2023 operação de rega, setor 41, 120 min, "07:30"
        int op123 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/02");
        tblRepo.addToTableRegas(op123, 120, "min", 41, "07:30");

//        09/06/2023 operação de rega, setor 41, 120 min, "06:20"
        int op124 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/09");
        tblRepo.addToTableRegas(op124, 120, "min", 41, "06:20");

//        16/07/2023 operação de rega, setor 41, 120 min, "06:20"
        int op126 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/16");
        tblRepo.addToTableRegas(op126, 120, "min", 41, "06:20");

//        23/07/2023 operação de rega, setor 41, 120 min, "06:20"
        int op127 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/23");
        tblRepo.addToTableRegas(op127, 120, "min", 41, "06:20");

//        30/07/2023 operação de rega, setor 41, 120 min, "06:20"
        int op128 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/30");
        tblRepo.addToTableRegas(op128, 120, "min", 41, "06:20");

//        07/08/2023 operação de rega, setor 41, 120 min, "06:20"
        int op129 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/07");
        tblRepo.addToTableRegas(op129, 120, "min", 41, "06:20");

//        14/08/2023 operação de rega, setor 41, 120 min, "06:20"
        int op130 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/14");
        tblRepo.addToTableRegas(op130, 120, "min", 41, "06:20");

//        21/08/2023 operação de rega, setor 41, 120 min, "06:20"
        int op131 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/21");
        tblRepo.addToTableRegas(op131, 120, "min", 41, "06:20");

//        28/08/2023 operação de rega, setor 41, 120 min, "06:20"
        int op132 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/28");
        tblRepo.addToTableRegas(op132, 120, "min", 41, "06:20");

//        06/09/2023 operação de rega, setor 41, 120 min, "06:20"
        int op133 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/06");
        tblRepo.addToTableRegas(op133, 120, "min", 41, "06:20");

//        13/09/2023 operação de rega, setor 41, 120 min, "07:00"
        int op134 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/13");
        tblRepo.addToTableRegas(op134, 120, "min", 41, "07:00");

//        20/09/2023 operação de rega, setor 41, 120 min, "07:00"
        int op135 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/20");
        tblRepo.addToTableRegas(op135, 120, "min", 41, "07:00");
    }

    public void addExtraDataSprint3() {

        // Inserir os seguintes fatores de produção:
        // Matéria Orgânica Líquida, Tecniferti MOL, da Tecniferti, líquido, Matéria Orgânica 27%, Azoto (N) 3,6%, Azoto orgânico (N) 2%, Fósforo total (P2O5) 1%, Potássio total (K2O) 3%, Carbono Orgânico Total (COT) 15%, Ácidos Fúlvicos (AF) 10%
        int ft1 = tblRepo.addToTableFichasTecnicas("TECNIFERTI MOL");
        tblRepo.addToTableFormulacoes("LIQUIDO", "MATERIA ORGANICA", "SOLO");
        tblRepo.addToTableFatoresProducao("TECNIFERTI MOL", "TECNIFERTI", ft1, "LIQUIDO", "MATERIA ORGANICA", "SOLO");
        int subsId1 = tblRepo.addToTableSubstancias("Matéria Orgânica", 0.27, "%");
        tblRepo.addToTableComponentesFT(ft1, 1, subsId1);
        int subsId2 = tblRepo.addToTableSubstancias("N", 5.6, "%");
        tblRepo.addToTableComponentesFT(ft1, 2, subsId2);
        int subsId3 = tblRepo.addToTableSubstancias("P2O5", 1.0, "%");
        tblRepo.addToTableComponentesFT(ft1, 3, subsId3);
        int subsId4 = tblRepo.addToTableSubstancias("K2O", 3.0, "%");
        tblRepo.addToTableComponentesFT(ft1, 4, subsId4);
        int subId5 = tblRepo.addToTableSubstancias("COT", 15.0, "%");
        tblRepo.addToTableComponentesFT(ft1, 5, subId5);
        int subsId6 = tblRepo.addToTableSubstancias("AF", 10.0, "%");
        tblRepo.addToTableComponentesFT(ft1, 6, subsId6);

        // Adubo orgânico, soluSOP 52, da K+S, pó molhavel, Enxofre (SO3) 45%, Potássio total (K2O) 52,5%, pH 7
        int ft2 = tblRepo.addToTableFichasTecnicas("SOLUSOP 52");
        tblRepo.addToTableFormulacoes("PO MOLHAVEL", "ADUBO ORGANICO", "SOLO");
        tblRepo.addToTableFatoresProducao("SOLUSOP 52", "K+S", ft2, "PO MOLHAVEL", "ADUBO ORGANICO", "SOLO");
        int subsId7 = tblRepo.addToTableSubstancias("SO3", 45.0, "%");
        tblRepo.addToTableComponentesFT(ft2, 1, subsId7);
        int subsId8 = tblRepo.addToTableSubstancias("K2O", 52.5, "%");
        tblRepo.addToTableComponentesFT(ft2, 2, subsId8);
        int subsId9 = tblRepo.addToTableSubstancias("pH", 7.0, "pH");
        tblRepo.addToTableComponentesFT(ft2, 3, subsId9);

        // Adubo líquido, Floracal Flow SL, da Plymag, líquido, pH 7.8, densidade 1,6 kg/l, Óxido de calcio (CaO) 35%
        int ft3 = tblRepo.addToTableFichasTecnicas("FLORACAL FLOW SL");
        tblRepo.addToTableFormulacoes("LIQUIDO", "ADUBO LIQUIDO", "SOLO");
        tblRepo.addToTableFatoresProducao("FLORACAL FLOW SL", "PLYMAG", ft3, "LIQUIDO", "ADUBO LIQUIDO", "SOLO");
        int subsId10 = tblRepo.addToTableSubstancias("pH", 7.8, "pH");
        tblRepo.addToTableComponentesFT(ft3, 1, subsId10);
        int subsId11 = tblRepo.addToTableSubstancias("DENSIDADE", 1.6, "kg/l");
        tblRepo.addToTableComponentesFT(ft3, 1, subsId11);
        int subsId12 = tblRepo.addToTableSubstancias("CaO", 35.0, "%");
        tblRepo.addToTableComponentesFT(ft3, 1, subsId12);

        // Adubo líquido, Kiplant AllGrip, da Asfertglobal, líquido
        int ft4 = tblRepo.addToTableFichasTecnicas("KIPLANT ALLGRIP");
        tblRepo.addToTableFormulacoes("LIQUIDO", "ADUBO LIQUIDO", "SOLO");
        tblRepo.addToTableFatoresProducao("KIPLANT ALLGRIP", "ASFERTGOBAL", ft4, "LIQUIDO", "ADUBO LIQUIDO", "SOLO");

        // Adubo líquido, Cuperdem, da Asfertglobal, líquido, Cobre (Cu) 6%
        int ft5 = tblRepo.addToTableFichasTecnicas("CUPERDEM");
        tblRepo.addToTableFormulacoes("LIQUIDO", "ADUBO LIQUIDO", "SOLO");
        tblRepo.addToTableFatoresProducao("CUPERDEM", "ASFERTGOBAL", ft5, "LIQUIDO", "ADUBO LIQUIDO", "SOLO");
        int subsId13 = tblRepo.addToTableSubstancias("Cu", 6.0, "%");
        tblRepo.addToTableComponentesFT(ft5, 1, subsId13);


        //Inserir as seguintes receitas de fertirrega
        // Receita 10
        //      EPSO TOP, K+S, 1.5 kg/ha
        //      soluSOP 52, K+S, 2.5 kg/ha
        //      Floracal Flow SL, Plymag, 1.7 l/ha
        tblRepo.addToTableMix(10);
        tblRepo.addToTableReceitas(10, "EPSO TOP", 1.5, "kg");
        tblRepo.addToTableReceitas(10, "SOLUSOP 52", 2.5, "kg");
        tblRepo.addToTableReceitas(10, "FLORACAL FLOW SL", 1.7, "l");


        // Receita 11
        //      Tecniferti MOL, Tecniferti, 60 l/ha
        //      Kiplant AllGrip, Asfertglobal, 2 l/ha
        tblRepo.addToTableMix(11);
        tblRepo.addToTableReceitas(11, "TECNIFERTI MOL", 60, "l");
        tblRepo.addToTableReceitas(11, "KIPLANT ALLGRIP", 2, "l");


        ///// Campo Novo: (108)

        // 30/06/2023 operação de fertirrega, setor 42, 120 min, 04:00, receita 11
        int op1 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/30");
        tblRepo.addToTableFertirregas(op1, 120, "min", 42, 11, "04:00");

        // 15/07/2023 operação de fertirrega, setor 42, 120 min, 04:00, receita 10
        int op2 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/15");
        tblRepo.addToTableFertirregas(op2, 120, "min", 42, 10, "04:00");

        // 29/07/2023 operação de fertirrega, setor 42, 150 min, 04:00, receita 11
        int op3 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/29");
        tblRepo.addToTableFertirregas(op3, 150, "min", 42, 11, "04:00");

        // 12/08/2023 operação de fertirrega, setor 42, 120 min, 21:30, receita 10
        int op4 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/12");
        tblRepo.addToTableFertirregas(op4, 120, "min", 42, 10, "21:30");

        // 20/05/2023 operação de fertirrega, setor 41, 120 min, 07:30, receita 11
        int op5 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/20");
        tblRepo.addToTableFertirregas(op5, 120, "min", 41, 11, "07:30");

        // 09/07/2023 operação de fertirrega, setor 41, 120 min, 06:20, receita 10
        int op6 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/09");
        tblRepo.addToTableFertirregas(op6, 120, "min", 41, 10, "06:20");

        // 02/06/2023 operação de fertirrega, setor 10, 60 min, 06:00, receita 10
        int op7 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/02");
        tblRepo.addToTableFertirregas(op7, 60, "min", 10, 10, "06:00");

        // 17/06/2023 operação de rega, setor 10, 30 min, 05:00
        int op8 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/17");
        tblRepo.addToTableRegas(op8, 30, "min", 10, "05:00");

        // 02/07/2023 operação de fertirrega, setor 10, 120 min, 06:00, receita 10
        int op9 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/02");
        tblRepo.addToTableFertirregas(op9, 120, "min", 10, 10, "06:00");

        // 17/07/2023 operação de rega, setor 10, 30 min, 05:00
        int op10 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/17");
        tblRepo.addToTableRegas(op10, 30, "min", 10, "05:00");

        // 02/08/2023 operação de fertirrega, setor 10, 180 min, 05:00, receita 10
        int op11 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/02");
        tblRepo.addToTableFertirregas(op11, 180, "min", 10, 10, "05:00");

        // 17/08/2023 operação de rega, setor 10, 60 min, 05:00
        int op12 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/17");
        tblRepo.addToTableRegas(op12, 60, "min", 10, "05:00");

        // 04/09/2023 operação de rega, setor 10, 120 min, 06:00
        int op13 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/04");
        tblRepo.addToTableRegas(op13, 120, "min", 10, "06:00");

        // 18/09/2023 operação de rega, setor 10, 30 min, 05:00
        int op14 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/18");
        tblRepo.addToTableRegas(op14, 30, "min", 10, "05:00");

        // 02/10/2023 operação de rega, setor 10, 60 min, 06:00
        int op15 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/10/02");
        tblRepo.addToTableRegas(op15, 60, "min", 10, "06:00");

        //
        // 13/05/2023 operação de rega, setor 22, 120 min, 23:00
        int op16 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/05/13");
        tblRepo.addToTableRegas(op16, 120, "min", 22, "23:00");

        // 02/06/2023 operação de rega, setor 22, 120 min, 23:00
        int op17 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/02");
        tblRepo.addToTableRegas(op17, 120, "min", 22, "23:00");

        // 16/06/2023 operação de fertirrega, setor 22, 120 min, 23:00, receita 10
        int op18 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/06/16");
        tblRepo.addToTableFertirregas(op18, 120, "min", 22, 10, "23:00");

        // 01/07/2023 operação de rega, setor 22, 120 min, 23:00
        int op19 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/01");
        tblRepo.addToTableRegas(op19, 120, "min", 22, "23:00");

        // 08/07/2023 operação de rega, setor 22, 180 min, 23:00
        int op20 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/08");
        tblRepo.addToTableRegas(op20, 180, "min", 22, "23:00");

        // 15/07/2023 operação de fertirrega, setor 22, 180 min, 23:00, receita 11
        int op21 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/15");
        tblRepo.addToTableFertirregas(op21, 180, "min", 22, 11, "23:00");

        // 22/07/2023 operação de rega, setor 22, 180 min, 23:00
        int op22 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/22");
        tblRepo.addToTableRegas(op22, 180, "min", 22, "23:00");

        // 29/07/2023 operação de rega, setor 22, 180 min, 23:00
        int op23 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/07/29");
        tblRepo.addToTableRegas(op23, 180, "min", 22, "23:00");

        // 05/08/2023 operação de rega, setor 22, 150 min, 23:00
        int op24 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/05");
        tblRepo.addToTableRegas(op24, 150, "min", 22, "23:00");

        // 10/08/2023 operação de fertirrega, setor 22, 150 min, 23:00, receita 10
        int op25 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/10");
        tblRepo.addToTableFertirregas(op25, 150, "min", 22, 10, "23:00");

        // 17/08/2023 operação de rega, setor 22, 150 min, 23:00
        int op26 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/17");
        tblRepo.addToTableRegas(op26, 150, "min", 22, "23:00");

        // 24/08/2023 operação de rega, setor 22, 120 min, 23:00
        int op27 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/08/24");
        tblRepo.addToTableRegas(op27, 120, "min", 22, "23:00");

        // 02/09/2023 operação de rega, setor 22, 120 min, 23:00
        int op28 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/02");
        tblRepo.addToTableRegas(op28, 120, "min", 22, "23:00");

        // 09/09/2023 operação de fertirrega, setor 22, 120 min, 23:00, receita 10
        int op29 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/09");
        tblRepo.addToTableFertirregas(op29, 120, "min", 22, 10, "23:00");

        // 18/09/2023 operação de rega, setor 22, 120 min, 23:00
        int op30 = tblRepo.addToTableOperacoes(1, 108, null, 0, "2023/09/18");
        tblRepo.addToTableRegas(op30, 120, "min", 22, "23:00");
    }
}
