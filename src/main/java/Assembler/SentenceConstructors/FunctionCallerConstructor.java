package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;
import Assembler.CodeGenerator;

import java.util.Optional;

public class FunctionCallerConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {

        // Push parameters if needed
        SyntaxNode leftChild = node.getLeftChild();
        SyntaxNode rightChild = node.getRightChild();

        String returnCode = "";
        String funcName = "";

        if (rightChild != null) { // Function receives parameters!
            if (rightChild.getName().equalsIgnoreCase("parametro")) {

                if (leftChild != null) {
                    funcName = leftChild.getName();

                    if (rightChild.getLeftChild() != null) {
                        final String parameterType = rightChild.getLeftChild().getType();
                        final String parameterName = getParameterName(rightChild.getLeftChild());

                        Optional<Attribute> funcAtt = SymbolTable.getInstance().getAttribute(funcName);

                        String auxVariableName = "";

                        if (funcAtt.isPresent()) {
                            Attribute paramAtt = funcAtt.get().getParameter();
                            if (paramAtt != null) {
                                auxVariableName += paramAtt.getToken().replace(":", "_");
                            }
                        }

                        switch (parameterType) {
                            case UsesType.USHORT -> {
                                returnCode = "\tMOV AL, " + parameterName + "\n" + // Load the 8-bit value into AL
                                             "\tMOV " + auxVariableName + ",AL\n";//  // Zero-extend AX to EAX (32-bit register) to maintain alignment
                            }
                            case UsesType.LONG -> {
                                returnCode = "\tMOV EAX, " + parameterName + "\n" +
                                             "\tMOV " + auxVariableName + ",EAX"; // Store the 32 bit LONG mul in aux variable.
                            }
                            case UsesType.FLOAT -> {
                                returnCode = "\tFLD " + parameterName + "\n" + // Load left node to FPU stack
                                            "\tFSTP " + auxVariableName + "\n"; // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
                            }
                         }
                    }
                }
            } else { // Function does not receive any parameters!
                funcName = rightChild.getName();
            }
        }

        returnCode += "\n\tCALL FUNCTION_" + funcName.replace(":", "_") + "\n";

        return returnCode;
    }

    private static String getParameterName(SyntaxNode parameterNode) {
        if (parameterNode.getName().equalsIgnoreCase("Acceso")) {
            return AccessConstructor.generateStructureCode(parameterNode);
        }
        if (!parameterNode.isLeaf()) {
            return parameterNode.getLeftChild().getName().replace(":", "_");
        }
        Optional<Attribute> attr = SymbolTable.getInstance().getAttribute(parameterNode.getName());
        if (attr.isPresent() && attr.get().getUso().equals(UsesType.CONSTANT)) {
            return "c_" + parameterNode.getName().replace(":", "_").replace(".","_");
        }
        return parameterNode.getName().replace(":", "_");
    }
}