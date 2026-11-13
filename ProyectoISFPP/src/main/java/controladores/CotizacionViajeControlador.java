package controladores;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CalificacionViaje;
import enums.CategoriaVehiculo;
import enums.EstadoViaje;
import enums.TipoServicio;
import enums.TipoVehiculo;
import internacionalizacion.I18N;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logica.Alternativa;
import logica.Cotizador;
import modelo.Ubicacion;
import modelo.Viaje;
import netscape.javascript.JSObject;

/**
 * Controlador de la pantalla donde el cliente cotiza y solicita viajes. Permite cargar el origen y el destino
 * (escribiendo las coordenadas o marcándolas en el mapa), ver las alternativas con su costo y solicitar una.
 */
public class CotizacionViajeControlador implements Controlador {

	private Navegador navegador;
	private Contexto contexto;
	private Cotizador cotizador;

	@FXML
	private TextField TextoLatDestino;

	@FXML
	private TextField TextoLatOrigen;

	@FXML
	private TextField TextoLonDestino;

	@FXML
	private TextField TextoLonOrigen;

	@FXML
	private ComboBox<TipoVehiculo> cmbVehiculo;
	@FXML
	private ComboBox<TipoServicio> cmbServicio;
	@FXML
	private ComboBox<CategoriaVehiculo> cmbCategoria;
    @FXML
    private ComboBox<CalificacionViaje> cmbCalificacion;

	@FXML
	private TableView<FilaAlternativa> tblAlternativas;
	@FXML
	private TableColumn<FilaAlternativa, String> colServicio;
	@FXML
	private TableColumn<FilaAlternativa, String> colPrecio;
	@FXML
	private TableColumn<FilaAlternativa, String> colTiempo;

	@FXML
	private Label mensajel;

	@FXML
	private WebView mapaWeb;

	private WebEngine motor;
	private PuenteMapa puente;

	/**
	 * Guarda los datos compartidos y crea el cotizador con los servicios del sistema.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
	@Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
		this.cotizador = new Cotizador(contexto.getSistema().getServicios());
		this.contexto = contexto;
	}

	/**
	 * Se ejecuta al cargar la vista FXML: configura la tabla, carga los selectores y el mapa.
	 */
	@FXML
	private void initialize() {
		configurarTabla();
		cargarCombos();
		cargarMapa();
	}

	/**
	 * Indica de qué atributo de {@link FilaAlternativa} sale el contenido de cada columna de la tabla.
	 */
	private void configurarTabla() {
		colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
	}

	/**
	 * Carga en los selectores los tipos de vehículo, los tipos de servicio y las categorías.
	 */
	private void cargarCombos() {
		cmbVehiculo.getItems().addAll(TipoVehiculo.values());
		cmbServicio.getItems().addAll(TipoServicio.values());
		cmbCategoria.getItems().addAll(CategoriaVehiculo.values());
		
		for (CalificacionViaje c : CalificacionViaje.values()) {
		    if (c != CalificacionViaje.NO_CALIFICADO) {
		        cmbCalificacion.getItems().add(c);
		    }
		}
	}

	/**
	 * Cotiza el viaje entre el origen y el destino y muestra las alternativas en la tabla. Muestra un error
	 * si falta alguna selección, las coordenadas no son válidas o no hay servicios disponibles.
	 *
	 * @param event evento del botón de cotizar
	 */
	@FXML
	void onCotizar(ActionEvent event) {
		mensajel.setText("67");
		try {
			Ubicacion origen = new Ubicacion(leer(TextoLatOrigen), leer(TextoLonOrigen));
			Ubicacion destino = new Ubicacion(leer(TextoLatDestino), leer(TextoLonDestino));

			if (cmbServicio.getValue() == null || cmbCategoria.getValue() == null) {
				mensajel.setText(I18N.texto("cotizacion.error.seleccion"));
				return;
			}

			List<Alternativa> alternativas = cotizador.consultar(origen, destino, cmbServicio.getValue(),
					cmbCategoria.getValue());

			tblAlternativas.getItems().clear();

			for (Alternativa a : alternativas) {
				tblAlternativas.getItems().add(new FilaAlternativa(a));
			}
			if (alternativas.isEmpty()) {
				mensajel.setText(I18N.texto("cotizacion.error.sinServicios"));
			}
		} catch (IllegalArgumentException e) {
			mensajel.setText(e.getMessage());
		}
	}

	/**
	 * Solicita un viaje con la alternativa seleccionada y lo agrega a los viajes pendientes.
	 * Muestra un error si no hay alternativa elegida o si el viaje no se puede solicitar.
	 *
	 * @param event evento del botón de solicitar
	 */
	@FXML
	void onSolicitar(ActionEvent event) {
		mensajel.setText("");
		FilaAlternativa fila = tblAlternativas.getSelectionModel().getSelectedItem();
		if (fila == null) {
			mensajel.setText(I18N.texto("cotizacion.error.sinAlternativa"));
			return;
		}
		try {
			Ubicacion origen = new Ubicacion(leer(TextoLatOrigen), leer(TextoLonOrigen));
			Ubicacion destino = new Ubicacion(leer(TextoLatDestino), leer(TextoLonDestino));

			Viaje viaje = new Viaje(contexto.getUsuarioActual(), origen, destino, fila.getAlternativa().getServicio());
			viaje.solicitar(LocalDateTime.now());
			contexto.getSistema().agregarViajePendiente(viaje);

			mensajel.setText("viaje solicitado, id " + viaje.getId());
			tblAlternativas.getItems().clear();
		} catch (IllegalStateException | IllegalArgumentException e) {
			mensajel.setText(e.getMessage());
		}
	}

