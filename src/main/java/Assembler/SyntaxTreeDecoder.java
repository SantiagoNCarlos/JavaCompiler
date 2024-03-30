package Assembler;

import ArbolSintactico.SyntaxNode;

// Reads the whole syntax tree and decodes it.
public class SyntaxTreeDecoder {

    private static boolean error = false;
    public static final CodeWriter assemblerWriter = CodeWriter.getInstance();

//    public static void exploreTree(SyntaxNode fatherNode) { // capaz habria que hacer algo mas aca...
//        processNode(fatherNode);
//    }

    public static void processNode(SyntaxNode fatherNode) {
        if (fatherNode != null && !fatherNode.isLeaf()) {
            if (isLastParentNode(fatherNode)) {
                generateAndPrintCode(fatherNode);
            } else {
                processNode(fatherNode.getRightChild());
                processNode(fatherNode.getLeftChild());

//                if (fatherNode.getLeftChild() != null && !fatherNode.getLeftChild().isLeaf())
//                    processNode(fatherNode.getLeftChild());
//
//                if (fatherNode.getRightChild() != null && !fatherNode.getRightChild().isLeaf())
//                    processNode(fatherNode.getRightChild());
            }
        }
    }

    private static boolean isLastParentNode(SyntaxNode node) { // Is last node before leaf nodes?
        return (node.getLeftChild() == null || node.getLeftChild().isLeaf())
                && (node.getRightChild() == null || node.getRightChild().isLeaf());
    }

    private static void generateAndPrintCode(SyntaxNode node) {
        String generatedCode = CodeGenerator.generateNodeCode(node);
        if (generatedCode == null || generatedCode.equals("error")) {
            error = true;
        }

        if (!error && !generatedCode.isBlank()) {
            assemblerWriter.addSentence(generatedCode);
        }
    }


}
