package simulacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import interfaz_consola.interfazConsola;
import logica.GeneradorUbicacion;
import logica.OperacionesConductor;
import logica.SistemaGuardado;
import modelo.EstadoViaje;
import modelo.RolUsuario;
import modelo.Servicio;
import modelo.Usuario;
import modelo.Viaje;

public class TestProductorConsumidor {

    private static final double ESCALA = 100; // 1 minuto simulado = 100 ms reales
    private static final int TOPE_MINUTOS = 10; // tope de seguridad para no quedar colgado

    
    public static void main(String[] args) throws InterruptedException {
        SistemaGuardado sistema = new SistemaGuardado();
        GeneradorUbicacion generador = new GeneradorUbicacion(-42.87689, -65.20872, -42.57303, -64.78616);

        // 1. Separo conductores y clientes
        List<Usuario> usuariosConductores = new ArrayList<>();
        List<Usuario> clientes = new ArrayList<>();
        for (Usuario u : sistema.getUsuarios()) {
            if (u.getConductor() != null) {
                usuariosConductores.add(u);
            } else {
                clientes.add(u);
            }
        }

        // 2. Dejo disponibles a los conductores que tienen vehiculo activo
        List<Usuario> conductoresListos = new ArrayList<>();
        for (Usuario c : usuariosConductores) {
            if (c.getConductor().getVehiculoActivo() == null) {
                continue;
            }
            c.cambiarRolActivo(RolUsuario.CONDUCTOR);
            OperacionesConductor.ponerDisponible(c.getConductor(), generador);
            conductoresListos.add(c);
        }

        List<Servicio> servicios = sistema.getServicios();

        interfazConsola.mostrarMensaje("Conductores: " + conductoresListos.size() + " / Clientes: " + clientes.size()
                + " / Servicios: " + servicios.size());

        if (conductoresListos.isEmpty() || clientes.isEmpty() || servicios.isEmpty()) {
            interfazConsola.mostrarMensaje("No hay datos suficientes para simular.");
            return;
        }

        // 3. Parametro: cuantos viajes simular (cada viaje necesita un cliente distinto)
        Scanner teclado = new Scanner(System.in);
        System.out.print("Cuantos viajes queres simular? ");
        int cantidad = teclado.nextInt();
        
        if (cantidad > clientes.size()) {
            interfazConsola.mostrarMensaje("Solo hay " + clientes.size() + " clientes, se simularan "
                    + clientes.size() + " viajes.");
            cantidad = clientes.size();
        }

        // 4. Armo los viajes (los servicios se reparten en ciclo, algunos pueden no tener conductor compatible)
        List<Viaje> viajes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Servicio servicio = servicios.get(i % servicios.size());
            viajes.add(new Viaje(clientes.get(i), generador.generar(), generador.generar(), servicio));
        }

        // 5. Estructuras compartidas
        BlockingQueue<Viaje> buffer = new ArrayBlockingQueue<>(cantidad); // pedidos de los clientes
        CountDownLatch viajesPendientes = new CountDownLatch(cantidad);

        List<ConsumidorConductor> conductores = new ArrayList<>(); // la LISTA de conductores
        for (Usuario c : conductoresListos) {
            conductores.add(new ConsumidorConductor(c, cantidad, ESCALA, viajesPendientes));
        }

        ExecutorService pool = Executors.newFixedThreadPool(cantidad + conductores.size() + 1);

        // 6. Arranca la simulacion
        long inicio = System.nanoTime();

        for (ConsumidorConductor c : conductores) {
            pool.submit(c); // un hilo por conductor
        }
        pool.submit(new Despachante(buffer, conductores, viajesPendientes)); // un hilo despachante
        for (int i = 0; i < cantidad; i++) {
            pool.submit(new ProductorSolicitud(buffer, viajes.get(i), i * 300L, viajesPendientes)); // uno por cliente
        }

        // 7. Espero a que todos los viajes queden cerrados (finalizados o rechazados)
        boolean termino = viajesPendientes.await(TOPE_MINUTOS, TimeUnit.MINUTES);

        // 8. Corto los hilos que siguen esperando en take()
        pool.shutdownNow();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        long duracionMs = (System.nanoTime() - inicio) / 1_000_000;

        // 9. Resumen
        int finalizados = 0;
        int rechazados = 0;
        for (Viaje v : viajes) {
            if (v.estadoActual() == EstadoViaje.FINALIZADO) {
                finalizados++;
            } else if (v.estadoActual() == EstadoViaje.RECHAZADO) {
                rechazados++;
            }
        }

        interfazConsola.mostrarMensaje("----------------------------------------");
        interfazConsola.mostrarMensaje(termino ? "Simulacion terminada!" : "Simulacion cortada por tiempo.");
        interfazConsola.mostrarMensaje("Viajes simulados: " + cantidad + " | Finalizados: " + finalizados
                + " | Rechazados: " + rechazados + " | Otros: " + (cantidad - finalizados - rechazados));
        interfazConsola.mostrarMensaje(String.format("Duracion de la simulacion: %.2f segundos", duracionMs / 1000.0));
    }
}