	/**
	 * Lee una coordenada escrita en un campo de texto.
	 *
	 * @param campo campo de texto con la coordenada
	 * @return la coordenada como número
	 * @throws IllegalArgumentException si el texto no es un número
	 */
	private double leer(TextField campo) {
		try {
			return Double.parseDouble(campo.getText().trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("cotizacion.error.coordenadas");
		}
	}

	/**
	 * Limpia los campos, los selectores, la tabla, el mensaje y los marcadores del mapa.
	 *
	 * @param event evento del botón de limpiar
	 */
	@FXML
	void onLimpiar(ActionEvent event) {
		TextoLatOrigen.clear();
		TextoLonOrigen.clear();
		TextoLatDestino.clear();
		TextoLonDestino.clear();
		cmbVehiculo.setValue(null);
		cmbServicio.setValue(null);
		cmbCategoria.setValue(null);
		tblAlternativas.getItems().clear();
		mensajel.setText("");
		motor.executeScript("limpiarMarcadores()");

	}

	/**
	 * Vuelve a la pantalla de ingreso de clientes.
	 *
	 * @param event evento del botón de volver
	 */
	@FXML
	void onVolver(ActionEvent event) {
		navegador.irA("/vistas/Cliente.fxml");
	}

	/**
	 * Activa la selección del destino haciendo clic en el mapa.
	 *
	 * @param event evento del botón de marcar destino
	 */
	@FXML
	void onMarcarDestinoMapa(ActionEvent event) {
	    motor.executeScript("activarSeleccion('destino')");

	}

	/**
	 * Activa la selección del origen haciendo clic en el mapa.
	 *
	 * @param event evento del botón de marcar origen
	 */
	@FXML
	void onMarcarOrigenMapa(ActionEvent event) {
	    motor.executeScript("activarSeleccion('origen')");
	}

	/**
	 * Carga el mapa en la vista web y lo conecta con Java para recibir los puntos que se marcan.
	 */
	private void cargarMapa() {

	    motor = mapaWeb.getEngine();
	    motor.setUserAgent("CotizadorViajes/1.0");

	    puente = new PuenteMapa();
	    motor.getLoadWorker().stateProperty().addListener((obs, viejo, nuevo) -> {
	        if (nuevo == Worker.State.SUCCEEDED) {
	            JSObject ventana = (JSObject) motor.executeScript("window");
	            ventana.setMember("javaBridge", puente);
	        }
	    });

	    URL url = getClass().getResource("/vistas/mapa.html");
	    motor.load(url.toExternalForm());

	}

	/**
	 * Puente entre el JavaScript del mapa y Java: recibe los puntos que el usuario marca en el mapa.
	 */
	public class PuenteMapa {
		/**
		 * Completa los campos de latitud y longitud del origen o del destino con el punto marcado en el mapa.
		 *
		 * @param tipo {@code "origen"} o {@code "destino"}
		 * @param lat latitud del punto marcado
		 * @param lon longitud del punto marcado
		 */
		public void seleccionarPunto(String tipo, double lat, double lon) {
			if ("origen".equals(tipo)) {
				TextoLatOrigen.setText(String.valueOf(lat));
				TextoLonOrigen.setText(String.valueOf(lon));
			} else if ("destino".equals(tipo)) {
				TextoLatDestino.setText(String.valueOf(lat));
				TextoLonDestino.setText(String.valueOf(lon));
			}
		}
	}
	
	
	private Viaje ultimoViaje() {
	    List<Viaje> viajes = contexto.getUsuarioActual().getCliente().getViajes();

	    if (viajes.isEmpty()) {
	        return null; // el cliente todavía no hizo ningún viaje
	    }

	    int posicionUltimo = viajes.size() - 1;
	    return viajes.get(posicionUltimo);
	}
	
	
	
	
	/** Habilita el selector solo si el último viaje terminó y todavía no fue calificado. */
	private void actualizarCalificacion() {
	    Viaje viaje = ultimoViaje();
	    boolean puede = false;
	    if (viaje != null && viaje.estadoActual() == EstadoViaje.FINALIZADO) {
	        CalificacionViaje nota = viaje.getCalificacionConductor();
	        puede = (nota == null || nota == CalificacionViaje.NO_CALIFICADO);
	    }
	    cmbCalificacion.setDisable(!puede);
	}
	
	
	@FXML
	void onCalificar(ActionEvent event) {
	    CalificacionViaje nota = cmbCalificacion.getValue();
	    Viaje viaje = ultimoViaje();
	    if (nota == null || viaje == null) {
	        return; // se ejecuta también cuando se limpia el combo
	    }
	    viaje.setCalificacionConductor(nota);
	    cmbCalificacion.setValue(null);
	    actualizarCalificacion();
	}

}
