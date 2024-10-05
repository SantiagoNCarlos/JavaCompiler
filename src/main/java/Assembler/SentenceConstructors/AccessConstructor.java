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
            final String varName = getFullMemberName(leftChild.getName(), rightChild.getName());


            node.setLeftChild(null);
			node.setRightChild(null);
			node.setLeaf(true);

            return varName;
        } else
            return "";
    }

    private static String getFullMemberName(final String objectName, final String membName) {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());

        String fullObjectName = "";

        for (Attribute entry : entries) {
            if (entry.getToken().startsWith(objectName + ":") && entry.getUso().equals(UsesType.CLASS_VAR)) {
                fullObjectName = entry.getToken();
                break;
            }
        }

        for (Attribute entry : entries) {
            if (entry.getToken().startsWith(membName + ":") &&
                entry.getToken().endsWith(fullObjectName))
            {
                return CodeConstructor.replaceTokenUnvalidChars(entry.getToken());
            }
        }

        return "";
    }
}
