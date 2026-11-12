package interfaz_consola;


/**
 * Utilidades para mostrar mensajes y errores por consola sin repetir los {@code print}.
 */
public class interfazConsola {

	/**
	 * Crea una instancia de la clase.
	 */
	public interfazConsola() {
		super();
	}

	/**
	 * Muestra un mensaje por la salida estándar.
	 *
	 * @param mensaje texto a mostrar
	 */
	//Metodos que hize para evitar hacer los Prints 
	public static void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	/**
	 * Muestra un mensaje de error por la salida de error, con el prefijo {@code ERROR:}.
	 *
	 * @param mensaje texto del error a mostrar
	 */
	public static void mostrarError(String mensaje) {
		System.err.println("ERROR: " + mensaje);
	}
	
}

