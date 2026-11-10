package controladores;

import logica.Cotizador;
import modelo.Viaje;

public class FilaViaje {

	private final Viaje viaje;
	private final String servicio;
	private final String precio;
	private final String tiempo;
	private final String kilometro;

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

	public Viaje getViaje() {
		return viaje;
	}

	public String getServicio() {
		return servicio;
	}

	public String getPrecio() {
		return precio;
	}

	public String getTiempo() {
		return tiempo;
	}

	public String getKilometro() {
		return kilometro;
	}
}