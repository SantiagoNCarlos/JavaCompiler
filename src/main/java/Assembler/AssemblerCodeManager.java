package Assembler;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import java.util.ArrayList;


public class AssemblerCodeManager {

    public static final CodeWriter assemblerWriter = CodeWriter.getInstance();

    public AssemblerCodeManager(String assemblerFileDir) {
        assemblerWriter.writerInitializer(assemblerFileDir);
    }

    public void generateCode(SyntaxNode node) {
        SyntaxTreeDecoder.processNode(node);
    }

    public void assemblerFileCreation() {
        final String nl = System.lineSeparator();
        // Define code directives
        final String header = ".586" + nl +
                ".model flat, stdcall" + nl +
                "option casemap:none" + nl;

        // Define code includes
        final String includes = nl +
                "include \\masm32\\include\\windows.inc" + nl +
                "include \\masm32\\include\\kernel32.inc" + nl +
                "include \\masm32\\include\\masm32.inc" + nl +
                nl +
                "includelib \\masm32\\lib\\kernel32.lib" + nl +
                "includelib \\masm32\\lib\\masm32.lib" + nl +
                "includelib \\masm32\\lib\\msvcrt.lib" + nl +

                "printf PROTO C :PTR BYTE, :VARARG"  ;

        final String stack_keyboard = ".stack 200h";
        // Define .data keyword
        final String data_keyword = ".data";
        // Define .code keyword
        final String code_keyword = ".code";

        final String directives = "\tformatStringLong db \"%d\", 0" + nl +
                                  "\tformatStringUShort db \"%hu\", 0" + nl +
                                  "\tformatStringFloat db \"%f\", 0" + nl;

        // Start keyword
        final String start_keyword = "start:";

        // End keyword
        final String end_keyword = "END start";

        assemblerWriter.writeSentence(header);
        assemblerWriter.writeSentence(includes);
        assemblerWriter.writeSentence(nl + stack_keyboard);
        assemblerWriter.writeSentence(data_keyword + nl);
        assemblerWriter.writeSentence(directives);
        writeSymbolTable();
        assemblerWriter.writeSentence(nl + code_keyword);
        assemblerWriter.writeSentence(start_keyword + nl);
        assemblerWriter.writeSentences();
        assemblerWriter.writeSentence(nl + end_keyword);
        assemblerWriter.close();
    }

    public void writeSymbolTable() {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());

        for (Attribute entry : entries) {
            final String token = entry.getToken();
            final String use = entry.getUso();
            final String type = entry.getType();

            if (use == null || token == null) {
                continue;
            }
            StringBuilder directive = new StringBuilder();

            switch (use.toUpperCase()) {
                case UsesType.VARIABLE, UsesType.PARAMETER, UsesType.AUX_VAR -> {
                        directive = new StringBuilder(token.replace(":", "_") + (type.equals("USHORT") ? " DB ?" : " DD ?"));
                }
                case UsesType.CONSTANT -> { // Use a 'c_' prefix for constants
                    if (type.equals(UsesType.FLOAT)) {
                        directive = new StringBuilder("c_" + token.replace(".", "_") + " DD " + token);
                    } else {
                        final String constant_value = token.contains("_") ? token.substring(0, token.indexOf("_")) : "";

                        directive = new StringBuilder("c_" + token.replace(":", "_") +
                                (type.equals("USHORT") ? " DB " : " DD ") +
                                constant_value);
                    }
                }
                case UsesType.CADENA -> { // Use a 's_' prefix for constants
                    directive = new StringBuilder("s_" + token.replace(" ", "_") + " DB \"" + token + "\", 10, 0"); // New line char (10) and null terminator (0)
                }
//                case UsesType.CLASE -> {}
//                case UsesType.FUNCTION -> {}
//                case UsesType.CLASS_VAR -> {}
            }

            if (!directive.isEmpty()) {
                assemblerWriter.writeSentence("\t" + directive);
            }
        }
    }
}
