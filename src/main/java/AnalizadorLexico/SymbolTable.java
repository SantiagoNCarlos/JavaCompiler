package AnalizadorLexico;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class SymbolTable {
	
	private static Map<String, Attribute> table = new HashMap<String, Attribute>();
    private static SymbolTable instance = new SymbolTable();

	private SymbolTable() {
		table = new HashMap<>(); 
		instance = this;
	}
	public static SymbolTable getInstance() {
        return instance;
    }
    public void updateKey(String oldKey, String newKey) {
        if (table.containsKey(oldKey)) {
            Attribute attribute = table.get(oldKey);
            table.remove(oldKey); 
            table.put(newKey, attribute); 
        }
    }
	public static void addSymbol(String token, int id, String type, int line) {
		Attribute a = new Attribute(token, id, type, line);
		table.put(token, a);
	}

	public static void addSymbol(String token, int id, String type, String use, int line) {
		Attribute a = new Attribute(id, token, type, use, line);
		table.put(token, a);
	}

	public static void addSymbol(String token, int id, String type, String use) {
		Attribute a = new Attribute(id, token, type, use);
		table.put(token, a);
	}
	
	public static int getSymbol(String token) {
		if (table.containsKey(token))
			return table.get(token).getIdToken();
		return -1;
	}
	public static Map<String, Attribute> getTableMap() {
		return table;
	}
	public static String getTable() {
	    StringBuilder message = new StringBuilder();
	    message.append(System.lineSeparator()).append("Tabla de Simbolos: ").append(System.lineSeparator());

	    for (Entry<String, Attribute> e : table.entrySet()) {
	        message.append(e.getKey()).append(": ").append(e.getValue()).append(System.lineSeparator());
	    }
	    return message.toString();
	}
	public Optional<Attribute> getAttribute(String lexema) {
		Attribute x = table.get(lexema);
//		 System.out.println("TENGO A: " + x + " Y BUSCO " + lexema);
        if (x==null)
            return Optional.empty();
        return Optional.of(table.get(lexema));
	}
	public void insertAttribute(Attribute x) {
        if (!table.containsKey(x.getToken()))
        	table.put(x.getToken(), x);		
	}
	
	

}
