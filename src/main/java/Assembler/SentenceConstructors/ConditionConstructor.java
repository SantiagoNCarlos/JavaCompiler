package Assembler.SentenceConstructors;


import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class ConditionConstructor implements CodeConstructor {

    private static String assembler_command;

    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
		String rightNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(rightChild));

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

    private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        setCondition(node.getName());
        final String labelName = " label" + CodeGenerator.codeLabelsCounter;
        CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
        CodeGenerator.codeLabelsCounter++;

        // Determine actual types if nodes are leaves (constants)
        final String leftType = CodeConstructor.verifyActualType(node.getLeftChild(), leftNodeToken, node.getLeftChild().getType());
        final String rightType = CodeConstructor.verifyActualType(node.getRightChild(), rightNodeToken, node.getRightChild().getType());

        StringBuilder returnCode = new StringBuilder();

        switch (node.getType()) {
            case UsesType.USHORT -> {
                returnCode.append("\tMOV AL, ").append(leftNodeToken).append("\n")
                        .append("\tMOV BL, ").append(rightNodeToken).append("\n")
                        .append("\tCMP AL, BL\n")
                        .append("\t").append(assembler_command).append(labelName).append("\n");
            }
            case UsesType.LONG -> {
                // Handle left operand
                if (leftType.equals(UsesType.USHORT)) {
                    returnCode.append("\tMOVZX EBX, BYTE PTR ").append(leftNodeToken).append("\n");
                } else {
                    returnCode.append("\tMOV EBX, ").append(leftNodeToken).append("\n");
                }

                // Handle right operand
                if (rightType.equals(UsesType.USHORT)) {
                    returnCode.append("\tMOVZX ECX, BYTE PTR ").append(rightNodeToken).append("\n");
                } else {
                    returnCode.append("\tMOV ECX, ").append(rightNodeToken).append("\n");
                }

                returnCode.append("\tCMP EBX, ECX\n")
                        .append("\t").append(assembler_command).append(labelName).append("\n");
            }
            case UsesType.FLOAT -> {
                // Handle left operand
                CodeConstructor.generateFloatLoadSentences(leftNodeToken, leftType, returnCode);

                // Handle right operand and comparison
                if (rightType.equals(UsesType.USHORT)) {
                    CodeConstructor.generateFloatUshortLoad(rightNodeToken, returnCode);
                    returnCode.append("\tFCOMP\n");
                } else if (rightType.equals(UsesType.LONG)) {
                    CodeConstructor.generateFloatLongLoad(rightNodeToken, returnCode);
                    returnCode.append("\tFCOMP\n");
                } else {
                    returnCode.append("\tFCOM ").append(rightNodeToken).append("\n");
                }

                returnCode.append("\tFSTSW AX\n")
                        .append("\tSAHF\n")
                        .append("\t").append(assembler_command).append(labelName).append("\n");
            }
        }

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode.toString();
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
