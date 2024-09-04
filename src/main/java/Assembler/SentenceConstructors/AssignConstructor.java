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

		String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
		String rightNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(rightChild));

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
		final String varType = node.getType();
		final String rightNodeType = node.getRightChild().getType();

		node.setLeftChild(null);
		node.setRightChild(null);
		node.setLeaf(true);

		String directive = "";

		switch (varType) {
			case UsesType.USHORT -> // 8 bits
				directive = "\tMOV AL, " + rightNodeToken + "\n" +
							"\tMOV " + leftNodeToken + ",AL\n";
			case UsesType.LONG -> {// 32 bits
				if (rightNodeType.equals(UsesType.USHORT)) {
					directive = "\tMOVZX EAX, " + rightNodeToken + "\n" +
								"\tMOV " + leftNodeToken + ",EAX\n";
				} else {
					directive = "\tMOV EAX, " + rightNodeToken + "\n" +
								"\tMOV " + leftNodeToken + ",EAX\n";
				}
			}
			case UsesType.FLOAT -> {// 32 bits FP
				if (rightNodeType.equals(UsesType.USHORT)) {
					directive = "\tMOVZX EAX, " + rightNodeToken + "\n" +
								"\tMOV DWORD PTR [esp-4], EAX\n" +
								"\tFLD DWORD PTR [esp-4]\n" +
								"\tFSTP " + leftNodeToken + "\n";
				} else {
					directive = "\tFLD " + rightNodeToken + "\n" +
								"\tFSTP " + leftNodeToken + "\n";
				}
			}
			default -> directive = "";
		};
		return directive;
	}
}
