package simulacion;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import interfaz_consola.interfazConsola;
import modelo.Viaje;

/**
 * PRODUCTOR: representa a un cliente que solicita un viaje.
 * Hace solicitar() y deja el viaje en el buffer compartido (ArrayBlockingQueue)
 * para que el despachante lo tome.
 */
public class ProductorSolicitud implements Runnable {

    private final BlockingQueue<Viaje> buffer;
    private final Viaje viaje;
    private final long retardoMs; // cuanto espera antes de pedir el viaje
    private final CountDownLatch viajesPendientes;

    public ProductorSolicitud(BlockingQueue<Viaje> buffer, Viaje viaje, long retardoMs,
            CountDownLatch viajesPendientes) {
        this.buffer = buffer;
        this.viaje = viaje;
        this.retardoMs = retardoMs;
        this.viajesPendientes = viajesPendientes;
    }

    @Override
    public void run() {
        String cliente = viaje.getCliente().getNombre();
        try {
            // Para que los clientes no pidan todos en el mismo instante
            Thread.sleep(retardoMs);

            viaje.solicitar(LocalDateTime.now());
            interfazConsola.mostrarMensaje("[Cliente " + cliente + "] solicito un viaje");

            // put() espera si el buffer esta lleno
            buffer.put(viaje);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IllegalStateException e) {
            interfazConsola.mostrarError("[Cliente " + cliente + "] no pudo solicitar el viaje -> " + e.getMessage());
            // Este viaje nunca va a llegar al buffer: lo damos por cerrado para no colgar al main
            viajesPendientes.countDown();
        }
    }
}
