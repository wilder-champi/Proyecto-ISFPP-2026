package logica;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import interfaz_consola.interfazConsola;

import modelo.Viaje;

/**
 * PRODUCTOR: representa a un cliente que solicita un viaje.
 * Hace solicitar() y deja el viaje en la cola compartida para que
 * algun conductor (consumidor) lo tome.
 */
public class ProductorSolicitud implements Runnable {

	private final BlockingQueue<Viaje> cola;
	private final Viaje viaje;
	private final long retardoMs; // cuanto espera antes de pedir el viaje

	public ProductorSolicitud(BlockingQueue<Viaje> cola, Viaje viaje, long retardoMs) {
		this.cola = cola;
		this.viaje = viaje;
		this.retardoMs = retardoMs;
	}

	@Override
	public void run() {
		String cliente = viaje.getCliente().getNombre();
		try {
			// Para que los clientes no pidan todos en el mismo instante
			Thread.sleep(retardoMs);

			viaje.solicitar(LocalDateTime.now());
			interfazConsola.mostrarMensaje("[Cliente " + cliente + "] solicito un viaje");

			// put() espera si la cola esta llena
			cola.put(viaje);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (IllegalStateException e) {
			interfazConsola.mostrarError("[Cliente " + cliente + "] no pudo solicitar el viaje -> " + e.getMessage());
		}
	}
}
