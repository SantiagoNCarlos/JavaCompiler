package Assembler.SentenceConstructors;

import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class WhileConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
        final String labelName = " label" + CodeGenerator.codeLabelsCounter;

        String returnCode = "JMP " + labelName + "\n" + "label" + CodeGenerator.labelCountStack.peek() + ":\n";

        CodeGenerator.labelCountStack.pop();
        CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
        CodeGenerator.codeLabelsCounter++;

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
    }

}
