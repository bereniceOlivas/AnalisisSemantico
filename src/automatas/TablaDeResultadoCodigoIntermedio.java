package automatas;

public class TablaDeResultadoCodigoIntermedio {
	
	String Identificador;
	String Valor1;
	String Operacion;
	String Valor2;
	double Resultado;
	
	public TablaDeResultadoCodigoIntermedio(String identificador, String valor1, String operacion, String valor2,double Resultado) {
		super();
		this.Identificador = identificador;
		this.Valor1 = valor1;
		this.Operacion = operacion;
		this.Valor2 = valor2;
		this.Resultado = Resultado;
	}
	public String getIdentificador() {
		return Identificador;
	}
	public void setIdentificador(String identificador) {
		Identificador = identificador;
	}
	public String getValor1() {
		return Valor1;
	}
	public void setValor1(String valor1) {
		Valor1 = valor1;
	}
	public String getOperacion() {
		return Operacion;
	}
	public void setOperacion(String operacion) {
		Operacion = operacion;
	}
	public String getValor2() {
		return Valor2;
	}
	public void setValor2(String valor2) {
		Valor2 = valor2;
	}
	public double getResultado() {
		return Resultado;
	}
	public void setResultado(double resultado) {
		Resultado = resultado;
	}
	
	
	
	
	
	
	

}
