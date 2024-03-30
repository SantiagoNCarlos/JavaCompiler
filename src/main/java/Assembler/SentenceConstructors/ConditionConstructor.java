package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Optional;

public class ConditionConstructor implements CodeConstructor {

    private static String assembler_command;
    public static String generateStructureCode(SyntaxNode node) {
        setCondition(node.getName());

        Optional<Attribute> leftNode = node.getLeftChild().getNodeValue();
        Optional<Attribute> rightNode = node.getRightChild().getNodeValue();

        if (leftNode.isPresent() && rightNode.isPresent()) {

            final String leftNodeToken = leftNode.get().getToken();
            final String rightNodeToken = rightNode.get().getToken();

            String returnCode = null;

            final String labelName = " label" + CodeGenerator.codeLabelsCounter;

            CodeGenerator.labelCountStack.push(CodeGenerator.codeLabelsCounter);
            CodeGenerator.codeLabelsCounter++;

            switch (node.getType()) {
                case UsesType.USHORT -> {
                    returnCode = "    MOV AL, " + leftNodeToken + "\n" +
                                "    MOV BL, " + rightNodeToken + "\n" +
                                "    CMP AL, BL\n" +
                                "    " + assembler_command + labelName; // Use sentence to determine if jump is needed
                }
                case UsesType.LONG -> {
                    returnCode = "    MOV EBX, " + leftNodeToken + "\n" +
                                "    MOV ECX, " + rightNodeToken + "\n" +
                                "    CMP EBX, ECX\n" + // Compare...
                                "    " + assembler_command + labelName; // Use sentence to determine if jump is needed
                }
                case UsesType.FLOAT -> {
                    returnCode = "    FLD " + leftNodeToken.replace(".", "_") + "\n" + // Load left node to FPU stack
                                "    FLD " + rightNodeToken.replace(".", "_") + "\n" + // Load right node to FPU stack
                                "    FSTSW aux_mem_2bytes\n" + // Compare...
                                "    MOV AX, aux_mem_2bytes\n" +
                                "    SAHF\n" + // Set the state bits in flags to enable comparison next
                                "    " + assembler_command + labelName;
                }
            };

            node.setLeftChild(null);
            node.setRightChild(null);
            node.setLeaf(true);

            return returnCode;
        }
        return "";
    }

    private static void setCondition(String condition) {
        switch (condition) {
            case "==" -> assembler_command = "JE";
            case "<=" -> assembler_command = "JBE";
            case ">=" -> assembler_command = "JAE";
            case "<" -> assembler_command = "JB";
            case ">" -> assembler_command = "JA";
            case "!!" -> assembler_command = "JNE";
        }
    }

}
