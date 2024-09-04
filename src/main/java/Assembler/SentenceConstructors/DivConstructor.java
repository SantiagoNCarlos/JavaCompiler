package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Optional;

public class DivConstructor implements CodeConstructor {
    public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
		String rightNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(rightChild));

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

    private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
        String returnCode = null;

        final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
        CodeGenerator.auxVariableCounter++;

        switch (node.getType()) {
            case UsesType.USHORT ->
                        returnCode = "\tMOV AL, " + leftNodeToken + "\n" +
                        "\tDIV " + rightNodeToken + "\n" +
                        "\tMOV " + auxVariableName + ",AL"; // Store the 8 bit USHORT mul in aux variable.
            case UsesType.LONG ->
                        returnCode = "\tMOV EAX, " + leftNodeToken + "\n" +
                        "\tDIV " + rightNodeToken + "\n" +
                        "\tMOV " + auxVariableName + ",EAX"; // Store the 32 bit LONG mul in aux variable.
            case UsesType.FLOAT ->
                        returnCode = "\tFLD " + leftNodeToken + "\n" + // Load left node to FPU stack
                        "\tFLD " + rightNodeToken + "\n" + // Load right node to FPU stack
                        "\tFDIV\n" + // Divide...
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
