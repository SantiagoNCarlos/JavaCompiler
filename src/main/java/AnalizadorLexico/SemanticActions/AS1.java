package AnalizadorLexico.SemanticActions;
import java.io.IOException;
import java.io.Reader;

/* Esta clase contiene la implementacion de la accion semantica 1.
** AS 1: Se lee el siguiente carácter y lo concatena. */
public class AS1 implements SemanticAction {

	@Override
	public int executeAction(Reader reader, StringBuilder token) {
		try {
			final char character = (char) reader.read();
			token.append(character);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return active_state;
	}

}
