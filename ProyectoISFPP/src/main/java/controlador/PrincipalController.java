package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrincipalController {

	@FXML
	private Label lblMensaje;   // igual al fx:id del archivo

	@FXML
	private void onProbar() {    // igual al onAction="#onProbar"
		lblMensaje.setText("Boton presionado");
	}
	
}