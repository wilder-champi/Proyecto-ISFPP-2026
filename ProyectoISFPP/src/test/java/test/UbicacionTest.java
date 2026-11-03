package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.Ubicacion;

class UbicacionTest {

	private static final double DELTA_KM = 0.01; // 10 metros de tolerancia

	@Test
	void distanciaAUnoMismoEsCero() {
		Ubicacion a = new Ubicacion(-42.783382312723056, -65.0173768255493);
		assertEquals(0.0, a.calcularDistancia(a), 1e-9);
	}

	@Test
	void distanciaEntreTusDosPuntosDePuertoMadryn() {
		Ubicacion a = new Ubicacion(-42.783382312723056, -65.0173768255493);
		Ubicacion b = new Ubicacion(-42.78552405295517, -65.0242432807606);

		assertEquals(0.609, a.calcularDistancia(b), 0.005); // ±5 m
	}

	@Test
	void distanciaEsSimetrica() {
		Ubicacion a = new Ubicacion(-42.783382312723056, -65.0173768255493);
		Ubicacion b = new Ubicacion(-42.78552405295517, -65.0242432807606);

		assertEquals(a.calcularDistancia(b), b.calcularDistancia(a), 1e-9);
	}

	@Test
	void unGradoDeLatitudSonUnos111Km() {
		Ubicacion a = new Ubicacion(0, 0);
		Ubicacion b = new Ubicacion(1, 0);

		// 2 * PI * 6371 / 360 = 111.195 km
		assertEquals(111.195, a.calcularDistancia(b), DELTA_KM);
	}

	@Test
	void unGradoDeLongitudEnElEcuadorSonUnos111Km() {
		Ubicacion a = new Ubicacion(0, 0);
		Ubicacion b = new Ubicacion(0, 1);

		assertEquals(111.195, a.calcularDistancia(b), DELTA_KM);
	}

	@Test
	void puntosAntipodasDanMediaCircunferencia() {
		Ubicacion a = new Ubicacion(0, 0);
		Ubicacion b = new Ubicacion(0, 180);

		// PI * 6371 = 20015.087 km (prueba el caso borde del Math.min)
		assertEquals(20015.087, a.calcularDistancia(b), DELTA_KM);
	}

	@Test
	void entreLosPolosEsMediaCircunferencia() {
		Ubicacion norte = new Ubicacion(90, 0);
		Ubicacion sur = new Ubicacion(-90, 0);

		assertEquals(20015.087, norte.calcularDistancia(sur), DELTA_KM);
	}

	@Test
	void distanciaConocidaLondresParis() {
		Ubicacion londres = new Ubicacion(51.5074, -0.1278);
		Ubicacion paris = new Ubicacion(48.8566, 2.3522);

		assertEquals(343.5, londres.calcularDistancia(paris), 1.0);
	}

	@Test
	void ubicacionNullLanzaExcepcion() {
		Ubicacion a = new Ubicacion(0, 0);

		assertThrows(IllegalArgumentException.class, () -> a.calcularDistancia(null));
	}
}