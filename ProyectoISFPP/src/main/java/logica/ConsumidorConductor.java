package logica;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import enums.CalificacionViaje;
import modelo.Usuario;
import modelo.Viaje;

import interfaz_consola.interfazConsola;
/**
 * CONSUMIDOR: representa a un conductor.
 * En un ciclo saca un pedido de la cola (take), intenta aceptarlo y, si puede,
 * simula el viaje completo. Si el pedido no le sirve (otro tipo de vehiculo,
 * categoria, etc.) lo devuelve a la cola para que lo tome otro conductor.
 */
public class ConsumidorConductor implements Runnable {

	private final BlockingQueue<Viaje> cola;
	private final Usuario conductor;
	private final double escala; // ms reales por cada minuto simulado
	private final CountDownLatch viajesPendientes;

	public ConsumidorConductor(BlockingQueue<Viaje> cola, Usuario conductor, double escala,
			CountDownLatch viajesPendientes) {
		this.cola = cola;
		this.conductor = conductor;
		this.escala = escala;
		this.viajesPendientes = viajesPendientes;
	}

	/**
	 * @param minutosSimulados
	 * @throws InterruptedException
	 * Su objetivo es dormir los hilos el tiempo que sea necesario
	 */
	private void esperar(double minutosSimulados) throws InterruptedException {
		Thread.sleep((long) (minutosSimulados * escala));
	}

	/**
	 * @param km
	 * @return Devuelve lo que seria el tiempo calculado mediante los km 
	 */
	private double minutosPara(double km) {
		return km / Cotizador.VELOCIDAD_POR_DEFECTO * 60;
	}

	@Override
	public void run() {
		String nombre = "[Conductor " + conductor.getNombre() + "]";

		try {
			while (!Thread.currentThread().isInterrupted()) {

				// take() se bloquea hasta que haya un pedido en la cola
				Viaje viaje = cola.take();
				String cliente = viaje.getCliente().getNombre();

				// 1. Intento aceptar
				try {
					viaje.aceptar(LocalDateTime.now(), conductor);
				} catch (IllegalStateException e) {
					interfazConsola.mostrarError(nombre + " no puede tomar el viaje de " + cliente + " (" + e.getMessage()
							+ "), lo devuelve a la cola");
					Thread.sleep(300); // para darle chance a otro conductor
					cola.put(viaje);
					continue;
				}
				interfazConsola.mostrarMensaje(nombre + " acepto el viaje de " + cliente);

				// 2. Simulo el viaje completo
				try {
					double kmHastaCliente = viaje.getVehiculo().getUbicacion().calcularDistancia(viaje.getOrigen());
					interfazConsola.mostrarMensaje(nombre + " va en camino a buscar a " + cliente);
					esperar(minutosPara(kmHastaCliente));

					viaje.iniciar(LocalDateTime.now());
					interfazConsola.mostrarMensaje(nombre + " inicio el viaje con " + cliente);

					double kmViaje = viaje.getOrigen().calcularDistancia(viaje.getDestino());
					esperar(minutosPara(kmViaje));

					viaje.finalizar(LocalDateTime.now(), CalificacionViaje.BUENO, CalificacionViaje.MUY_BUENO);
					interfazConsola.mostrarMensaje(nombre + " finalizo el viaje de " + cliente);
				} catch (IllegalStateException e) {
					interfazConsola.mostrarError(nombre + " error durante el viaje de " + cliente + " -> " + e.getMessage());
				}

				// Un viaje menos pendiente
				viajesPendientes.countDown();
			}
		} catch (InterruptedException e) {
			// shutdownNow() desde el main: el conductor termina su turno
			Thread.currentThread().interrupt();
		}
	}
}
