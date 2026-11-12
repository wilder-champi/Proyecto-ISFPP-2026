package controladores;

import java.util.Locale;

import aplicacion.Contexto;
import aplicacion.Navegador;
import internacionalizacion.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * Controlador de la pantalla inicial: permite elegir el idioma e ingresar como usuario o como conductor.
 */
public class SesionUsuarioControlador implements Controlador {

	// Permite cambiar entre las vistas FXML
    private Navegador navegador;
    
	/**
	 * Guarda el navegador y carga las opciones de idioma.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
	@Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
		cargarComboBox();
	}
	
	@FXML
    private ComboBox<String> cmbIdioma;
	
    /**
     * Carga los idiomas disponibles en el selector y marca el idioma actual.
     */
	// Carga los idiomas y selecciona el idioma actual.
    private void cargarComboBox() {

        // Agregamos las opciones disponibles.
        cmbIdioma.getItems().addAll("Español", "English");

        // Obtenemos el idioma actual de la aplicación.
        String idiomaActual = I18N.getIdioma().getLanguage();

        // Seleccionamos el idioma que corresponde.
        if (idiomaActual.equals("en")) {
            cmbIdioma.setValue("English");
        } else {
            cmbIdioma.setValue("Español");
        }
    }

	    /**
	     * Cambia el idioma de la aplicación según la opción elegida y recarga la pantalla inicial.
	     *
	     * @param event evento del selector de idioma
	     */
	 @FXML
	    void onCambiarIdioma(ActionEvent event) {

		        String seleccionado = cmbIdioma.getValue();

		        if (seleccionado == null) {
		            return;
		        }

		        Locale nuevoIdioma;

		        if (seleccionado.equals("English")) {
		            nuevoIdioma = Locale.US;
		        } else {
		            nuevoIdioma = Locale.forLanguageTag("es-ES");
		        }

		        // Evitamos recargar si ya estamos usando ese idioma.
		        if (I18N.getIdioma().equals(nuevoIdioma)) {
		            return;
		        }

		        // Guardamos el idioma seleccionado.
		        I18N.cambiarIdioma(nuevoIdioma);

		        // Recargamos solamente la primera pantalla.
		        navegador.irA("/vistas/SesionUsuario.fxml");
		    }
	    
	
	 /**
	  * Pasa a la pantalla de ingreso de clientes.
	  *
	  * @param event evento del botón de ingresar como usuario
	  */
	// Se ejecuta al presionar "Ingresar como Usuario"
	 @FXML
	 void onIngresarUsuario(ActionEvent event) {
		 navegador.irA("/vistas/Cliente.fxml"); 
	 }
	
	 /**
	  * Pasa a la pantalla de ingreso de conductores.
	  *
	  * @param event evento del botón de ingresar como conductor
	  */
	// Se ejecuta al presionar "Ingresar como Conductor"
	 @FXML
	 void onIngresarConductor(ActionEvent event) {
		 navegador.irA("/vistas/Conductor.fxml"); 
	 }
			
}
