package AnalizadorLexico.SemanticActions;
import java.io.IOException;
import java.io.Reader;

import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.Enums.SymbolType;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica de error.
	ASE: Esta acción se encarga de informar acerca de un error ocurrido.
*/
public class ASE implements SemanticAction {
	Logger logger = Logger.getInstance();
	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		try {
			final char character = (char) reader.read();
			logger.logErrorLexical("Error lexico en la linea: " + LexicalAnalyzer.getLine());
			if(character == SymbolType.NL)
				LexicalAnalyzer.setLine(LexicalAnalyzer.getLine()+1);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return error;
	}

}
