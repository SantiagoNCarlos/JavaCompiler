package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Optional;

public class ConditionConstructor implements CodeConstructor {

    private static String assembler_command;

    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.getToken(leftChild);
		String rightNodeToken = CodeConstructor.getToken(rightChild);

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

    private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        setCondition(node.getName());

        String returnCode = null;

        final String labelName = " label" + CodeGenerator.codeLabelsCounter;

        CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
        CodeGenerator.codeLabelsCounter++;

        switch (node.getType()) {
            case UsesType.USHORT -> {
                returnCode = "\tMOV AL, " + leftNodeToken + "\n" +
                            "\tMOV BL, " + rightNodeToken + "\n" +
                            "\tCMP AL, BL\n" +
                            "\t" + assembler_command + labelName + "\n"; // Use sentence to determine if jump is needed
            }
            case UsesType.LONG -> {
                returnCode = "\tMOV EBX, " + leftNodeToken + "\n" +
                            "\tMOV ECX, " + rightNodeToken + "\n" +
                            "\tCMP EBX, ECX\n" + // Compare...
                            "\t" + assembler_command + labelName + "\n"; // Use sentence to determine if jump is needed
            }
            case UsesType.FLOAT -> {
                returnCode = "\tFLD " + leftNodeToken.replace(".", "_").replace(":", "_") + "\n" + // Load left node to FPU stack
                            "\tFLD " + rightNodeToken.replace(".", "_").replace(":", "_") + "\n" + // Load right node to FPU stack
                            "\tFSTSW aux_mem_2bytes\n" + // Compare...
                            "\tMOV AX, aux_mem_2bytes\n" +
                            "\tSAHF\n" + // Set the state bits in flags to enable comparison next
                            "\t" + assembler_command + labelName + "\n";
            }
        }

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
	}

    private static void setCondition(String condition) {
        switch (condition) {
            case "==" -> assembler_command = "JNE";
            case "<=" -> assembler_command = "JA";
            case ">=" -> assembler_command = "JB";
            case "<" -> assembler_command = "JAE";
            case ">" -> assembler_command = "JBE";
            case "!!" -> assembler_command = "JE";
        }
    }

}
