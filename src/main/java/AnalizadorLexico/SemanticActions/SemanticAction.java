package AnalizadorLexico.SemanticActions;
import java.io.Reader;

public interface SemanticAction {
	
	int active_state = -1;
	int error = -2;

	int executeAction(Reader reader, StringBuilder token);

}
