package ArbolSintactico;

import java.util.Optional;

import AnalizadorLexico.Attribute;
import AnalizadorLexico.SymbolTable;
import Assembler.SentenceConstructors.AccessConstructor;

/** Clase Nodo del �rbol que conserva el nombre, el tipo y si es una hoja */
public class SyntaxNode {
    private String name;
    private SyntaxNode leftChild;
    private SyntaxNode rightChild;
    private boolean isLeaf;
    private String type = "";

    public SyntaxNode(String name, SyntaxNode leftChild, SyntaxNode rightChild){
        this.name = name;
        this.rightChild = rightChild;
        this.leftChild = leftChild;
        this.isLeaf = false;
    }

    public SyntaxNode(String name, SyntaxNode leftChild, SyntaxNode rightChild, String type){
        this.name = name;
        this.rightChild = rightChild;
        this.leftChild = leftChild;
        this.isLeaf = false;
        this.type = type;
    }

    public SyntaxNode(String name){
        this.name = name;
        this.rightChild = null;
        this.leftChild = null;
        this.isLeaf = true;
    }

    public SyntaxNode(String name, String type){
        this.name = name;
        this.rightChild = null;
        this.leftChild = null;
        this.isLeaf = true;
        this.type = type;
    }

    public SyntaxNode(String name, SyntaxNode rightChild) {
        this.name = name;
        this.rightChild = rightChild;
        this.leftChild = null;
        this.isLeaf = false;
    }

    public SyntaxNode getRightChild() {
        return rightChild;
    }

    public SyntaxNode getLeftChild() {
        return leftChild;
    }

    public String getName() {
        return name;
    }

    public boolean isLeaf(){
        return isLeaf;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setRightChild(SyntaxNode rightChild) {
        this.rightChild = rightChild;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public void setLeftChild(SyntaxNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SyntaxNode [name=" + name + ", leftChild=" + leftChild + ", rightChild=" + rightChild + ", isLeaf=" + isLeaf + ", type=" + type + "]";
    }

    public void addRightChild(SyntaxNode child) {
        this.rightChild = child;
    }
    
    public Optional<Attribute> getNodeValue() {
    	SymbolTable table = SymbolTable.getInstance();
//    	System.out.println("busco x" + this.name);
        var entrada = table.getAttribute(this.name);

    	if (entrada.isPresent())
    		return entrada;
//    	System.out.println(entrada);
    	return null;
    }
}