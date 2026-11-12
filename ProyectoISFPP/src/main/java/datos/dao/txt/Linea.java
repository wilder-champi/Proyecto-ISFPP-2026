package datos.dao.txt;

import java.util.List;

/**
 * Línea de un archivo de datos: guarda su número dentro del archivo y sus campos ya separados.
 */
public class Linea {

	private int numero; //guarda el numero del linea que se esta leyendo
	private List<String> campos;//Se usa para guardar los atributos o partes de cada linea 

	/**
	 * Crea una línea.
	 *
	 * @param numero número de línea dentro del archivo
	 * @param campos campos de la línea
	 */
	//constructor
	public Linea(int numero, List<String> campos) {
		this.numero = numero;
		this.campos = campos;
	}

	/**
	 * Devuelve el número de línea.
	 *
	 * @return número de línea dentro del archivo
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Devuelve los campos de la línea.
	 *
	 * @return lista de campos
	 */
	public List<String> getCampos() {
		return campos;
	}
}