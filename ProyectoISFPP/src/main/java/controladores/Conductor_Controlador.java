package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.RolUsuario;
import internacionalizacion.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;

/**
 * Controlador de la pantalla de ingreso de conductores: permite ingresar con un email de conductor
 * registrado o iniciar el registro de uno nuevo.
 */
public class Conductor_Controlador implements Controlador {
	
	private Navegador navegador;
	private SistemaGuardado sistema;
	private Contexto contexto;
	 
	/**
	 * Guarda el navegador, el contexto y el sistema para usarlos en la pantalla.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
	@Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
		this.sistema = contexto.getSistema();
		this.contexto = contexto;
	}

	 @FXML
	 private TextField TextoMail;

	 @FXML
	 private TextField TextoNombre;
	 
	 @FXML
	 private TextField TextoTelefono;

	 @FXML
	 private TextField TextoLicenciaConducir;
	 
	 @FXML
	 private TextField TextoPatente;
	 
	 @FXML
	 private Label errorMensaje;

    /**
     * Ingresa a un conductor ya registrado. Muestra un error si el email está vacío, no existe
     * o el usuario no es conductor.
     *
     * @param event evento del botón de ingresar
     */
    // Ingresa un conductor que ya está registrado
    @FXML
    void onIngresar(ActionEvent event) {
    	errorMensaje.setText("");
    	
    	String email = TextoMail.getText().trim();
		if (email.isEmpty()) {
			errorMensaje.setText(I18N.texto("conductor.error.emailVacio"));
			return;
		}
        Usuario usuario = sistema.buscarPorEmail(email);
        if (usuario == null) {
            errorMensaje.setText(I18N.texto("conductor.error.noExiste"));
            return;
        }
        // Verificamos si está registrado como conductor
        if (usuario.getConductor() == null) {
            errorMensaje.setText(I18N.texto("conductor.error.noEsConductor"));
            return;
        }
            entrar(usuario);
    }
    
    /**
     * Activa el rol de conductor, guarda al usuario y su vehículo como actuales y pasa a la pantalla de viajes.
     *
     * @param usuario conductor que ingresa
     */
 // Ingresa a la pantalla del conductor
    private void entrar(Usuario usuario) {
 // Activamos el rol conductor	
    usuario.cambiarRolActivo(RolUsuario.CONDUCTOR);
 // Guardamos el usuario que ingresó
    contexto.setUsuarioActual(usuario);
    
    contexto.setVehiculoActual(null);
	if (!usuario.getConductor().getVehiculos().isEmpty()) {
		contexto.setVehiculoActual(usuario.getConductor().getVehiculoActivo());
		}
    // Abrimos la siguiente pantalla 
	
    navegador.irA("/vistas/ConductorViajes.fxml");
    }

    /**
     * Guarda temporalmente los datos del conductor nuevo y pasa a la pantalla para registrar su vehículo.
     * Muestra un error si el email ya existe, falta la licencia o los datos no son válidos.
     *
     * @param event evento del botón de crear
     */
    // Recibe los datos para crear un conductor
    @FXML
    void onCrear(ActionEvent event) {
		errorMensaje.setText("");
        try {
			String email = TextoMail.getText().trim();
         
            if (sistema.buscarPorEmail(email) != null) {
                errorMensaje.setText(I18N.texto("conductor.error.yaExiste"));
                return;
            }
            if (TextoLicenciaConducir.getText().isBlank()) {
                errorMensaje.setText(I18N.texto("conductor.error.licenciaVacia"));
                return;
            }

            Usuario nuevo = new Usuario(TextoNombre.getText(), TextoTelefono.getText(), email);
            
         // Guardamos temporalmente los datos
            contexto.setUsuarioPendiente(nuevo);
            contexto.setLicenciaPendiente(TextoLicenciaConducir.getText().trim());
    
         // Pasamos a registrar el vehículo
            navegador.irA("/vistas/Vehiculo.fxml");
            
          } catch (IllegalArgumentException e) {
            errorMensaje.setText(e.getMessage());
        }
        
    }
    
}
