package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Viaje {
	private UUID id;
	private String motivoCancelacion;
	private Ubicacion origen;
	private Ubicacion destino;
	private Servicio servicio;
	private Vehiculo vehiculo;
	private Usuario cliente;
	private Usuario conductor;
	private RolUsuario rolCancela;
	private CalificacionViaje calificacionCliente = CalificacionViaje.NO_CALIFICADO;
	private CalificacionViaje calificacionConductor = CalificacionViaje.NO_CALIFICADO;

	private List<RegistroViaje> registroViaje;

	public Viaje(Usuario cliente, Ubicacion origen, Ubicacion destino, Servicio servicio) {
		this.id = UUID.randomUUID();
		this.cliente = cliente;
		this.origen = origen;
		this.destino = destino;
		this.servicio = servicio;
		registroViaje = new ArrayList<RegistroViaje>();
	}

	public void solicitar(LocalDateTime fechaHora) {
		if (cliente.getRolActivo() != RolUsuario.CLIENTE)
			throw new IllegalStateException("El usuario debe tener el rol de cliente activo para solicitar un viaje.");

		if (cliente.getCliente().enViaje())
			throw new IllegalStateException("No se puede solicitar un nuevo viaje porque ya tenés un viaje activo.");

		cliente.getCliente().agregarViaje(this);
		registroViaje.add(new RegistroViaje(EstadoViaje.SOLICITADO, fechaHora));
	}

	public void aceptar(LocalDateTime fechaHora, Usuario conductor) {
		if (estadoActual() != EstadoViaje.SOLICITADO)
			throw new IllegalStateException("Solo se puede aceptar un viaje que esté SOLICITADO");

		if (conductor.getRolActivo() != RolUsuario.CONDUCTOR)
			throw new IllegalStateException("El usuario asignado no se encuentra con el rol de conductor activo.");

		EstadoConductor estadoActualConductor = conductor.getConductor().getEstadoConductor();
		if (estadoActualConductor == EstadoConductor.FUERA_DE_SERVICIO)
			throw new IllegalStateException("El viaje no puede ser aceptado por un conductor fuera de servicio.");

		if (estadoActualConductor == EstadoConductor.VIAJE_A_ORIGEN
				|| estadoActualConductor == EstadoConductor.VIAJE_A_DESTINO)
			throw new IllegalStateException(
					"El viaje no puede ser aceptado porque el conductor ya está realizando un viaje.");

		Vehiculo vehiculoConductor = conductor.getConductor().getVehiculoActivo();
		if (vehiculoConductor == null)
			throw new IllegalStateException("El conductor no tiene un vehículo activo asignado.");

		if (vehiculoConductor.getTipoVehiculo() != this.servicio.getTipoVehiculo())
			throw new IllegalStateException("El tipo de vehículo del conductor (" + vehiculoConductor.getTipoVehiculo()
					+ ") no coincide con el requerido por el servicio (" + this.servicio.getTipoVehiculo() + ").");

		if (vehiculoConductor.getCategoriaVehiculo().getValorNumerico() < this.servicio.getCategoriaVehiculo()
				.getValorNumerico())
			throw new IllegalStateException("La categoría del vehículo (" + vehiculoConductor.getCategoriaVehiculo()
					+ ") es inferior a la requerida por el servicio (" + this.servicio.getCategoriaVehiculo() + ").");

		if (!vehiculoConductor.getTipoServicios().contains(this.servicio.getTipoServicio()))
			throw new IllegalStateException("El vehículo no está habilitado para ofrecer el tipo de servicio: "
					+ this.servicio.getTipoServicio());

		this.conductor = conductor;
		vehiculo = conductor.getConductor().getVehiculoActivo();
		conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_ORIGEN);
		conductor.getConductor().agregarViaje(this);

		registroViaje.add(new RegistroViaje(EstadoViaje.ACEPTADO, fechaHora));
	}

	public void iniciar(LocalDateTime fechaHora) {
		if (estadoActual() != EstadoViaje.ACEPTADO)
			throw new IllegalStateException("Solo puede iniciar un viaje que esté ACEPTADO");

		if (this.conductor != null) {
			this.conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_DESTINO);
		}
		registroViaje.add(new RegistroViaje(EstadoViaje.INICIADO, fechaHora));
	}

	public void finalizar(LocalDateTime fechaHora, CalificacionViaje calificacionCliente,
			CalificacionViaje calificacionConductor) {
		if (estadoActual() != EstadoViaje.INICIADO)
			throw new IllegalStateException("Solo puede finalizar un viaje que esté INICIADO");

		this.calificacionCliente = calificacionCliente;
		this.calificacionConductor = calificacionConductor;
		this.conductor.getConductor().setEstado(EstadoConductor.DISPONIBLE);
		registroViaje.add(new RegistroViaje(EstadoViaje.FINALIZADO, fechaHora));
	}

	public void cancelar(LocalDateTime fechaHora, Usuario usuario, String motivo) {
		if (estadoActual() == EstadoViaje.CANCELADO || estadoActual() == EstadoViaje.FINALIZADO
				|| estadoActual() == EstadoViaje.RECHAZADO)
			throw new IllegalStateException(
					"No se puede cancelar un viaje que ya esté CANCELADO, FINALIZADO o RECHAZADO.");

		if (estadoActual() == EstadoViaje.SOLICITADO && usuario.getRolActivo() != RolUsuario.CLIENTE)
			throw new IllegalStateException("Un Conductor no puede cancelar un viaje que todavía está SOLICITADO.");

		if (this.conductor != null)
			this.conductor.getConductor().setEstado(EstadoConductor.DISPONIBLE);

		this.rolCancela = usuario.getRolActivo();
		this.motivoCancelacion = motivo;
		registroViaje.add(new RegistroViaje(EstadoViaje.CANCELADO, fechaHora));
	}

	public void rechazar(LocalDateTime fechaHora) {
		if (estadoActual() != EstadoViaje.SOLICITADO)
			throw new IllegalStateException("Solo se puede rechazar un viaje que esté SOLICITADO");

		registroViaje.add(new RegistroViaje(EstadoViaje.RECHAZADO, fechaHora));
	}

	public EstadoViaje estadoActual() {
		if (registroViaje == null || registroViaje.isEmpty())
			return null;

		return registroViaje.get(registroViaje.size() - 1).getEstadoViaje();
	}

	public UUID getId() {
		return id;
	}

	public CalificacionViaje getCalificacionCliente() {
		return calificacionCliente;
	}

	public CalificacionViaje getCalificacionConductor() {
		return calificacionConductor;
	}

	public Ubicacion getOrigen() {
		return origen;
	}

	public Ubicacion getDestino() {
		return destino;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public Usuario getConductor() {
		return conductor;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	public RolUsuario getRolCancela() {
		return rolCancela;
	}

	public List<RegistroViaje> getRegistroViaje() {
	    return new ArrayList<RegistroViaje>(this.registroViaje);
	}
}
