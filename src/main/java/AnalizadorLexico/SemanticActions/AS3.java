package AnalizadorLexico.SemanticActions;

import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import AnalizadorLexico.*;
import AnalizadorLexico.Enums.*;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica 3.
	AS 3: Destinada principalmente al enviar tokens de identificadores.
	Revisa si lo leído hasta el momento es un identificador, verifica su longitud y
	envía un warning si supera el valor maximo de 20 caracteres. También lo agrega a la tabla de símbolos y devuelve su identificación.
*/
public class AS3 implements SemanticAction {
	Logger logger = Logger.getInstance();

	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		String symbol = token.toString();

		if (token.length() > DelimiterType.maxIDLength) { //se pasa de longitud
			final String truncatedSymbol = token.substring(0, DelimiterType.maxIDLength);
			final String uniqueSuffix = generateUniqueSuffix(token.toString());
			symbol = truncatedSymbol + uniqueSuffix;

			logger.logWarningLexical("Warning: Línea " + LexicalAnalyzer.getLine() + ": el identificador " + token +
							   " fue truncado porque supera la longitud máxima de caracteres.");

			LexicalAnalyzer.currentToken.setLength(0);
			LexicalAnalyzer.currentToken.append(symbol);
		}
		//SymbolTable.addSymbol(symbol, TokenType.ID, "ID", LexicalAnalyzer.getLine());

		final int id = TokenType.ID;
		logger.logDebugLexical("ID: " + symbol);

		return id;
	}

	public String generateUniqueSuffix(String input) {
		int checksum = calculateChecksum(input);
		return "_" + Integer.toHexString(checksum);
	}

	private int calculateChecksum(String input) {
		// Calcula el checksum complemento a 2 del token completo, previo a ser truncado.
		int sum = 0;
		for (char c : input.toCharArray()) {
			sum += c;
		}
		return sum & 0xFF; // Tomar solo los 8 bits menos significativos
	}
}
