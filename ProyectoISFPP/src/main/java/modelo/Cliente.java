package modelo;

import java.util.ArrayList;
import java.util.List;

import enums.EstadoViaje;

public class Cliente {

	private List<Viaje> viajes;

	public Cliente() {
		viajes = new ArrayList<>();
	}

	public void agregarViaje(Viaje viaje) {
		if (!viajes.contains(viaje)) {
			viajes.add(viaje);
		}
	}

	public boolean enViaje() {
		for (Viaje viaje : viajes) {
			EstadoViaje estado = viaje.estadoActual();
			if (estado == EstadoViaje.SOLICITADO || estado == EstadoViaje.ACEPTADO || estado == EstadoViaje.INICIADO) {
				return true;
			}
		}
		return false;
	}

	public List<Viaje> getViajes() {
		return new ArrayList<>(viajes);
	}
}