package AnalizadorLexico.SemanticActions;
import java.io.Reader;

import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.SymbolTable;
import AnalizadorLexico.Enums.*;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica 6.
	AS 6: Agrega el valor de la constante flotante leída a la tabla de símbolos si no está en ella,
	    también verifica el rango correspondiente y devuelve el identificador del Token CTE.
*/
public class AS6 implements SemanticAction {
//	Logger logger = Logger.getInstance();
    @Override
	public int executeAction(Reader r, StringBuilder token) {
		String symbol = token.toString();

		SymbolTable.addSymbol(symbol, TokenType.FLOAT_CONSTANT, UsesType.FLOAT, UsesType.CONSTANT, LexicalAnalyzer.getLine());

		return TokenType.FLOAT_CONSTANT;
	}




}