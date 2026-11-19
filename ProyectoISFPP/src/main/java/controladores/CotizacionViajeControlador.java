package controladores;

import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import internacionalizacion.I18N;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logica.Alternativa;
import logica.Cotizador;
import logica.OperacionesViaje;
import modelo.CalificacionViaje;
import modelo.CategoriaVehiculo;
import modelo.EstadoViaje;
import modelo.TipoServicio;
import modelo.TipoVehiculo;
import modelo.Ubicacion;
import modelo.Viaje;
import netscape.javascript.JSObject;

/**
 * Controlador de la pantalla donde el cliente cotiza y solicita viajes. Permite cargar el origen y el destino
 * (marcándolos en el mapa), ver las alternativas con su costo y solicitar una.
 */
public class CotizacionViajeControlador implements Controlador {

	private Navegador navegador;
	private Contexto contexto;
	private Cotizador cotizador;

	// puntos marcados en el mapa (reemplazan a los campos de coordenadas)
	private Ubicacion puntoOrigen;
	private Ubicacion puntoDestino;

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
		mensajel.setText("");
		try {
			if (puntoOrigen == null || puntoDestino == null) {
				mensajel.setText(I18N.texto("cotizacion.error.marcarPuntos"));
				return;
			}
			Ubicacion origen = puntoOrigen;
			Ubicacion destino = puntoDestino;

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
			mensajel.setText(I18N.texto("cotizacion.error.coordenadas"));
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
			if (puntoOrigen == null || puntoDestino == null) {
				mensajel.setText(I18N.texto("cotizacion.error.marcarPuntos"));
				return;
			}
			Ubicacion origen = puntoOrigen;
			Ubicacion destino = puntoDestino;

			Viaje viaje = new Viaje(contexto.getUsuarioActual(), origen, destino, fila.getAlternativa().getServicio());
			viaje.solicitar(LocalDateTime.now());
			contexto.getSistema().agregarViajePendiente(viaje);

			mensajel.setText(MessageFormat.format(I18N.texto("cotizacion.mensaje.solicitado"), viaje.getId()));
			tblAlternativas.getItems().clear();
		} catch (IllegalStateException | IllegalArgumentException e) {
			mensajel.setText(I18N.texto("cotizacion.error.coordenadas"));
		}
	}

	/**
	 * Limpia los campos, los selectores, la tabla, el mensaje y los marcadores del mapa.
	 *
	 * @param event evento del botón de limpiar
	 */
	@FXML
	void onLimpiar(ActionEvent event) {
		puntoOrigen = null;
		puntoDestino = null;
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
		 * Guarda el origen o el destino con el punto marcado en el mapa.
		 *
		 * @param tipo {@code "origen"} o {@code "destino"}
		 * @param lat latitud del punto marcado
		 * @param lon longitud del punto marcado
		 */
		public boolean seleccionarPunto(String tipo, double lat, double lon) {
		    Ubicacion punto = new Ubicacion(lat, lon);
		    if (!contexto.getSistema().getGeneradorUbicacion().contiene(punto)) {
		        return false;               // fuera de la ciudad, no se guarda
		    }
		    if ("origen".equals(tipo)) {
		        puntoOrigen = punto;
		    } else if ("destino".equals(tipo)) {
		        puntoDestino = punto;
		    }
		    return true;
		}
	}
	
	
	private Viaje ultimoViaje() {
	    List<Viaje> viajes = contexto.getUsuarioActual().getCliente().getViajes();

	    if (viajes.isEmpty()) {
	        return null; // el cliente todavia no hizo ningun viaje
	    }

	    int posicionUltimo = viajes.size() - 1;
	    return viajes.get(posicionUltimo);
	}
	
	
	
	@FXML
	void onCalificar(ActionEvent event) {
	    CalificacionViaje nota = cmbCalificacion.getValue();
	    Viaje viaje = ultimoViaje();
	    if (nota == null || viaje == null || viaje.estadoActual() != EstadoViaje.INICIADO) {
	        return; 
	    }
	    viaje.finalizar(LocalDateTime.now(), CalificacionViaje.NO_CALIFICADO, nota);
	    cmbCalificacion.setValue(null);
	}
	
	
	@FXML
	void onCancelarViaje(ActionEvent event) {
		mensajel.setText("");
		Viaje viaje = ultimoViaje();
		if (viaje == null || !contexto.getUsuarioActual().getCliente().enViaje()) {
			mensajel.setText("No tenés ningún viaje para cancelar.");
			return;
		}
		try {
			double costo = OperacionesViaje.cancelar(viaje, contexto.getUsuarioActual(),
					"Cancelado por el cliente", LocalDateTime.now());
			contexto.getSistema().removerViajePendiente(viaje);
			if (costo > 0) {
				mensajel.setText("Viaje cancelado. Costo de la cancelación: $" + String.format("%.2f", costo));
			} else {
				mensajel.setText("Viaje cancelado. No genera ningún costo.");
			}
		} catch (IllegalStateException | IllegalArgumentException e) {
			mensajel.setText(e.getMessage());
		}
	}
	

}