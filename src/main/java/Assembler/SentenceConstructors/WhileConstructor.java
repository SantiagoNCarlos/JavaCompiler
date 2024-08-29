package Assembler.SentenceConstructors;

import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

public class WhileConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {

        final String labelName = "\tlabel" + CodeGenerator.labelCountStack.pop();
        final String labelName2 = " label" + CodeGenerator.labelCountStack.pop();

        String returnCode = "\tJMP" + labelName2 + "\n" + labelName + ":\n";

        node.setLeftChild(null);
        node.setRightChild(null);
        node.setLeaf(true);

        return returnCode;
    }

}
