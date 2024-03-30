package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Optional;

public class MulConstructor implements CodeConstructor {
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
            String returnCode = null;

            final String auxVariableName = "@aux" + CodeGenerator.auxVariableCounter;
            CodeGenerator.auxVariableCounter++;

            switch (node.getType()) {
                case UsesType.USHORT -> {
                    returnCode = "    MOV AL, " + leftNodeToken + "\n" +
                                "    MUL AL, " + rightNodeToken + "\n" +
                                "    MOV " + auxVariableName + ",AL"; // Store the 8 bit USHORT mul in aux variable.
                }
                case UsesType.LONG -> {
                    returnCode = "    MOV EAX, " + leftNodeToken + "\n" +
                                 "    MUL EAX, " + rightNodeToken + "\n" +
                                 "    MOV " + auxVariableName + ",EAX"; // Store the 32 bit LONG mul in aux variable.
                }
                case UsesType.FLOAT -> {
                    returnCode = "    FLD " + leftNodeToken.replace(".", "_") + "\n" + // Load left node to FPU stack
                                 "    FLD " + rightNodeToken.replace(".", "_") + "\n" + // Load right node to FPU stack
                                 "    FMUL \n" + // Multiply...
                                 "    FSTP " + auxVariableName + "\n"; // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
                }
            };

            SymbolTable.addSymbol(auxVariableName, TokenType.ID, node.getType(), UsesType.AUX_VAR);

            // Clean node...
            node.setName(auxVariableName);
            node.setLeftChild(null);
            node.setRightChild(null);
            node.setLeaf(true);

            return returnCode;
        }
        return null;
    }
}
