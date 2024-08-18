package AnalizadorLexico;

import java.util.HashSet;
import java.util.Set;

public class Attribute {
	private int tokenID;
    private String token;
    private String type;
    private int line;
    public String uso;    
    boolean esCheck = false;
    Set<String> ambitosUsoIzquierdo = new HashSet<>();
    boolean usadaDerecho = false;
	private Attribute parameter = null;
	private String value = null;
	private boolean isActive = false;
    
    public Attribute(String token, int id) {
    	this.token = token;
    	this.tokenID = id;
    }

	public Attribute(int tokenID, String token, String type, String uso) {
		this.tokenID = tokenID;
		this.token = token;
		this.type = type;
		this.uso = uso;
	}

	public Attribute(String token, int tokenID, String type, int line){
        this.token = token;
    	this.tokenID = tokenID;
        this.type = type;
        this.line = line;
    }
    public Attribute( short tokenID, String token, String type, int line, boolean esCheck){
        this.token = token;
    	this.tokenID = tokenID;
        this.type = type;
        this.line = line;
        this.esCheck = esCheck;
    }

    public Attribute(int tokenID, String token, String type, String uso, int line) {
		this.tokenID = tokenID;
		this.token = token;
		this.type = type;
		this.line = line;
		this.uso = uso;
	}

    public Attribute(int tokenID, String token, String type, String uso, int line, boolean esCheck) {
		this.tokenID = tokenID;
		this.token = token;
		this.type = type;
		this.line = line;
		this.uso = uso;
        this.esCheck = esCheck;
	}

	public String getUso() {
		return uso;
	}

	public boolean isEsCheck() {
		return esCheck;
	}
	public void addAmbito(String ambito) {
		if (!this.ambitosUsoIzquierdo.contains(ambito))
			this.ambitosUsoIzquierdo.add(ambito);
	}
	public void setEsCheck(boolean esCheck) {
		this.esCheck = esCheck;
	}

	public Set<String> getAmbitosUsoIzquierdo() {
		return ambitosUsoIzquierdo;
	}

	public void setAmbitosUsoIzquierdo(Set<String> ambitosUsoIzquierdo) {
		this.ambitosUsoIzquierdo = ambitosUsoIzquierdo;
	}

	public boolean isUsadaDerecho() {
		return usadaDerecho;
	}

	public void setUsadaDerecho(boolean usadaDerecho) {
		this.usadaDerecho = usadaDerecho;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public int getIdToken() {
        return this.tokenID;
    }

    public String getToken() {
		return token;
    }

    public void setToken(String token) {
        this.token = token;
    }    

    public void setTokenID(int idToken) {
        this.tokenID = idToken;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setParameter(Attribute parameter) {
		this.parameter = parameter;
	}

	public Attribute getParameter() {
		return parameter;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Attribute [tokenID= " + tokenID + ", token= " + token + ", type= " + type + ", line= " + line + ", uso= "
				+ uso + ", esCheck= " + esCheck + ", ambitosUsoIzquierdo= " + ambitosUsoIzquierdo + ", usadaDerecho= "
				+ usadaDerecho + ", value= " + value + ", isActive= " + isActive + "]";
	}
}
