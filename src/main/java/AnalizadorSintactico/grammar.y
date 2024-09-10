%{
package AnalizadorSintactico;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Map;
import static java.lang.Math.abs;
import AnalizadorLexico.*;
import Utilities.Logger;
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.SemanticActions.SemanticAction;
import AnalizadorLexico.Enums.DelimiterType;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
%}

%token ID CTE CADENA IF END_IF ELSE CLASS PRINT VOID LONG USHORT FLOAT WHILE DO MENORIGUAL MAYORIGUAL IGUAL NEGADO RETURN MASMAS CTE_FLOTANTE UNEXISTENT_RES_WORD CHECK
n
%left '+' '-'
%left '*' '/'
%start programa

%%

/************ PROGRAM - BLOCK - SENTENCES************/
programa:
            '{' bloque '}' { padre = new SyntaxNode("root", (SyntaxNode) $2.obj , null);
            					verificarReglasCheck();}
           	| '{' '}'
			| '{' bloque {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
			| bloque '}' {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un {.");}
	   		;


bloque:
            sentencia {
                $$ = $1;
            }
            | bloque sentencia {
                if ((SyntaxNode) $2.obj != null) {
                    $$ = new ParserVal(new SyntaxNode("Bloque de sentencias", (SyntaxNode) $1.obj, (SyntaxNode) $2.obj));

                }

                String leftNodeName = ((SyntaxNode) $1.obj ).getName();
                if (leftNodeName != "Bloque de sentencias") {
                    basicBlockCounter++;
                }
            }
      		| definicion_class {
      		    $$ = $1;
            }
      		| bloque definicion_class {
      		    if ((SyntaxNode) $2.obj != null)
                    $$ = new ParserVal(new SyntaxNode("Bloque de sentencias21", (SyntaxNode) $2.obj, (SyntaxNode) $1.obj));
            }
            | bloque_cambio_bloque_basico { $$ = $1; }
            | bloque bloque_cambio_bloque_basico {
                if ((SyntaxNode) $2.obj != null) {
                    $$ = new ParserVal(new SyntaxNode("Bloque de sentencias - Cambio de bloque", (SyntaxNode) $1.obj, (SyntaxNode) $2.obj));
                    basicBlockCounter++;
                }
            }
      		;

sentencia:
            ejecucion ',' { $$ = $1; }
	 		| declaracion ',' 
            | ejecucion error { //$$ = $1;
                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                }
            | declaracion error { logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','."); }
         	;

bloque_cambio_bloque_basico:
            bloque_if ',' { $$ = $1; }
            | bloque_while ',' { $$ = $1; }
            ;

ejecucion:
            asignacion { $$ = $1;}
            | impresion { $$ = $1;}
            | expresion { $$ = $1;}
            ;

expresion:
            llamada { $$ = $1;}
            | acceso { $$ = $1;}
            | incremento { $$ = $1;}
            ;

			
			
/************ DECLARATIONS ************/
declaracion:
            declaracion_var {
                              logger.logDebugSyntax("Declaración1 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
           	| declaracion_void { basicBlockCounter++; }
           	| declaracion_var_class
	   		;

tipo:
            FLOAT {$$ = $1;}
            | LONG {$$ = $1;}
    		| USHORT {$$ = $1;}
    		| UNEXISTENT_RES_WORD {$$ = $1;}
    		;


declaracion_var:
			CHECK tipo lista_de_variables
				{
			        var t = SymbolTable.getInstance();
			        String tipoVariable = $2.sval; 
			        for (String varName : lista_variables) {
			            String nombreCompleto = varName;
			            var entrada = t.getAttribute(nombreCompleto);
			            if (entrada.isPresent()) {
			                if (entrada.get().getType() == null) {
			                    entrada.get().setUso(UsesType.VARIABLE);
			                    entrada.get().setType(tipoVariable); // Asigna el tipo correcto
			                    entrada.get().setEsCheck(true);
			                } else {
			                    yyerror("La variable declarada ya existe " + (varName.contains(":") ? varName.substring(0, varName.indexOf(':')) : "en ambito global"));
			                }
			             }
		                 else {
			                // Agregar la variable con CHECK a la tabla de símbolos
			                t.insertAttribute(new Attribute(Parser.ID, nombreCompleto, tipoVariable, UsesType.VARIABLE, LexicalAnalyzer.getLine(), true));
			            }
			        }
			        lista_variables.clear();
		        }
		    | tipo lista_de_variables {
                    var t = SymbolTable.getInstance();
                    String tipoVariable = $1.sval;
                    for (String varName : lista_variables) {
                        String nombreCompleto = varName;
                        var entrada = t.getAttribute(nombreCompleto);
                        if (entrada.isPresent()) {
                            if (entrada.get().getType() == null) {
                                entrada.get().setUso(UsesType.VARIABLE);
                                entrada.get().setType(tipoVariable); // Asigna el tipo correcto
                            } else {
                                yyerror("La variable declarada ya existe " + (varName.contains(":") ? varName.substring(0, varName.indexOf(':')) : "en ambito global"));
                            }
                        } else {
                            t.insertAttribute(new Attribute(Parser.ID, nombreCompleto, tipoVariable, UsesType.VARIABLE, LexicalAnalyzer.getLine()));
                        }
                    }
                    lista_variables.clear();
		        
		    }
            | tipo ',' {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la variable.");
	       	}
	       	;

lista_de_variables:
            lista_de_variables ';' ID {
                    $$ = new ParserVal(new SyntaxNode("ListaVariables", (SyntaxNode)$1.obj, new SyntaxNode($3.sval)));
                    lista_variables.add($3.sval +":" +  Parser.ambito);
            }
		  	| ID {
                    $$ = new ParserVal(new SyntaxNode($1.sval));
                    lista_variables.add($1.sval +":" +  Parser.ambito);
            }
		  	;

/********* Asignaciones ********/
asignacion:
			ID '=' expresion_aritmetica {
                    logger.logDebugSyntax("Asignacion en la linea " + LexicalAnalyzer.getLine());

                    var symbolType = getTypeSymbolTableVariables($1.sval);

                    if (!symbolType.equals("Error")) {
                        String nombreCompleto = getNameSymbolTableVariables($1.sval);

                        if (!nombreCompleto.equals("Error")) {
                            SyntaxNode leftSyntaxNode = new SyntaxNode(nombreCompleto, symbolType);
                            SyntaxNode rightSyntaxNode = (SyntaxNode) $3.obj;

                            String tipo_validado = validarTipos(leftSyntaxNode, rightSyntaxNode, true);

                            if (!tipo_validado.equals("Error")) {

                                //var asign = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);
                                //asign.setType(tipo_validado);
                                //$$ = new ParserVal(asign);

                                var t = SymbolTable.getInstance();

                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent()) {
                                    Attribute entry = entrada.get();

                                    entry.addAmbito(ambito);

                                    var asign = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);
                                    asign.setType(tipo_validado);

                                    if (rightSyntaxNode != null) {
                                      Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();
                                      if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { /* If its a leaf node, then the value its a constant*/
                                          entry.setActive(true);
                                          entry.setValue(rightSyntaxNode.getName());
                                          entry.setConstantValueBlock(basicBlockCounter);

                                          //$$ = new ParserVal(null);
                                          $$ = new ParserVal(asign);

                                      } else {
                                          // Only create node if there's not a constant. (Delete previous declaration)
                                          $$ = new ParserVal(asign);

                                          entry.setActive(false);
                                          entry.setValue(null);
                                      }
                                    }
                                }
                            }
                        }
                    }
                    else {
	                    yyerror("La variable declarada '" + $1.sval+ "' no existe");
                    }
            }
			| ID '=' error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
			| acceso '=' expresion_aritmetica {
                    logger.logDebugSyntax("Asignación en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode accessNode = (SyntaxNode) $1.obj;
                    SyntaxNode rightSyntaxNode = (SyntaxNode) $3.obj;

                    String tipo_validado = validarTipos(accessNode, rightSyntaxNode, true);

                    if (!tipo_validado.equals("Error")) {
                        //var asign = new SyntaxNode("=", accessNode, rightSyntaxNode);
                        //asign.setType(tipo_validado);
                        //$$ = new ParserVal(asign);

                        Attribute memberAttr = getMemberVarAttribute(accessNode);

                        if (memberAttr != null && rightSyntaxNode != null) {
                            Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();

                            var asignNode = new SyntaxNode("=", accessNode, rightSyntaxNode);
                            asignNode.setType(tipo_validado);

                            if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { // If its a leaf node, then the value its a constant
                              memberAttr.setActive(true);
                              memberAttr.setValue(rightSyntaxNode.getName());
                              memberAttr.setConstantValueBlock(basicBlockCounter);

                              final String objectName = accessNode.getLeftChild().getName(); // Store propagated asignation constant!

                              propagatedConstantsDefinitionsMap.remove(objectName); // Remove previous constant node if exists
                              propagatedConstantsDefinitionsMap.put(objectName, asignNode);

                              //$$ = new ParserVal(null);
                              $$ = new ParserVal(asignNode);
                            } else {
                              // Only create node if there's not a constant. (Delete previous declaration)
                              $$ = new ParserVal(asignNode);

                              memberAttr.setActive(false);
                              memberAttr.setValue(null);
                            }
                        }
                    }
            }
			| acceso '=' error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
	  		;

incremento:
		    ID MASMAS {
                    logger.logDebugSyntax("Incremento en la linea " + LexicalAnalyzer.getLine());

                    var symbolType = getTypeSymbolTableVariables($1.sval);
                    String nombreCompleto = getNameSymbolTableVariables($1.sval);

                    if (!symbolType.equals("Error") && !nombreCompleto.equals("Error")) {
                        SyntaxNode variableNode = new SyntaxNode(nombreCompleto, symbolType);
			            // Determinar el valor de incremento basado en el tipo de la variable
			            String incrementValue;
			            switch (symbolType) {
			                case UsesType.LONG:
			                    incrementValue = "1_l";
			                    break;
			                case UsesType.FLOAT:
			                    incrementValue = "1.0";
			                    break;
			                case UsesType.USHORT:
			                    incrementValue = "1_us";
			                    break;
			                default:
			                    incrementValue = "1"; // Valor por defecto si no es ninguno de los anteriores
			                    break;
			            }
			
			            SyntaxNode incrementValueNode = new SyntaxNode(incrementValue, UsesType.CONSTANT); /* Nodo para el valor de incremento*/
			            SyntaxNode incrementNode = new SyntaxNode("++", variableNode, incrementValueNode, symbolType);

                        SyntaxNode asign = new SyntaxNode("=", variableNode, incrementNode, symbolType);

                        $$ = new ParserVal(asign);

                        var t = SymbolTable.getInstance();
                        t.insertAttribute(new Attribute (CTE, incrementValue, symbolType, UsesType.CONSTANT, LexicalAnalyzer.getLine()));
                        
                        var entrada = t.getAttribute(nombreCompleto);

                        if (entrada.isPresent()){
                            entrada.get().addAmbito(ambito);
                            entrada.get().setUsadaDerecho(true);
                        }
                    }
		    }
            ;


expresion_aritmetica:
            expresion_aritmetica '+' termino {
                    String tipo_validado = validarTipos((SyntaxNode) $1.obj, (SyntaxNode) $3.obj, false);

                    if (!tipo_validado.equals("Error")) {

                        $$ = new ParserVal(new SyntaxNode("+", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj, tipo_validado));

                        if ((SyntaxNode) $3.obj != null && ((SyntaxNode) $3.obj).isLeaf()){
                            String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)$3.obj).getName());

                            if (!nombreCompleto.equals("Error")) {
                                var t = SymbolTable.getInstance();
                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent())
                                    entrada.get().setUsadaDerecho(true);
                            }
                        }
                    }
			}
            | expresion_aritmetica '-' termino {
                        String tipo_validado = validarTipos((SyntaxNode) $1.obj, (SyntaxNode) $3.obj, false);

                        if (!tipo_validado.equals("Error")) {
                            $$ = new ParserVal(new SyntaxNode("-", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj, tipo_validado));

                            if ((SyntaxNode) $3.obj != null && ((SyntaxNode) $3.obj).isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)$3.obj).getName());

                                if (!nombreCompleto.equals("Error")) {
                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent())
                                        entrada.get().setUsadaDerecho(true);
                                }
                            }
                        }
			}
            | expresion_aritmetica '+' error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
            | expresion_aritmetica '-' error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
            | termino { $$ = $1; }
            ;

