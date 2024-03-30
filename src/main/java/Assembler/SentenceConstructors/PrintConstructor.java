package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

import java.util.Optional;

public class PrintConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
        Optional<Attribute> childNode = node.getLeftChild().getNodeValue();
        if (childNode.isPresent()) {
            Attribute attribute = childNode.get();
            String token = attribute.getToken();
            String type = attribute.getType();

            node.setLeftChild(null);
            node.setRightChild(null);
            node.setLeaf(true);

            return generatePrintCode(token, type);
        }
        return "";
    }

    private static String generatePrintCode(String token, String type) {
        return switch (type) {
            case UsesType.LONG -> "    invoke printf, offset formatStringLong, " + token.replace(":", "_") + "\n";
            case UsesType.USHORT -> "    invoke printf, offset formatStringUShort, " + token.replace(":", "_") + "\n";
            case UsesType.FLOAT -> "    invoke printf, offset formatStringFloat, " + token.replace(":", "_") + "\n";
            case UsesType.CADENA -> "    invoke printf, addr " + token + "\n"; // Asumiendo que 'token' es una cadena de caracteres
            default -> "";
        };
    }
}
