package Assembler.SentenceConstructors;

import AnalizadorLexico.Enums.UsesType;
import AnalizadorSintactico.Parser;
import ArbolSintactico.SyntaxNode;

public class AssignConstructor implements CodeConstructor{

	public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
		String rightNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(rightChild));

		return createDirective(node, leftNodeToken, rightNodeToken);
	}
	/*
	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {

		if (node.isPropagated()) return "";

		String varType = node.getType();
		String rightNodeType = node.getRightChild().getType();

		if (node.getRightChild().isLeaf()) {
			if (rightNodeToken.contains("_us")) {
				rightNodeType = UsesType.USHORT;
			} else if (rightNodeToken.contains("_l")) {
				rightNodeType = UsesType.LONG;
			} else {
				rightNodeType = UsesType.FLOAT;
			}
		}

		if (node.getLeftChild().isLeaf()) {
			varType = node.getLeftChild().getType();
		}

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
	}*/
	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
		if (node.isPropagated()) return "";

		// Determine types
		String varType = node.getType();
		String rightNodeType = node.getRightChild().getType();

		// Handle leaf nodes type determination
		if (node.getRightChild().isLeaf() && !rightNodeToken.contains("aux") && !node.getType().equals(UsesType.FLOAT)) {
			rightNodeType = determineTypeFromToken(rightNodeToken);
		}
		if (node.getLeftChild().isLeaf() && !leftNodeToken.contains("aux")) {
			varType = node.getLeftChild().getType();
		}

		// Clean up node
		node.setLeftChild(null);
		node.setRightChild(null);
		node.setLeaf(true);

		// Generate appropriate directive
		return generateDirective(varType, rightNodeType, leftNodeToken, rightNodeToken);
	}

	private static String determineTypeFromToken(String token) {
		if (token.contains("_us")) {
			return UsesType.USHORT;
		} else if (token.contains("_l")) {
			return UsesType.LONG;
		} else {
			return UsesType.FLOAT;
		}
	}

	private static String generateDirective(String leftType, String rightType,
											String leftToken, String rightToken) {
		StringBuilder directive = new StringBuilder();

		switch (leftType) {
			case UsesType.USHORT -> {
				if (!rightType.equals(UsesType.USHORT)) {
					Parser.yyerror("USHORTS solo pueden tener valores USHORTS asignados.");
				}
				directive.append("\tMOV AL, ").append(rightToken).append("\n")
						 .append("\tMOV ").append(leftToken).append(",AL\n");
			}
			case UsesType.LONG -> {
				if (rightType.equals(UsesType.USHORT)) {
					directive.append("\tMOVZX EAX, ").append(rightToken).append("\n")
							.append("\tMOV ").append(leftToken).append(",EAX\n");
				} else if (rightType.equals(UsesType.LONG)) {
					directive.append("\tMOV EAX, ").append(rightToken).append("\n")
							.append("\tMOV ").append(leftToken).append(",EAX\n");
				}
			}
			case UsesType.FLOAT -> {
				if (rightType.equals(UsesType.USHORT)) {
					directive.append("\tMOVZX EAX, ").append(rightToken).append("\n")
							.append("\tMOV DWORD PTR [esp-4], EAX\n")
							.append("\tFLD DWORD PTR [esp-4]\n")
							.append("\tFSTP ").append(leftToken).append("\n");
				} else if (rightType.equals(UsesType.LONG)) {
					directive.append("\tMOV EAX, ").append(rightToken).append("\n")
							.append("\tMOV DWORD PTR [esp-4], EAX\n")
							.append("\tFILD DWORD PTR [esp-4]\n")
							.append("\tFSTP ").append(leftToken).append("\n");
				} else
				{
					directive.append("\tFLD ").append(rightToken).append("\n")
							.append("\tFSTP ").append(leftToken).append("\n");
				}
			}
		}

		return directive.toString();
	}

}