termino:
			termino '*' factor {
                        String tipo_validado = validarTipos((SyntaxNode) $1.obj, (SyntaxNode) $3.obj, false);

                        if (!tipo_validado.equals("Error")) {

                            SyntaxNode factorNode = (SyntaxNode) $3.obj;

                            if (factorNode != null) {
                                if (factorNode.isLeaf()){
                                    String nombreCompleto = getNameSymbolTableVariables(factorNode.getName());

                                    if (!nombreCompleto.equals("Error")) {
                                        var t = SymbolTable.getInstance();
                                        var entrada = t.getAttribute(nombreCompleto);
                                        if (entrada.isPresent()) {
                                            Attribute entry = entrada.get();
                                            entry.setUsadaDerecho(true);

                                            if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                                final String value = entry.getValue();

                                                var x = new SyntaxNode("*", (SyntaxNode) $1.obj, new SyntaxNode(value, entry.getType()));
                                                x.setType(tipo_validado);
                                                $$ = new ParserVal(x);
                                            } else {
                                                var x = new SyntaxNode("*", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj);
                                                x.setType(tipo_validado);
                                                $$ = new ParserVal(x);
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);
                                        if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = memberAttr.getValue();

                                            var x = new SyntaxNode("*", (SyntaxNode) $1.obj, new SyntaxNode(value, memberAttr.getType()));
                                            x.setType(tipo_validado);
                                            $$ = new ParserVal(x);
                                        } else {
                                            var x = new SyntaxNode("*", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj);
                                            x.setType(tipo_validado);
                                            $$ = new ParserVal(x);
                                        }
                                    }
                                }
                            }
                        }
            }
   			| termino '/' factor {
                        String tipo_validado = validarTipos((SyntaxNode) $1.obj, (SyntaxNode) $3.obj, false);

                        if (!tipo_validado.equals("Error")) {
                            SyntaxNode factorNode = (SyntaxNode) $3.obj;

                            if (factorNode != null) {
                                if (factorNode.isLeaf()){
                                    String nombreCompleto = getNameSymbolTableVariables(factorNode.getName());

                                    if (!nombreCompleto.equals("Error")) {
                                        var t = SymbolTable.getInstance();
                                        var entrada = t.getAttribute(nombreCompleto);
                                        if (entrada.isPresent()) {
                                            Attribute entry = entrada.get();
                                            entry.setUsadaDerecho(true);

                                            if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                                final String value = entry.getValue();

                                                var x = new SyntaxNode("/", (SyntaxNode) $1.obj, new SyntaxNode(value, entry.getType()));
                                                x.setType(tipo_validado);
                                                $$ = new ParserVal(x);
                                            } else {
                                                var x = new SyntaxNode("/", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj);
                                                x.setType(tipo_validado);
                                                $$ = new ParserVal(x);
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);
                                        if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = memberAttr.getValue();

                                            var x = new SyntaxNode("/", (SyntaxNode) $1.obj, new SyntaxNode(value, memberAttr.getType()));
                                            x.setType(tipo_validado);
                                            $$ = new ParserVal(x);
                                        } else {
                                            var x = new SyntaxNode("/", (SyntaxNode) $1.obj, (SyntaxNode)  $3.obj);
                                            x.setType(tipo_validado);
                                            $$ = new ParserVal(x);
                                        }
                                    }
                                }
                            }
                        }
            }
   			| termino '*' error {
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
   			}
   			| termino '/' error {
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
            }
   			| factor {
   			            // $$ = $1;
   			            SyntaxNode node = (SyntaxNode) $1.obj;

                        if (node != null) {
                            if (node.isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(node.getName());

                                if (!nombreCompleto.equals("Error")) {

                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent()) {
                                        Attribute entry = entrada.get();
                                        entry.setUsadaDerecho(true);

                                        if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = entry.getValue();

                                            $$ = new ParserVal(new SyntaxNode(value, entry.getType()));
                                        } else {
                                            $$ = $1;
                                        }
                                    }
                                }
                            } else {
                                if (node.getName().equalsIgnoreCase("acceso")) {
                                    Attribute memberAttr = getMemberVarAttribute(node);
                                    if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                        final String value = memberAttr.getValue();
                                        $$ = new ParserVal(new SyntaxNode(value, memberAttr.getType()));
                                    } else {
                                        $$ = $1;
                                    }
                                }
                            }
                        }
   			}
   			;

