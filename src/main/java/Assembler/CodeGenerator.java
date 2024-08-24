package Assembler;

import ArbolSintactico.SyntaxNode;
import Assembler.SentenceConstructors.*;

import java.util.Stack;

// Esta clase se encarga de determinar que codigo assembler debe generar para un determinado nodo.
public class CodeGenerator {
    public static int auxVariableCounter = 1;
    public static int codeLabelsCounter = 1;
    public static Stack<Integer> labelCountStack = new Stack<>();
    public static Stack<Boolean> enviromentCountStack = new Stack<>();
    public static Stack<String> functionsLabelsStack = new Stack<>();

    public static String generateNodeCode(SyntaxNode node) {

        final String nodeName = node.getName();

        return switch (nodeName.toLowerCase()) {
            case "++", "+" -> AddConstructor.generateStructureCode(node);
            case "=" -> AssignConstructor.generateStructureCode(node);
            case "-" -> SubConstructor.generateStructureCode(node);
            case "*" -> MulConstructor.generateStructureCode(node);
            case "/" -> DivConstructor.generateStructureCode(node);
            case "==" , "<=" , ">=" , "<" , ">" , "!!" -> ConditionConstructor.generateStructureCode(node);
            case "then", "else" -> IfConstructor.generateStructureCode(node);
            case "while" -> WhileConstructor.generateStructureCode(node);
            case "llamadofuncion" -> FunctionCallerConstructor.generateStructureCode(node);
            case "llamadometodoclase" -> MethodCallerConstructor.generateStructureCode(node);
            case "print" -> PrintConstructor.generateStructureCode(node);
            default -> nodeName.contains("DeclaracionFuncion") ? "\tMOV _current_function_, 0\n\tRET \n\t" + functionsLabelsStack.pop() + " ENDP\n" : "";

        };
    }
}
