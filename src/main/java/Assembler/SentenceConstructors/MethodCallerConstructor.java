package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import ArbolSintactico.SyntaxNode;

import java.util.*;

public class MethodCallerConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
        // Push parameters if needed
        SyntaxNode leftChild = node.getLeftChild();
        SyntaxNode rightChild = node.getRightChild();

        if (leftChild == null || leftChild == null)
            return "";

        String returnCode = "";
        String funcName = "";

        SyntaxNode parameterNode = rightChild.getLeftChild();

        if (parameterNode != null) { // Function receives parameters!
            if (parameterNode.getName().equalsIgnoreCase("parametro")) {
                funcName = getFullFuncName(rightChild.getName(), leftChild);

                returnCode = loadCorrectMembers(node.getLeftChild(), node.getRightChild());

                final String parameterType = parameterNode.getLeftChild().getType();
                final String parameterName = getParameterName(parameterNode.getLeftChild());

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
                        returnCode += "\tMOV AL, " + parameterName + "\n" + // Load the 8-bit value into AL
                                     "\tMOV " + auxVariableName + ",AL\n";//  // Zero-extend AX to EAX (32-bit register) to maintain alignment
                    }
                    case UsesType.LONG -> {
                        returnCode += "\tMOV EAX, " + parameterName + "\n" +
                                     "\tMOV " + auxVariableName + ",EAX"; // Store the 32 bit LONG mul in aux variable.
                    }
                    case UsesType.FLOAT -> {
                        returnCode += "\tFLD " + parameterName + "\n" + // Load left node to FPU stack
                                    "\tFSTP " + auxVariableName + "\n"; // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
                    }
                 }
            }
        } else { // Function does not receive any parameters!
            funcName = getFullFuncName(rightChild.getName(), leftChild);
            returnCode = loadCorrectMembers(node.getLeftChild(), node.getRightChild());
        }

        returnCode += "\n\tCALL FUNCTION_" + funcName.replace(":", "_") + "\n";

        return returnCode;
    }

//    private static String getParameterName(SyntaxNode parameterNode) {
//        if (parameterNode.getName().equalsIgnoreCase("Acceso")) {
//            return AccessConstructor.generateStructureCode(parameterNode);
//        }
//        return parameterNode.getLeftChild().getName().replace(":", "_");
//    }

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

    private static String getFullFuncName(final String funcName, SyntaxNode objectNode) {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());
        Optional<Attribute> objectAttr = SymbolTable.getInstance().getAttribute(objectNode.getName());

        if (objectAttr.isPresent()) {
            for (Attribute entry : entries) {
                final String token = entry.getToken();
                if (token.startsWith(funcName + ":") && token.contains(objectAttr.get().getType())) {
                    return token;
                }
            }
        }
        return "";
    }

    private static String loadCorrectMembers(SyntaxNode objectNode, SyntaxNode funcNode) {
        // Loads to the function used variables the values from the caller object
        Optional<Attribute> objectAttr = SymbolTable.getInstance().getAttribute(objectNode.getName());
        StringBuilder returnCode = new StringBuilder();

        if (objectAttr.isPresent()) {
            HashMap<String, String> variablesUsed = getUsedVariables(objectAttr.get().getType(), funcNode.getName());
            for (Map.Entry<String, String> functionVariable : variablesUsed.entrySet()) {
                final String objectMember = functionVariable.getKey() + "_" + objectAttr.get().getToken().replace(":", "_");
                switch (functionVariable.getValue()) {
                    case UsesType.USHORT ->
                        returnCode.append("\tMOV AL, ").append(objectMember).append("\n").append( // Load the 8-bit value into AL
                                "\tMOV ").append(functionVariable.getKey()).append(",AL\n");//  // Zero-extend AX to EAX (32-bit register) to maintain alignment
                    case UsesType.LONG ->
                        returnCode.append("\tMOV EAX, ").append(objectMember).append("\n").append("\tMOV ").append(functionVariable.getKey()).append(",EAX"); // Store the 32 bit LONG mul in aux variable.
                    case UsesType.FLOAT ->
                        returnCode.append("\tFLD ").append(objectMember).append("\n").append( // Load left node to FPU stack
                                "\tFSTP ").append(functionVariable.getKey()).append("\n"); // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
                }
            }
        }

        return returnCode.toString();
    }


    private static HashMap<String, String> getUsedVariables(String className, String methodName) {
        ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());
        HashMap<String, String> members = new HashMap<>();

        for (Attribute entry : entries) {
            final String token = entry.getToken();

            if (token.contains(className)
                    && token.startsWith(methodName + ":")
                    && entry.getUso().equals(UsesType.FUNCTION))
            {
                // Get method name and rearrange it
                String[] parts = token.split(":");

                if (parts.length > 1) {
                    StringBuilder rearrangedToken = new StringBuilder();

                    // Add all parts except the first (method name)
                    for (int i = 1; i < parts.length; i++) {
                        rearrangedToken.append(parts[i]);
                        if (i < parts.length - 1) {
                            rearrangedToken.append(":");
                        }
                    }

                    // Add the method name at the end
                    rearrangedToken.append(":").append(parts[0]);

                    // Add the full name of the method to the variable
                    methodName = rearrangedToken.toString();
                }
                break;
            }
        }

        // Get all members from a class
        for (Attribute entry : entries) {
            final String token = entry.getToken();
            Set<String> scopes = entry.getAmbitosUsoIzquierdo();
            if (token.contains(className)
                    && entry.getUso().equals(UsesType.VARIABLE)
                    && scopes != null
                    && scopes.contains(methodName))
            {
                members.put(token.replace(":", "_"), entry.getType());
            }
        }
        return members;
    }
}
