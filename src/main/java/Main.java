import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.text.SimpleDateFormat;

import AnalizadorLexico.*;
import AnalizadorSintactico.*;
import ArbolSintactico.SyntaxNode;
import ArbolSintactico.SyntaxTreeVisualizer;
import Assembler.AssemblerCodeManager;
import Assembler.SyntaxTreeDecoder;
import Utilities.Logger;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try {
				if (args.length == 0) {
					System.out.println("Provea una ruta a un archivo.");
					System.exit(1); // exit the program with an error code
				}

				final String testPath = args[0];

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final String timestamp = dateFormat.format(new Date());
				final String logMessage = timestamp + " - " + "Utilizando archivo de prueba: " + testPath;

				Logger logger = Logger.getInstance();

				// Initialize with file name without extension
				final String testDir = testPath.replaceFirst("[.][^.]+$", "");
				logger.loggerInitializer( testDir + "-", logMessage);

				Parser.error = false; // Set error to 'false' to start the compiler. It is set to 'true' in the logError methods.
				
				System.out.println();

				MatrixManager.fillMatrices();

				try {
					LexicalAnalyzer.reader = new BufferedReader(new FileReader(testPath));
				} catch (FileNotFoundException e) {
					System.out.println("No se encontró el archivo: " + testPath);
					return;
				}

				Parser parser = new Parser();
				LexicalAnalyzer.setParser(parser);
				parser.run();

				if (!Parser.error) { // If there's an error, we shouldn't generate a tree!
					// Show symbol table
					System.out.println(System.lineSeparator() + SymbolTable.getTable());

					// Show tree
					SyntaxTreeVisualizer treeVisualizer = new SyntaxTreeVisualizer(Parser.padre);
					System.out.println(treeVisualizer.traversePreOrder(Parser.padre));

					for (SyntaxNode s : Parser.getArbolFunciones()) {
						SyntaxTreeVisualizer funTreeVisualizer = new SyntaxTreeVisualizer(s);
						System.out.println(funTreeVisualizer.traversePreOrder(s));
					}

					// Generate code
					AssemblerCodeManager codeManager = new AssemblerCodeManager(testDir);


					codeManager.generateCode(Parser.padre);

					for (SyntaxNode functionNode : Parser.getArbolFunciones()) {
//						SyntaxTreeDecoder.exploreTree(s);
						codeManager.generateCode(functionNode);
					}



					codeManager.assemblerFileCreation();
				} else {
					System.out.println("\nSe han detectado errores. No es posible generar un arbol.");
				}


				logger.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (LexicalAnalyzer.reader != null)
						LexicalAnalyzer.reader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
	}

}


