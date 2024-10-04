package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;

import java.util.ArrayList;

public class AccessConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
        SyntaxNode leftChild = node.getLeftChild();
        SyntaxNode rightChild = node.getRightChild();

        if (leftChild != null && rightChild != null) {
            final String varName = getVarName(leftChild, rightChild);

            node.setLeftChild(null);
			node.setRightChild(null);
			node.setLeaf(true);

            return varName;
        } else
            return "";
    }

    private static String getVarName(SyntaxNode objectNode, SyntaxNode memberNode) {
        // Gets member variable name for a particular object.
        final String memb = memberNode.getName();
        return memberNode.getName() + "_" + getFullClassName(objectNode.getName());
    }

    private static String getFullClassName(final String objectName) {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());

        String className = "";
        String fullObjectName = "";

        for (Attribute entry : entries) {
            if (entry.getToken().contains(objectName + ":") && entry.getUso().equals(UsesType.CLASS_VAR)) {
                className = entry.getType();
                fullObjectName = CodeConstructor.replaceTokenUnvalidChars(entry.getToken());
                break;
            }
        }
        for (Attribute entry : entries) {
            if (entry.getToken().contains(className + ":") && entry.getUso().equals(UsesType.CLASE)) {
                return swapComponents(entry.getToken()) + "_" + fullObjectName;
            }
        }

        return "";
    }

    private static String swapComponents(String input) {
        String[] parts = input.split(":");
        if (parts.length == 2) {
            return parts[1] + "_" + parts[0];
        } else {
            System.out.println("Input string format is incorrect: " + input);
            return input;  // Devuelve la cadena original si el formato es incorrecto
        }
    }
}