factor:
			acceso {$$ = $1;}
			| '-' acceso {$$ = $1;}
      		| '-' CTE  {
      		            String value = $2.sval;

      		            value = processInteger(value, false);

      		            if (!value.equals("Error")) {
      		                var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                String finalValue = "";
                                if (symbolType.equals(UsesType.USHORT)) {
                                    finalValue = value;
                                } else {
                                    finalValue = "-" + value;
                                    SymbolTable.addSymbol(finalValue, TokenType.CONSTANT, symbolType, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                                }
                                $$ = new ParserVal(new SyntaxNode(finalValue, symbolType)); // Crear un nodo para la constante
                            }
      		            }

      		}
      		| '-' CTE_FLOTANTE {
                        String value = $2.sval;

                        value = processFloat(value, false);

                        if (!value.equals("Error")) {
                            SymbolTable.addSymbol("-" + value, TokenType.CONSTANT, UsesType.FLOAT, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                            $$ = new ParserVal(new SyntaxNode("-" + value, UsesType.FLOAT)); // Crear un nodo para la constante
                        }
            }
      		| '-' ID {
                         var type = getTypeSymbolTableVariables($2.sval);
                         var name = getNameSymbolTableVariables($2.sval);

                         if (!type.equals("Error") && !name.equals("Error"))
                             $$ =  new ParserVal( new SyntaxNode(name, type));
                         else{
                           //  $$ =  new ParserVal( new SyntaxNode("parametroNoEncontrado", type));
                             yyerror("No se encontro esta variable en un ambito adecuado");}
            }
      		| CTE {
                        String value = $1.sval;

                        value = processInteger(value, true);

                        if (!value.equals("Error")) {
                            var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                $$ = new ParserVal(new SyntaxNode(value, symbolType)); // Crear un nodo para la constante
                            }
			            }
			}
      		| CTE_FLOTANTE {
      		            String value = $1.sval;

                        value = processFloat(value, true);

                        if (!value.equals("Error")) {
                            var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                $$ = new ParserVal(new SyntaxNode(value, symbolType)); // Crear un nodo para la constante
                            }
				        }
	        }
      		| incremento {
                        $$=$1;
            }
      		| ID {
                        var type = getTypeSymbolTableVariables($1.sval);
                        var name = getNameSymbolTableVariables($1.sval);

                        if (!type.equals("Error") && !name.equals("Error")) {
                            $$ =  new ParserVal(new SyntaxNode(name, type));
                        } else{
                          //  $$ =  new ParserVal( new SyntaxNode("parametroNoEncontrado", type));
                            yyerror("No se encontro esta variable en un ambito adecuado");}
            }
      		;

/*********** IF - WHILE ---> CONDITIONALS ***********/
operador:
			IGUAL { $$ = new ParserVal("=="); }
			| MENORIGUAL { $$ = new ParserVal("<="); }
			| MAYORIGUAL { $$ = new ParserVal(">="); }
			| '<' { $$ = new ParserVal("<"); }
			| '>' { $$ = new ParserVal(">"); }
			| NEGADO { $$ = new ParserVal("!!"); }
			| '=' {
			        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": no puede realizarse una asignacion. Quizas queria poner '=='?");
			        }
			;

