package controladores;

import java.time.LocalDateTime;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.EstadoConductor;
import enums.TipoServicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logica.Cotizador;
import modelo.Conductor;
import modelo.Vehiculo;
import modelo.Viaje;

import java.net.URL;
import java.util.Locale;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import modelo.Ubicacion;

public class ConductorViajesControlador implements Controlador {

	private Navegador navegador;
	private Contexto contexto;
	private Cotizador cotizador;
	
    @FXML
    private ComboBox<Vehiculo> cmbVehiculos;

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
	}
    
    private void cargarViajesPendientes() {//se utiliza para cargar en la tabla los viajes pendientes o disponibles
    	tblAlternativas.getItems().clear();//limiamos los items de la tabla
    	for (Viaje v : contexto.getSistema().getViajesPendientes()) {//exploramso todos los viajes pendientes que se guardarond e manera de compilacion en el porgrama
    		tblAlternativas.getItems().add(new FilaViaje(v));//se crea una fila apartir de ese viaje pendiente y se añade como un item de la tabla
    	}
    }
    	
    private void configurarTabla() {
    	colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
		colKilometro.setCellValueFactory(new PropertyValueFactory<>("kilometro"));
    }
    
    private void cargarVehiculos() {//se usa para cargar en el combobox lso vehiculos del usuario añadiendolos como items
    	cmbVehiculos.getItems().addAll(contexto.getUsuarioActual().getConductor().getVehiculos());
    	cmbVehiculos.setConverter(new ConversorVehiculo());//pasamos en si los items de manera convertida como si es un auto o moto con la calse conversor vehiculo
    	cmbVehiculos.setValue(contexto.getVehiculoActual()); // deja marcado el actual vehiculo
    }
    
    private void mostrarServiciosDelVehiculo() {
    	Vehiculo vehiculo = contexto.getVehiculoActual();
    	if (vehiculo == null) {
    		return;
    	}
    	chkEnvios.setSelected(vehiculo.getTipoServicios().contains(TipoServicio.ENVIOS));
    	chkPasajeros.setSelected(vehiculo.getTipoServicios().contains(TipoServicio.PASAJEROS));
    }
    
    @FXML
    void onAgregarVehiculo(ActionEvent event) {
    	navegador.irA("/vistas/Vehiculo.fxml");
    }
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

    @FXML
    void onEstadoConductor(ActionEvent event) {
    	mensajel.setText("");
    	Conductor conductor = contexto.getUsuarioActual().getConductor();
    	EstadoConductor actual = conductor.getEstadoConductor();

    	if (actual == EstadoConductor.VIAJE_A_ORIGEN || actual == EstadoConductor.VIAJE_A_DESTINO) {
    		mensajel.setText("no podés cambiar tu estado durante un viaje");
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
    
    private void mostrarEstado() {
    	EstadoConductor estado = contexto.getUsuarioActual().getConductor().getEstadoConductor();
    	if (estado == EstadoConductor.DISPONIBLE) {
    		lblEstado.setText("Disponible");
    	} else if (estado == EstadoConductor.FUERA_DE_SERVICIO) {
    		lblEstado.setText("Fuera de servicio");
    	} else {
    		lblEstado.setText("En viaje");
    	}
    }

    @FXML
    void onSalir(ActionEvent event) {
    	Conductor conductor = contexto.getUsuarioActual().getConductor();
    	EstadoConductor estado = conductor.getEstadoConductor();
    	if (estado == EstadoConductor.VIAJE_A_ORIGEN || estado == EstadoConductor.VIAJE_A_DESTINO) {
    		mensajel.setText("no podés salir durante un viaje");
    		return;
    	}
    	conductor.setEstado(EstadoConductor.FUERA_DE_SERVICIO);
    	contexto.setUsuarioActual(null);
    	contexto.setVehiculoActual(null);
    	navegador.irA("/vistas/SesionUsuario.fxml");
    }

    @FXML
    void onServicioEnvios(ActionEvent event) {
    	cambiarServicio(TipoServicio.ENVIOS, chkEnvios, chkPasajeros);
    }

    @FXML
    void onServicioPasajeros(ActionEvent event) {
    	cambiarServicio(TipoServicio.PASAJEROS, chkPasajeros, chkEnvios);
    }

    
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
    			mensajel.setText("tiene que quedar al menos un tipo de servicio activo");
    			return;
    		}
    		vehiculo.quitarTipoServicio(tipo);;
    	}
    }
    
    @FXML
    void onAceptarViaje(ActionEvent event) {
    	mensajel.setText("");
    	FilaViaje fila = tblAlternativas.getSelectionModel().getSelectedItem();
    	if (fila == null) {
    		mensajel.setText("elegí un viaje de la tabla");
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

    private void mostrarEnMapa(Viaje viaje) {
        Ubicacion o = viaje.getOrigen();
        Ubicacion d = viaje.getDestino();
        String js = String.format(Locale.US, "mostrarViaje(%.6f, %.6f, %.6f, %.6f)",
                o.getLatitud(), o.getLongitud(), d.getLatitud(), d.getLongitud());
        motor.executeScript(js);
    }
    
    
    
    

}
