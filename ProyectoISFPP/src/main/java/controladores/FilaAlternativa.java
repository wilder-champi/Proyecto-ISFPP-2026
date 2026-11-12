package controladores;

import logica.Alternativa;

/**
 * Fila de la tabla de alternativas de una cotización: guarda como texto lo que se muestra
 * en cada columna (servicio, precio, tiempo y kilómetros).
 */
// una fila de la tabla de alternativas: lo que se ve en cada columna
public class FilaAlternativa {

	private final String servicio;
	private final String precio;
	private final String tiempo;
	private final String kilometro;
	private final Alternativa alternativa; 

	/**
	 * Crea la fila a partir de una alternativa, dando formato a sus valores.
	 *
	 * @param alternativa alternativa cotizada que representa la fila
	 */
	public FilaAlternativa(Alternativa alternativa) {
		this.servicio = alternativa.getServicio().getNombre();
		this.precio = String.format("$%.2f", alternativa.getCosto());
		this.tiempo = String.format("%.0f", alternativa.getMinutos());
		this.kilometro=String.format("%.2f",alternativa.getDistanciaKm());
		this.alternativa = alternativa; 

	}
	
	/**
	 * Devuelve la alternativa que representa la fila.
	 *
	 * @return la alternativa original
	 */
	public Alternativa getAlternativa() {    // ← NUEVO
        return alternativa;
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
	 * Devuelve el precio con formato.
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