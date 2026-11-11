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

	private Navegador navegador;//se utiliza para tener referencia ala clase navegador que es la que maneja las vistas
	private SistemaGuardado sistema;//se usa para guardar la informacion de los usuarios cargados por el txt o bd
	private Contexto contexto;// es el contexto que maneja la aplicacion fx como usuario actual y demas

	@Override
	public void init(Navegador navegador, Contexto contexto) {//init se utiliza como un seudo contructor mas que todo para inicializar todo
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
    void onIngresar(ActionEvent event) {//evento cual que se activa al tcar el boton de ingresar
    		
    	Usuario usuario = sistema.buscarPorEmail(TextoMail.getText());//busco al usuario apartir del vlor unico que el el email que no se repite
    	if(usuario == null) {
    		errorMensaje.setText("no existe ese usuario porfavor complete los datos y cree un nuevo usuario");
    		return;
    	}
    	entrar(usuario);//si este se encontro se le pasa a un metodo para que el lo haga entrar
    }

    @FXML
    void  onCrear(ActionEvent event) {//evevnto que se activa al precionar el boton de crear usuario
    	try {
    		if(sistema.buscarPorEmail(TextoMail.getText()) != null) {//verificamos que el mail ingresaado no le pertenezca a un usuario ya previamente cargado
    			errorMensaje.setText("ya existe un usuario con ese mail solo toca ingresar");
        		return;
    		}
    		Usuario nuevo = new Usuario(TextoNombre.getText(), TextoTelefono.getText(), TextoMail.getText());//se crea un usuario leyenco la informacion ingresada en los "TextField"
    		sistema.registrarUsuario(nuevo);//registamos el usuario
    		entrar(nuevo);//una vez creado y registrado le pasamos el usuario al metodo de ayuda para que lo haga entrar
    	}catch(IllegalArgumentException | UnsupportedOperationException e){//atrapamos la excepcion por si este no se pudo crear por un x motivo
			errorMensaje.setText(e.getMessage());
    	}

    }
    
    private void entrar(Usuario usu) {
    	contexto.setUsuarioActual(usu);//aca a contexto le decimos cual es el nuevo usuario actual
    	navegador.irA("/vistas/CotizacionViaje.fxml");//y  con navegador vamos ala siguiente vista 
    }
    
    @FXML
    void onsalir(ActionEvent event) {//al precionar el boton de salir este evento usando navegador va a la vista main o la vista principal
    	navegador.irA("/vistas/SesionUsuario.fxml");
    }


}	
