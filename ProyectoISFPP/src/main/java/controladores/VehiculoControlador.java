package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    @FXML
    private Label mensajel;

    @Override
	public void init(Navegador navegador, Contexto contexto) {
    	this.navegador = navegador;
		this.contexto = contexto;
		this.sistema = contexto.getSistema();
		cargarCombobox();
	}
    
    private void cargarCombobox() {
    	cmbTipoServicio.getItems().addAll(TipoServicio.values());
    	cmbCategoria.getItems().addAll(CategoriaVehiculo.values());
    	cmbTipoVehiculo.getItems().addAll(TipoVehiculo.values());
    }
    
    @FXML
    void OnVolver(ActionEvent event) {
    	if(esRegistroNuevo()) {
    	contexto.limpiarRegistroPendiente();
    	navegador.irA("/vistas/Conductor.fxml");
    	}else {
        	navegador.irA("/vistas/ConductorViajes.fxml");
    	}

    }
    
    private boolean esRegistroNuevo() {
		return contexto.getUsuarioPendiente() != null;
	}

    @FXML
    void onFinalizarRegistro(ActionEvent event) {
    	mensajel.setText("");
		try {
			if (cmbTipoVehiculo.getValue() == null || cmbCategoria.getValue() == null
					|| cmbTipoServicio.getValue() == null) {
				mensajel.setText("completá tipo de vehículo, categoría y tipo de servicio");
				return;
			}

			Vehiculo vehiculo = new Vehiculo(TextoPatente.getText().trim(), TextoModelo.getText().trim(),
					leerCapacidad(), cmbTipoVehiculo.getValue(), cmbCategoria.getValue(),
					cmbTipoServicio.getValue());

			if (esRegistroNuevo()) {
				// conductor nuevo: se crea con su licencia y este vehículo
				Usuario nuevo = contexto.getUsuarioPendiente();
				nuevo.altaConductor(contexto.getLicenciaPendiente(), vehiculo);
				sistema.registrarUsuario(nuevo);

				contexto.setUsuarioActual(nuevo);
				contexto.setVehiculoActual(vehiculo);
				contexto.limpiarRegistroPendiente();
			} else {
				// conductor existente: se le suma el vehículo
				sistema.agregarVehiculoAConductor(contexto.getUsuarioActual(), vehiculo);
			}

			navegador.irA("/vistas/ConductorViajes.fxml");

		} catch (IllegalArgumentException | UnsupportedOperationException e) {
			mensajel.setText(e.getMessage());
		}
    }
    
    private int leerCapacidad() {
		try {
			return Integer.parseInt(TextoCapacidad.getText().trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("la capacidad tiene que ser un número entero");
		}
	}
}
