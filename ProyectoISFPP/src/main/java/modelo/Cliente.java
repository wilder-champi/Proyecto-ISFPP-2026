package modelo;

import java.util.ArrayList;
import java.util.List;

import enums.EstadoViaje;

/**
 * Representa el rol de cliente de un usuario y guarda su historial de viajes.
 */
public class Cliente {

	private List<Viaje> viajes;

	/**
	 * Crea un cliente con su lista de viajes vacía.
	 */
	public Cliente() {
		viajes = new ArrayList<>();
	}

	/**
	 * Agrega un viaje al historial del cliente si todavía no estaba cargado.
	 *
	 * @param viaje viaje a agregar
	 */
	public void agregarViaje(Viaje viaje) {
		if (!viajes.contains(viaje)) {
			viajes.add(viaje);
		}
	}

	/**
	 * Indica si el cliente tiene un viaje activo (solicitado, aceptado o iniciado).
	 *
	 * @return {@code true} si tiene algún viaje activo, {@code false} en caso contrario
	 */
	public boolean enViaje() {
		for (Viaje viaje : viajes) {
			EstadoViaje estado = viaje.estadoActual();
			if (estado == EstadoViaje.SOLICITADO || estado == EstadoViaje.ACEPTADO || estado == EstadoViaje.INICIADO) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve una copia del historial de viajes del cliente.
	 *
	 * @return lista con los viajes del cliente
	 */
	public List<Viaje> getViajes() {
		return new ArrayList<>(viajes);
	}
}