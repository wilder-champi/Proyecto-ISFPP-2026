package controladores;

import javafx.util.StringConverter;
import modelo.Vehiculo;

/**
 * Conversor que muestra un vehículo en un {@code ComboBox} usando su tipo (AUTO, MOTO).
 */
public class ConversorVehiculo extends StringConverter<Vehiculo> {

	/**
	 * Convierte un vehículo en el texto que se muestra.
	 *
	 * @param vehiculo vehículo a mostrar
	 * @return el tipo de vehículo, o un texto vacío si el vehículo es {@code null}
	 */
	@Override
	public String toString(Vehiculo vehiculo) {
		if (vehiculo == null) {
			return "";
		}
		return vehiculo.getTipoVehiculo().toString(); // MOTO, AUTO...
	}

	/**
	 * No se usa: la conversión de texto a vehículo no está soportada.
	 *
	 * @param string texto ingresado
	 * @return siempre {@code null}
	 */
	@Override
	public Vehiculo fromString(String string) {
		return null;
	}

	
}
