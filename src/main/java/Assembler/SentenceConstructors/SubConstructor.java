package Assembler.SentenceConstructors;

import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class SubConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		final String leftNodeToken = CodeConstructor.getToken(leftChild);
		final String rightNodeToken = CodeConstructor.getToken(rightChild);

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
        CodeGenerator.auxVariableCounter++;

        // Determine actual types if nodes are leaves (constants)
        final String leftType = CodeConstructor.verifyActualType(node.getLeftChild(), leftNodeToken, node.getLeftChild().getType());
        final String rightType = CodeConstructor.verifyActualType(node.getRightChild(), rightNodeToken, node.getRightChild().getType());

        StringBuilder returnCode = new StringBuilder();

        switch (node.getType()) {
            case UsesType.USHORT -> {
                returnCode.append("\tMOV AL, ").append(leftNodeToken).append("\n")
                        .append("\tSUB AL, ").append(rightNodeToken).append("\n")
                        .append("\tMOV ").append(auxVariableName).append(", AL");
            }
            case UsesType.LONG -> {
                // Handle left operand
                if (leftType.equals(UsesType.USHORT)) {
                    returnCode.append("\tMOVZX EAX, BYTE PTR ").append(leftNodeToken).append("\n");
                } else {
                    returnCode.append("\tMOV EAX, ").append(leftNodeToken).append("\n");
                }

                // Handle right operand
                if (rightType.equals(UsesType.USHORT)) {
                    returnCode.append("\tMOVZX ECX, BYTE PTR ").append(rightNodeToken).append("\n")
                            .append("\tSUB EAX, ECX\n");
                } else {
                    returnCode.append("\tSUB EAX, ").append(rightNodeToken).append("\n");
                }

                returnCode.append("\tMOV ").append(auxVariableName).append(", EAX");
            }
            case UsesType.FLOAT -> {
                // Handle left operand
                CodeConstructor.generateFloatLoadSentences(leftNodeToken, leftType, returnCode);

                // Handle right operand
                CodeConstructor.generateFloatLoadSentences(rightNodeToken, rightType, returnCode);

                returnCode.append("\tFSUB\n")
                        .append("\tFSTP ").append(auxVariableName);
            }
        }

        SymbolTable.addSymbol(auxVariableName, TokenType.ID, node.getType(), UsesType.AUX_VAR);

        node.setName(auxVariableName);
        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode.toString();
	}
}
