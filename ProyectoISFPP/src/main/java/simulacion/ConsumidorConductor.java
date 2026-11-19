package simulacion;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import interfaz_consola.interfazConsola;
import logica.Cotizador;
import modelo.CalificacionViaje;
import modelo.EstadoViaje;
import modelo.Usuario;
import modelo.Viaje;

/**
 * CONSUMIDOR: representa a un conductor.
 * Tiene su propia bandeja (ArrayBlockingQueue) donde el despachante le deja los viajes.
 * En un ciclo saca un viaje (take), lo acepta y simula el recorrido completo.
 * Como el despachante solo le entrega viajes compatibles, nunca hay que devolverlos.
 */
public class ConsumidorConductor implements Runnable {

    private final BlockingQueue<Viaje> bandeja;
    private final Usuario conductor;
    private final double escala; // ms reales por cada minuto simulado
    private final CountDownLatch viajesPendientes;
    private final AtomicInteger carga = new AtomicInteger(0); // viajes en bandeja + en curso

    public ConsumidorConductor(Usuario conductor, int capacidadBandeja, double escala,
            CountDownLatch viajesPendientes) {
        this.bandeja = new ArrayBlockingQueue<>(capacidadBandeja);
        this.conductor = conductor;
        this.escala = escala;
        this.viajesPendientes = viajesPendientes;
    }

    public Usuario getConductor() {
        return conductor;
    }

    public int getCarga() {
        return carga.get();
    }

    /** Lo llama el despachante para entregarle un viaje a este conductor. */
    public void recibir(Viaje viaje) throws InterruptedException {
        carga.incrementAndGet();
        bandeja.put(viaje);
    }

    private void esperar(double minutosSimulados) throws InterruptedException {
        Thread.sleep((long) (minutosSimulados * escala));
    }

    private double minutosPara(double km) {
        return km / Cotizador.VELOCIDAD_POR_DEFECTO * 60;
    }

    private void atender(Viaje viaje, String nombre, String cliente) throws InterruptedException {

        // 1. Acepto
        try {
            viaje.aceptar(LocalDateTime.now(), conductor);
        } catch (IllegalStateException e) {
            // No deberia pasar (el despachante ya filtro), pero si pasa lo cerramos como rechazado
            interfazConsola.mostrarError(nombre + " no pudo aceptar el viaje de " + cliente + " -> " + e.getMessage());
            if (viaje.estadoActual() == EstadoViaje.SOLICITADO) {
                viaje.rechazar(LocalDateTime.now());
            }
            return;
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
    }

    @Override
    public void run() {
        String nombre = "[Conductor " + conductor.getNombre() + "]";

        try {
            while (!Thread.currentThread().isInterrupted()) {

                // take() se bloquea hasta que el despachante le deje un viaje
                Viaje viaje = bandeja.take();
                String cliente = viaje.getCliente().getNombre();

                try {
                    atender(viaje, nombre, cliente);
                } finally {
                    carga.decrementAndGet();
                    viajesPendientes.countDown(); // un viaje menos pendiente (termine bien o mal)
                }
            }
        } catch (InterruptedException e) {
            // shutdownNow() desde el main
            Thread.currentThread().interrupt();
        }
    }
}
