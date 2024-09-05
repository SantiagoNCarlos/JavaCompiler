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
        String returnCode = null;

        final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
        CodeGenerator.auxVariableCounter++;

        switch (node.getType()) {
            case UsesType.USHORT ->
                returnCode = "\tMOV AL, " + leftNodeToken + "\n" +
                        "\tMUL " + rightNodeToken + "\n" +
                        "\tMOV " + auxVariableName + ",AL"; // Store the 8 bit USHORT mul in aux variable.
            case UsesType.LONG ->
                returnCode = "\tMOV EAX, " + leftNodeToken + "\n" +
                         "\tMUL " + rightNodeToken + "\n" +
                         "\tMOV " + auxVariableName + ",EAX"; // Store the 32 bit LONG mul in aux variable.
            case UsesType.FLOAT ->
                returnCode = "\tFLD " + CodeConstructor.replaceTokenUnvalidChars(leftNodeToken) + "\n" + // Load left node to FPU stack
                        "\tFLD " + CodeConstructor.replaceTokenUnvalidChars(rightNodeToken) + "\n" + // Load right node to FPU stack
                        "\tFMUL \n" + // Multiply...
                        "\tFLD ST(0)\n" + // Duplicate the result (ST(0) is now also in ST(1))
                        "\tFABS \n" + // Take the absolute value of the result in ST(0)
                        "\tFCOM _max_float_value_\n" + // Compare result with max float value (ST remains the same)
                        "\tFSTSW AX\n" + // Store FPU status word in AX
                        "\tSAHF\n" + // Transfer condition codes to CPU flags
                        "\tJA _ProductOverflowError_\n" + // Jump if result > max_float_reg (overflow)
                        "\tFXCH\n" + // Exchange ST(0) with ST(1). ST(1) has the original result!
                        "\tFSTP " + auxVariableName + "\n"; // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
        }

        SymbolTable.addSymbol(auxVariableName, TokenType.ID, node.getType(), UsesType.AUX_VAR);

        node.setName(auxVariableName);
        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
	}
}
