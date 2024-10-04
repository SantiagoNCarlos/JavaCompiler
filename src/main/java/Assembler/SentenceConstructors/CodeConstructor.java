package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;

import java.util.Optional;

public interface CodeConstructor {
    static String generateStructureCode(SyntaxNode node) {
        return null;
    }

    static String getToken(SyntaxNode child) {
        String returnedToken = "";
		if (child.getName().equalsIgnoreCase("Acceso")) {
			returnedToken = AccessConstructor.generateStructureCode(child);
		} else {
			Optional<Attribute> childNodeValue = child.getNodeValue();
			if (childNodeValue.isPresent()) {
				String token = childNodeValue.get().getToken();
				returnedToken = childNodeValue.get().getUso().equals("CTE") ? "c_".concat(token) : token;
			}
		}
		return returnedToken.replace(":", "_");
	}

	static String replaceTokenUnvalidChars(String token) { // Removes all unvalid characters for assembler
		return token.replace(":", "_").replace("+","_")
				.replace("-","_").replace("#","")
				.replace(" ", "_").replace(".", "_");
	}

	static void generateFloatLoadSentences(String rightNodeToken, String rightType, StringBuilder code) {
		if (rightType.equals(UsesType.USHORT)) {
			generateFloatUshortLoad(rightNodeToken, code);
		} else if (rightType.equals(UsesType.LONG)) {
			generateFloatLongLoad(rightNodeToken, code);
		} else {
			code.append("\tFLD ").append(CodeConstructor.replaceTokenUnvalidChars(rightNodeToken)).append("\n");
		}
	}

	static void generateFloatUshortLoad(String rightNodeToken, StringBuilder code) {
		code.append("\tMOVZX EAX, BYTE PTR ").append(rightNodeToken).append("\n")
				.append("\tMOV DWORD PTR [ESP-4], EAX\n")
				.append("\tFILD DWORD PTR [ESP-4]\n");
	}

	static void generateFloatLongLoad(String rightNodeToken, StringBuilder code) {
		code.append("\tFILD DWORD PTR ").append(rightNodeToken).append("\n");
	}

	static String verifyActualType(SyntaxNode node, String nodeToken, String type) {
		if (node.isLeaf()) {
			if (nodeToken.contains("_us")) {
				return UsesType.USHORT;
			} else if (nodeToken.contains("_l")) {
				return UsesType.LONG;
			}
		}
		return type;
	}
}
