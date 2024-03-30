package Assembler.SentenceConstructors;

import java.util.Optional;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

public class AssignConstructor implements CodeConstructor{
	public static String generateStructureCode(SyntaxNode node) {
		Optional<Attribute> leftNode = node.getLeftChild().getNodeValue();
		Optional<Attribute> rightNode = node.getRightChild().getNodeValue();

		if (leftNode.isPresent() && rightNode.isPresent()) {

			String leftNodeToken = leftNode.get().getToken();
			String rightNodeToken = rightNode.get().getToken();

			if (leftNode.get().getUso().equals("CTE"))
				leftNodeToken = "c_".concat(leftNodeToken);
			if (rightNode.get().getUso().equals("CTE"))
				rightNodeToken = "c_".concat(rightNodeToken);

			node.setLeftChild(null);
			node.setRightChild(null);
			node.setLeaf(true);

			return switch (node.getType()) {
				case UsesType.USHORT -> // 8 bits
						"    MOV AL, " + rightNodeToken.replace(":", "_") + "\n" +
						"    MOV " + leftNodeToken.replace(":", "_") + ",AL\n";
				case UsesType.LONG -> // 32 bits
						"    MOV EAX, " + rightNodeToken.replace(":", "_") + "\n" +
						"    MOV " + leftNodeToken.replace(":", "_") + ",EAX\n";
				case UsesType.FLOAT -> // 32 bits FP
						"    FLD " + rightNodeToken.replace(".", "_").replace(":", "_") + "\n" +
						"    FSTP " + leftNodeToken.replace(".", "_").replace(":", "_") + "\n";
				default -> "";
			};

		}
		return "";
	}
}
