package logica;

import java.time.Duration;
import java.time.LocalDateTime;

import modelo.EstadoViaje;
import modelo.RegistroViaje;
import modelo.Servicio;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.Viaje;

public final class OperacionesViaje {

	private OperacionesViaje() {
	}

	// Cancela el viaje y devuelve el costo de cancelacion (0 si no corresponde)
	public static double cancelar(Viaje viaje, Usuario usuario, String motivo, LocalDateTime ahora) {
		if (viaje == null || usuario == null) {
			throw new IllegalArgumentException("el viaje y el usuario son obligatorios");
		}
		boolean esCliente = usuario.equals(viaje.getCliente());
		boolean esConductor = usuario.equals(viaje.getConductor());
		if (!esCliente && !esConductor) {
			throw new IllegalArgumentException("el usuario no pertenece al viaje");
		}
		if (esConductor && (motivo == null || motivo.isBlank())) {
			throw new IllegalArgumentException("Tenés que indicar el motivo de la cancelacion");
		}
		// el costo se calcula antes de cancelar, mientras el viaje sigue aceptado o iniciado
		double costo = 0.0;
		if (esCliente) {
			costo = costoCancelacion(viaje, ahora);
		}
		viaje.cancelar(ahora, usuario, motivo);
		return costo;
	}

	public static double costoCancelacion(Viaje viaje, LocalDateTime ahora) {
		EstadoViaje estado = viaje.estadoActual();
		if (estado != EstadoViaje.ACEPTADO && estado != EstadoViaje.INICIADO) {
			return 0.0;
		}
		LocalDateTime fechaAceptado = null;
		for (RegistroViaje registro : viaje.getRegistroViaje()) {
			if (registro.getEstadoViaje() == EstadoViaje.ACEPTADO) {
				fechaAceptado = registro.getFechaHora();
			}
		}
		if (fechaAceptado == null) {
			return 0.0;
		}
		long minutos = Duration.between(fechaAceptado, ahora).toMinutes();
		double km = 0.0;
		Vehiculo vehiculo = viaje.getVehiculo();
		if (vehiculo != null && vehiculo.getUbicacion() != null) {
			km = vehiculo.getUbicacion().calcularDistancia(viaje.getOrigen());
		}
		Servicio servicio = viaje.getServicio();
		return km * servicio.getPrecioKm() + minutos * servicio.getPrecioMinuto();
	}

	public static void aceptar(Viaje viaje, Usuario conductor, LocalDateTime ahora) {
		if (conductor == null || conductor.getConductor() == null) {
			throw new IllegalStateException("el conductor no puede ser null");
		}
		if (conductor.getEmail().equals(viaje.getCliente().getEmail())) {
			throw new IllegalStateException("el cliente y el conductor no pueden ser la misma persona");
		}
		int categoriaViaje = viaje.getServicio().getCategoriaVehiculo().getValorNumerico();
		int categoriaConductor = conductor.getConductor().getCategoriaVehiculoActivo().getValorNumerico();
		if (categoriaViaje > categoriaConductor) {
			throw new IllegalStateException("el viaje supera la categoria con la que trabaja el conductor");
		}
		viaje.aceptar(ahora, conductor);
	}
}