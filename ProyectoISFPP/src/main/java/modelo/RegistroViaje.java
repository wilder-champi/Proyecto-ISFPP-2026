package modelo;
import enums.EstadoViaje;

import java.time.LocalDateTime;

public class RegistroViaje {
	private LocalDateTime FechaHora;
	private EstadoViaje estadoViaje;
	
	public RegistroViaje(LocalDateTime fechaHora, EstadoViaje estadoViaje) {
		super();
		this.FechaHora = fechaHora;
		this.estadoViaje = estadoViaje;
	}

	public LocalDateTime getFechaHora() {
		return FechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		FechaHora = fechaHora;
	}

	public EstadoViaje getEstadoViaje() {
		return estadoViaje;
	}

	public void setEstadoViaje(EstadoViaje estadoViaje) {
		this.estadoViaje = estadoViaje;
	}

	@Override
	public String toString() {
		return "RegistroViaje [FechaHora=" + FechaHora + ", estadoViaje=" + estadoViaje + "]";
	}

}
