package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import logica.Cotizador;
import modelo.Vehiculo;

public class ConductorViajesControlador implements Controlador {

	private Navegador navegador;
	private Contexto contexto;
	private Cotizador cotizador;
	
    @FXML
    private ComboBox<Vehiculo> cmbVehiculos;

    @FXML
    private TableColumn<FilaAlternativa,String> colKilometro;

    @FXML
    private TableColumn<FilaAlternativa,String> colPrecio;

    @FXML
    private TableColumn<FilaAlternativa,String> colServicio;

    @FXML
    private TableColumn<FilaAlternativa,String> colTiempo;
    
    @FXML
    private TableView<FilaAlternativa> tblAlternativas;

    
    @Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
		this.contexto = contexto;
		this.cotizador = new Cotizador(contexto.getSistema().getServicios());
		cargarVehiculos();
	}
    
    private void inicializar() {
    	configurarTabla();
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
    }
    
    @FXML
    void onAgregarVehiculo(ActionEvent event) {

    }

    @FXML
    void onCambiarVehiculo(ActionEvent event) {
    	Vehiculo elegido = cmbVehiculos.getValue();
    	if (elegido == null) {
    		return;
    	}
    	
    	contexto.setVehiculoActual(elegido);
    }

    @FXML
    void onEstadoConductor(ActionEvent event) {
    	
    }

    @FXML
    void onSalir(ActionEvent event) {

    }

    @FXML
    void onServicioEnvios(ActionEvent event) {

    }

    @FXML
    void onServicioPasajeros(ActionEvent event) {

    }

    @FXML
    void onmensaje(MouseEvent event) {

    }

}
