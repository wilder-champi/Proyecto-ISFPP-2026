package logica;

import modelo.Servicio;

/**
 * Representa una opción de viaje cotizada: un servicio con su distancia, tiempo estimado y costo.
 */
public class Alternativa {

	private final Servicio servicio;
	private final double distanciaKm;
	private final double minutos;
	private final double costo;

	/**
	 * Crea una alternativa de viaje.
	 *
	 * @param servicio servicio cotizado
	 * @param distanciaKm distancia del recorrido en kilómetros
	 * @param minutos tiempo estimado del recorrido en minutos
	 * @param costo costo total del viaje
	 */
	public Alternativa(Servicio servicio, double distanciaKm, double minutos, double costo) {
		this.servicio = servicio;
		this.distanciaKm = distanciaKm;
		this.minutos = minutos;
		this.costo = costo;
	}

	/**
	 * Devuelve el servicio cotizado.
	 *
	 * @return el servicio
	 */
	public Servicio getServicio() {
		return servicio;
	}

	/**
	 * Devuelve la distancia del recorrido.
	 *
	 * @return distancia en kilómetros
	 */
	public double getDistanciaKm() {
		return distanciaKm;
	}

	/**
	 * Devuelve el tiempo estimado del recorrido.
	 *
	 * @return tiempo en minutos
	 */
	public double getMinutos() {
		return minutos;
	}

	/**
	 * Devuelve el costo del viaje.
	 *
	 * @return costo total
	 */
	public double getCosto() {
		return costo;
	}

	/**
	 * Devuelve una descripción de la alternativa con el nombre del servicio, los kilómetros, los minutos y el costo.
	 *
	 * @return texto con el resumen de la alternativa
	 */
	@Override
	public String toString() {
		return String.format("%s: %.1f km, %.0f min, $%.2f", servicio.getNombre(), distanciaKm, minutos, costo);
	}
}