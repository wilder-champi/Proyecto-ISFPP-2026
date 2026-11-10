package controladores;

import javafx.util.StringConverter;
import modelo.Vehiculo;

public class ConversorVehiculo extends StringConverter<Vehiculo> {

	@Override
	public String toString(Vehiculo Vehiculo) {
		 
		if (Vehiculo == null) {
			return "" ;
		}
		return Vehiculo.getClass().getSimpleName();// devuelve si es una moto o un auto p
	}

	@Override
	public Vehiculo fromString(String string) {
		return null;
	}

	
}
