package Assembler.SentenceConstructors;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.Enums.UsesType;
import AnalizadorLexico.SymbolTable;
import AnalizadorSintactico.Parser;
import ArbolSintactico.SyntaxNode;
import org.apache.commons.lang3.tuple.Pair;


import java.util.*;
import java.util.stream.Collectors;

import static AnalizadorSintactico.Parser.classFullNames;

public class MethodCallerConstructor implements CodeConstructor {

    public static String generateStructureCode(SyntaxNode node) {
        // Push parameters if needed
        SyntaxNode leftChild = node.getLeftChild();
        SyntaxNode rightChild = node.getRightChild();

        if (leftChild == null || rightChild == null)
            return "";

        String returnCode = "";
        String funcName = "";
        String restoreMemberState = ""; // We need to update the values of the object members used in a method! (Parameters are passed by value).

        SyntaxNode parameterNode = rightChild.getLeftChild();

        if (parameterNode != null) { // Function receives parameters!
            if (parameterNode.getName().equalsIgnoreCase("parametro")) {
                funcName = getFullFuncName(rightChild.getName(), leftChild);

                returnCode += "\tMOV EAX, OFFSET FUNCTION_" + CodeConstructor.replaceTokenUnvalidChars(funcName) + "\n\tCMP EAX, _current_function_\n\tJE _RecursionError_\n\tMOV _current_function_, EAX\n";

                Pair<String, String> membersPair = loadCorrectMembers(node.getLeftChild(), node.getRightChild());
                returnCode += membersPair.getLeft();
                restoreMemberState = membersPair.getRight();

                final String parameterType = parameterNode.getLeftChild().getType();
                final String parameterName = getParameterName(parameterNode.getLeftChild());

                Optional<Attribute> funcAtt = SymbolTable.getInstance().getAttribute(funcName);

                String auxVariableName = "";

                if (funcAtt.isPresent()) {
                    Attribute paramAtt = funcAtt.get().getParameter();
                    if (paramAtt != null) {
                        auxVariableName += CodeConstructor.replaceTokenUnvalidChars(paramAtt.getToken());
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
            returnCode = "\tMOV EAX, OFFSET FUNCTION_" + CodeConstructor.replaceTokenUnvalidChars(funcName) + "\n\tCMP EAX, _current_function_\n\tJE _RecursionError_\n\tMOV _current_function_, EAX\n";
            Pair<String, String> membersPair = loadCorrectMembers(node.getLeftChild(), node.getRightChild());
            returnCode += "\n" + membersPair.getLeft();
            restoreMemberState = membersPair.getRight();
        }

        returnCode += "\n\tCALL FUNCTION_" + CodeConstructor.replaceTokenUnvalidChars(funcName) + "\n";

        returnCode += "\n" + restoreMemberState;

        return returnCode;
    }

    private static String getParameterName(SyntaxNode parameterNode) {
        if (parameterNode.getName().equalsIgnoreCase("Acceso")) {
            return AccessConstructor.generateStructureCode(parameterNode);
        }
        if (!parameterNode.isLeaf()) {
            return CodeConstructor.replaceTokenUnvalidChars(parameterNode.getLeftChild().getName());
        }
        Optional<Attribute> attr = SymbolTable.getInstance().getAttribute(parameterNode.getName());
        if (attr.isPresent() && attr.get().getUso().equals(UsesType.CONSTANT)) {
            return "c_" + CodeConstructor.replaceTokenUnvalidChars(parameterNode.getName());
        }
        return CodeConstructor.replaceTokenUnvalidChars(parameterNode.getName());
    }

    private static String getFullFuncName(final String funcName, SyntaxNode objectNode) {
        // Obtener todas las entradas de atributos de la tabla de símbolos
        List<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());

        // Obtener el atributo del objeto y su tipo
        Optional<Attribute> objectAttr = SymbolTable.getInstance().getAttribute(objectNode.getName());
        final String objectType = objectAttr.map(Attribute::getType).orElse("");

        // Obtener las clases padre del tipo del objeto y agregar el tipo del objeto
        List<String> parentClasses = Parser.compositionMap.get(classFullNames.get(objectType));
        if (parentClasses == null) {
            parentClasses = new ArrayList<>();
        }
        parentClasses.add(objectType);

        // Crear un Set con solo el nombre de la clase
        Set<String> parentClassPrefixes = parentClasses.stream()
                .map(parentClass -> parentClass.split(":")[0])
                .collect(Collectors.toSet());

        // Verificar si existe un token que cumpla con las condiciones
        if (objectAttr.isPresent()) {
            for (Attribute entry : entries) {
                final String token = entry.getToken();

                // Verificar si el token contiene alguna de las partes relevantes de parentClasses
                boolean containsClass = parentClassPrefixes.stream()
                        .anyMatch(token::contains);

                // Comprobar si el token empieza con funcName y contiene una clase padre
                if (token.startsWith(funcName + ":") && containsClass) {
                    return token;
                }
            }
        }

        return "";
    }



    private static Pair<String, String> loadCorrectMembers(SyntaxNode objectNode, SyntaxNode funcNode) {
        // Loads to the function used variables the values from the caller object
        Optional<Attribute> objectAttr = SymbolTable.getInstance().getAttribute(objectNode.getName());
        StringBuilder returnCode = new StringBuilder();
        StringBuilder restoreMembersCode = new StringBuilder();

        if (objectAttr.isPresent()) {
            HashMap<String, String> variablesUsed = getUsedVariables(objectAttr.get().getType(), funcNode.getName());
            for (Map.Entry<String, String> functionVariable : variablesUsed.entrySet()) {
                final String objectMember = functionVariable.getKey() + "_" + CodeConstructor.replaceTokenUnvalidChars(objectAttr.get().getToken());
                switch (functionVariable.getValue()) {
                    case UsesType.USHORT -> {
                        returnCode.append("\tMOV AL, ").append(objectMember).append("\n").append( // Load the 8-bit value into AL
                                "\tMOV ").append(functionVariable.getKey()).append(",AL\n");//  // Zero-extend AX to EAX (32-bit register) to maintain alignment
                        // Update values of the affected members!
                        restoreMembersCode.append("\tMOV AL, ").append(functionVariable.getKey()).append("\n").append(
                                "\tMOV ").append(objectMember).append(",AL\n");
                        }
                    case UsesType.LONG -> {
                        returnCode.append("\tMOV EAX, ").append(objectMember).append("\n").append("\tMOV ").append(functionVariable.getKey()).append(",EAX"); // Store the 32 bit LONG mul in aux variable.
                        // Update values of the affected members!
                        restoreMembersCode.append("\tMOV EAX, ").append(functionVariable.getKey()).append("\n").append("\tMOV ").append(objectMember).append(",EAX\n");
                        }
                    case UsesType.FLOAT -> {
                        returnCode.append("\tFLD ").append(objectMember).append("\n").append( // Load left node to FPU stack
                                "\tFSTP ").append(functionVariable.getKey()).append("\n"); // Store the 32 bit FP mul in auxiliar variable. Also pop the stack
                        // Update values of the affected members!
                        restoreMembersCode.append("\tFLD ").append(functionVariable.getKey()).append("\n").append( // Load right node to FPU stack
                                "\tFSTP ").append(objectMember).append("\n"); // Store the 32 bit FP mul in auxiliar variable. Also pop the stack                    }
                    }
                }
            }
        }

        return Pair.of(returnCode.toString(), restoreMembersCode.toString());
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
                    && (scopes.contains(methodName) || entry.isUsadaDerecho()))
            {
                members.put(CodeConstructor.replaceTokenUnvalidChars(token), entry.getType());
            }
        }
        return members;
    }
}