condicion:
			expresion_aritmetica operador expresion_aritmetica {
                                    String tipo_validado = validarTipos((SyntaxNode) $1.obj, (SyntaxNode) $3.obj, false);
                                    if (!tipo_validado.equals("Error")) {
                                        $$ = new ParserVal(new SyntaxNode($2.sval, (SyntaxNode) $1.obj, (SyntaxNode) $3.obj, tipo_validado));
                                    }
            }
			| expresion_aritmetica operador error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
			| expresion_aritmetica {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
	 		;

condicion_parentesis:
            '(' condicion ')' { $$ = $2; }
            | error condicion {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '()'.");}
            | error condicion ')'{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '('.");}
            | '(' condicion {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un ')'."); $$ = $2; }
            | '(' ')' {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta establecer una condicion.");}
            ;

bloque_if:
			IF condicion_parentesis bloque_condicion END_IF {
			    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)$3.obj, null);
                $$ = new ParserVal(new SyntaxNode("IF", (SyntaxNode)$2.obj, thenSyntaxNode, null));
                logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
                                                                 }
		    | IF condicion_parentesis bloque_condicion ELSE bloque_condicion END_IF { // Usar "then" para distinguir los caminos... Then es el camino principal!
		        SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)$3.obj, null);
		        SyntaxNode elseSyntaxNode = new SyntaxNode("ELSE", (SyntaxNode)$5.obj, null);
		        SyntaxNode camino = new SyntaxNode("Camino", thenSyntaxNode, elseSyntaxNode);
		        SyntaxNode ifSyntaxNode = new SyntaxNode("IF", (SyntaxNode) $2.obj, camino);

		
		        // Crear un nodo 'if' con la condición y los nodos 'then' y 'else' como hijos
		        $$ = new ParserVal(ifSyntaxNode);
		        logger.logDebugSyntax("Bloque IF-ELSE en la linea " + LexicalAnalyzer.getLine());
		    }
            | IF condicion_parentesis bloque_condicion ELSE bloque_condicion error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
            | IF condicion_parentesis bloque_condicion error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
            | IF condicion_parentesis bloque_condicion bloque_condicion {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");}
            | IF bloque_condicion {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");}
	 		;

bloque_condicion:
            sentencia { $$ = $1; }
            | sentencia_return {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
            | '{' bloque_sentencias_condicion '}' { $$ = $2 ; }
            | '{' sentencia_return bloque_sentencias_condicion '}' {
                                                                    $$ = $3 ;
                                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");
                                                                    }
            | '{' bloque_sentencias_condicion error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
            ;

bloque_sentencias_condicion:
            sentencias_concatenacion sentencia_return {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
            | sentencias_concatenacion {$$ = $1;}
            ;

sentencias_concatenacion:
            sentencias_concatenacion sentencia { $$ = new ParserVal(new SyntaxNode("SecuenciaSentencias", (SyntaxNode)$1.obj, (SyntaxNode)$2.obj)); }
            | sentencia { $$ = $1; }
            ;

bloque_while:
            WHILE condicion_parentesis DO bloque_condicion  {
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    $$ = new ParserVal(new SyntaxNode("while", (SyntaxNode)$2.obj, (SyntaxNode)$4.obj));
            }
            | WHILE condicion_parentesis DO error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
            | WHILE condicion_parentesis bloque_condicion {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
            }
            ;

/************ FUNCTIONS ************/

declaracion_void:
            header_void '{' bloque_void '}' {
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    String nombreFuncion = $1.sval;
                    arbolFunciones.add(new SyntaxNode("DeclaracionFuncion=" + nombreFuncion, (SyntaxNode) $3.obj, null));
                    checkFlagAmbito();
            }
            | header_void '{' bloque_void error {
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '}'");
            }
            | header_void error bloque_void  '}' {
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '{'");
            }
            ;

header_void:
		    VOID ID '(' func_parameters ')' {
		        logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
		        var t = SymbolTable.getInstance();
		        var entradaFuncion = t.getAttribute($2.sval + ":" + Parser.ambito);
		        if (entradaFuncion.isPresent()) {
		            if (entradaFuncion.get().getUso() != null) {
		                yyerror("El nombre de la funcion " + $2.sval + " ya fue utilizado en este ambito");
		            } else {
		                entradaFuncion.get().setUso(UsesType.FUNCTION);
		            }
		        } else {
		            t.insertAttribute(new Attribute(Parser.ID, $2.sval + ":" + Parser.ambito, UsesType.FUNCTION, UsesType.FUNCTION, LexicalAnalyzer.getLine()));
		        }
		
		        // Agregar el parámetro a la tabla de símbolos
		        if ($4.obj != null) {
		            SyntaxNode parametroNode = (SyntaxNode) $4.obj;
		            String nombreParametro = parametroNode.getName();
		            String tipoParametro = parametroNode.getType();

		            Attribute attr = new Attribute(Parser.ID, nombreParametro + ":" + Parser.ambito + ":" + $2.sval, tipoParametro, UsesType.PARAMETER, LexicalAnalyzer.getLine());
		            t.insertAttribute(attr);

		            var entradaFuncionInsertada = t.getAttribute($2.sval + ":" + Parser.ambito);
		            entradaFuncionInsertada.get().setParameter(attr);
		        }

                String nombreFuncionSyntaxNode = $2.sval + ":" + Parser.ambito;
                $$.sval = nombreFuncionSyntaxNode;
		        nuevoAmbito($2.sval);
		    }
            | VOID ID '(' ')' {
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    var t = SymbolTable.getInstance();
                    var entradaFuncion = t.getAttribute($2.sval + ":" + Parser.ambito);
                    if (entradaFuncion.isPresent()) {
                        if (entradaFuncion.get().getUso() != null) {
                            yyerror("El nombre de la funcion " + $2.sval + " ya fue utilizado en este ambito");
                        } else {
                            entradaFuncion.get().setUso(UsesType.FUNCTION);
                        }
                    } else {
                        t.insertAttribute(new Attribute(Parser.ID, $2.sval + ":" +  Parser.ambito, UsesType.FUNCTION, UsesType.FUNCTION, LexicalAnalyzer.getLine()));
                    }
                    String nombreFuncionSyntaxNode = $2.sval + ":" + Parser.ambito;
                    $$.sval = nombreFuncionSyntaxNode;
                    nuevoAmbito($2.sval);
            }
            | VOID '(' ')' {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
            | VOID '(' func_parameters ')' {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
            | VOID ID {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
            | VOID ID func_parameters {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
            | VOID {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion.");
            }
            ;

func_parameters:
            parametro { $$ = $1; }
            | parametro ',' error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": Parametro mal declarado. Solo es posible declarar un único parámetro");}
            ;

parametro:
            tipo ID {
                    $$ = new ParserVal(new SyntaxNode($2.sval, $1.sval));
            }
            | ID {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el tipo del parametro.");
            }
            | tipo {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre del parametro.");
            }
            ;

bloque_void:
    sentencias_concatenacion_void sentencia_return {
        SyntaxNode sentenciasSyntaxNode = (SyntaxNode)$1.obj;
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        if (sentenciasSyntaxNode != null)
        	$$ = new ParserVal(new SyntaxNode("Bloque de sentencias1", sentenciasSyntaxNode, returnSyntaxNode));
    }
    | sentencia_return {
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        $$ = new ParserVal(new SyntaxNode("Bloque de sentencias2", null, returnSyntaxNode));
    }
    | sentencias_concatenacion_void {
        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una sentencia 'RETURN' en el bloque 'void'.");
        $$ = new ParserVal(new SyntaxNode("Bloque de sentencias3", (SyntaxNode)$1.obj, null));
    };

sentencias_concatenacion_void:
    sentencias_concatenacion_void sentencia_void {
        // Aquí, simplemente expandimos la secuencia de sentencias sin un nodo intermedio
        $$ = new ParserVal(new SyntaxNode("Sentencias", (SyntaxNode)$1.obj, (SyntaxNode)$2.obj));
    }
    | sentencia_void {
        $$ = $1;
    };


sentencias_void:
            asignacion { $$ = $1; }
            | bloque_if_void { $$ = $1; }
            | bloque_while_void { $$ = $1; }
            | impresion { $$ = $1; }
            | expresion { $$ = $1; }
	 		| declaracion { $$ = $1; }
         	;

sentencia_void:
            sentencias_void ','
            | sentencias_void error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
            ;

sentencia_return:
            RETURN ','
            | RETURN error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
            ;

bloque_while_void:
            WHILE condicion_parentesis DO bloque_condicion_void {
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    $$ = new ParserVal(new SyntaxNode("while", (SyntaxNode)$2.obj, (SyntaxNode)$4.obj));
            }
            | WHILE condicion_parentesis DO error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
            | WHILE condicion_parentesis bloque_condicion_void {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
                    $$ = new ParserVal(new SyntaxNode("while", (SyntaxNode)$2.obj, (SyntaxNode)$3.obj));
            }
            ;

bloque_if_void:
			IF condicion_parentesis bloque_condicion_void END_IF {
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)$3.obj, null);
                    $$ = new ParserVal(new SyntaxNode("IF", (SyntaxNode)$2.obj, thenSyntaxNode, null));
            }
			| IF condicion_parentesis bloque_condicion_void ELSE bloque_condicion_void END_IF  {
                    // Crear nodos para 'then' y 'else'
                    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)$3.obj, null);
                    SyntaxNode elseSyntaxNode = new SyntaxNode("ELSE", (SyntaxNode)$5.obj, null);

                    // Crear un nodo para el cuerpo del 'if' que contiene 'then' y 'else'
                    SyntaxNode camino = new SyntaxNode("Camino", thenSyntaxNode, elseSyntaxNode);

                    // Crear el nodo 'if' con la condici?n y el cuerpo como hijos
                    SyntaxNode ifSyntaxNode = new SyntaxNode("IF", (SyntaxNode) $2.obj, camino);

                    // Crear un nodo 'if' con la condición y los nodos 'then' y 'else' como hijos
                    $$ = new ParserVal(ifSyntaxNode);
                    logger.logDebugSyntax("Bloque IF-ELSE en la linea " + LexicalAnalyzer.getLine());
            }
            | IF condicion_parentesis bloque_condicion_void ELSE bloque_condicion_void error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
            | IF condicion_parentesis bloque_condicion_void error {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
            | IF condicion_parentesis bloque_condicion_void bloque_condicion_void {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");
            }
            | IF bloque_condicion_void {
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");
            }
	 		;

bloque_condicion_void:
            sentencia_void { $$ = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)$1.obj)); }
		    | sentencia_return {
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        $$ = new ParserVal(new SyntaxNode("Bloque de sentencias4", null, returnSyntaxNode));
		    }
		    | '{' sentencia_return '}'  {
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        $$ = new ParserVal(new SyntaxNode("Bloque de sentencias5", null, returnSyntaxNode));
		    }
            | '{' bloque_sentencias_condicion_void '}' { $$ = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)$2.obj)); }
            | '{' sentencia_return bloque_sentencias_condicion_void '}' { $$ = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)$3.obj, (SyntaxNode)$2.obj)); }
            | '{' bloque_sentencias_condicion_void error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
            ;

