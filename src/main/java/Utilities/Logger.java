package Utilities;

import AnalizadorSintactico.Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private static final String LOG_SYNTAX_DEBUG_FILE_PATH = "debug-sintactico.log";
    private static final String LOG_SYNTAX_ERRORS_FILE_PATH = "errors-sintactico.log";
    private static final String LOG_LEXICAL_DEBUG_FILE_PATH = "debug-lexico.log";
    private static final String LOG_LEXICAL_ERRORS_FILE_PATH = "errors-lexico.log";
    private PrintWriter writer_debug_syntax;
    private PrintWriter writer_errors_syntax;
    private PrintWriter writer_debug_lexical;
    private PrintWriter writer_errors_lexical;

    private static final class LoggerHolder {
        private static final Logger instance = new Logger();
    }

    public static Logger getInstance() {
        return LoggerHolder.instance;
    }

    private Logger() {}

    public void loggerInitializer(String fileSuffix, String fileInit) {
        try {
            writer_debug_syntax = new PrintWriter(new FileWriter(fileSuffix + LOG_SYNTAX_DEBUG_FILE_PATH, false));
            writer_errors_syntax = new PrintWriter(new FileWriter( fileSuffix + LOG_SYNTAX_ERRORS_FILE_PATH, false));
            writer_debug_lexical = new PrintWriter(new FileWriter(fileSuffix + LOG_LEXICAL_DEBUG_FILE_PATH, false));
            writer_errors_lexical = new PrintWriter(new FileWriter(fileSuffix + LOG_LEXICAL_ERRORS_FILE_PATH, false));

            writer_debug_syntax.println(fileInit);
            writer_errors_syntax.println(fileInit);
            writer_debug_lexical.println(fileInit);
            writer_errors_lexical.println(fileInit);

            System.out.println(fileInit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logDebugSyntax(String message) {
//    	System.out.println("Log Debug Sintax: " + message);
        writer_debug_syntax.println(message);     // Write to log file
        writer_debug_syntax.flush();                 // Ensure data is written immediately
    }

    public void logDebugLexical(String message) {
//        System.out.println("Log Debug Lexical: " + message);
        writer_debug_lexical.println(message);     // Write to log file
        writer_debug_lexical.flush();                 // Ensure data is written immediately
    }

    public void logErrorSyntax(String message) {
//    	System.out.println("Log Error Syntax: " + message);
        Parser.yyerror(message);
        writer_errors_syntax.println(message);     // Write to log file
        writer_errors_syntax.flush();                 // Ensure data is written immediately
    }

    public void logErrorLexical(String message) {
//        System.out.println("Log Error Lexical: " + message);
        Parser.yyerror(message);
        writer_errors_lexical.println(message);     // Write to log file
        writer_errors_lexical.flush();                 // Ensure data is written immediately
    }

    public void logWarningLexical(String message) {
        System.out.println(message);
        writer_errors_lexical.println(message);     // Write to log file
        writer_errors_lexical.flush();                 // Ensure data is written immediately
    }

    public void close() {
        if (writer_debug_syntax != null) {
            writer_debug_syntax.close();
        }
        if (writer_debug_lexical != null) {
            writer_debug_lexical.close();
        }
        if (writer_errors_syntax != null) {
            writer_errors_syntax.close();
        }
        if (writer_errors_lexical != null) {
            writer_errors_lexical.close();
        }
    }
}
