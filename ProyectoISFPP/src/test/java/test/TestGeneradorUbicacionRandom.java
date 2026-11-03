package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import logica.GeneradorUbicacion;
import modelo.Ubicacion;

public class TestGeneradorUbicacionRandom {
	private static final double latitud1 = -42.87689;
	private static final double longitud1 = -65.20872;
	private static final double latitud2 = -42.57303;
	private static final double longitud2 = -64.78616;
	
	
	private static final double latitudMinima = Math.min(latitud1, latitud2);
	private static final double latitudMaxima = Math.max(latitud1, latitud2);
	private static final double LongitudMinima = Math.min(longitud1, longitud2);
	private static final double Longitudmaxima = Math.max(longitud1, longitud2);
	
	private void dentroDeMadryn(Ubicacion u) {
		assertTrue(u.getLatitud() >= latitudMinima && u.getLatitud() <= latitudMaxima,
				"latitud fuera de rango: " + u.getLatitud());
		assertTrue(u.getLongitud() >= LongitudMinima && u.getLongitud() <= Longitudmaxima,
				"longitud fuera de rango: " + u.getLongitud());
	}
	
	@Test
	void todasLasUbicacionesCaenDentroDeLosLimites() {
		GeneradorUbicacion doscorde = new GeneradorUbicacion(latitud1, longitud1, latitud2, longitud2, new Random(42));

		for (int i = 0; i < 1000; i++) {
			dentroDeMadryn(doscorde.generar());
		}
	}
	
	@Test
	void constructorSinRandomTambienRespetaLosLimites() {
		GeneradorUbicacion doscorde = new GeneradorUbicacion(latitud1, longitud1, latitud2, longitud2);

		for (int i = 0; i < 1000; i++) {
			dentroDeMadryn(doscorde.generar());
		}
	}
	
	

}
