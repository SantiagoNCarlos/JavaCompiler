package Assembler.SentenceConstructors;

import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class IfConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
        String returnCode = null;

        switch (node.getName()) {
            case "THEN":
                final String labelName = "\tlabel" + CodeGenerator.labelCountStack.pop();

                if (CodeGenerator.enviromentCountStack.pop()) { // Cases where we have an "ELSE" sentence

                    CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
                    CodeGenerator.codeLabelsCounter++;

                    final String elseLabelName = "label" + CodeGenerator.labelCountStack.peek();
                    returnCode = "\tJMP " + elseLabelName + "\n" + labelName + ":\n";
                } else {
                    returnCode = labelName + ":\n";
                }

                break;
            case "ELSE":
                returnCode = "\tlabel" + CodeGenerator.labelCountStack.pop() + ":\n";
                break;
        }

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
    }
}
