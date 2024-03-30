package AnalizadorLexico.SemanticActions;
import java.io.Reader;

import AnalizadorLexico.ReservedWordsTable;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica 7.
	AS 7: Lee siguiente carácter, lo concatena con el carácter actual, devuelve el Token correspondiente
*/
public class AS7 implements SemanticAction {
//	Logger logger = Logger.getInstance();
	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		try {
			final char nextChar = (char) reader.read(); // Lee el siguiente caracter
			token.append(nextChar); // Concatena el caracter actual
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String symbol = token.toString();
//		logger.logDebugLexical("Simbolo: " + symbol);

		return ReservedWordsTable.wordID(symbol);
	}

}
