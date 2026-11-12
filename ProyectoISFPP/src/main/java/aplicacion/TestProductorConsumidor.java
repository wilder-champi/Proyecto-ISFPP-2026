package aplicacion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import enums.RolUsuario;
import interfaz_consola.interfazConsola;
import logica.ConsumidorConductor;
import logica.GeneradorUbicacion;
import logica.ProductorSolicitud;
import logica.SistemaGuardado;
import modelo.Servicio;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.Viaje;

public class TestProductorConsumidor {

	private static final double ESCALA = 100; // 1 minuto simulado = 100 ms reales

	// Busca un servicio compatible con el vehiculo activo del conductor
	private static Servicio buscarServicio(SistemaGuardado sistema, Usuario conductor) {
		Vehiculo vehiculo = conductor.getConductor().getVehiculoActivo();
		if (vehiculo == null) {
			return null;
		}
		for (Servicio s : sistema.getServicios()) {
			if (s.getTipoVehiculo() == vehiculo.getTipoVehiculo()
					&& vehiculo.getTipoServicios().contains(s.getTipoServicio())
					&& s.getCategoriaVehiculo() == vehiculo.getCategoriaVehiculo()) {
				return s;
			}
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		SistemaGuardado sistema = new SistemaGuardado();
		GeneradorUbicacion generador = new GeneradorUbicacion(-42.87689, -65.20872, -42.57303, -64.78616);

		// 1. Separo conductores y clientes
		List<Usuario> conductores = new ArrayList<>();
		List<Usuario> clientes = new ArrayList<>();
		for (Usuario u : sistema.getUsuarios()) {
			if (u.getConductor() != null) {
				conductores.add(u);
			} else {
				clientes.add(u);
			}
		}

		// 2. Preparo solo a los conductores que tienen un servicio compatible
		Map<Usuario, Servicio> servicioPorConductor = new LinkedHashMap<>();
		for (Usuario c : conductores) {
			Servicio s = buscarServicio(sistema, c);
			if (s == null) {
				continue; // sin vehiculo activo o sin servicio compatible
			}
			c.cambiarRolActivo(RolUsuario.CONDUCTOR);
			c.getConductor().ponerDisponible(generador);
			servicioPorConductor.put(c, s);
		}

		List<Usuario> aptos = new ArrayList<>(servicioPorConductor.keySet());
		interfazConsola.mostrarMensaje(
				"Conductores aptos: " + aptos.size() + " / Clientes disponibles: " + clientes.size());

		if (aptos.isEmpty() || clientes.isEmpty()) {
			interfazConsola.mostrarMensaje("No hay datos suficientes para simular.");
			return;
		}

		// 3. Hasta 2 viajes por conductor, asi se ve como los pedidos esperan en la cola
		int cantidadViajes = Math.min(clientes.size(), aptos.size() * 2);

		BlockingQueue<Viaje> cola = new ArrayBlockingQueue<>(cantidadViajes);
		
		//Esto es un contador de hilos pendientes 
		CountDownLatch viajesPendientes = new CountDownLatch(cantidadViajes);
		ExecutorService pool = Executors.newFixedThreadPool(cantidadViajes + aptos.size());

		// 4. Consumidores: un hilo por conductor (quedan esperando pedidos)
		for (Usuario conductor : aptos) {
			pool.submit(new ConsumidorConductor(cola, conductor, ESCALA, viajesPendientes));
		}

		// 5. Productores: un hilo por cliente
		for (int i = 0; i < cantidadViajes; i++) {
			Usuario cliente = clientes.get(i);
			// El servicio se elige compatible con algun conductor, asi alguien puede aceptarlo
			Servicio servicio = servicioPorConductor.get(aptos.get(i % aptos.size()));
			Viaje viaje = new Viaje(cliente, generador.generar(), generador.generar(), servicio);
			pool.submit(new ProductorSolicitud(cola, viaje, i * 300L));
		}

		// 6. Espero a que terminen todos los viajes (con tope de seguridad)
		boolean termino = viajesPendientes.await(2, TimeUnit.MINUTES);

		// 7. Corto a los conductores que siguen esperando en take()
		pool.shutdownNow();
		pool.awaitTermination(5, TimeUnit.SECONDS);

		if (termino) {
			interfazConsola.mostrarMensaje("Simulacion terminada! Se completaron los " + cantidadViajes + " viajes.");
		} else {
			interfazConsola.mostrarMensaje("Simulacion cortada por tiempo: quedaron viajes sin completar.");
		}
	}
}
