package ArbolSintactico;

import ArbolSintactico.SyntaxNode;

/** Clase para mostrar el árbol sintáctico */
public class SyntaxTreeVisualizer {

    private final SyntaxNode rootNode;

    public SyntaxTreeVisualizer(SyntaxNode rootNode) {
        this.rootNode = rootNode;
    }

    // Recorre el árbol en preorden
    public String traversePreOrder(SyntaxNode rootNode) {
        if (rootNode == null) {
            return "";
        }

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(rootNode.getName());

        String rightArrow = "`-->";
        String leftArrow = (rootNode.getRightChild() != null) ? "|-->" : "`-->";

        navigateTree(textBuilder, "", leftArrow, rootNode.getLeftChild(), rootNode.getRightChild() != null);
        navigateTree(textBuilder, "", rightArrow, rootNode.getRightChild(), false);

        return textBuilder.toString();
    }

    // Método para navegar y visualizar la estructura del árbol
    public void navigateTree(StringBuilder textBuilder, String padding, String arrow, SyntaxNode node, boolean hasRightSibling) {

        if (node != null) {
            textBuilder.append("\n");
            textBuilder.append(padding);   // Espacio
            textBuilder.append(arrow);     // Indica izquierda o derecha
            textBuilder.append(node.getName()); // Muestra el nombre

            if (node.getType() != null && !node.getType().equals("NoType"))
                textBuilder.append(" ").append(node.getType());

            StringBuilder paddingBuilder = new StringBuilder(padding);

            if (hasRightSibling) {
                paddingBuilder.append("|  ");  // Añade una rama
            } else {
                paddingBuilder.append("   ");
            }

            String bothPadding = paddingBuilder.toString();
            String rightArrow = "`-->";
            String leftArrow = (node.getRightChild() != null) ? "|-->" : "`-->";

            navigateTree(textBuilder, bothPadding, leftArrow, node.getLeftChild(), node.getRightChild() != null);
            navigateTree(textBuilder, bothPadding, rightArrow, node.getRightChild(), false);
        }
    }
}