bloque_sentencias_condicion_void:
            sentencias_concatenacion_void sentencia_return { $$ = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)$2.obj)); }
            | sentencias_concatenacion_void { $$ = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)$1.obj)); }
            ;

/*********** CLASSES ***********/
definicion_class:
            header_class '{' bloque_class '}' { 
            		//$$ = $3;

                    //String nombreClase = $1.sval;
                    //arbolFunciones.add(new SyntaxNode("DeclaracionClass=" + nombreClase, (SyntaxNode) $3.obj, null));
                    //checkFlagAmbito();

                    eliminarAmbito(); // Restaura el ámbito anterior al salir de la clase
            		}
            | header_class '{' bloque_class error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
            | forward_declaration
            
            | header_class {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
            ;

forward_declaration:	
            CLASS ID ',' {
                logger.logDebugSyntax("FORWARD DECLARATION en la linea " + LexicalAnalyzer.getLine());
                String nombreClase = $2.sval + ":" + Parser.ambito;

                /* Registrar la clase en la tabla de simbolos*/
                SymbolTable tablaSimbolos = SymbolTable.getInstance();

                Optional<Attribute> classAttr = tablaSimbolos.getAttribute(nombreClase);

                if (classAttr.isPresent()) {
                    yyerror("La clase " + nombreClase + " ya fue declarada.");
                } else {
                    Attribute newAttr = new Attribute(Parser.ID, nombreClase, "Clase", UsesType.CLASE, LexicalAnalyzer.getLine());
                    newAttr.setWasForwardDeclared(true);
                    tablaSimbolos.insertAttribute(newAttr);

                    parseAndAddToClassMap(newAttr.getToken());

                    currentClass = nombreClase;

                    $$.sval = nombreClase;
                }
            }

header_class:
		    CLASS ID {
		        logger.logDebugSyntax("CLASE en la linea " + LexicalAnalyzer.getLine());
		        String nombreClase = $2.sval + ":" + Parser.ambito;
		
		        /* Registrar la clase en la tabla de simbolos*/
		        SymbolTable tablaSimbolos = SymbolTable.getInstance();

                Optional<Attribute> classAttr = tablaSimbolos.getAttribute(nombreClase);

                if (classAttr.isPresent()) {
                  if (classAttr.get().isWasForwardDeclared()) {
                    currentClass = nombreClase;
                    nuevoAmbito($2.sval); /* Agrega el nuevo ambito de la clase*/
                    $$.sval = nombreClase;
                    classAttr.get().setWasForwardDeclared(false);
                  } else {
                    yyerror("La clase " + nombreClase + " ya fue declarada previamente.");
                  }
                } else {
                  Attribute newAttr = new Attribute(Parser.ID, nombreClase, "Clase", UsesType.CLASE, LexicalAnalyzer.getLine());
                  tablaSimbolos.insertAttribute(newAttr);

                  parseAndAddToClassMap(newAttr.getToken());

                  currentClass = nombreClase;

                  nuevoAmbito($2.sval); /* Agrega el nuevo ambito de la clase*/

                  $$.sval = nombreClase;
                }
		    }
            | CLASS error{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");}
            ;

sentencias_class:
            declaracion_var {
                              logger.logDebugSyntax("Declaración2 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
            | declaracion_var_class
            | inheritance_by_composition
            | ejecucion {
                        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": la sentencia declarada no es permitida fuera de un metodo.");
                        }
            ;

bloque_class:
            bloque_class sentencias_class ',' {
               if ((SyntaxNode) $2.obj != null)
                    $$ = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) $1.obj, (SyntaxNode) $2.obj));
            }
            | bloque_class sentencias_class error {
                                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                             }
            | bloque_class declaracion_void ',' {
                if ((SyntaxNode) $2.obj != null)
                    $$ = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) $1.obj, (SyntaxNode) $2.obj));
            }
            | bloque_class declaracion_void error {
                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                                   }
		    | bloque_class definicion_class {
		        $$ = new ParserVal(new SyntaxNode("BloqueClase" , (SyntaxNode) $2.obj, (SyntaxNode) $1.obj));
		    }
		    | sentencias_class ',' {$$ = $1;}
		    | sentencias_class error {
                                     logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                     $$ = $1;
                                  }
            ;

inheritance_by_composition:
            ID {
              if (!esTipoClaseValido($1.sval)) {
                yyerror("Tipo de clase no declarado: " + $1.sval);
              } else {
                logger.logDebugSyntax("Herencia por composicion en la linea " + LexicalAnalyzer.getLine());
                ArrayList<String> inheritanceClasses = compositionMap.get(currentClass);
                final String classFullName = getNameSymbolTableVariables($1.sval);

                if (inheritanceClasses == null) {
                  inheritanceClasses = new ArrayList<>();
                  compositionMap.put(currentClass, inheritanceClasses);
                }

                // Verificar la profundidad de la herencia antes de añadir la nueva clase
                int inheritanceDepth = getInheritanceDepth(classFullName, 1);
                if (inheritanceDepth >= 3) {
                  yyerror("Error: Se excede el límite de niveles de herencia (máximo 3) al heredar de " + val_peek(0).sval);
                } else {
                  inheritanceClasses.add(classFullName);

                  // Verificar si al añadir esta clase se excede el límite para la clase actual
                  if (getInheritanceDepth(currentClass, 0) >= 3) {
                    yyerror("Error: Se excede el límite de niveles de herencia (máximo 3) para la clase " + currentClass);
                    inheritanceClasses.remove(classFullName); // Revertir la adición
                  }
                }
              }
            }
            ;

declaracion_var_class:
			ID lista_de_variables {
                    logger.logDebugSyntax("Declaración 3de variables en la linea " + LexicalAnalyzer.getLine());
                    if (!esTipoClaseValido($1.sval)) {
                        yyerror("Tipo de clase no declarado: " + $1.sval);
                    }
                    else {
                        var t = SymbolTable.getInstance();
                        String tipoVariable = $1.sval;

                        ArrayList<Attribute> classAttributes = getClassMembers(tipoVariable);
                        ArrayList<String> parentClasses = compositionMap.get(classFullNames.get(tipoVariable));

                        if (parentClasses != null) {
                          for (String parentClass : parentClasses) {
                            classAttributes.addAll(getClassMembers(parentClass));
                          }
                        }

                        for (String varName : lista_variables) {
                            String nombreCompleto = varName;
                            var entrada = t.getAttribute(nombreCompleto);
                            if (entrada.isPresent()) {
                                if (entrada.get().getType() == null) {
                                    entrada.get().setUso(UsesType.VARIABLE);
                                    entrada.get().setType(tipoVariable); // Asigna el tipo correcto
                                } else {
                                    yyerror("La variable declarada ya existe " + (varName.contains(":") ? varName.substring(0, varName.indexOf(':')) : "en ambito global"));
                                }
                            } else {
                                Attribute classVarAttr = new Attribute(Parser.ID, varName, tipoVariable, UsesType.CLASS_VAR, LexicalAnalyzer.getLine());
                                t.insertAttribute(classVarAttr);

                                for (Attribute attr : classAttributes) {
                                    final String attrName = attr.getToken() + ":" + varName;
                                    Attribute memberAttr = new Attribute(Parser.ID, attrName, attr.getType(), attr.getUso(), LexicalAnalyzer.getLine());
                                    t.insertAttribute(memberAttr);
                                }
                            }
                        }
                        lista_variables.clear();
                    }
                  }
			;

