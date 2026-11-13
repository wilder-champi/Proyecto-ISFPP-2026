package controladores;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CalificacionViaje;
import enums.EstadoConductor;
import enums.EstadoViaje;
import enums.TipoServicio;
import internacionalizacion.I18N;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logica.Cotizador;
import modelo.Conductor;
import modelo.Ubicacion;
import modelo.Vehiculo;
import modelo.Viaje;

/**
 * Controlador de la pantalla del conductor: permite cambiar de vehículo, elegir los servicios que presta,
 * cambiar su estado y ver y aceptar los viajes pendientes, que se muestran también en el mapa.
 */
public class ConductorViajesControlador implements Controlador {

	private Navegador navegador;
	private Contexto contexto;
	private Cotizador cotizador;
	
    @FXML
    private ComboBox<Vehiculo> cmbVehiculos;
    
    @FXML
    private ComboBox<CalificacionViaje> cmbCalificacion;

    @FXML
    private TableColumn<FilaViaje, String> colKilometro;

    @FXML
    private TableColumn<FilaViaje, String> colPrecio;

    @FXML
    private TableColumn<FilaViaje, String> colServicio;

    @FXML
    private TableColumn<FilaViaje, String> colTiempo;
    
    @FXML
    private TableView<FilaViaje> tblAlternativas;
    
    @FXML
    private Label mensajel;
    @FXML
    private Label lblEstado;
    
    @FXML
    private CheckBox chkEnvios;
    @FXML
    private CheckBox chkPasajeros;
    
    @FXML
    private WebView mapaWeb;

    private WebEngine motor;
    private boolean mapaListo = false;

    
	/**
	 * Guarda los datos compartidos y prepara la pantalla: tabla, vehículos, servicios, viajes pendientes, estado y mapa.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
    @Override
	public void init(Navegador navegador, Contexto contexto) {//usamos int para las asignaciones necesariaspara que la vista funcione adecuadamente
    	this.navegador = navegador;//asigno el navegador que me pedmite moverme entre vistas
    	this.contexto = contexto;//asigno el contexto que contiene lo que necesito apra que el progama funcione
    	configurarTabla();//llamamos al metodo que configura la tabla donde se mostraran los viajes disponibles
    	cargarVehiculos();//cargamos ala combobox de vehiculos lso vehiculos que le pertenecen al usuario actual/logeado
    	mostrarServiciosDelVehiculo();//aignamos a los CheckBox lso servicios del vehiculo actual del usuario
    	cargarViajesPendientes();//llamamos a este metodo para que cargue la tabla con los viajes pendientes
    	mostrarEstado();//cargamso el estado actual del conductor
    	cargarMapa();
    	configurarSeleccion();
    	cargarCalificaciones();
	}
    
    /**
     * Carga en la tabla los viajes pendientes del sistema.
     */
    private void cargarViajesPendientes() {//se utiliza para cargar en la tabla los viajes pendientes o disponibles
    	tblAlternativas.getItems().clear();//limiamos los items de la tabla
    	for (Viaje v : contexto.getSistema().getViajesPendientes()) {//exploramso todos los viajes pendientes que se guardarond e manera de compilacion en el porgrama
    		tblAlternativas.getItems().add(new FilaViaje(v));//se crea una fila apartir de ese viaje pendiente y se añade como un item de la tabla
    	}
    }
    	
    /**
     * Indica de qué atributo de {@link FilaViaje} sale el contenido de cada columna de la tabla.
     */
    private void configurarTabla() {
    	colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
		colKilometro.setCellValueFactory(new PropertyValueFactory<>("kilometro"));
    }
    
    /**
     * Carga en el selector los vehículos del conductor y marca el vehículo actual.
     */
    private void cargarVehiculos() {//se usa para cargar en el combobox lso vehiculos del usuario añadiendolos como items
    	cmbVehiculos.getItems().addAll(contexto.getUsuarioActual().getConductor().getVehiculos());
    	cmbVehiculos.setConverter(new ConversorVehiculo());//pasamos en si los items de manera convertida como si es un auto o moto con la calse conversor vehiculo
    	cmbVehiculos.setValue(contexto.getVehiculoActual()); // deja marcado el actual vehiculo
    }
    
    /**
     * Marca las casillas de los servicios (envíos y pasajeros) que presta el vehículo actual.
     */
    private void mostrarServiciosDelVehiculo() {
    	Vehiculo vehiculo = contexto.getVehiculoActual();
    	if (vehiculo == null) {
    		return;
    	}
    	chkEnvios.setSelected(vehiculo.getTipoServicios().contains(TipoServicio.ENVIOS));
    	chkPasajeros.setSelected(vehiculo.getTipoServicios().contains(TipoServicio.PASAJEROS));
    }
    
