package controladores;

import logica.Cotizador;
import modelo.Viaje;

/**
 * Fila de la tabla de viajes de un conductor: guarda como texto el servicio, el precio, el tiempo
 * y los kilómetros estimados de un viaje.
 */
public class FilaViaje {

	private final Viaje viaje;
	private final String servicio;
	private final String precio;
	private final String tiempo;
	private final String kilometro;

	/**
	 * Crea la fila a partir de un viaje, calculando la distancia, el tiempo y el costo estimados.
	 *
	 * @param viaje viaje que representa la fila
	 */
	public FilaViaje(Viaje viaje) {
		this.viaje = viaje;
		double km = viaje.getOrigen().calcularDistancia(viaje.getDestino());
		double minutos = km / Cotizador.VELOCIDAD_POR_DEFECTO * 60;
		double costo = viaje.getServicio().calcularCosto(km, minutos);

		this.servicio = viaje.getServicio().getNombre();
		this.precio = String.format("$%.2f", costo);
		this.tiempo = String.format("%.0f", minutos);
		this.kilometro = String.format("%.2f", km);
	}

	/**
	 * Devuelve el viaje que representa la fila.
	 *
	 * @return el viaje original
	 */
	public Viaje getViaje() {
		return viaje;
	}

	/**
	 * Devuelve el nombre del servicio.
	 *
	 * @return nombre del servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * Devuelve el precio estimado con formato.
	 *
	 * @return precio como texto
	 */
	public String getPrecio() {
		return precio;
	}

	/**
	 * Devuelve el tiempo estimado en minutos.
	 *
	 * @return tiempo como texto
	 */
	public String getTiempo() {
		return tiempo;
	}

	/**
	 * Devuelve la distancia en kilómetros.
	 *
	 * @return distancia como texto
	 */
	public String getKilometro() {
		return kilometro;
	}
}