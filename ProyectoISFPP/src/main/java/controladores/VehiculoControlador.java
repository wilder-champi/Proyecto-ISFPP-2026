package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import internacionalizacion.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.SistemaGuardado;
import modelo.Usuario;
import modelo.Vehiculo;

/**
 * Controlador de la pantalla de registro de vehículos. Se usa tanto al registrar un conductor nuevo
 * como al agregar un vehículo a un conductor existente.
 */
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

	/**
	 * Guarda los datos compartidos y carga las opciones de los selectores.
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
    @Override
	public void init(Navegador navegador, Contexto contexto) {
    	this.navegador = navegador;
		this.contexto = contexto;
		this.sistema = contexto.getSistema();
		cargarCombobox();
	}
    
    /**
     * Carga en los selectores los tipos de servicio, las categorías y los tipos de vehículo.
     */
    private void cargarCombobox() {
    	cmbTipoServicio.getItems().addAll(TipoServicio.values());
    	cmbCategoria.getItems().addAll(CategoriaVehiculo.values());
    	cmbTipoVehiculo.getItems().addAll(TipoVehiculo.values());
    }
    
    /**
     * Vuelve a la pantalla anterior: al registro de conductor si el registro es nuevo,
     * o a la pantalla de viajes del conductor si no lo es.
     *
     * @param event evento del botón de volver
     */
    @FXML
    void OnVolver(ActionEvent event) {
    	if(esRegistroNuevo()) {
    	contexto.limpiarRegistroPendiente();
    	navegador.irA("/vistas/Conductor.fxml");
    	}else {
        	navegador.irA("/vistas/ConductorViajes.fxml");
    	}

    }
    
    /**
     * Indica si se está registrando un conductor nuevo.
     *
     * @return {@code true} si hay un usuario pendiente de registro
     */
    private boolean esRegistroNuevo() {
		return contexto.getUsuarioPendiente() != null;
	}

    /**
     * Crea el vehículo con los datos ingresados y lo registra: junto con el conductor nuevo, o agregado
     * a un conductor existente. Muestra un error si falta alguna selección o los datos no son válidos.
     *
     * @param event evento del botón de finalizar registro
     */
    @FXML
    void onFinalizarRegistro(ActionEvent event) {
    	mensajel.setText("");
		try {
			if (cmbTipoVehiculo.getValue() == null || cmbCategoria.getValue() == null
					|| cmbTipoServicio.getValue() == null) {
				mensajel.setText(I18N.texto("vehiculo.error.seleccion"));
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
    
    /**
     * Lee la capacidad de pasajeros escrita en pantalla.
     *
     * @return la capacidad como número entero
     * @throws IllegalArgumentException si el texto no es un número
     */
    private int leerCapacidad() {
		try {
			return Integer.parseInt(TextoCapacidad.getText().trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(I18N.texto("vehiculo.error.capacidad"));
		}
	}
}