    /**
     * Pasa a la pantalla de registro de un vehículo nuevo.
     *
     * @param event evento del botón de agregar vehículo
     */
    @FXML
    void onAgregarVehiculo(ActionEvent event) {
    	navegador.irA("/vistas/Vehiculo.fxml");
    }
    /**
     * Cambia el vehículo activo del conductor por el elegido en el selector. Si no se puede
     * (por ejemplo, porque no está fuera de servicio) muestra el error y deja el vehículo anterior.
     *
     * @param event evento del selector de vehículos
     */
    @FXML
    void onCambiarVehiculo(ActionEvent event) {
        Vehiculo elegido = cmbVehiculos.getValue();
        if (elegido == null || elegido == contexto.getVehiculoActual()) {
            return;
        }
        mensajel.setText("");
        try {
            contexto.getUsuarioActual().getConductor().setVehiculoActivo(elegido);
            contexto.setVehiculoActual(elegido);
            mostrarServiciosDelVehiculo();
        } catch (IllegalStateException | IllegalArgumentException e) {
            mensajel.setText(e.getMessage());
            cmbVehiculos.setValue(contexto.getVehiculoActual());
        }
    }

    /**
     * Alterna el estado del conductor entre disponible y fuera de servicio. No se puede cambiar mientras está en viaje.
     *
     * @param event evento del botón de estado
     */
    @FXML
    void onEstadoConductor(ActionEvent event) {
    	mensajel.setText("");
    	Conductor conductor = contexto.getUsuarioActual().getConductor();
    	EstadoConductor actual = conductor.getEstadoConductor();

    	if (actual == EstadoConductor.VIAJE_A_ORIGEN || actual == EstadoConductor.VIAJE_A_DESTINO) {
    		mensajel.setText(I18N.texto("viajes.error.cambiarEstado"));
    		return;
    	}
    	try {
    		if (actual == EstadoConductor.DISPONIBLE) {
    			conductor.setEstado(EstadoConductor.FUERA_DE_SERVICIO);
    		} else {
    			conductor.ponerDisponible(contexto.getSistema().getGeneradorUbicacion());
    		}
    	} catch (IllegalStateException | IllegalArgumentException e) {
    		mensajel.setText(e.getMessage());
    	}
    	mostrarEstado();
    }
    
    /**
     * Muestra en pantalla el estado actual del conductor.
     */
    private void mostrarEstado() {
    	EstadoConductor estado = contexto.getUsuarioActual().getConductor().getEstadoConductor();
    	if (estado == EstadoConductor.DISPONIBLE) {
    		lblEstado.setText(I18N.texto("viajes.estado.disponible"));
    	} else if (estado == EstadoConductor.FUERA_DE_SERVICIO) {
    		lblEstado.setText("viajes.estado.fueraServicio");
    	} else {
    		lblEstado.setText(I18N.texto("viajes.estado.enViaje"));
    	}
    }

    /**
     * Cierra la sesión del conductor, dejándolo fuera de servicio, y vuelve a la pantalla inicial.
     * No se puede salir mientras está en viaje.
     *
     * @param event evento del botón de salir
     */
    @FXML
    void onSalir(ActionEvent event) {
    	Conductor conductor = contexto.getUsuarioActual().getConductor();
    	EstadoConductor estado = conductor.getEstadoConductor();
    	if (estado == EstadoConductor.VIAJE_A_ORIGEN || estado == EstadoConductor.VIAJE_A_DESTINO) {
    		mensajel.setText(I18N.texto("viajes.error.salir"));
    		return;
    	}
    	conductor.setEstado(EstadoConductor.FUERA_DE_SERVICIO);
    	contexto.setUsuarioActual(null);
    	contexto.setVehiculoActual(null);
    	navegador.irA("/vistas/SesionUsuario.fxml");
    }

    /**
     * Agrega o quita el servicio de envíos del vehículo actual según la casilla.
     *
     * @param event evento de la casilla de envíos
     */
    @FXML
    void onServicioEnvios(ActionEvent event) {
    	cambiarServicio(TipoServicio.ENVIOS, chkEnvios, chkPasajeros);
    }

    /**
     * Agrega o quita el servicio de pasajeros del vehículo actual según la casilla.
     *
     * @param event evento de la casilla de pasajeros
     */
    @FXML
    void onServicioPasajeros(ActionEvent event) {
    	cambiarServicio(TipoServicio.PASAJEROS, chkPasajeros, chkEnvios);
    }

    
    /**
     * Agrega o quita un tipo de servicio del vehículo actual. El vehículo no puede quedar sin ningún servicio.
     *
     * @param tipo tipo de servicio a cambiar
     * @param este casilla del servicio que se cambió
     * @param otro casilla del otro servicio
     */
    private void cambiarServicio(TipoServicio tipo, CheckBox este, CheckBox otro) {
    	mensajel.setText("");
    	Vehiculo vehiculo = contexto.getVehiculoActual();
    	if (vehiculo == null) {
    		return;
    	}

    	if (este.isSelected()) {
    		vehiculo.agregarTipoServicio(tipo);
    	} else {
    		if (!otro.isSelected()) { // no puede quedar sin ninguno
    			este.setSelected(true);
    			mensajel.setText(I18N.texto("viajes.error.sinServicioActivo"));
    			return;
    		}
    		vehiculo.quitarTipoServicio(tipo);;
    	}
    }
    
