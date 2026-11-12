package datos.dao.txt;

/**
 * Se lanza cuando una linea de un archivo de datos no se puede convertir en un objeto del modelo
 * Indicando el archivo y el numero de linea para poder corregirlo mas tarde
 */
public class ArchivoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String archivo;
	private final int numeroLinea;

	/**
	 * Crea la excepción indicando dónde está el error.
	 *
	 * @param archivo nombre del archivo con el error
	 * @param numeroLinea número de la línea con el error
	 * @param motivo descripción del problema
	 */
	public ArchivoInvalidoException(String archivo, int numeroLinea, String motivo) {
		super("error en " + archivo + ", linea " + numeroLinea + ": " + motivo);
		this.archivo = archivo;
		this.numeroLinea = numeroLinea;
	}

	/**
	 * Devuelve el archivo donde ocurrió el error.
	 *
	 * @return nombre del archivo
	 */
	public String getArchivo() {
		return archivo;
	}

	/**
	 * Devuelve la línea donde ocurrió el error.
	 *
	 * @return número de línea
	 */
	public int getNumeroLinea() {
		return numeroLinea;
	}
}