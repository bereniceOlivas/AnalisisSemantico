package automatas;

public class TablaDeVariablesCodigoIntermedio 
{
    
	String NombreaVariable;
	int valor;
	public TablaDeVariablesCodigoIntermedio(String nombreaVariable, int valor) {
		super();
		this.NombreaVariable = nombreaVariable;
		this.valor = valor;
	}
	public String getNombreaVariable() {
		return NombreaVariable;
	}
	public void setNombreaVariable(String nombreaVariable) {
		NombreaVariable = nombreaVariable;
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	
	
	
	
}
