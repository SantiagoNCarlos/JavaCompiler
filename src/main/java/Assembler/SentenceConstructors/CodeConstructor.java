package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
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
}
