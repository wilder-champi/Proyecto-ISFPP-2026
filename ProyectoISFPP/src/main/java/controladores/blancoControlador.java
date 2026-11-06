package controladores;

import java.util.List;

import aplicacion.Contexto;
import aplicacion.Navegador;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import logica.Alternativa;
import logica.Cotizador;
import modelo.Ubicacion;

public class blancoControlador implements Controlador {

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
	private TableView<FilaAlternativa> tblAlternativas;
	@FXML
	private TableColumn<FilaAlternativa, String> colServicio;
	@FXML
	private TableColumn<FilaAlternativa, String> colPrecio;
	@FXML
	private TableColumn<FilaAlternativa, String> colTiempo;

	@FXML
	private Label mensajel;

	@Override
	public void init(Navegador navegador, Contexto contexto) {
		this.navegador = navegador;
		this.cotizador = new Cotizador(contexto.getSistema().getServicios());
		this.contexto = contexto;
	}

	@FXML
	private void initialize() {
		configurarTabla();
		cargarCombos();
	}

	private void configurarTabla() {
		colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
	}

	private void cargarCombos() {
		cmbVehiculo.getItems().addAll(TipoVehiculo.values());
		cmbServicio.getItems().addAll(TipoServicio.values());
		cmbCategoria.getItems().addAll(CategoriaVehiculo.values());
	}

	@FXML
	void onCotizar(ActionEvent event) {
		mensajel.setText("67");
		try {
			Ubicacion origen = new Ubicacion(leer(TextoLatOrigen), leer(TextoLonOrigen));
			Ubicacion destino = new Ubicacion(leer(TextoLatDestino), leer(TextoLonDestino));

			if (cmbServicio.getValue() == null || cmbCategoria.getValue() == null) {
				mensajel.setText("se tiene que elegir el tipo de viaje y categoria");
				return;
			}

			List<Alternativa> alternativas = cotizador.consultar(origen, destino, cmbServicio.getValue(),cmbCategoria.getValue());

			tblAlternativas.getItems().clear();
			
			for (Alternativa a : alternativas) {
				tblAlternativas.getItems().add(new FilaAlternativa(a));
			}
			if (alternativas.isEmpty()) {
				mensajel.setText("no hay servicios para esa combinacion");
			}
		}catch (IllegalArgumentException e) {
			mensajel.setText(e.getMessage());
		}
	}

	private double leer(TextField campo) {
	    try {
	        return Double.parseDouble(campo.getText().trim());
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException(
	            "las cordenadas tienen que ser numeros con punto (ej: -42.67)");
	    }
	}

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

	}

	@FXML
	void onVolver(ActionEvent event) {
		navegador.irA("/vistas/nose.fxml");
	}

}
