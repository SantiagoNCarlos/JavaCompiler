package AnalizadorLexico.SemanticActions;

import java.io.IOException;
import java.io.Reader;

import AnalizadorLexico.*;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica de salida.
	ASa: Agrega el valor de la cadena leída a la tabla de símbolos si no está en ella y
	     devuelve el identificador del Token Cadena.
*/
public class ASa implements SemanticAction {
//	Logger logger = Logger.getInstance();
    @Override
    public int executeAction(Reader reader, StringBuilder token) {
        try {
            reader.read(); // Lee el siguiente caracter
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        SymbolTable.addSymbol(token.toString(), TokenType.CHAIN, UsesType.CADENA, UsesType.CADENA ,LexicalAnalyzer.getLine());

        return TokenType.CHAIN;
    }

}
