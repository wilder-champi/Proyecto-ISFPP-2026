package modelo;

import enums.EstadoConductor;
import enums.RolUsuario;

/**
 * Usuario del sistema. Siempre es cliente y puede registrarse además como conductor,
 * cambiando de rol activo cuando lo necesite.
 */
public class Usuario {

	private String nombre;
	private String telefono;
	private String email;

	private RolUsuario rolActivo;
	private Cliente cliente;
	private Conductor conductor;

	/**
	 * Crea un usuario con el rol de cliente activo.
	 *
	 * @param nombre nombre del usuario (obligatorio y sin números)
	 * @param telefono teléfono del usuario
	 * @param email correo del usuario, debe terminar en {@code @email.com}
	 * @throws IllegalArgumentException si el nombre está vacío o contiene números, o si el email no tiene el formato válido
	 */
	public Usuario(String nombre, String telefono, String email) {
		if(nombre == null||nombre.isBlank()) {
			throw new IllegalArgumentException("el nombre es obligatorio");
		}
		for(int digi=0;digi<=9;digi++) {
			if(nombre.contains(String.valueOf(digi))) {
				throw new IllegalArgumentException("el nobre no puede contener numeros");
			}
		}
		if(email==null || !(email.endsWith("@email.com"))) {
			throw new IllegalArgumentException("el formato del email no es valido le falta el '@email.com' al final");
		}
		
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;

		this.cliente = new Cliente();
		this.rolActivo = RolUsuario.CLIENTE;
		this.conductor = null;
	}

	/**
	 * Devuelve el nombre del usuario.
	 *
	 * @return nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve el teléfono del usuario.
	 *
	 * @return teléfono del usuario
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Devuelve el email del usuario.
	 *
	 * @return email del usuario
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el email del usuario.
	 *
	 * @param email nuevo email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Devuelve el rol con el que está usando el sistema.
	 *
	 * @return rol activo (cliente o conductor)
	 */
	public RolUsuario getRolActivo() {
		return rolActivo;
	}

	/**
	 * Devuelve los datos del usuario como cliente.
	 *
	 * @return el cliente asociado
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Devuelve los datos del usuario como conductor.
	 *
	 * @return el conductor asociado, o {@code null} si no está registrado como conductor
	 */
	public Conductor getConductor() {
		return conductor;
	}

	/**
	 * Registra al usuario como conductor.
	 *
	 * @param licencia licencia de conducir
	 * @param vehiculo primer vehículo del conductor
	 * @throws IllegalArgumentException si la licencia o el vehículo son {@code null}, o si el usuario ya es conductor
	 */
	public void altaConductor(String licencia, Vehiculo vehiculo) {
		if (licencia == null || vehiculo == null) {
			throw new IllegalArgumentException("Se debe registrar la licencia y vehiculo");
		}
		if (conductor != null) {
			throw new IllegalArgumentException("El usuario ya esta registrado como conductor");
		}
		conductor = new Conductor(licencia, vehiculo);
	}

	/**
	 * Cambia el rol activo del usuario. Para pasar a conductor debe estar registrado como tal,
	 * fuera de servicio y sin viajes activos como cliente. Al volver a cliente, el conductor queda fuera de servicio.
	 *
	 * @param rolNuevo rol que se quiere activar
	 * @throws IllegalArgumentException si el rol es {@code null}
	 * @throws IllegalStateException si no se cumplen las condiciones para pasar a conductor
	 */
	public void cambiarRolActivo(RolUsuario rolNuevo) {

		if (rolNuevo == null) {
			throw new IllegalArgumentException("Debe ingresar un rol");
		}
		if (rolNuevo == RolUsuario.CONDUCTOR) {
			if (conductor == null) {
				throw new IllegalStateException("El usuario no esta registrado como conductor");
			}
			if (conductor.getEstadoConductor() != EstadoConductor.FUERA_DE_SERVICIO) {
				throw new IllegalStateException("El conductor debe estar fuera de servicio");
			}
			if (cliente.enViaje()) {
				throw new IllegalStateException("El cliente tiene un viaje activo");
			}
		}
		if (rolNuevo == RolUsuario.CLIENTE && conductor != null) {
			conductor.setEstado(EstadoConductor.FUERA_DE_SERVICIO);
		}
		this.rolActivo = rolNuevo;
	}
}