package Assembler;

import ArbolSintactico.SyntaxNode;
import Assembler.SentenceConstructors.*;

import java.util.Stack;

// Esta clase se encarga de determinar que codigo assembler debe generar para un determinado nodo.
public class CodeGenerator {
    public static int auxVariableCounter = 1;
    public static int codeLabelsCounter = 1;
    public static Stack<Integer> labelCountStack = new Stack<Integer>();
//    public static int currectCodeLabelNum = 1;

    public static String generateNodeCode(SyntaxNode node) {
        return switch (node.getName()) {
            case "++", "+" -> AddConstructor.generateStructureCode(node);
            case "=" -> AssignConstructor.generateStructureCode(node);
            case "-" -> SubConstructor.generateStructureCode(node);
            case "*" -> MulConstructor.generateStructureCode(node);
            case "/" -> DivConstructor.generateStructureCode(node);
            case "==" , "<=" , ">=" , "<" , ">" , "!!" -> ConditionConstructor.generateStructureCode(node);
            case "THEN", "ELSE" -> IfConstructor.generateStructureCode(node);
            case "while" -> WhileConstructor.generateStructureCode(node);
//            case "acceso" -> new ;
//            case "llamadoFuncion" -> new ;
            case "Print" -> PrintConstructor.generateStructureCode(node);
            default -> node.getName();
        };
    }
}
