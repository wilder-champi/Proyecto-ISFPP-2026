package aplicacion;

import java.io.IOException;

import controladores.Controlador;
import internacionalizacion.I18N;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Cambia de pantalla: carga la vista que se le pide, le entrega los datos
 * compartidos a su controlador y la muestra en la ventana
 */
public class Navegador {

	private final Stage stage; // la ventana
	private final Contexto contexto; // los datos compartidos

	/**
	 * Crea el navegador para una ventana y sus datos compartidos.
	 *
	 * @param stage ventana donde se muestran las pantallas
	 * @param contexto datos compartidos que se entregan a cada controlador
	 */
	public Navegador(Stage stage, Contexto contexto) {
		this.stage = stage;
		this.contexto = contexto;
	}

	/**
	 * Carga la vista indicada, inicializa su controlador y la muestra en la ventana.
	 *
	 * @param rutaFxml ruta del archivo FXML dentro de los recursos (por ejemplo {@code /vistas/Cliente.fxml})
	 * @throws IllegalArgumentException si no se encuentra la vista
	 * @throws IllegalStateException si la vista no se puede cargar o su controlador no implementa {@link Controlador}
	 */
	public void irA(String rutaFxml) {
		if (getClass().getResource(rutaFxml) == null) {
			throw new IllegalArgumentException("no se encontro la vista " + rutaFxml);
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml),I18N.getBundle());//NUEVO);
			Parent raiz = loader.load(); // crea la pantalla y su controlador
			Object controlador = loader.getController();

			if (controlador instanceof Controlador) {
				((Controlador) controlador).init(this, contexto); // le entrega el Navegador y los datos
			} else if (controlador != null) {
				throw new IllegalStateException(
						controlador.getClass().getName() + " tiene que implementar Controlador");
			}
			stage.setScene(new Scene(raiz));
		} catch (IOException e) {
			throw new IllegalStateException("no se pudo cargar " + rutaFxml, e);
		}
	}
}