package aplicacion;

import java.io.IOException;

import controladores.Controlador;
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

	public Navegador(Stage stage, Contexto contexto) {
		this.stage = stage;
		this.contexto = contexto;
	}

	public void irA(String rutaFxml) {
		if (getClass().getResource(rutaFxml) == null) {
			throw new IllegalArgumentException("no se encontro la vista " + rutaFxml);
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
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