acceso:
		    ID '.' ID {
		        logger.logDebugSyntax("Acceso en la linea " + LexicalAnalyzer.getLine());

		        String varType = getTypeSymbolTableVariablesEnAcceso($3.sval, $1.sval);

		        if (!varType.equals("Error")) {
		            SyntaxNode objetoNode = new SyntaxNode($1.sval, "Clase");
		            SyntaxNode propiedadNode = new SyntaxNode($3.sval, varType);
		            SyntaxNode accesoNode = new SyntaxNode("Acceso", objetoNode, propiedadNode, varType);
		            $$ = new ParserVal(accesoNode);
		        } else {
		            yyerror("La variable " + $1.sval + " no fue declarada.");
		        }

		    }
		    | ID '.' error {
		        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");
		    }
		    ;


/************* CALLS *************/

llamada:
		    ID '(' ')'  {
                        logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
        				String nombreMetodo = getNameSymbolTableVariables($1.sval);
                        if (!nombreMetodo.equals("Error")) {
				            if (metodoExiste(Parser.ambito, nombreMetodo)) {
				            	SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion", new SyntaxNode(nombreMetodo));
				                logger.logDebugSyntax("Llamado a método en la linea " + LexicalAnalyzer.getLine());
                        		$$ = new ParserVal(llamadaSyntaxNode);

				            	checkFunctionCall(nombreMetodo, null);
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + $1.sval + " no existe " );
				            }
                        }
		    }
            | ID '(' factor ')' {
   			        //    SyntaxNode node = (SyntaxNode) val_peek(1).obj;
   			            logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
   			            SyntaxNode node = (SyntaxNode) $3.obj;

                        if (node != null){ //  && node.isLeaf()
                            String nombreMetodo = getNameSymbolTableVariables($1.sval);
                            //String nombreCompleto = getNameSymbolTableVariables(node.getName());

                            if (!nombreMetodo.equals("Error")) {
                                if (metodoExiste(Parser.ambito, nombreMetodo)) {

                                    SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion",
                                                                                    new SyntaxNode(nombreMetodo),
                                                                                    new SyntaxNode("parametro", (SyntaxNode) $3.obj, null),
                                                                                    getTypeSymbolTableVariables($1.sval));
                                    logger.logDebugSyntax("Llamado a método en la linea " + LexicalAnalyzer.getLine());
                                    $$ = new ParserVal(llamadaSyntaxNode);

                                    checkFunctionCall(nombreMetodo, ((SyntaxNode) $3.obj).getType());
                                } else {
                                    yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + $1.sval + " no existe " );
                                }
                            }
                        } else {
                            yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": Parametro inexistente." );
                        }
            }
		    | ID '.' ID '(' ')' {
                        //SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion", new SyntaxNode($1.sval), (SyntaxNode)$3.obj);
				        String nombreInstancia = getNameSymbolTableVariables($1.sval);
				        String tipoClase = getTypeSymbolTableVariables(nombreInstancia);
                        if (!tipoClase.equals("Error")) {
				            // Verificar si el método existe en la clase
				            String nombreMetodo = $3.sval;
				            if (metodoExisteEnClase(tipoClase, nombreMetodo)) {
				                SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoMetodoClase", new SyntaxNode(nombreInstancia), new SyntaxNode(nombreMetodo));
				                logger.logDebugSyntax("Llamado a método de clase en la linea " + LexicalAnalyzer.getLine());
				                $$ = new ParserVal(llamadaSyntaxNode);

				                checkFunctionCall(nombreMetodo, null);
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe en la clase " + tipoClase);
				            }
				        }
		    }
		    | ID '.' ID '(' factor ')' {
				        logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
   			            SyntaxNode node = (SyntaxNode) $5.obj;

                        if (node != null){ //  && node.isLeaf()
                            final String nombreInstancia = getNameSymbolTableVariables($1.sval);
                            final String tipoClase = getTypeSymbolTableVariables(nombreInstancia);

                            if (!tipoClase.equals("Error")) {
                                // Verificar si el método existe en la clase
                                String nombreMetodo = $3.sval;
                                if (metodoExisteEnClase(tipoClase, nombreMetodo)) {
                                    SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoMetodoClase",
                                                                                new SyntaxNode(nombreInstancia),
                                                                                new SyntaxNode(nombreMetodo, new SyntaxNode("parametro", node, null), null),
                                                                                getTypeSymbolTableVariables($1.sval));
                                    logger.logDebugSyntax("Llamado a método de clase en la linea " + LexicalAnalyzer.getLine());
                                    $$ = new ParserVal(llamadaSyntaxNode);
                                    checkFunctionCall(nombreMetodo, node.getType());
                                } else {
                                    yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe en la clase " + tipoClase);
                                }
				            }
                        }
		    }
			;
/********** PRINT ***********/

impresion:
            PRINT CADENA  {
                                    logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
                                    $$ = new ParserVal( new SyntaxNode("Print", new SyntaxNode($2.sval, "CADENA"), null, "CADENA"));
                               }
			| PRINT factor  {
			                        logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
			                        $$ = new ParserVal( new SyntaxNode("Print", (SyntaxNode) $2.obj, null, "Factor" ));
			                     }
			| PRINT error {logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el contenido de la impresion.");}
			;

%%
public static Logger logger = Logger.getInstance();
public static String ambito = "global";
public static boolean error = false;
public static SyntaxNode padre = null;
public static List<SyntaxNode> arbolFunciones = new ArrayList<SyntaxNode>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static Boolean flagAmbitoCambiado = false;
public static Map<String, String> classFullNames = new HashMap<>();
public static Map<String, ArrayList<String>> compositionMap = new HashMap<>();
public static String currentClass = "";
public static int basicBlockCounter = 1;
public static Map<String, SyntaxNode> propagatedConstantsDefinitionsMap = new HashMap<>();

private boolean metodoExisteEnClase(String classType, String methodName) {
    ArrayList<String> parentClasses = compositionMap.get(classFullNames.get(classType));

    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (parentClasses == null) {
          parentClasses = new ArrayList<>();
        }

        parentClasses.add(classType);

        boolean containsClass = false;
        for (String parentClass : parentClasses) { // Buscamos en clase y en sus padres
            if (attribute.getToken().contains(parentClass.split(":")[0])) {
                containsClass = true;
                break;
            }
        }

        if (attribute.getUso() != null && attribute.getUso().equals(UsesType.FUNCTION) && containsClass) {
            // Extraer el nombre del método del token
            String metodo = attribute.getToken().split(":")[0];
            // Verificar si el nombre del método encontrado coincide con el nombre del método buscado
            if (metodo.equals(methodName)) {
                return true;
            }
        }
    }
    return false;
}

private boolean metodoExiste(String classType, String funcName) {
    // Check for recursion...  put method name last!
    final int index = funcName.indexOf(':');
    final String swappedClass = funcName.substring(index + 1) + ":" + funcName.substring(0, index);

    if (swappedClass.equals(classType)) {
      return true;
    }

    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (attribute.getUso() != null && attribute.getUso().equals(UsesType.FUNCTION) && attribute.getToken().contains(classType)) {
            // Extraer el nombre del metodo del token
            String metodo = attribute.getToken();
            // Verificar si el nombre del metodo encontrado coincide con el nombre del metodo buscado
            if (metodo.equals(funcName)) {
                return true;
            }
        }
    }
    return false;
}

public static List<SyntaxNode> getArbolFunciones() {
	return arbolFunciones;
}

