package modelo;

public class Ubicacion {
	
	private static final double RADIO_TIERRA_KM = 6371.0;

	private double latitud;
	private double longitud;
	
	public Ubicacion(double latitud, double longitud) {
		super();
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
	
	@Override
	public String toString() {
		return "Ubicacion [latitud=" + latitud + ", longitud=" + longitud + "]";
	}
	
	public double calcularDistancia(Ubicacion otra) {
		//se utiliza la formula de hearvane
		if (otra == null) {
			throw new IllegalArgumentException("La ubicacion no puede ser null");
		}
		double lat1 = Math.toRadians(this.latitud);
		double lat2 = Math.toRadians(otra.latitud);
		double lon1 = Math.toRadians(this.longitud);
		double lon2 = Math.toRadians(otra.longitud);

		double difLat = lat2 - lat1;
		double difLon = lon2 - lon1;

		double senoLat = Math.sin(difLat / 2);
		double terminoLat = senoLat * senoLat;

		double senoLon = Math.sin(difLon / 2);
		double terminoLon = Math.cos(lat1) * Math.cos(lat2) * senoLon * senoLon;

		double suma = terminoLat + terminoLon;

		double raiz = Math.sqrt(Math.min(1.0, suma));

		double angulo = Math.asin(raiz);

		return 2 * RADIO_TIERRA_KM * angulo;	
	}
	
}
