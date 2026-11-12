package logica;

import java.util.ArrayList;
import java.util.List;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import modelo.Servicio;
import modelo.Ubicacion;

/**
 * Calcula cuánto costaría un viaje entre dos ubicaciones para los servicios disponibles,
 * estimando el tiempo con una velocidad promedio.
 */
public class Cotizador {

	/**
	 * Velocidad promedio (en km/h) que se supone cuando no se indica otra.
	 */
	public static final double VELOCIDAD_POR_DEFECTO = 40.0; // velocidad promedio supuesta en calles y rutas

	private final List<Servicio> servicios; // servicios disponibles en el sistema
	private final double velocidadKmh; // velocidad con la que se estima el tiempo de recorrido

	/**
	 * Crea un cotizador con la velocidad promedio por defecto.
	 *
	 * @param servicios servicios disponibles en el sistema
	 */
	// usa la velocidad por defecto
	public Cotizador(List<Servicio> servicios) {
		this(servicios, VELOCIDAD_POR_DEFECTO);
	}

	/**
	 * Crea un cotizador con una velocidad promedio indicada.
	 *
	 * @param servicios servicios disponibles en el sistema
	 * @param velocidadKmh velocidad promedio en km/h usada para estimar el tiempo
	 * @throws IllegalArgumentException si la velocidad es menor o igual a 0
	 */
	public Cotizador(List<Servicio> servicios, double velocidadKmh) {
		if (velocidadKmh <= 0) {
			throw new IllegalArgumentException("la velocidad promedio debe ser mayor a 0");
		}
		this.servicios = servicios;
		this.velocidadKmh = velocidadKmh;
	}

	/**
	 * Devuelve las alternativas de viaje entre dos ubicaciones para un tipo de servicio y una categoría de vehículo.
	 *
	 * @param origen ubicación de partida
	 * @param destino ubicación de llegada
	 * @param tipo tipo de servicio buscado
	 * @param categoria categoría de vehículo buscada
	 * @return lista de alternativas con distancia, tiempo y costo (vacía si ningún servicio coincide)
	 * @throws IllegalArgumentException si el origen o el destino son {@code null}, o si son el mismo punto
	 */
	public List<Alternativa> consultar(Ubicacion origen, Ubicacion destino, TipoServicio tipo,
			CategoriaVehiculo categoria) {
		if (origen == null || destino == null) {
			throw new IllegalArgumentException("el origen y el destino no pueden ser null");
		}

		double km = origen.calcularDistancia(destino);
		if (km == 0) {
			throw new IllegalArgumentException("el origen y el destino no pueden ser el mismo punto");
		}
		double minutos = km / velocidadKmh * 60; // tiempo = distancia / velocidad pasado en minutos

		List<Alternativa> alternativas = new ArrayList<>();
		for (Servicio servi : servicios) {
			if (servi.getTipoServicio() == tipo && servi.getCategoriaVehiculo() == categoria) {
				alternativas.add(new Alternativa(servi, km, minutos, servi.calcularCosto(km, minutos)));
			}
		}
		return alternativas;
	}
}