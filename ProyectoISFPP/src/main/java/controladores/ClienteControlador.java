package controladores;

import javafx.scene.control.Label;
import aplicacion.Contexto;
import aplicacion.Navegador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;


public class ClienteControlador implements Controlador {

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
    private Label errorMensaje;

    @FXML
    void onIngresar(ActionEvent event) {
    	Usuario usuario = sistema.buscarPorEmail(TextoMail.getText());
    	if(usuario == null) {
    		errorMensaje.setText("no existe ese usuario porfavor complete los datos y cree un nuevo usuario");
    		return;
    	}
    	entrar(usuario);
    }

    @FXML
    void  onCrear(ActionEvent event) {
    	try {
    		if(sistema.buscarPorEmail(TextoMail.getText()) != null) {
    			errorMensaje.setText("ya existe un usuario con ese mail solo toca ingresar");
        		return;
    		}
    		Usuario nuevo = new Usuario(TextoNombre.getText(), TextoTelefono.getText(), TextoMail.getText());
    		sistema.registrarUsuario(nuevo);
    		entrar(nuevo);
    	}catch(IllegalArgumentException | UnsupportedOperationException e){
			errorMensaje.setText(e.getMessage());
    	}

    }
    
    private void entrar(Usuario usu) {
    	contexto.setUsuarioActual(usu);
    	navegador.irA("/vistas/CotizacionViaje.fxml");
    }

}	
