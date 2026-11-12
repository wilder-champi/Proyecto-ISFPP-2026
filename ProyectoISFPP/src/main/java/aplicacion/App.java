package aplicacion;

import javafx.application.Application;
import javafx.stage.Stage;
import logica.SistemaGuardado;

/**
 * Arranca la interfaz grafica: crea los datos una sola vez, crea el Navegador
 * y le pide que muestre la primera pantalla
 */
public class App extends Application {

	// ruta de la primera vista, dentro de src/main/resources
	private static final String PRIMERA_VISTA = "/vistas/SesionUsuario.fxml";

	/**
	 * Crea el sistema de guardado, el contexto y el navegador, y muestra la primera pantalla.
	 *
	 * @param stage ventana principal de la aplicación
	 */
	@Override
	public void start(Stage stage) {
		SistemaGuardado sistema = new SistemaGuardado(); // carga usuarios, vehiculos y servicios
		Contexto contexto = new Contexto(sistema);
		Navegador navegador = new Navegador(stage, contexto);

		stage.setTitle("ISFPP");
		navegador.irA(PRIMERA_VISTA);
		stage.show();
	}

	/**
	 * Punto de entrada de la aplicación: muestra la versión de Java y lanza JavaFX.
	 *
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		System.out.println("Java en ejecución: " + System.getProperty("java.version"));
		launch(args);
	}
}