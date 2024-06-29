package Assembler;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssemblerCodeManager {

    public static final CodeWriter assemblerWriter = CodeWriter.getInstance();
    private static Map<String, String> classFullNames = new HashMap<>();

    public AssemblerCodeManager(String assemblerFileDir) {
        assemblerWriter.writerInitializer(assemblerFileDir);
    }

    public void generateCode(SyntaxNode node) {
//        SyntaxTreeDecoder.processNode(node);
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
                                  "\tformatStringFloat db \"%f\", 0";

        // Start keyword
        final String start_keyword = "start:";

        // End keyword
        final String end_keyword = "end start";

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
                case UsesType.CLASE -> {
                    parseAndAddToClassMap(entry.getToken());
                }
//                case UsesType.FUNCTION -> {}
                case UsesType.CLASS_VAR -> {
                    for (Attribute attr : getClassMembers(entry.getType())) {
                        final String dir = attr.getToken().replace(":", "_") + "_" + entry.getToken().replace(":", "_") + (type.equals("USHORT") ? " DB ?" : " DD ?") + "\n\t";
                        directive.append(dir);
                    }

                    if (!directive.isEmpty()) { // removes last \n\t
                        directive.setLength(directive.length() - 2);
                    }
                }
            }

            if (!directive.isEmpty()) {
                assemblerWriter.writeSentence("\t" + directive);
            }
        }
    }

    private void parseAndAddToClassMap(String input) {
        String[] parts = input.split(":");
        if (parts.length > 1) {
            String key = parts[0];
            classFullNames.put(key, input);
        } else {
            System.out.println("Input string format is incorrect: " + input);
        }
    }

    private String swapComponents(String input) {
        String[] parts = input.split(":");
        if (parts.length == 2) {
            return parts[1] + ":" + parts[0];
        } else {
            System.out.println("Input string format is incorrect: " + input);
            return input;  // Devuelve la cadena original si el formato es incorrecto
        }
    }

    private ArrayList<Attribute> getClassMembers(final String className) {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());
        ArrayList<Attribute> members = new ArrayList<>();

        // Find entry related to class definition
        final String classAmbit = swapComponents(classFullNames.get(className));

        // Get all members from a class
        for (Attribute entry : entries) {
            if (entry.getToken().contains(classAmbit) && entry.getUso().equals(UsesType.VARIABLE)) {
                members.add(entry);
            }
        }
        return members;
    }

}
