package modelo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import enums.CalificacionViaje;
import enums.RolUsuario;
import enums.EstadoViaje;
import enums.EstadoConductor;

/**
 * Viaje de un cliente entre dos ubicaciones. Guarda su historial de estados (solicitado, aceptado,
 * iniciado, finalizado, cancelado o rechazado) y controla que cada cambio sea válido.
 */
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
	
	private double costoCancelacion = 0.0;

	/**
	 * Crea un viaje sin estados registrados.
	 *
	 * @param id identificador del viaje
	 * @param origen ubicación de partida
	 * @param destino ubicación de llegada
	 * @param servicio servicio solicitado
	 * @param cliente usuario que solicita el viaje
	 */
	public Viaje(UUID id, Ubicacion origen, Ubicacion destino, Servicio servicio, Usuario cliente) {
		super();
		this.id = id;
		this.origen = origen;
		this.destino = destino;
		this.servicio = servicio;
		this.cliente = cliente;
		this.registroViaje = new ArrayList<>();
	}
	/**
	 * Crea un viaje con un identificador generado automáticamente (usado en los tests JUnit).
	 *
	 * @param cliente usuario que solicita el viaje
	 * @param origen ubicación de partida
	 * @param destino ubicación de llegada
	 * @param servicio servicio solicitado
	 */
	//segundo constructor para los test Junit
	public Viaje(Usuario cliente, Ubicacion origen, Ubicacion destino, Servicio servicio) {
		
		this(UUID.randomUUID(), origen, destino, servicio, cliente);
	}
	

	/**
	 * Solicita el viaje y lo agrega al historial del cliente.
	 *
	 * @param fechaHora fecha y hora de la solicitud
	 * @throws IllegalStateException si el viaje ya fue solicitado o el cliente ya tiene un viaje activo
	 */
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
	
	

	/**
	 * Acepta el viaje con un conductor, siempre que cumpla las condiciones: estar disponible,
	 * no ser el mismo cliente y tener un vehículo y una categoría compatibles con el servicio.
	 *
	 * @param fechaHora fecha y hora de la aceptación
	 * @param conductor usuario que acepta el viaje
	 * @throws IllegalStateException si el viaje no está solicitado o el conductor no cumple las condiciones
	 */
	public void aceptar(LocalDateTime fechaHora, Usuario conductor) {
		
		if (estadoActual() != EstadoViaje.SOLICITADO) {//verifica que el viaje aun este en solicitud
			throw new IllegalStateException("El viaje no esta solicitado");
		}
		if(conductor==null||conductor.getConductor()==null) {
			throw new IllegalStateException("El conductor no puede ser null"); 
		}
		if(conductor.getEmail().equals(this.cliente.getEmail())) {
			throw new IllegalStateException("El usuario y le conductor no pueden ser la misma persona"); 

		}
		if(conductor.getRolActivo().equals(RolUsuario.CLIENTE)) {
			throw new IllegalStateException("El conductor ahora mismo se encuentra inactivo"); 
		}
		if(!(conductor.getConductor().getEstadoConductor().equals(EstadoConductor.DISPONIBLE))) {
			throw new IllegalStateException("El conductor no esta disponible");
		}
		
		Vehiculo vehiculoConductor = conductor.getConductor().getVehiculoActivo(); //asigno en vehiculo del cunductor
		
		if(vehiculoConductor.getTipoVehiculo()!= this.servicio.getTipoVehiculo()) {
			throw new IllegalStateException("el vehiculo catual del condutor no es el solicitado por el cliente"); 
		}
		
		if(!(vehiculoConductor.getTipoServicios().contains(this.servicio.getTipoServicio()))) {
			throw new IllegalStateException("el conductor no cumple con los tipos de servcios solicitado"); 
		}
		if (servicio.getCategoriaVehiculo().getValor() < conductor.getConductor().getCategoriaMinimaAceptada().getValor()) {
		    throw new IllegalStateException("el viaje no cumple con la categoria minima aceptada por el conductor");
		}

		// un auto estandar no puede hacer un viaje premium aunque el conductor quiera
		if (vehiculoConductor.getCategoriaVehiculo().getValor() < servicio.getCategoriaVehiculo().getValor()) {
		    throw new IllegalStateException("el vehiculo no cumple con la categoria solicitada para el viaje");
		}
		
		
		//si paso los dictintos chequeos
		this.conductor = conductor;//se le asigna al viaje un conductor que cumple las condiciones
		this.vehiculo = vehiculoConductor;//el vehiculo pertenesiente a dicho conductor
		conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_ORIGEN);//se cambia el estado del conductor en vieje hacia la ubicacion del cliente
		conductor.getConductor().agregarViaje(this);
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.ACEPTADO));//se crea un nuevo registro de viaje para el registro general del viaje y lo coloca como aceptado
	}
	
	
	
	
	

	/**
	 * Inicia un viaje que fue aceptado y pone al conductor viajando hacia el destino.
	 *
	 * @param fechaHora fecha y hora de inicio
	 * @throws IllegalStateException si el viaje no está aceptado
	 */
	public void iniciar(LocalDateTime fechaHora) {

		if (estadoActual() != EstadoViaje.ACEPTADO) {//verifica si el estado del viaje fue aceptado
			throw new IllegalStateException("El viaje no esta aceptado");
		}
		conductor.getConductor().setEstado(EstadoConductor.VIAJE_A_DESTINO);//se modifica el estado del conductor
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.INICIADO));//cambia el registro general del viaje  y y crea un nuevo registro aceptado para el historial de registro
	}

	
	
	
	
	/**
	 * Finaliza un viaje iniciado, guarda las calificaciones y deja al conductor disponible.
	 *
	 * @param fechaHora fecha y hora de finalización
	 * @param calificacionCliente calificación que recibe el cliente
	 * @param calificacionConductor calificación que recibe el conductor
	 * @throws IllegalStateException si el viaje no está iniciado
	 */
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

	
	
	
	/**
	 * Cancela el viaje. Si lo cancela el cliente con el viaje aceptado o iniciado se calcula un costo de cancelación.
	 * El conductor, si lo hay, queda disponible.
	 *
	 * @param fechaHora fecha y hora de la cancelación
	 * @param usuario usuario que cancela (el cliente o el conductor del viaje)
	 * @param motivo motivo de la cancelación
	 * @throws IllegalStateException si el viaje ya está finalizado, cancelado o rechazado
	 * @throws IllegalArgumentException si el usuario no pertenece al viaje
	 */
	public void cancelar(LocalDateTime fechaHora, Usuario usuario, String motivo) {

		EstadoViaje estado = estadoActual();
		if (estado == EstadoViaje.FINALIZADO || estado == EstadoViaje.CANCELADO || estado == EstadoViaje.RECHAZADO) {//antes de cancelar un viaje se verifica si este viaje no fue finalizado previamente o que no se alla cancelado o dicho viaje no fuera rechazado
			throw new IllegalStateException("El viaje no se puede cancelar");
		}
		if (usuario.equals(cliente)) {//verifica que el lo cancele sea el usuario que solicito el viaje
			this.rolCancela = RolUsuario.CLIENTE;//se avisa que el usuario fue el que cancelo el viaje
			if(estado == EstadoViaje.ACEPTADO ||estado == EstadoViaje.INICIADO) {
				LocalDateTime fechaAceptado = null;
				for(RegistroViaje regiAceptado :registroViaje) {
					if(regiAceptado.getEstadoViaje() == EstadoViaje.ACEPTADO) {
						fechaAceptado = regiAceptado.getFechaHora();
					}
				}
				if(fechaAceptado !=null) {
					long minutosTranscurridos = java.time.Duration.between(fechaAceptado, fechaHora).toMinutes();
					double distanciaKm = 0.0;
					if(this.conductor != null && this.conductor.getConductor().getVehiculoActivo() != null 
							&& this.conductor.getConductor().getVehiculoActivo().getUbicacion() != null) {
						distanciaKm = this.conductor.getConductor().getVehiculoActivo().getUbicacion().calcularDistancia(this.origen);
					}
					this.costoCancelacion = (distanciaKm*this.servicio.getPrecioKm())+(minutosTranscurridos*this.servicio.getPrecioMinuto());

				}
			}
			
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
	
	
	

	/**
	 * Rechaza un viaje que está solicitado.
	 *
	 * @param fechaHora fecha y hora del rechazo
	 * @throws IllegalStateException si el viaje no está solicitado
	 */
	public void rechazar(LocalDateTime fechaHora) {
		if (estadoActual() != EstadoViaje.SOLICITADO) {//verica si el viaje fue solicitado si no es asi se le avisa que no se puede rechasar un viaje n solicitado
			throw new IllegalStateException("El viaje no esta solicitado");
		}
		this.registroViaje.add(new RegistroViaje(fechaHora, EstadoViaje.RECHAZADO));//si este viaje fue solicitado se le añade al registro general un registro de RACHAZO
	}

	
	
	
	/**
	 * Devuelve el estado actual del viaje, que es el último registrado.
	 *
	 * @return estado actual, o {@code null} si el viaje todavía no fue solicitado
	 */
	public EstadoViaje estadoActual() {
		if (registroViaje.isEmpty()) {//si el registro general del viaje esta vacion devuelve null
			return null;
		}
		return this.registroViaje.get(this.registroViaje.size() - 1).getEstadoViaje();//sino devuelve el registro actual o el ultimo registro que fue cargado al registro general
	}
	
	
	

	/**
	 * Devuelve el identificador del viaje.
	 *
	 * @return identificador del viaje
	 */
	// Getters y Setters
	public UUID getId() {
		return id;
	}

	/**
	 * Establece el identificador del viaje.
	 *
	 * @param id identificador del viaje
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Devuelve el motivo de la cancelación.
	 *
	 * @return motivo de la cancelación, o {@code null} si no fue cancelado
	 */
	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	/**
	 * Establece el motivo de la cancelación.
	 *
	 * @param motivoCancelacion motivo de la cancelación
	 */
	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	/**
	 * Devuelve el origen del viaje.
	 *
	 * @return ubicación de partida
	 */
	public Ubicacion getOrigen() {
		return origen;
	}

	/**
	 * Establece el origen del viaje.
	 *
	 * @param origen ubicación de partida
	 */
	public void setOrigen(Ubicacion origen) {
		this.origen = origen;
	}

	/**
	 * Devuelve el destino del viaje.
	 *
	 * @return ubicación de llegada
	 */
	public Ubicacion getDestino() {
		return destino;
	}

	/**
	 * Establece el destino del viaje.
	 *
	 * @param destino ubicación de llegada
	 */
	public void setDestino(Ubicacion destino) {
		this.destino = destino;
	}

	/**
	 * Devuelve el servicio del viaje.
	 *
	 * @return servicio solicitado
	 */
	public Servicio getServicio() {
		return servicio;
	}

	/**
	 * Establece el servicio del viaje.
	 *
	 * @param servicio servicio solicitado
	 */
	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	/**
	 * Devuelve el historial de estados del viaje.
	 *
	 * @return lista de registros de estado, en orden cronológico
	 */
	public List<RegistroViaje> getRegistroViaje() {
		return registroViaje;
	}

	/**
	 * Devuelve el vehículo asignado al viaje.
	 *
	 * @return vehículo del viaje, o {@code null} si todavía no fue aceptado
	 */
	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	/**
	 * Establece el vehículo del viaje.
	 *
	 * @param vehiculo vehículo asignado
	 */
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	/**
	 * Devuelve el cliente que solicitó el viaje.
	 *
	 * @return usuario cliente
	 */
	public Usuario getCliente() {
		return cliente;
	}

	/**
	 * Establece el cliente del viaje.
	 *
	 * @param cliente usuario cliente
	 */
	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	/**
	 * Devuelve el conductor del viaje.
	 *
	 * @return usuario conductor, o {@code null} si todavía no fue aceptado
	 */
	public Usuario getConductor() {
		return conductor;
	}

	/**
	 * Establece el conductor del viaje.
	 *
	 * @param conductor usuario conductor
	 */
	public void setConductor(Usuario conductor) {
		this.conductor = conductor;
	}

	/**
	 * Devuelve el rol de quien canceló el viaje.
	 *
	 * @return rol de quien canceló, o {@code null} si no fue cancelado
	 */
	public RolUsuario getRolCancela() {
		return rolCancela;
	}

	/**
	 * Establece el rol de quien canceló el viaje.
	 *
	 * @param rolCancela rol de quien canceló
	 */
	public void setRolCancela(RolUsuario rolCancela) {
		this.rolCancela = rolCancela;
	}

	/**
	 * Devuelve la calificación recibida por el conductor.
	 *
	 * @return calificación del conductor
	 */
	public CalificacionViaje getCalificacionConductor() {
		return calificacionConductor;
	}

	/**
	 * Establece la calificación recibida por el conductor.
	 *
	 * @param calificacionConductor calificación del conductor
	 */
	public void setCalificacionConductor(CalificacionViaje calificacionConductor) {
		this.calificacionConductor = calificacionConductor;
	}

	/**
	 * Devuelve la calificación recibida por el cliente.
	 *
	 * @return calificación del cliente
	 */
	public CalificacionViaje getCalificacionCliente() {
		return calificacionCliente;
	}

	/**
	 * Establece la calificación recibida por el cliente.
	 *
	 * @param calificacionCliente calificación del cliente
	 */
	public void setCalificacionCliente(CalificacionViaje calificacionCliente) {
		this.calificacionCliente = calificacionCliente;
	}
	/**
	 * Devuelve el costo de cancelación del viaje.
	 *
	 * @return costo de cancelación (0 si no corresponde cobrar)
	 */
	public double getCostoCancelacion() {
	    return costoCancelacion;
	}
	

}
