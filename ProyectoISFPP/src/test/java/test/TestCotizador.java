package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import logica.Alternativa;
import logica.Cotizador;
import modelo.Servicio;
import modelo.Ubicacion;

// Prueba que el cotizador arme bien las alternativas de viaje
class TestCotizador {

	// dos puntos de la zona, a unos 23,37 km en linea recta
	private final Ubicacion origen = new Ubicacion(-42.65, -64.89);
	private final Ubicacion destino = new Ubicacion(-42.86, -64.88);

	// servicios armados a mano: no se lee ningun archivo ni base de datos
	private List<Servicio> servicios() {
		List<Servicio> lista = new ArrayList<>();
		lista.add(new Servicio("Estandar", 2000, 200, 100, TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR,
				TipoServicio.PASAJEROS));
		lista.add(new Servicio("Mototaxi", 1000, 100, 50, TipoVehiculo.MOTO, CategoriaVehiculo.ESTANDAR,
				TipoServicio.PASAJEROS));
		lista.add(new Servicio("Premium", 3000, 300, 150, TipoVehiculo.AUTO, CategoriaVehiculo.PREMIUM,
				TipoServicio.PASAJEROS));
		lista.add(new Servicio("Envios", 1500, 150, 75, TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR,
				TipoServicio.ENVIOS));
		return lista;
	}

	// ---------- que servicios aparecen ----------




	@Test
	void sinServiciosCoincidentesDevuelveListaVacia() {
		Cotizador cotizador = new Cotizador(servicios());

		// no existe un servicio de envios en categoria premium
		List<Alternativa> alternativas = cotizador.consultar(origen, destino, TipoServicio.ENVIOS,
				CategoriaVehiculo.PREMIUM);

		assertTrue(alternativas.isEmpty());
	}

	@Test
	void sinServiciosCargadosDevuelveListaVacia() {
		Cotizador cotizador = new Cotizador(new ArrayList<>());

		assertTrue(cotizador.consultar(origen, destino, TipoServicio.PASAJEROS, CategoriaVehiculo.ESTANDAR)
				.isEmpty());
	}

	// ---------- los calculos ----------


	@Test
	void elCostoCoincideConElCalculoDelServicio() {
		Cotizador cotizador = new Cotizador(servicios());

		for (Alternativa a : cotizador.consultar(origen, destino, TipoServicio.PASAJEROS,
				CategoriaVehiculo.ESTANDAR)) {
			double esperado = a.getServicio().calcularCosto(a.getDistanciaKm(), a.getMinutos());
			assertEquals(esperado, a.getCosto(), 0.0001);
		}
	}

	@Test
	void conMenorVelocidadElTiempoEsMayor() {
		Alternativa a40 = new Cotizador(servicios(), 40).consultar(origen, destino, TipoServicio.PASAJEROS,
				CategoriaVehiculo.ESTANDAR).get(0);
		Alternativa a20 = new Cotizador(servicios(), 20).consultar(origen, destino, TipoServicio.PASAJEROS,
				CategoriaVehiculo.ESTANDAR).get(0);

		// la mitad de velocidad es el doble de tiempo
		assertEquals(a40.getMinutos() * 2, a20.getMinutos(), 0.0001);
		assertTrue(a20.getCosto() > a40.getCosto());
	}

	// ---------- datos invalidos ----------

	@Test
	void origenNuloLanzaExcepcion() {
		Cotizador cotizador = new Cotizador(servicios());

		assertThrows(IllegalArgumentException.class,
				() -> cotizador.consultar(null, destino, TipoServicio.PASAJEROS, CategoriaVehiculo.ESTANDAR));
	}

	@Test
	void destinoNuloLanzaExcepcion() {
		Cotizador cotizador = new Cotizador(servicios());

		assertThrows(IllegalArgumentException.class,
				() -> cotizador.consultar(origen, null, TipoServicio.PASAJEROS, CategoriaVehiculo.ESTANDAR));
	}

	@Test
	void origenIgualADestinoLanzaExcepcion() {
		Cotizador cotizador = new Cotizador(servicios());
		Ubicacion mismoPunto = new Ubicacion(-42.65, -64.89);

		assertThrows(IllegalArgumentException.class, () -> cotizador.consultar(origen, mismoPunto,
				TipoServicio.PASAJEROS, CategoriaVehiculo.ESTANDAR));
	}

	@Test
	void velocidadCeroONegativaLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Cotizador(servicios(), 0));
		assertThrows(IllegalArgumentException.class, () -> new Cotizador(servicios(), -10));
	}
}