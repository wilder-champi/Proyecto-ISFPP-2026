package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

	private List<Viaje> viajes;

	public Cliente() {
		viajes = new ArrayList<Viaje>();
	}

	public boolean enViaje() {
		if (viajes.isEmpty())
			return false;

		Viaje ultimoViaje = viajes.get(viajes.size() - 1);
		EstadoViaje estado = ultimoViaje.estadoActual();

		return estado == EstadoViaje.SOLICITADO || estado == EstadoViaje.ACEPTADO || estado == EstadoViaje.INICIADO;
	}

	public void agregarViaje(Viaje viaje) {
		viajes.add(viaje);
	}

	public List<Viaje> getViajes() {
		return new ArrayList<Viaje>(viajes);
	}

}
