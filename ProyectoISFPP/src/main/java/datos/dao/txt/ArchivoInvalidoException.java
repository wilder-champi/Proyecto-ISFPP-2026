package datos.dao.txt;

/**
 * Se lanza cuando una linea de un archivo de datos no se puede convertir en un objeto del modelo
 * Indicando el archivo y el numero de linea para poder corregirlo mas tarde
 */
public class ArchivoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String archivo;
	private final int numeroLinea;

	public ArchivoInvalidoException(String archivo, int numeroLinea, String motivo) {
		super("error en " + archivo + ", linea " + numeroLinea + ": " + motivo);
		this.archivo = archivo;
		this.numeroLinea = numeroLinea;
	}

	public String getArchivo() {
		return archivo;
	}

	public int getNumeroLinea() {
		return numeroLinea;
	}
}