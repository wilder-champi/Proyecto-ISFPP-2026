package modelo;
import enums.EstadoViaje;

import java.time.LocalDateTime;

/**
 * Registro de un cambio de estado de un viaje: guarda el estado y la fecha y hora en que ocurrió.
 */
public class RegistroViaje {
	private LocalDateTime FechaHora;
	private EstadoViaje estadoViaje;
	
	/**
	 * Crea un registro de estado de un viaje.
	 *
	 * @param fechaHora fecha y hora del cambio de estado
	 * @param estadoViaje estado en el que quedó el viaje
	 */
	public RegistroViaje(LocalDateTime fechaHora, EstadoViaje estadoViaje) {
		super();
		this.FechaHora = fechaHora;
		this.estadoViaje = estadoViaje;
	}

	/**
	 * Devuelve la fecha y hora del registro.
	 *
	 * @return fecha y hora del cambio de estado
	 */
	public LocalDateTime getFechaHora() {
		return FechaHora;
	}

	/**
	 * Establece la fecha y hora del registro.
	 *
	 * @param fechaHora fecha y hora del cambio de estado
	 */
	public void setFechaHora(LocalDateTime fechaHora) {
		FechaHora = fechaHora;
	}

	/**
	 * Devuelve el estado registrado.
	 *
	 * @return estado del viaje
	 */
	public EstadoViaje getEstadoViaje() {
		return estadoViaje;
	}

	/**
	 * Establece el estado registrado.
	 *
	 * @param estadoViaje estado del viaje
	 */
	public void setEstadoViaje(EstadoViaje estadoViaje) {
		this.estadoViaje = estadoViaje;
	}

	/**
	 * Devuelve una descripción del registro con su fecha y su estado.
	 *
	 * @return texto con los datos del registro
	 */
	@Override
	public String toString() {
		return "RegistroViaje [FechaHora=" + FechaHora + ", estadoViaje=" + estadoViaje + "]";
	}

}
