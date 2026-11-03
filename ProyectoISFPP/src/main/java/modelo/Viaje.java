package modelo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import enums.CalificacionViaje;
import enums.RolUsuario;
import enums.EstadoViaje;
import enums.EstadoConductor;

public class Viaje {
	private UUID id;
	private String motivoCancelacion;

	private Ubicacion origen;
	private Ubicacion destino;
	private Servicio servicio;
	private List<RegistroViaje> registroViaje;
	private Vehiculo vehiculo;
	private Usuario cliente;
	private Usuario conductor;

	private RolUsuario rolCancela;
	private CalificacionViaje calificacionConductor;
	private CalificacionViaje calificacionCliente;

	public Viaje(UUID id, Ubicacion origen, Ubicacion destino, Servicio servicio, Usuario cliente) {
		super();
		this.id = id;
		this.origen = origen;
		this.destino = destino;
		this.servicio = servicio;
		this.cliente = cliente;
		this.registroViaje = new ArrayList<>();
	}
	//segundo constructor para los test Junit
	public Viaje(Usuario cliente, Ubicacion origen, Ubicacion destino, Servicio servicio) {
		
		this(UUID.randomUUID(), origen, destino, servicio, cliente);
	}
	

	public void solicitar(LocalDateTime fechaHora) {
		if (!registroViaje.isEmpty()) {//solo veirica que el viaje ya este en algunos de los estados  ya que dicha list guarda el historial de estados del viaje
			throw new IllegalStateException("El viaje ya fue solicitado");
		}
		if (cliente.getCliente().enViaje()) {
			throw new IllegalStateException("El cliente ya tiene un viaje activo");
		}
		//si pasa dichas reviciones se crea un nuevo registro viaje 
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.SOLICITADO));
		this.cliente.getCliente().agregarViaje(this);//se le agrega dicho viaje a la lista del cliente
	}
	
	

	public void aceptar(LocalDateTime fechaHora, Usuario conductor) {
		if (estadoActual() != EstadoViaje.SOLICITADO) {//verifica que el viaje aun este en solicitud
			throw new IllegalStateException("El viaje no esta solicitado");
		}
		Vehiculo vehiculoConductor = conductor.getConductor().getVehiculoActivo(); //asigno en vehiculo del cunductor
		if (vehiculoConductor.getCategoriaVehiculo().getValor() < servicio.getCategoriaVehiculo().getValor()) {//veirfiaca si el vehiculo no cumple con la categoria sulicitada 
			throw new IllegalStateException("El vehiculo no cumple con la categoria solicitada"); 
		}
		//si paso los dictintos chequeos
		this.conductor = conductor;//se le asigna al viaje un conductor que cuple las ocndiciones
		this.vehiculo = vehiculoConductor;//el vehiculo pertenesiente a dicho conductor
		conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_ORIGEN);//se cambia el estado del conductor en vieje hacia la ubicacion del cliente
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.ACEPTADO));//se crea un nuevo registro de viaje para el registro general del viaje y lo coloca como aceptado
	}
	
	
	

	public void iniciar(LocalDateTime fechaHora) {

		if (estadoActual() != EstadoViaje.ACEPTADO) {//verifica si el estado del viaje fue aceptado
			throw new IllegalStateException("El viaje no esta aceptado");
		}
		conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_DESTINO);//se modifica el estado del conductor
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.INICIADO));//cambia el registro general del viaje  y y crea un nuevo registro aceptado para el historial de registro
	}

	
	
	
	
	public void finalizar(LocalDateTime fechaHora, CalificacionViaje calificacionCliente,
			CalificacionViaje calificacionConductor) {
		if (estadoActual() != EstadoViaje.INICIADO) {//verifica si el viaje fue previamente iniciado
			throw new IllegalStateException("El viaje no esta iniciado");
		}
		this.calificacionCliente = calificacionCliente;//se le anade la caficacion al cliente
		this.calificacionConductor = calificacionConductor;//se le añade la calificacion al conductor
		conductor.getConductor().setEstado(EstadoConductor.DISPONIBLE);//como el viaje ya fue finalizado al estado del conductor vuelve a cambiar a disponible
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.FINALIZADO));//al registro general del viaje se le añade un nuevo registro finalizado
	}

	
	
	
	public void cancelar(LocalDateTime fechaHora, Usuario usuario, String motivo) {

		EstadoViaje estado = estadoActual();
		if (estado == EstadoViaje.FINALIZADO || estado == EstadoViaje.CANCELADO || estado == EstadoViaje.RECHAZADO) {//antes de cancelar un viaje se verifica si este viaje no fue finalizado previamente o que no se alla cancelado o dicho viaje no fuera rechazado
			throw new IllegalStateException("El viaje no se puede cancelar");
		}
		if (usuario.equals(cliente)) {//verifica que el lo cancele sea el usuario que solicito el viaje
			this.rolCancela = RolUsuario.CLIENTE;//se avisa que el usuario fue el que cancelo el viaje
		} else if (usuario.equals(conductor)) {//verifica que el usuario que cancelo el vaije sea el conductor
			this.rolCancela = RolUsuario.CONDUCTOR;//se asigna que el que cancelo el viaje fue el mismo conductor
		} else {//si ninguno de estos fue el que que pidio la cancelaciion del viaje entonces no se realiza dicha cancelacion
			throw new IllegalArgumentException("El usuario no pertenece al viaje");
		}
		//si pasa todas las condiciones y dicha cancelacion es valida
		this.motivoCancelacion = motivo;//se le asigna el motivo de la cancelacion
		if (conductor != null) {//verifica si el conductor no es null y se coloca que el estado del conductor es disponible
			conductor.getConductor().setEstado(EstadoConductor.DISPONIBLE);
		}
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.CANCELADO));//al historial de registro se le agraga un nuevo registro de tipo cancelado
	}
	
	
	

	public void rechazar(LocalDateTime fechaHora) {
		if (estadoActual() != EstadoViaje.SOLICITADO) {//verica si el viaje fue solicitado si no es asi se le avisa que no se puede rechasar un viaje n solicitado
			throw new IllegalStateException("El viaje no esta solicitado");
		}
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.RECHAZADO));//si este viaje fue solicitado se le añade al registro general un registro de RACHAZO
	}

	
	
	
	public EstadoViaje estadoActual() {
		if (registroViaje.isEmpty()) {//si el registro general del viaje esta vacion devuelve null
			return null;
		}
		return this.registroViaje.get(this.registroViaje.size() - 1).getEstadoViaje();//sino devuelve el registro actual o el ultimo registro que fue cargado al registro general
	}
	
	
	

	// Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	public Ubicacion getOrigen() {
		return origen;
	}

	public void setOrigen(Ubicacion origen) {
		this.origen = origen;
	}

	public Ubicacion getDestino() {
		return destino;
	}

	public void setDestino(Ubicacion destino) {
		this.destino = destino;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public List<RegistroViaje> getRegistroViaje() {
		return registroViaje;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public Usuario getConductor() {
		return conductor;
	}

	public void setConductor(Usuario conductor) {
		this.conductor = conductor;
	}

	public RolUsuario getRolCancela() {
		return rolCancela;
	}

	public void setRolCancela(RolUsuario rolCancela) {
		this.rolCancela = rolCancela;
	}

	public CalificacionViaje getCalificacionConductor() {
		return calificacionConductor;
	}

	public void setCalificacionConductor(CalificacionViaje calificacionConductor) {
		this.calificacionConductor = calificacionConductor;
	}

	public CalificacionViaje getCalificacionCliente() {
		return calificacionCliente;
	}

	public void setCalificacionCliente(CalificacionViaje calificacionCliente) {
		this.calificacionCliente = calificacionCliente;
	}

}
