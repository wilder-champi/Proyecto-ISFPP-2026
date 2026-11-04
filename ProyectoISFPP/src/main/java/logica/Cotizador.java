package logica;

import java.util.ArrayList;
import java.util.List;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import modelo.Servicio;
import modelo.Ubicacion;

public class Cotizador {

	public static final double VELOCIDAD_POR_DEFECTO = 40.0; // velocidad promedio supuesta en calles y rutas

	private final List<Servicio> servicios; // servicios disponibles en el sistema
	private final double velocidadKmh; // velocidad con la que se estima el tiempo de recorrido

	// usa la velocidad por defecto
	public Cotizador(List<Servicio> servicios) {
		this(servicios, VELOCIDAD_POR_DEFECTO);
	}

	public Cotizador(List<Servicio> servicios, double velocidadKmh) {
		if (velocidadKmh <= 0) {
			throw new IllegalArgumentException("La velocidad promedio debe ser mayor a 0");
		}
		this.servicios = servicios;
		this.velocidadKmh = velocidadKmh;
	}

	public List<Alternativa> consultar(Ubicacion origen, Ubicacion destino, TipoServicio tipo,
			CategoriaVehiculo categoria) {
		if (origen == null || destino == null) {
			throw new IllegalArgumentException("el origen y el destino no pueden ser null");
		}

		double km = origen.calcularDistancia(destino);
		if (km == 0) {
			throw new IllegalArgumentException("El origen y el destino no pueden ser el mismo punto");
		}
		double minutos = km / velocidadKmh * 60; // tiempo = distancia / velocidad pasado en minutos

		List<Alternativa> alternativas = new ArrayList<>();
		for (Servicio s : servicios) {
			if (s.getTipoServicio() == tipo && s.getCategoriaVehiculo() == categoria) {
				alternativas.add(new Alternativa(s, km, minutos, s.calcularCosto(km, minutos)));
			}
		}
		return alternativas;
	}
}