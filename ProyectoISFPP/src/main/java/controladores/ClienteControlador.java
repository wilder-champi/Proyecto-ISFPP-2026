package controladores;

import javafx.scene.control.Label;
import aplicacion.Contexto;
import aplicacion.Navegador;
import internacionalizacion.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;


/**
 * Controlador de la pantalla de ingreso de clientes: permite ingresar con un email existente
 * o crear un usuario nuevo.
 */
public class ClienteControlador implements Controlador {

	private Navegador navegador;//se utiliza para tener referencia ala clase navegador que es la que maneja las vistas
	private SistemaGuardado sistema;//se usa para guardar la informacion de los usuarios cargados por el txt o bd
	private Contexto contexto;// es el contexto que maneja la aplicacion fx como usuario actual y demas

	/**
	 * Guarda el navegador, el contexto y el sistema para usarlos en la pantalla.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
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

    /**
     * Ingresa al usuario cuyo email se escribió. Muestra un error si no existe.
     *
     * @param event evento del botón de ingresar
     */
    @FXML
    void onIngresar(ActionEvent event) {//evento cual que se activa al tcar el boton de ingresar
    		
    	Usuario usuario = sistema.buscarPorEmail(TextoMail.getText());//busco al usuario apartir del vlor unico que el el email que no se repite
    	if(usuario == null) {
    		errorMensaje.setText(I18N.texto("cliente.error.noExiste"));
    		return;
    	}
    	entrar(usuario);//si este se encontro se le pasa a un metodo para que el lo haga entrar
    }

    /**
     * Crea y registra un usuario con los datos escritos y lo ingresa. Muestra un error si el email ya existe
     * o los datos no son válidos.
     *
     * @param event evento del botón de crear usuario
     */
    @FXML
    void  onCrear(ActionEvent event) {//evevnto que se activa al precionar el boton de crear usuario
    	try {
    		if(sistema.buscarPorEmail(TextoMail.getText()) != null) {//verificamos que el mail ingresaado no le pertenezca a un usuario ya previamente cargado
    			errorMensaje.setText(I18N.texto("cliente.error.yaExiste"));
        		return;
    		}
    		Usuario nuevo = new Usuario(TextoNombre.getText(), TextoTelefono.getText(), TextoMail.getText());//se crea un usuario leyenco la informacion ingresada en los "TextField"
    		sistema.registrarUsuario(nuevo);//registamos el usuario
    		entrar(nuevo);//una vez creado y registrado le pasamos el usuario al metodo de ayuda para que lo haga entrar
    	}catch(IllegalArgumentException | UnsupportedOperationException e){//atrapamos la excepcion por si este no se pudo crear por un x motivo
			errorMensaje.setText(e.getMessage());
    	}

    }
    
    /**
     * Establece al usuario como el actual y pasa a la pantalla de cotización de viajes.
     *
     * @param usu usuario que ingresa
     */
    private void entrar(Usuario usu) {
    	contexto.setUsuarioActual(usu);//aca a contexto le decimos cual es el nuevo usuario actual
    	navegador.irA("/vistas/CotizacionViaje.fxml");//y  con navegador vamos ala siguiente vista 
    }
    
    /**
     * Vuelve a la pantalla inicial.
     *
     * @param event evento del botón de salir
     */
    @FXML
    void onsalir(ActionEvent event) {//al precionar el boton de salir este evento usando navegador va a la vista main o la vista principal
    	navegador.irA("/vistas/SesionUsuario.fxml");
    }


}	
