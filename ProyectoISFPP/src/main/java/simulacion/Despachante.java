package simulacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import interfaz_consola.interfazConsola;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.Viaje;

/**
 * DESPACHANTE: toma los pedidos del buffer y busca, en la LISTA de conductores,
 * uno compatible con el servicio pedido (el menos cargado).
 * - Si hay uno: le entrega el viaje en su bandeja.
 * - Si no hay ninguno compatible: rechaza el viaje en el momento (no vuelve a ninguna cola).
 */
public class Despachante implements Runnable {

    private final BlockingQueue<Viaje> buffer;
    private final List<ConsumidorConductor> conductores;
    private final CountDownLatch viajesPendientes;

    public Despachante(BlockingQueue<Viaje> buffer, List<ConsumidorConductor> conductores,
            CountDownLatch viajesPendientes) {
        this.buffer = buffer;
        this.conductores = conductores;
        this.viajesPendientes = viajesPendientes;
    }

    // Mismas comprobaciones que Viaje.aceptar(), pero devolviendo true/false en vez de lanzar excepcion
    private static boolean puedeAtender(Usuario conductor, Viaje viaje) {
        if (conductor.getConductor() == null) {
            return false;
        }
        if (conductor.getEmail().equals(viaje.getCliente().getEmail())) {
            return false;
        }
        Vehiculo vehiculo = conductor.getConductor().getVehiculoActivo();
        if (vehiculo == null) {
            return false;
        }
        return vehiculo.getTipoVehiculo() == viaje.getServicio().getTipoVehiculo()
                && vehiculo.getCategoriaVehiculo().getValorNumerico() >= viaje.getServicio().getCategoriaVehiculo()
                        .getValorNumerico()
                && vehiculo.getTipoServicios().contains(viaje.getServicio().getTipoServicio());
    }

    // Entre los compatibles elige el que tiene menos viajes pendientes
    private ConsumidorConductor elegirConductor(Viaje viaje) {
        ConsumidorConductor elegido = null;
        for (ConsumidorConductor c : conductores) {
            if (!puedeAtender(c.getConductor(), viaje)) {
                continue;
            }
            if (elegido == null || c.getCarga() < elegido.getCarga()) {
                elegido = c;
            }
        }
        return elegido;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                // take() se bloquea hasta que haya un pedido en el buffer
                Viaje viaje = buffer.take();
                String cliente = viaje.getCliente().getNombre();

                ConsumidorConductor elegido = elegirConductor(viaje);

                if (elegido == null) {
                    viaje.rechazar(LocalDateTime.now());
                    interfazConsola.mostrarError("[Despachante] ningun conductor puede atender el viaje de " + cliente
                            + " (" + viaje.getServicio().getTipoVehiculo() + ", "
                            + viaje.getServicio().getTipoServicio() + "): RECHAZADO");
                    viajesPendientes.countDown();
                } else {
                    interfazConsola.mostrarMensaje("[Despachante] viaje de " + cliente + " asignado a "
                            + elegido.getConductor().getNombre());
                    elegido.recibir(viaje);
                }
            }
        } catch (InterruptedException e) {
            // shutdownNow() desde el main
            Thread.currentThread().interrupt();
        }
    }
}
