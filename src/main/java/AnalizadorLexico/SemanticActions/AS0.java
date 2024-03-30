package AnalizadorLexico.SemanticActions;
import java.io.IOException;
import java.io.Reader;

import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.Enums.SymbolType;

/* Esta clase contiene la implementacion de la accion semantica 0.
** AS 0: Se encarga únicamente de leer el siguiente carácter en el código del programa. */
public class AS0 implements SemanticAction {

	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		try {
			final int character = reader.read();
			if (character == SymbolType.NL)
				LexicalAnalyzer.setLine(LexicalAnalyzer.getLine()+1);
		}
		catch(IOException exception) {
            exception.printStackTrace();
        }
		
		return active_state;
	}

}
