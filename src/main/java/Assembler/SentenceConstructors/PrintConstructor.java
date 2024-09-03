package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

import java.util.Optional;

public class PrintConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();

		final String leftNodeToken = CodeConstructor.getToken(leftChild).replace("#","");
        final String type = leftChild.getType();

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

		return generatePrintCode(leftNodeToken, type);
	}

    private static String generatePrintCode(String token, String type) {
        System.out.println("[ " + token + " ]" + " " + type);
        return switch (type) {
            case UsesType.LONG -> "\tinvoke printf, offset formatStringLong, " + token.replace(":", "_") + "\n";
//            case UsesType.USHORT -> "\tinvoke printf, cfm$(\"%.%llu\\n\"), " + token.replace(":", "_") + "\n";
            case UsesType.USHORT -> "\tinvoke printf, cfm$(\"%hu\\n\"), " + token.replace(":", "_") + "\n";
            case UsesType.FLOAT -> "\tinvoke printf, cfm$(\"%.20Lf\\n\"), " + token.replace(":", "_") + "\n";
            case UsesType.CADENA -> "\tinvoke StdOut, addr " + "s_" + token.replace(" ", "_") + "\n"; // Asumiendo que 'token' es una cadena de caracteres
            default -> "";
        };
    }
}
