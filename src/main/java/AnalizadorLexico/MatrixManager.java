package AnalizadorLexico;

import AnalizadorLexico.SemanticActions.*;
import Utilities.Logger;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.Scanner;

public class MatrixManager {
    private static int rows = 18;
    private static int columns = 29;
    private static Integer [][] state_matrix;
    private static SemanticAction[][] actions_matrix;

    private static SemanticAction createAction(String action) {
        return switch (action) {
            case "AS0" -> new AS0();
            case "AS1" -> new AS1();
            case "AS2" -> new AS2();
            case "ASE" -> new ASE();
            case "AS3" -> new AS3();
            case "AS4" -> new AS4();
            case "AS5" -> new AS5();
            case "AS6" -> new AS6();
            case "AS7" -> new AS7();
            case "AS8" -> new AS8();
            case "AS9" -> new AS9();
            case "AS10" -> new AS10();
            case "ASa" -> new ASa();
            default -> null;
        };
    }

    private static int createState(String action) {
        return switch (action) {
            case "F" -> -1;
            case "E" -> -2;
            default -> Integer.parseInt(action);
        };
    }

    private static void fillMatrix(String path, int rows, int columns, Object[][] matrix, boolean isSemanticMatrix) {
        ClassLoader classLoader = MatrixManager.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        // Verifies if the file was found
        if (inputStream == null) {
            Logger.getInstance().logErrorLexical("No se pudo encontrar el recurso: " + path);
            Logger.getInstance().logErrorSyntax("No se pudo encontrar el recurso: " + path);
            return;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Reads matrix stored in csv
            String[] nextRecord;
            reader.readNext(); // Avoids reading first row

            for (int i = 0; i < rows; ++i) {
                nextRecord = reader.readNext();

                if (nextRecord != null) {
                    for (int j = 0; j < columns; ++j) {
                        matrix[i][j] = isSemanticMatrix ? createAction(nextRecord[j+1]) : createState(nextRecord[j+1]);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static void fillMatrices() {
        actions_matrix = new SemanticAction[rows][columns];
        fillMatrix("SemanticActionsMatrix.csv", rows, columns, actions_matrix, true);

        state_matrix = new Integer[rows][columns];
        fillMatrix("StateMatrix.csv", rows, columns, state_matrix, false);
    }

    public static int getState(int row, int column) {
        return state_matrix[row][column];
    }

    public static SemanticAction getSemanticAction(int row, int column) {
        return actions_matrix[row][column];
    }
}