public boolean esTipoClaseValido(String nombreClase) {
    String tipo = getTypeSymbolTableVariables(nombreClase);
    return (tipo.equals("Clase"));
}
void verificarReglasCheck() {
    Map<String, Attribute> table = SymbolTable.getInstance().getTableMap();
    for (Map.Entry<String, Attribute> entry : table.entrySet()) {
        Attribute entrada = entry.getValue();
        if (entrada.isEsCheck()) {
            if (entrada.getAmbitosUsoIzquierdo().size() > 1 && entrada.isUsadaDerecho()) {
                logger.logDebugSyntax("La variable: " + entrada.getToken() + " cumple con las condiciones de CHECK");
                System.out.println("La variable: " + entrada.getToken() + " cumple con las condiciones de CHECK");
            } else {
                // Reportar incumplimiento
                logger.logDebugSyntax("La variable: " + entrada.getToken() + " NO cumple con las condiciones de CHECK");
                System.out.println("La variable: " + entrada.getToken() + " NO cumple con las condiciones de CHECK");
            }
        }
    }
}

public void setYylval(ParserVal yylval) {
    this.yylval = yylval;
}

public String getTypeSymbolTable(String lexema){
    var x = valorSimbolo(lexema);
    if (x.equals( "Error"))
        yyerror("La variable no esta declarada");
    return x;
}

private String validarTipos(SyntaxNode obj, SyntaxNode obj1, boolean esAsignacion) {
    if (obj == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": ID no existe");
        return "obj is null";
    }
    if (obj1 == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": ID no existe.");
        return "obj1 is null";
    }
    if (obj.getType() == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": Uno de los tipos es nulo.");
        return "obj type is null";
    }
    if (obj1.getType() == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": Uno de los tipos es nulo.");
        return "obj1 type is null";
    }

    // Propagación de un error anterior si son distintos o si ya hay un error
    if (obj.getType().equals("Error") || obj1.getType().equals("Error")) {
        Parser.error = true;
        return "Error";
    }

    boolean esEnteroObj = esTipoEntero(obj.getType());
    boolean esEnteroObj1 = esTipoEntero(obj1.getType());
    boolean esFlotanteObj = obj.getType().equals(UsesType.FLOAT);
    boolean esFlotanteObj1 = obj1.getType().equals(UsesType.FLOAT);
    
    if (esEnteroObj && esEnteroObj1) { 
    	if (!obj.getType().equals(obj1.getType()))//determino que es op entre long y ushort
        	return UsesType.LONG;
        else
        	return obj.getType();
    }
    // Manejo de conversiones y errores para asignaciones
    if (esAsignacion) {
        if (esEnteroObj && esFlotanteObj1) {
            yyerror("Incompatibilidad de tipos: no se puede asignar FLOAT a tipo entero en linea " + LexicalAnalyzer.getLine());
            return "Error";
        }
        if (esFlotanteObj && esEnteroObj1) {
            // Conversión implícita de entero a flotante
            return UsesType.FLOAT;
        }
    }

    // Manejo de conversiones y errores para operaciones
    if (!esAsignacion) {
        if ((esEnteroObj && esFlotanteObj1) || (esFlotanteObj && esEnteroObj1)) {
            // Conversión implícita de entero a flotante, tal vez hay que logearlo
            return UsesType.FLOAT;
        }
    }

    // Verificación de igualdad de tipos
    if (!obj.getType().equals(obj1.getType())) {
        yyerror("Los tipos no coinciden en linea " + LexicalAnalyzer.getLine());
        return "Error";
    }

    return obj.getType(); // Retorna el tipo de ambos.
}

private boolean esTipoEntero(String tipo) {
    return tipo.equals(UsesType.CONSTANT) || tipo.equals(UsesType.LONG) || tipo.equals(UsesType.USHORT);
}

public void nuevoAmbito(String ambito){
    Parser.ambito = Parser.ambito + ":" + ambito ;
    this.flagAmbitoCambiado = true;
}

private void checkFlagAmbito() {
	if (flagAmbitoCambiado) {
		flagAmbitoCambiado = false;
		eliminarAmbito();
	}
}

public void eliminarAmbito() {
    if (Parser.ambito.contains(":")) {
        // Elimina la Ãºltima parte del Ã¡mbito
        Parser.ambito = Parser.ambito.substring(0, Parser.ambito.lastIndexOf(':'));
		flagAmbitoCambiado = false;
    }
}

public String valorSimbolo(String lexema) {
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    Optional<Attribute> entrada = tablaSimbolos.getAttribute(lexema);

    if (entrada.isPresent()) {
        return entrada.get().getType();
    } else {
        return "Error";
    }
}

private String getTypeSymbolTableVariables(String sval) {
    String ambitoActual = ":" + Parser.ambito;
    String nombreCompleto = sval + ambitoActual;

    while (!nombreCompleto.isEmpty()) {
        String tipo = valorSimbolo(nombreCompleto);
        if (!tipo.equals("Error")) {
            return tipo;
        }
        // Recortar el ámbito para buscar en el ámbito padre
        if (nombreCompleto.contains(":")) {
            nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
        } else {
            nombreCompleto = "";
        }
    }
    // Si llegamos aquí, significa que no se encontró el tipo de la variable
    if (!falloNombre(sval)) {
		yyerror("La variable o funcion no fue hallada en el ambito actual.");
    }
    return "Error";
}

private String getTypeSymbolTableVariablesEnAcceso(String sval, String sval2) {
    final String objectType = getTypeSymbolTableVariables(sval2);
    final String ambitoActual = ":" + Parser.ambito;

    ArrayList<String> composedClassesList = compositionMap.get(classFullNames.get(objectType));

    if (composedClassesList == null) {
      composedClassesList = new ArrayList<>();
    }

    composedClassesList.add(objectType);

    for (String composedClass : composedClassesList) {
      final String nombreCompleto = sval + ambitoActual + ":" + composedClass.split(":")[0];
      final String foundType = hallarTipoEnAmbito(nombreCompleto);
      if (!foundType.isEmpty()) {
          return foundType;
      }
    }

    // Si llegamos aquí, significa que no se encontró el tipo de la variable
    if (!falloNombre(sval)) {
      yyerror("La variable o funcion no es reconocible en el ambito actual.");
    }
    return "Error";
}

public String hallarTipoEnAmbito(String nombreCompleto) {
    while (!nombreCompleto.isEmpty()) {
      String tipo = valorSimbolo(nombreCompleto);
      if (!tipo.equals("Error")) {
        return tipo;
      }
      // Recortar el ámbito para buscar en el ámbito padre
      if (nombreCompleto.contains(":")) {
        nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
      } else {
        nombreCompleto = "";
      }
    }
    return "";
}

private Boolean falloNombre(String sval){
      var pila = ":" + Parser.ambito;

      while(!pila.isBlank() && valorSimbolo(sval + pila).equals("Error")){
        pila = pila.substring(1);
        if (pila.contains(":"))
          pila = pila.substring(pila.indexOf(':'));
        else
          pila = "";
      }
      return (valorSimbolo(sval + pila).equals("Error"));
}

private String getNameSymbolTableVariables(String sval) {
    sval = sval.replace("#", "");

    String ambitoActual = ":" + Parser.ambito;
    String nombreCompleto = sval + ambitoActual;
    while (!nombreCompleto.isEmpty()) {
        if (!valorSimbolo(nombreCompleto).equals("Error")) {
            return nombreCompleto;
        }
        // Recortar el ámbito para buscar en el ámbito padre
        if (nombreCompleto.contains(":")) {
            nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
        } else {
            nombreCompleto = "";
        }
    }
    // Si llegamos aquí, significa que no se encontró la variable
    yyerror("La variable o funcion '" + sval + "' no es reconocible en el ambito actual.");
    return "Error";
}

public static boolean isNegativeIntInRange(long valor, String type) {
    return switch (type) {
        case (UsesType.USHORT) -> !(valor > 0);
        case (UsesType.LONG) -> !(valor > abs(DelimiterType.minLong));
        default -> !(valor > 0);
    };
}

