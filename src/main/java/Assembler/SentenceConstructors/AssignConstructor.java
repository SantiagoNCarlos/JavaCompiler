package Assembler.SentenceConstructors;

import java.util.Objects;
import java.util.Optional;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

public class AssignConstructor implements CodeConstructor{

	public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.getToken(leftChild);
		String rightNodeToken = CodeConstructor.getToken(rightChild);

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
		node.setLeftChild(null);
		node.setRightChild(null);
		node.setLeaf(true);

		final String varType = node.getType();

		return switch (varType) {
			case UsesType.USHORT -> // 8 bits
					"\tMOV AL, " + rightNodeToken + "\n" +
					"\tMOV " + leftNodeToken + ",AL\n";
			case UsesType.LONG -> // 32 bits
					"\tMOV EAX, " + rightNodeToken + "\n" +
					"\tMOV " + leftNodeToken + ",EAX\n";
			case UsesType.FLOAT -> // 32 bits FP
					"\tFLD " + rightNodeToken.replace(".", "_") + "\n" +
					"\tFSTP " + leftNodeToken.replace(".", "_") + "\n";
			default -> "";
		};
	}
}
