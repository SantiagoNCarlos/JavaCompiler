package AnalizadorLexico.SemanticActions;
import java.io.Reader;

import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica 8.
	AS 8: Solo se encarga de devolver el primer caracter del token.
*/
public class AS8 implements SemanticAction {
//	Logger logger = Logger.getInstance();
    @Override
    public int executeAction(Reader reader, StringBuilder token) {
        try {
            final char character = token.toString().charAt(0);
//            logger.logDebugLexical("Simbolo: " + character);
            return character;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return error;
    }

}