public static boolean isPositiveIntInRange(long valor, String type) {
    return switch (type) {
        case (UsesType.USHORT) -> !(valor > DelimiterType.maxShort);
        case (UsesType.LONG) -> !(valor > DelimiterType.maxLong);
        default -> !(valor > DelimiterType.maxShort);
    };
}

public String processInteger(String value, boolean isPositive) {
    String suffix = null;
    String keyInMap = value;
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    Optional<Attribute> entrada = tablaSimbolos.getAttribute(value);
    String delimiter = "";

    String[] parts = value.split("_", 2);
    if (value.contains("_")) {
        value = parts[0];
        suffix = parts[1];
    }

    if (entrada.isPresent()) {
        final String type_cte = entrada.get().getType();

        try {
            final long valor = Long.parseLong(value);

            boolean outOfRange;


            if (isPositive) {
                outOfRange = !isPositiveIntInRange(valor, type_cte);
                delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.maxShort) : String.valueOf(DelimiterType.maxLong);
            } else {
                outOfRange = !isNegativeIntInRange(valor, type_cte);
                delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.minShort) : String.valueOf(DelimiterType.minLong);
            }

            if (outOfRange) {
                logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo " + type_cte + " fuera de los rangos permitidos. SerÃ¡ truncado");
                suffix = (suffix == null) ? "" : ("_" + suffix);
                value = delimiter + suffix;
                entrada.get().setToken(value);
                tablaSimbolos.updateKey(keyInMap, value);
            }
        } catch (NumberFormatException e) {
            logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo " + type_cte + " fuera de los rangos permitidos. Serï¿½ truncado");
            if (isPositive) {
              delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.maxShort) : String.valueOf(DelimiterType.maxLong);
            } else {
              delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.minShort) : String.valueOf(DelimiterType.minLong);
            }

            suffix = (suffix == null) ? "" : ("_" + suffix);
            value = delimiter + suffix;
            entrada.get().setToken(value);
            tablaSimbolos.updateKey(keyInMap, value);
        }

        if (!value.contains("_"))
            value = value + "_" + suffix;

        return value;
    } else {
        return "Error";
    }
}

public static boolean isFloatInRange(double valor) {
    if (valor == DelimiterType.zero ) {
    	return true;
    }
    return !(valor > DelimiterType.maxFloatPositive || valor < DelimiterType.minFloatPositive);
}

public String processFloat(String value, boolean isPositive) {
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    String keyInMap = value;

    Optional<Attribute> entrada = tablaSimbolos.getAttribute(value);

    if (entrada.isPresent()) {
        try {
            final double valor = Double.parseDouble(value);

            if (!isFloatInRange(valor)) {
                logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo FLOAT fuera de los rangos permitidos. Serï¿½ truncado");
                value = String.valueOf(isPositive ? DelimiterType.maxFloatPositive : DelimiterType.minFloatNegative);
                entrada.get().setToken(value);
                tablaSimbolos.updateKey(keyInMap, value);
            }
        } catch (NumberFormatException e) {
            logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo FLOAT fuera de los rangos permitidos. Serï¿½ truncado");
            value = String.valueOf(isPositive ? DelimiterType.maxFloatPositive : DelimiterType.minFloatNegative);
            entrada.get().setToken(value);
            tablaSimbolos.updateKey(keyInMap, value);
        }
        return value;
    } else {
        return "Error";
    }
}

public static void yyerror(String mensaje) {
    System.out.println("## ERROR ## " + mensaje);
    Parser.error = true;
}

public static int yylex() {
    int tokenID = 0;
    Reader reader = LexicalAnalyzer.reader;
    LexicalAnalyzer.currentState = 0;

    while (true) {
        try {
            if (LexicalAnalyzer.endOfFile(reader)) {
                    break;
            }
            char character = LexicalAnalyzer.getNextCharWithoutAdvancing(reader);
            tokenID = LexicalAnalyzer.changeState(reader, character);
            if (tokenID != SemanticAction.active_state) {
                Parser parser = LexicalAnalyzer.getParser();
                parser.yylval = new ParserVal(LexicalAnalyzer.currentToken.toString());
                LexicalAnalyzer.currentToken.delete(0, LexicalAnalyzer.currentToken.length());
                return tokenID;
            }
       } catch (IOException e) {
                e.printStackTrace();
       }
    }
    return tokenID;
}

public static void checkFunctionCall(String funcName, String parameterName) {
  boolean functionFound = false;
  boolean hasParameter = parameterName != null && !parameterName.isEmpty();

  for (Map.Entry<String, Attribute> functionVariable : SymbolTable.getTableMap().entrySet()) {
    if ((functionVariable.getKey().startsWith(funcName + ":") || functionVariable.getKey().contains(funcName)) && functionVariable.getValue().getUso().equals(UsesType.FUNCTION)) {
      functionFound = true;
      var funcAttr = functionVariable.getValue();
      var paramAttr = funcAttr.getParameter();

      if (paramAttr == null && hasParameter) {
        yyerror("Esta funcion no admite el pasaje de parametros.");
        return;
      }

      if (paramAttr != null && !hasParameter) {
        yyerror("Faltan parametros para llamar a esta funcion.");
        return;
      }

      if (hasParameter) {
        var paramEntry = SymbolTable.getInstance().getAttribute(parameterName);
        if (paramEntry.isPresent() && !paramAttr.getType().equals(paramEntry.get().getType())) {
          yyerror("El parametro es del tipo incorrecto.");
          return;
        }
      }

      // Si llegamos aquí, la llamada a la función es correcta
      return;
    }
  }

  if (!functionFound) {
    yyerror("La funcion '" + funcName + "' no esta definida.");
  }
}

private void parseAndAddToClassMap(String input) {
    String[] parts = input.split(":");
    if (parts.length > 1) {
        String key = parts[0];
        classFullNames.put(key, input);
    } else {
        System.out.println("Input string format is incorrect: " + input);
    }
}

private String swapComponents(String input) {
    String[] parts = input.split(":");
    if (parts.length == 2) {
        return parts[1] + ":" + parts[0];
    } else {
        System.out.println("Input string format is incorrect: " + input);
        return input;  // Devuelve la cadena original si el formato es incorrecto
    }
}

private ArrayList<Attribute> getClassMembers(final String className) {
    ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());
    ArrayList<Attribute> members = new ArrayList<>();

    // Find entry related to class definition
    final String classAmbit = swapComponents(className.contains(":") ? className : classFullNames.get(className));

    // Get all members from a class
    for (Attribute entry : entries) {
        if (entry.getToken().contains(classAmbit) && entry.getUso().equals(UsesType.VARIABLE)) {
            members.add(entry);
        }
    }
    return members;
}

private Attribute getMemberVarAttribute(SyntaxNode accessNode) {
  if (accessNode != null) {
    final String varName = accessNode.getLeftChild().getName();
    final String memberName = accessNode.getRightChild().getName();
    //final String memberType = accessNode.getRightChild().getType();

    for (Attribute entry : SymbolTable.getTableMap().values()) {
      final String token = entry.getToken();
      if (token.startsWith(memberName) && token.contains(varName + ":")) {
        return entry;
      }
    }
  }
  return null;
}

private int getInheritanceDepth(String className, int currentDepth) {
  if (currentDepth > 3) {
    return currentDepth;
  }

  ArrayList<String> parentClasses = compositionMap.get(className);
  if (parentClasses == null || parentClasses.isEmpty()) {
    return currentDepth;
  }

  int maxDepth = currentDepth;
  for (String parentClass : parentClasses) {
    int depth = getInheritanceDepth(parentClass, currentDepth + 1);
    maxDepth = Math.max(maxDepth, depth);
  }

  return maxDepth;
}