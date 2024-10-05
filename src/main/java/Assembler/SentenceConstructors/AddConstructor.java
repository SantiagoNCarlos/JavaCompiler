package Assembler.SentenceConstructors;

import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class AddConstructor implements CodeConstructor{

    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.getToken(leftChild);
		String rightNodeToken = CodeConstructor.getToken(rightChild);

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

    private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        String returnCode = null;
        final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
        CodeGenerator.auxVariableCounter++;

        // Determine actual types if nodes are leaves (constants)
        final String leftType = CodeConstructor.verifyActualType(node.getLeftChild(), leftNodeToken, node.getLeftChild().getType());
        final String rightType = CodeConstructor.verifyActualType(node.getRightChild(), rightNodeToken, node.getRightChild().getType());

        final String varType = node.getType();
        switch (varType) {
            case UsesType.USHORT -> {
                returnCode = "\tMOV AL, " + leftNodeToken + "\n" +
                        "\tADD AL, " + rightNodeToken + "\n" +
                        "\tMOV " + auxVariableName + ", AL\n" +
                        "\tJC _SumOverflowError_\n";
            }
            case UsesType.LONG -> {
                StringBuilder code = new StringBuilder();

                // Handle left operand
                if (leftType.equals(UsesType.USHORT)) {
                    code.append("\tMOVZX EAX, ").append(leftNodeToken).append("\n");
                } else {
                    code.append("\tMOV EAX, ").append(leftNodeToken).append("\n");
                }

                // Handle right operand
                if (rightType.equals(UsesType.USHORT)) {
                    code.append("\tMOVZX ECX, ").append(rightNodeToken).append("\n")
                            .append("\tADD EAX, ECX\n");
                } else {
                    code.append("\tADD EAX, ").append(rightNodeToken).append("\n");
                }

                code.append("\tMOV ").append(auxVariableName).append(", EAX\n")
                        .append("\tJO _SumOverflowError_\n");

                returnCode = code.toString();
            }
            case UsesType.FLOAT -> {
                StringBuilder code = new StringBuilder();

                // Handle left operand
                CodeConstructor.generateFloatLoadSentences(leftNodeToken, leftType, code);

                // Handle right operand
                CodeConstructor.generateFloatLoadSentences(rightNodeToken, rightType, code);

                code.append("\tFADD\n")
                        .append("\tFSTP ").append(auxVariableName);

                returnCode = code.toString();
            }
        }

        SymbolTable.addSymbol(auxVariableName, TokenType.ID, node.getType(), UsesType.AUX_VAR);
        node.setName(auxVariableName);
        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
    }


}
