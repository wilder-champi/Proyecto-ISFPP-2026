package modelo;

/**
 * Punto geográfico definido por su latitud y su longitud.
 */
public class Ubicacion {
	
	/**
	 * Radio medio de la Tierra en kilómetros, usado para calcular distancias.
	 */
	private static final double RADIO_TIERRA_KM = 6371.0;

	private double latitud;
	private double longitud;
	
	/**
	 * Crea una ubicación.
	 *
	 * @param latitud latitud en grados
	 * @param longitud longitud en grados
	 */
	public Ubicacion(double latitud, double longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}
	/**
	 * Devuelve la latitud.
	 *
	 * @return latitud en grados
	 */
	public double getLatitud() {
		return latitud;
	}
	/**
	 * Establece la latitud.
	 *
	 * @param latitud latitud en grados
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	/**
	 * Devuelve la longitud.
	 *
	 * @return longitud en grados
	 */
	public double getLongitud() {
		return longitud;
	}
	/**
	 * Establece la longitud.
	 *
	 * @param longitud longitud en grados
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	/**
	 * Devuelve una descripción de la ubicación con su latitud y su longitud.
	 *
	 * @return texto con las coordenadas
	 */
	@Override
	public String toString() {
		return "Ubicacion [latitud=" + latitud + ", longitud=" + longitud + "]";
	}
	
	/**
	 * Calcula la distancia entre esta ubicación y otra usando la fórmula de Haversine.
	 *
	 * @param otra ubicación con la que se calcula la distancia
	 * @return distancia en kilómetros
	 * @throws IllegalArgumentException si la otra ubicación es {@code null}
	 */
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
