package AnalizadorLexico.SemanticActions;
import java.io.Reader;

/*
	Esta clase contiene la implementacion de la accion semantica 9.
	AS 9: Esta acción está destinada a los comentarios. Suprime el token leído hasta el momento, y lee el siguiente carácter
	Ya que los comentarios no debemos enviarlos al Parser, no devolvemos el token correspondiente, lo ignoramos.
*/
public class AS9 implements SemanticAction {
	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		token.delete(0, token.length()); // Reinicia el token

        try {
            reader.read(); // Lee el siguiente caracter
        } catch (Exception e) {
            e.printStackTrace();
        }

        return active_state;
	}

}
