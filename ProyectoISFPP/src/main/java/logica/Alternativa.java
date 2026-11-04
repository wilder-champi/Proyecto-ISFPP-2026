package logica;

import modelo.Servicio;

public class Alternativa {

	private final Servicio servicio;
	private final double distanciaKm;
	private final double minutos;
	private final double costo;

	public Alternativa(Servicio servicio, double distanciaKm, double minutos, double costo) {
		this.servicio = servicio;
		this.distanciaKm = distanciaKm;
		this.minutos = minutos;
		this.costo = costo;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public double getDistanciaKm() {
		return distanciaKm;
	}

	public double getMinutos() {
		return minutos;
	}

	public double getCosto() {
		return costo;
	}

	@Override
	public String toString() {
		return String.format("%s: %.1f km, %.0f min, $%.2f", servicio.getNombre(), distanciaKm, minutos, costo);
	}
}