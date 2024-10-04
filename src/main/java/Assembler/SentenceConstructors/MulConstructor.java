package Assembler.SentenceConstructors;

import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class MulConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.getToken(leftChild);
		String rightNodeToken = CodeConstructor.getToken(rightChild);

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

    private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        StringBuilder returnCode = new StringBuilder();
        final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
        CodeGenerator.auxVariableCounter++;

        // Determine actual types if nodes are leaves (constants)
        final String leftType = CodeConstructor.verifyActualType(node.getLeftChild(), leftNodeToken, node.getLeftChild().getType());
        final String rightType = CodeConstructor.verifyActualType(node.getRightChild(), rightNodeToken, node.getRightChild().getType());

        switch (node.getType()) {
            case UsesType.USHORT -> {
                if (!leftType.equals(UsesType.USHORT) || !rightType.equals(UsesType.USHORT)) {
                    throw new RuntimeException("Cannot multiply non-USHORT values in USHORT context");
                }
                returnCode.append("\tMOV AL, ").append(leftNodeToken).append("\n")
                        .append("\tMUL BYTE PTR ").append(rightNodeToken).append("\n")
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
                            .append("\tMUL ECX\n");
                } else {
                    returnCode.append("\tMUL ").append(rightNodeToken).append("\n");
                }

                returnCode.append("\tMOV ").append(auxVariableName).append(", EAX");
            }
            case UsesType.FLOAT -> {
                // Handle left operand
                CodeConstructor.generateFloatLoadSentences(leftNodeToken, leftType, returnCode);

                // Handle right operand
                CodeConstructor.generateFloatLoadSentences(rightNodeToken, rightType, returnCode);


                // Multiply and check for overflow
                returnCode.append("\tFMUL\n")
                        .append("\tFLD ST(0)\n")
                        .append("\tFABS\n")
                        .append("\tFCOM _max_float_value_\n")
                        .append("\tFSTSW AX\n")
                        .append("\tSAHF\n")
                        .append("\tJA _ProductOverflowError_\n")
                        .append("\tFXCH\n")
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
