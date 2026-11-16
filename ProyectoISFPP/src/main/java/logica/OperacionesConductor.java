package logica;

import modelo.CategoriaVehiculo;
import modelo.Conductor;
import modelo.EstadoConductor;
import modelo.Vehiculo;

public final class OperacionesConductor {

	private OperacionesConductor() {
	}

	public static void ponerDisponible(Conductor conductor, GeneradorUbicacion generador) {
		if (generador == null) {
			throw new IllegalArgumentException("el generador de ubicaciones no puede ser null");
		}
		if (conductor.getVehiculoActivo() == null) {
			throw new IllegalStateException("no hay un vehiculo activo seleccionado");
		}
		conductor.getVehiculoActivo().setUbicacion(generador.generar());
		conductor.setEstado(EstadoConductor.DISPONIBLE);
	}

	public static void cambiarVehiculo(Conductor conductor, Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new IllegalArgumentException("el vehiculo activo no puede ser null");
		}
		if (!conductor.getVehiculos().contains(vehiculo)) {
			throw new IllegalArgumentException("el vehiculo no pertenece a este conductor");
		}
		if (conductor.getEstadoConductor() != EstadoConductor.FUERA_DE_SERVICIO) {
			throw new IllegalStateException("debe estar fuera de servicio para cambiar de vehiculo");
		}
		conductor.setVehiculoActivo(vehiculo);
		conductor.setCategoriaVehiculoActivo(vehiculo.getCategoriaVehiculo());
	}

	public static void cambiarCategoriaActiva(Conductor conductor, CategoriaVehiculo categoria) {
		if (categoria == null) {
			throw new IllegalArgumentException("la categoria no puede ser null");
		}
		int categoriaVehiculo = conductor.getVehiculoActivo().getCategoriaVehiculo().getValorNumerico();
		if (categoria.getValorNumerico() > categoriaVehiculo) {
			throw new IllegalArgumentException("no se puede trabajar en una categoria superior a la del vehiculo");
		}
		conductor.setCategoriaVehiculoActivo(categoria);
	}
}