package AnalizadorLexico;
import AnalizadorLexico.Enums.SymbolType;
import AnalizadorLexico.SemanticActions.*;
import AnalizadorSintactico.Parser;

import java.io.IOException;
import java.io.Reader;

public class LexicalAnalyzer {

    public static Reader reader;
    public static StringBuilder currentToken = new StringBuilder();
    private static int line = 1;
    public static int currentState = 0;
    private static Parser parser;
	public static int getLine() {
		return line;
	}
	
	public static Parser getParser() {
		return parser;
	}

	public static void setParser(Parser parserInstance) {
		parser = parserInstance;
	}

	public static void setLine(final int linea) {
		line = linea;
	}

	public static boolean endOfFile(Reader reader) throws IOException {
        reader.mark(1);
        int value = reader.read();
        reader.reset();

        return value < 0;
    }
	
	public static char getNextCharWithoutAdvancing(Reader reader) throws IOException {
        reader.mark(1);
        char next_character = (char) reader.read();
        reader.reset();

        return next_character;
    }
	
	private static int getCharType(final char character) {
        if (Character.isDigit(character)) {
            return SymbolType.DIGIT;
        } 
        else if (character == 'e' || character == 'E' || character == 'u' || character == 's' || character == 'l') {
        	return character;
        }
        else if (Character.isLowerCase(character)) {
            return SymbolType.LOWERCASE;
        //} else if (character != 'F' && Character.isUpperCase(character)) {

        } else if (Character.isUpperCase(character)) {
            return SymbolType.UPPERCASE;
        } else {
            return character;
        }
    }
	
	public static int changeState(Reader lector, final char character) {
        final int currentCharacter = switch (getCharType(character)) {
            case SymbolType.BLANK -> 0;
            case SymbolType.TAB -> 1;
            case SymbolType.NL, SymbolType.PL -> 2;
            case SymbolType.LOWERCASE -> 3;
            case SymbolType.UPPERCASE -> 4;
            case '_' -> 5;
            case SymbolType.DIGIT -> 6;
            case '.' -> 7;
            case 'E' -> 8;
            case 'e' -> 9;
            case '+' -> 10;
            case '-' -> 11;
            case '/' -> 12;
            case '(' -> 13;
            case ')' -> 14;
            case ',' -> 15;
            case ';' -> 16;
            case '!' -> 17;
            case '=' -> 18;
            case '>' -> 19;
            case '<' -> 20;
            case '{' -> 21;
            case '}' -> 22;
            case '*' -> 23;
            case '#' -> 24;
	        case 'u' -> 25;
	        case 's' -> 26;
	        case 'l' -> 27;
            default -> 28;
        };

//        System.out.print("DATOS: " + character + "   " + currentState);

        SemanticAction action = MatrixManager.getSemanticAction(currentState, currentCharacter);
        currentState = MatrixManager.getState(currentState, currentCharacter);

//        System.out.println("  NEXT S: " + currentState);

        return action.executeAction(lector, currentToken);
    }

}