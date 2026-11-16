package logica;

import java.util.List;

import modelo.CategoriaVehiculo;
import modelo.TipoServicio;
import modelo.TipoVehiculo;
import modelo.Vehiculo;

public final class ValidadorVehiculo {

	public static final int CAPACIDAD_MAX_MOTO = 1;
	public static final int CAPACIDAD_MAX_AUTO = 4;

	private ValidadorVehiculo() {
	}

	// Valida los datos, limpia la patente y el modelo y devuelve el vehiculo ya armado
	public static Vehiculo crear(String patente, String modelo, int capacidad, TipoVehiculo tipo,
			CategoriaVehiculo categoria, List<TipoServicio> servicios) {

		if (patente == null || patente.isBlank()) {
			throw new IllegalArgumentException("la patente es obligatoria");
		}
		String patenteLimpia = patente.trim().toUpperCase();
		if (patenteLimpia.length() < 6 || patenteLimpia.length() > 7) {
			throw new IllegalArgumentException("la patente debe tener 6 o 7 caracteres");
		}
		for (int i = 0; i < patenteLimpia.length(); i++) {
			char c = patenteLimpia.charAt(i);
			boolean esLetra = c >= 'A' && c <= 'Z';
			boolean esNumero = c >= '0' && c <= '9';
			if (!esLetra && !esNumero) {
				throw new IllegalArgumentException("la patente solo puede tener letras y numeros");
			}
		}
		if (modelo == null || modelo.isBlank()) {
			throw new IllegalArgumentException("el modelo es obligatorio");
		}
		if (tipo == null) {
			throw new IllegalArgumentException("el tipo de vehiculo es obligatorio");
		}
		if (categoria == null) {
			throw new IllegalArgumentException("la categoria es obligatoria");
		}
		if (servicios == null || servicios.isEmpty()) {
			throw new IllegalArgumentException("el vehiculo debe tener al menos un tipo de servicio");
		}
		for (int i = 0; i < servicios.size(); i++) {
			if (servicios.get(i) == null) {
				throw new IllegalArgumentException("los tipos de servicio no pueden ser null");
			}
		}
		if (capacidad < 1) {
			throw new IllegalArgumentException("la capacidad de pasajeros debe ser al menos 1");
		}
		if (tipo == TipoVehiculo.MOTO && capacidad > CAPACIDAD_MAX_MOTO) {
			throw new IllegalArgumentException("una moto admite como maximo " + CAPACIDAD_MAX_MOTO + " pasajero");
		}
		if (tipo == TipoVehiculo.AUTO && capacidad > CAPACIDAD_MAX_AUTO) {
			throw new IllegalArgumentException("un auto admite como maximo " + CAPACIDAD_MAX_AUTO + " pasajeros");
		}

		Vehiculo vehiculo = new Vehiculo(patenteLimpia, modelo.trim(), capacidad, tipo, categoria, servicios.get(0));
		for (int i = 1; i < servicios.size(); i++) {
			vehiculo.agregarTipoServicio(servicios.get(i));
		}
		return vehiculo;
	}
}