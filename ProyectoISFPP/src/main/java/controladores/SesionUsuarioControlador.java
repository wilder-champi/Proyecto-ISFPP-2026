package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SesionUsuarioControlador implements Controlador {

	// Permite cambiar entre las vistas FXML
    private Navegador navegador;
    
	@Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
	}
	
	// Se ejecuta al presionar "Ingresar como Usuario"
	 @FXML
	 void onIngresarUsuario(ActionEvent event) {
		 navegador.irA("/vistas/Cliente.fxml"); 
	 }
	
	// Se ejecuta al presionar "Ingresar como Conductor"
	 @FXML
	 void onIngresarConductor(ActionEvent event) {
		 navegador.irA("/vistas/Conductor.fxml"); 
	 }
			
}
