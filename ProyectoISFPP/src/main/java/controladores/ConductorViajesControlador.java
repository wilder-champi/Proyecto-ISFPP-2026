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

    
    @Override
	public void init(Navegador navegador, Contexto contexto) {
    	this.navegador = navegador;
    	this.contexto = contexto;
    	configurarTabla();
    	cargarVehiculos();
    	mostrarServiciosDelVehiculo();
    	cargarViajesPendientes();
    	mostrarEstado();
	}
    private void cargarViajesPendientes() {
    	tblAlternativas.getItems().clear();
    	for (Viaje v : contexto.getSistema().getViajesPendientes()) {
    		tblAlternativas.getItems().add(new FilaViaje(v));
    	}
    }
    	
    private void configurarTabla() {
    	colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
		colKilometro.setCellValueFactory(new PropertyValueFactory<>("kilometro"));
    }
    
    private void cargarVehiculos() {
    	cmbVehiculos.getItems().addAll(contexto.getUsuarioActual().getConductor().getVehiculos());
    	cmbVehiculos.setConverter(new ConversorVehiculo());
    	cmbVehiculos.setValue(contexto.getVehiculoActual()); // deja marcado el actual
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
    	mensajel.setText("");
    	Vehiculo elegido = cmbVehiculos.getValue();
    	if (elegido == null || elegido == contexto.getVehiculoActual()) {
    		return; // nada que cambiar
    	}
    	try {
    		contexto.getUsuarioActual().getConductor().setVehiculoActivo(elegido);
    		contexto.setVehiculoActual(elegido);
    		mostrarServiciosDelVehiculo();
    	} catch (IllegalStateException | IllegalArgumentException e) {
    		mensajel.setText(e.getMessage()); // "debe estar fuera de servicio para cambiar de vehículo"
    		cmbVehiculos.setValue(contexto.getVehiculoActual()); // el combo vuelve al anterior
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
    
    
    
    

}
