package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Objects;
import java.util.Optional;

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

		final String varType = node.getType();

        switch (varType) {
            case UsesType.USHORT ->
                returnCode = "\tMOV AL, " + leftNodeToken + "\n" +
                             "\tADD AL, " + rightNodeToken + "\n" +
                             "\tMOV " + auxVariableName + ",AL" + "\n" + // Store the 8 bit USHORT mul in aux variable.
                             "\tJC _SumOverflowError_\n"; // Gets triggered when an overflow without signed numbers occurs...
            case UsesType.LONG ->
                returnCode = "\tMOV EAX, " + leftNodeToken + "\n" +
                            "\tADD EAX, " + rightNodeToken + "\n" +
                            "\tMOV " + auxVariableName + ",EAX" + "\n" + // Store the 32 bit LONG mul in aux variable.
                            "\tJO _SumOverflowError_\n"; // Gets triggered when an overflow with signed numbers occurs...
            case UsesType.FLOAT ->
                returnCode = "\tFLD " + leftNodeToken.replace(".", "_").replace("+","_") + "\n" + // Load left node to FPU stack
                        "\tFLD " + rightNodeToken.replace(".", "_").replace("+","_") + "\n" + // Load right node to FPU stack
                        "\tFADD\n" + // Add...
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
