package AnalizadorLexico.SemanticActions;

import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.LexicalAnalyzer;
import AnalizadorLexico.SymbolTable;
import Utilities.Logger;

import java.io.Reader;

public class AS10 implements SemanticAction  {

    Logger logger = Logger.getInstance();

    @Override
    public int executeAction(Reader reader, StringBuilder token) {
        final String symbol = token.toString();

        SymbolTable.addSymbol(symbol, TokenType.CONSTANT,  UsesType.CONSTANT, LexicalAnalyzer.getLine());
        logger.logErrorLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo entera no posee sufijo.");
//        logger.logDebugLexical("Entero: " + symbol);

        return TokenType.CONSTANT;
    }
}
