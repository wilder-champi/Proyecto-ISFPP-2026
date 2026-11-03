package datos.dao.txt;

import java.util.List;

public class Linea {

	private int numero; //guarda el numero del linea que se esta leyendo
	private List<String> campos;//Se usa para guardar los atributos o partes de cada linea 

	//constructor
	public Linea(int numero, List<String> campos) {
		this.numero = numero;
		this.campos = campos;
	}

	public int getNumero() {
		return numero;
	}

	public List<String> getCampos() {
		return campos;
	}
}