package modelo;

public class Ubicacion {
	private double latitud;
	private double longitud;

	public Ubicacion(double latitud, double longitud) {
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double calcularDistancia(Ubicacion ubicacion) {
		final int RADIO_TIERRA = 6371;
		double lat1 = Math.toRadians(latitud);
		double lon1 = Math.toRadians(longitud);
		double lat2 = Math.toRadians(ubicacion.getLatitud());
		double lon2 = Math.toRadians(ubicacion.getLongitud());

		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return RADIO_TIERRA * c;
	}

	@Override
	public String toString() {
		return String.format("Ubicacion [latitud=%.2f, longitud=%.2f]", latitud, longitud);
	}

}
