package AnalizadorLexico.SemanticActions;
import java.io.Reader;
import AnalizadorLexico.ReservedWordsTable;
import Utilities.Logger;

/*
	Esta clase contiene la implementacion de la accion semantica 4.
	AS 4: Destinada principalmente al enviar tokens de palabras reservadas.
	Revisa si lo leído hasta el momento es una palabra reservada.
    Si es una palabra reservada, devuelve el identificador de token de la misma. Caso contrario, devuelve un valor por default.
*/
public class AS4 implements SemanticAction {
    Logger logger = Logger.getInstance();
    @Override
    public int executeAction(Reader reader, StringBuilder token) {
        final String symbol = token.toString();
        final int id_rw = ReservedWordsTable.wordID(symbol);
        if (id_rw == ReservedWordsTable.nonReservedWord) {
            logger.logErrorLexical("Error Lexico: La palabra reservada " + symbol + " no existe.");
        }

        return id_rw;
    }
}
