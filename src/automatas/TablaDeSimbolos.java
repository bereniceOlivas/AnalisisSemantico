/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatas;

/**

 */
public class TablaDeSimbolos {
    
        String rol;
	String nombre;
	String tipo;
	String valor;
	int posicion;
	
	
	public TablaDeSimbolos(String rol, String nombre, String tipo, String valor, int posicion) {
		super();
                this.rol = rol;
		this.nombre = nombre;
		this.tipo = tipo;
		this.valor = valor;	
		this.posicion = posicion;
	}
        public String getrol() {
		return rol;
	}
	public void setrol(String rol) {
		this.rol = rol;
	}
           
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getposicion() {
		return posicion;
	}
	public void setposicion(int posicion) {
		this.posicion = posicion;
	}


	
}