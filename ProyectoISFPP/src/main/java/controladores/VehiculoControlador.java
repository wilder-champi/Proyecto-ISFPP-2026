package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;
import modelo.Vehiculo;

public class VehiculoControlador implements Controlador {

	private Navegador navegador;
	private SistemaGuardado sistema;
	private Contexto contexto;
	
    @FXML
    private TextField TextoCapacidad;

    @FXML
    private TextField TextoModelo;

    @FXML
    private TextField TextoPatente;

    @FXML
    private ComboBox<TipoServicio> cmbTipoServicio;

    @FXML
    private ComboBox<CategoriaVehiculo> cmbCategoria;

    @FXML
    private ComboBox<TipoVehiculo> cmbTipoVehiculo;

    @Override
	public void init(Navegador navegador, Contexto contexto) {
    	this.navegador = navegador;
		this.contexto = contexto;
		inicializar();
	}
    
    private void inicializar() {
    	cargarCombobox();
    }
    
    private void cargarCombobox() {
    	cmbTipoServicio.getItems().addAll(TipoServicio.values());
    	cmbCategoria.getItems().addAll(CategoriaVehiculo.values());
    	cmbTipoVehiculo.getItems().addAll(TipoVehiculo.values());
    }
    
    @FXML
    void OnVolver(ActionEvent event) {

    }

    @FXML
    void onFinalizarRegistro(ActionEvent event) {
    	
    	try {
    		Vehiculo vehiculo = new Vehiculo(TextoPatente.getText(),TextoModelo.getText(),Integer.parseInt(TextoCapacidad.getText().trim()),
        			cmbTipoVehiculo.getValue(),cmbCategoria.getValue(),cmbTipoServicio.getValue());
    		contexto.setVehiculoPendiente(vehiculo);
    		Conductor conductor = contexto.getUsuarioPendiente();
    		usuario.getConductor().setLicenciaPendiente();
    		
    	} catch (IllegalArgumentException | UnsupportedOperationException e) {
    			
    	}
    	
    }

    @FXML
    void onTipoServicio(ActionEvent event) {
    	TipoServicio nombres = cmbTipoServicio.getValue();

    }

    private TipoServicio tipoServicio() {
    	
    	
    	TipoServicio nombres = cmbTipoServicio.getValue();
    	
    	if (nombres.equals(TipoServicio.ENVIOS)) {
    		return nombres;
    		
    	} else if (nombres.equals(TipoServicio.PASAJEROS)) {
    		return nombres;
    		
    	} else {
    		
    	}
    	return null;
    }

}
