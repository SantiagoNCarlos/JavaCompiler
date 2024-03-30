package AnalizadorLexico;
import Utilities.Logger;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReservedWordsTable {
	
	private static final Map<String, Integer> table = readMap("ReservedWords.csv");
    
	public static final int nonReservedWord = 278;
	
	public static int wordID(String word) {
		return table.getOrDefault(word, nonReservedWord);
	}

	public static Map<String, Integer> readMap(String path) {
		Map<String, Integer> table = new HashMap<>();

        ClassLoader classLoader = MatrixManager.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        // Verifies if the file was found
        if (inputStream == null) {
            Logger.getInstance().logErrorLexical("No se pudo encontrar el recurso: " + path);
            Logger.getInstance().logErrorSyntax("No se pudo encontrar el recurso: " + path);
            return null;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] nextRecord;

            while ((nextRecord = reader.readNext()) != null) {
                final String reservedWord = nextRecord[0];
                final int id = Integer.parseInt(nextRecord[1]);
                table.put(reservedWord, id);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return table;
	}
}
