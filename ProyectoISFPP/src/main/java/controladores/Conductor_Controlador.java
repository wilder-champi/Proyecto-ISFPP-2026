package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.RolUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;

public class Conductor_Controlador implements Controlador {
	
	private Navegador navegador;
	private SistemaGuardado sistema;
	private Contexto contexto;
	 
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

    // Ingresa un conductor que ya está registrado
    @FXML
    void onIngresar(ActionEvent event) {
    	errorMensaje.setText("");
    	
    	String email = TextoMail.getText().trim();
		if (email.isEmpty()) {
			errorMensaje.setText("Ingrese su email");
			return;
		}
        Usuario usuario = sistema.buscarPorEmail(email);
        if (usuario == null) {
            errorMensaje.setText("No existe un usuario con ese email");
            return;
        }
        // Verificamos si está registrado como conductor
        if (usuario.getConductor() == null) {
            errorMensaje.setText("El usuario no está registrado como conductor");
            return;
        }
            entrar(usuario);
    }
    
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

    // Recibe los datos para crear un conductor
    @FXML
    void onCrear(ActionEvent event) {
		errorMensaje.setText("");
        try {
			String email = TextoMail.getText().trim();
         
            if (sistema.buscarPorEmail(email) != null) {
                errorMensaje.setText("Ya existe un usuario con ese email.");
                return;
            }
            if (TextoLicenciaConducir.getText().isBlank()) {
                errorMensaje.setText("Ingrese su licencia de conducir");
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
