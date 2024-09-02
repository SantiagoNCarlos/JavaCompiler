package Assembler;

import ArbolSintactico.SyntaxNode;

import java.util.Objects;

// Reads the whole syntax tree and decodes it.
public class SyntaxTreeDecoder {

    private static boolean error = false;
    public static final CodeWriter assemblerWriter = CodeWriter.getInstance();

    public static void processNode(SyntaxNode fatherNode) {
        if (fatherNode != null && !fatherNode.isLeaf()) {
            final String fatherNodeName = fatherNode.getName();

            addLabel(fatherNodeName);

            if (isLastParentNode(fatherNode)) {
                generateAndPrintCode(fatherNode);
            } else {

                final String[] parts = fatherNodeName.split("=", 2); // Limitamos a 2 partes

                switch (parts[0].toLowerCase()) {
                    case "if" -> CodeGenerator.enviromentCountStack.push(false);
                    case "camino" -> {
                        CodeGenerator.enviromentCountStack.pop();
                        CodeGenerator.enviromentCountStack.push(true);
                    }
                    case "declaracionfuncion" -> {
                        final String funcName = parts.length > 1 ? parts[1] : "";
                        CodeGenerator.functionsLabelsStack.push("FUNCTION_" + funcName.replace(':', '_'));

                        assemblerWriter.addSentence(CodeGenerator.functionsLabelsStack.peek() + " PROC");
                    }
                }

                processNode(fatherNode.getLeftChild());
                processNode(fatherNode.getRightChild());
                generateAndPrintCode(fatherNode);

                if (parts[0].equalsIgnoreCase("root")) {
                    assemblerWriter.addSentence("\tJMP _end_\n");
                }
            }
        }
    }

    private static void addLabel(String nodeName) {
        if (Objects.equals(nodeName.toLowerCase(), "while")) {
            final String labelName = "\tlabel" + CodeGenerator.codeLabelsCounter + ":";
            assemblerWriter.addSentence(labelName);

            CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
            CodeGenerator.codeLabelsCounter++;
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
