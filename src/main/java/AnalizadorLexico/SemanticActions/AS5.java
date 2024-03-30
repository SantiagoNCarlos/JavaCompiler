package AnalizadorLexico.SemanticActions;
import java.io.IOException;
import java.io.Reader;

import AnalizadorLexico.*;
import AnalizadorLexico.Enums.*;
import Utilities.Logger;

import static java.lang.Math.abs;

/*
	Esta clase contiene la implementacion de la accion semantica 5.
	AS 5: Agrega el valor de la constante entera leída a la tabla de símbolos si no está en ella,
	      también verifica el rango entero y devuelve el identificador del Token CTE
*/
public class AS5 implements SemanticAction {
	Logger logger = Logger.getInstance();

	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		String symbol = null;
		String suffix = "";
		String tipo = "Entero"; //default
		try {
			final char nextChar = (char) reader.read(); // Lee el siguiente caracter
			token.append(nextChar); // Concatena el caracter actual
			symbol = token.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (symbol != null && symbol.contains("_")) {

			String[] parts = symbol.split("_", 2);
			symbol = parts[0];
			suffix = parts[1];

			// Determinar el tipo correcto según el sufijo
	        if (suffix.equals("l")) {
	            tipo = UsesType.LONG;
	        } else if (suffix.equals("us")) {
	            tipo = UsesType.USHORT;
	        }
			final String completeInt = symbol + "_" + suffix;
			SymbolTable.addSymbol(completeInt, TokenType.CONSTANT, tipo, UsesType.CONSTANT, LexicalAnalyzer.getLine());
//			logger.logDebugLexical("Entero: " + completeInt);
		} else {
			SymbolTable.addSymbol(symbol, TokenType.CONSTANT, tipo, UsesType.CONSTANT, LexicalAnalyzer.getLine());
			logger.logErrorLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo entera no posee sufijo.");
//			logger.logDebugLexical("Entero: " + symbol);
		}
		return TokenType.CONSTANT;
	}

}