    /**
     * Acepta el viaje seleccionado en la tabla con el conductor actual y lo quita de los pendientes.
     * Muestra un error si no hay viaje elegido o el conductor no puede aceptarlo.
     *
     * @param event evento del botón de aceptar viaje
     */
    @FXML
    void onAceptarViaje(ActionEvent event) {
    	mensajel.setText("");
    	FilaViaje fila = tblAlternativas.getSelectionModel().getSelectedItem();
    	if (fila == null) {
    		mensajel.setText(I18N.texto("viajes.error.elegirViaje"));
    		return;
    	}
    	try {
    		fila.getViaje().aceptar(LocalDateTime.now(), contexto.getUsuarioActual());
    		contexto.getSistema().removerViajePendiente(fila.getViaje());
    		cargarViajesPendientes();
    	} catch (IllegalStateException | IllegalArgumentException e) {
    		mensajel.setText(e.getMessage());
    	}
    }
    
    
    
    
    
    /**
     * Carga el mapa en la vista web y registra cuándo está listo.
     */
    private void cargarMapa() {
        motor = mapaWeb.getEngine();
        motor.setUserAgent("CotizadorViajes/1.0");
        motor.getLoadWorker().stateProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo == Worker.State.SUCCEEDED) {
                mapaListo = true;
            }
        });
        URL url = getClass().getResource("/vistas/mapa.html");
        motor.load(url.toExternalForm());
    }

    /**
     * Hace que al seleccionar un viaje en la tabla se muestre su recorrido en el mapa, y que se limpie al deseleccionarlo.
     */
    private void configurarSeleccion() {
        tblAlternativas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, fila) -> {
            if (!mapaListo) {
                return;
            }
            if (fila == null) {
                motor.executeScript("limpiarViaje()");
            } else {
                mostrarEnMapa(fila.getViaje());
            }
        });
    }

    /**
     * Dibuja en el mapa el origen y el destino de un viaje.
     *
     * @param viaje viaje a mostrar
     */
    private void mostrarEnMapa(Viaje viaje) {
        Ubicacion o = viaje.getOrigen();
        Ubicacion d = viaje.getDestino();
        String js = String.format(Locale.US, "mostrarViaje(%.6f, %.6f, %.6f, %.6f)",
                o.getLatitud(), o.getLongitud(), d.getLatitud(), d.getLongitud());
        motor.executeScript(js);
    }
    
    
    /** Busca el viaje aceptado o iniciado del conductor actual. */
    private Viaje viajeActivo() {
        for (Viaje v : contexto.getUsuarioActual().getConductor().getViajes()) {
            EstadoViaje e = v.estadoActual();
            if (e == EstadoViaje.ACEPTADO || e == EstadoViaje.INICIADO) {
                return v;
            }
        }
        return null;
    }
    
    @FXML
    void oniniciarViaje(ActionEvent event) {
    	 mensajel.setText("");
    	    Viaje viaje = viajeActivo();
    	    if (viaje == null) {
    	        mensajel.setText("No hay un viaje aceptado para iniciar");
    	        return;
    	    }
    	    try {
    	        viaje.iniciar(LocalDateTime.now());
    	        mostrarEstado();
    	    } catch (IllegalStateException e) {
    	        mensajel.setText(e.getMessage());
    	    }
    }

    @FXML
    void ontermianrViaje(ActionEvent event) {
    	 mensajel.setText("");
    	    Viaje viaje = viajeActivo();
    	    if (viaje == null) {
    	        mensajel.setText("No hay un viaje iniciado para finalizar");
    	        return;
    	    }
    	    try {
    	        CalificacionViaje nota = cmbCalificacion.getValue();
    	        if (nota == null) {
    	            nota = CalificacionViaje.NO_CALIFICADO;
    	        }
    	        viaje.finalizar(LocalDateTime.now(), nota, CalificacionViaje.NO_CALIFICADO);
    	        cmbCalificacion.setValue(null);
    	        mostrarEstado();
    	    } catch (IllegalStateException e) {
    	        mensajel.setText(e.getMessage());
    	    }
    }
    
    
    private void cargarCalificaciones() {
        for (CalificacionViaje c : CalificacionViaje.values()) {
            if (c != CalificacionViaje.NO_CALIFICADO) {
                cmbCalificacion.getItems().add(c);
            }
        }
    }

    

}
