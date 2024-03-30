package Assembler.SentenceConstructors;

import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class IfConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
//        Optional<Attribute> leftNode = node.getLeftChild().getNodeValue();
//        Optional<Attribute> rightNode = node.getRightChild().getNodeValue();
        String returnCode = null;

        switch (node.getName()) {
            case "THEN": {
                final String labelName = " label" + CodeGenerator.codeLabelsCounter;

                returnCode = "JMP " + labelName + "\n" + "label" + CodeGenerator.labelCountStack.peek() + ":\n";

                CodeGenerator.labelCountStack.pop();
                CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
                CodeGenerator.codeLabelsCounter++;

            }
            case "ELSE": {
                returnCode = "label" + CodeGenerator.labelCountStack.peek() + ":\n";
                CodeGenerator.labelCountStack.pop();
            }
        };

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
    }
}
