package interfaz_consola;


public class interfazConsola {

	public interfazConsola() {
		super();
	}

	//Metodos que hize para evitar hacer los Prints 
	public static void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	public static void mostrarError(String mensaje) {
		System.err.println("ERROR: " + mensaje);
	}
	
}

