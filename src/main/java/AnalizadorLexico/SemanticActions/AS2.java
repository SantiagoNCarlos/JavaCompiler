package AnalizadorLexico.SemanticActions;
import java.io.Reader;

import Utilities.Logger;

/* Esta clase contiene la implementacion de la accion semantica 2.
** AS 2: Encargada de leer los símbolos:"-", "/", "(", ")", ",", ";", "{", "}"
** reconocer el literal, y devolver Token del mismo. */
public class AS2 implements SemanticAction {
//	Logger logger = Logger.getInstance();
    @Override
    public int executeAction(Reader reader, StringBuilder token) {
        // TODO Auto-generated method stub
        try {
            final char character = (char) reader.read();
            //logger.logDebugLexical("Simbolo: " + character);
            return character;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return error;
    }
}
