package controladores;

import javafx.util.StringConverter;
import modelo.Vehiculo;

public class ConversorVehiculo extends StringConverter<Vehiculo> {

	@Override
	public String toString(Vehiculo vehiculo) {
		if (vehiculo == null) {
			return "";
		}
		return vehiculo.getTipoVehiculo().toString(); // MOTO, AUTO...
	}

	@Override
	public Vehiculo fromString(String string) {
		return null;
	}

	
}
