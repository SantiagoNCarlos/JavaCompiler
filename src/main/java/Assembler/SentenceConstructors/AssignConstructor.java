package Assembler.SentenceConstructors;

import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

import static Assembler.CodeGenerator.functionsLabelsStack;

public class AssignConstructor implements CodeConstructor{

	public static String generateStructureCode(SyntaxNode node) {
		SyntaxNode leftChild = node.getLeftChild();
		SyntaxNode rightChild = node.getRightChild();

		String leftNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(leftChild));
		String rightNodeToken = CodeConstructor.replaceTokenUnvalidChars(CodeConstructor.getToken(rightChild));

		return createDirective(node, leftNodeToken, rightNodeToken);
	}

	private static String createDirective(SyntaxNode node, final String leftNodeToken, final String rightNodeToken) {
		if (node.isPropagated() && functionsLabelsStack.isEmpty()) return "";

		// Determine types
		String varType = node.getType();
		String rightNodeType = node.getRightChild().getType();

		// Handle leaf nodes type determination
		if (node.getRightChild().isLeaf() && !rightNodeToken.contains("aux") && !node.getType().equals(UsesType.FLOAT) && !rightNodeToken.contains("global")) {
			rightNodeType = determineTypeFromToken(rightNodeToken);
		}
		if (node.getLeftChild().isLeaf() && !leftNodeToken.contains("aux") && !node.getName().contains(":")) {
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

	public static String generateDirective(String leftType, String rightType,
											String leftToken, String rightToken) {
		StringBuilder directive = new StringBuilder();

		switch (leftType) {
			case UsesType.USHORT -> {
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
				CodeConstructor.generateFloatLoadSentences(rightToken, rightType, directive);
				directive.append("\tFSTP ").append(leftToken).append("\n");
			}
		}

		return directive.toString();
	}

}
