package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

import java.util.Optional;

public class PrintConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();

		final String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
        final String type = leftChild.getType();

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

		return generatePrintCode(leftNodeToken, type);
	}

    private static String generatePrintCode(String token, String type) {
        System.out.println("[ " + token + " ]" + " " + type);
        return switch (type) {
            case UsesType.LONG -> "\tinvoke printf, cfm$(\"%d\\n\"), " + CodeConstructor.replaceTokenUnvalidChars(token) + "\n";
            case UsesType.USHORT -> "\tinvoke printf, cfm$(\"%hu\\n\"), " + CodeConstructor.replaceTokenUnvalidChars(token) + "\n";
            case UsesType.FLOAT ->  "\tFLD " + CodeConstructor.replaceTokenUnvalidChars(token) + // For floats, we pass the value to a double. Printf expects doubles!
                                    "\n\tFSTP _float_aux_print_" +
                                    "\n\tinvoke printf, cfm$(\"%.20Lf\\n\"), _float_aux_print_\n";
            case UsesType.CADENA -> "\tinvoke StdOut, addr " + "s_" + CodeConstructor.replaceTokenUnvalidChars(token) + "\n"; // Asumiendo que 'token' es una cadena de caracteres
            default -> "";
        };
    }
}
