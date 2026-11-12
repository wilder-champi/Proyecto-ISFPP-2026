package aplicacion;

import logica.SistemaGuardado;
import modelo.Usuario;
import modelo.Vehiculo;

/**
 * Almacena los objetos compartidos entre los controladores: el sistema de guardado,
 * el usuario y el vehículo actuales, y los datos temporales de un registro en curso.
 */
//se usa para almacenar todos lo objetos que lo usaran dichos controladores
public class Contexto {

	private final SistemaGuardado sistema;
	
	// Datos temporales del registro de conductor
	private Usuario usuarioPendiente;
	private String licenciaPendiente;

	/**
	 * Crea el contexto con el sistema de guardado que usarán los controladores.
	 *
	 * @param sistema sistema que carga y guarda los datos de la aplicación
	 * @throws IllegalArgumentException si el sistema es {@code null}
	 */
	public Contexto(SistemaGuardado sistema) {
		if (sistema == null) {
			throw new IllegalArgumentException("el sistema no puede ser null");
		}
		this.sistema = sistema;
	}

	/**
	 * Devuelve el sistema de guardado.
	 *
	 * @return el sistema de guardado compartido
	 */
	public SistemaGuardado getSistema() {
		return sistema;
	}
	
	private Usuario usuarioActual;
	/**
	 * Devuelve el usuario que inició sesión.
	 *
	 * @return el usuario actual, o {@code null} si no hay sesión iniciada
	 */
	public Usuario getUsuarioActual() { return usuarioActual; }
	/**
	 * Establece el usuario que inició sesión.
	 *
	 * @param usuario usuario actual
	 */
	public void setUsuarioActual(Usuario usuario) { this.usuarioActual = usuario; }
	
	
	private Vehiculo vehiculoActual;
	
	/**
	 * Devuelve el vehículo seleccionado actualmente.
	 *
	 * @return el vehículo actual, o {@code null} si no hay ninguno
	 */
	public Vehiculo getVehiculoActual() {
		return vehiculoActual;
	}

	/**
	 * Establece el vehículo seleccionado actualmente.
	 *
	 * @param vehiculoActual vehículo actual
	 */
	public void setVehiculoActual(Vehiculo vehiculoActual) {
		this.vehiculoActual = vehiculoActual;
	}

	private Vehiculo vehiculoPendiente;
	
	
	/**
	 * Devuelve el vehículo cargado en un registro que todavía no se confirmó.
	 *
	 * @return el vehículo pendiente, o {@code null} si no hay
	 */
	public Vehiculo getVehiculoPendiente() {
		return vehiculoPendiente;
	}

	/**
	 * Guarda temporalmente el vehículo de un registro en curso.
	 *
	 * @param vehiculoPendiente vehículo pendiente de confirmar
	 */
	public void setVehiculoPendiente(Vehiculo vehiculoPendiente) {
		this.vehiculoPendiente = vehiculoPendiente;
	}

	
	/**
	 * Devuelve el usuario cargado en un registro de conductor que todavía no se confirmó.
	 *
	 * @return el usuario pendiente, o {@code null} si no hay
	 */
	public Usuario getUsuarioPendiente() {
	    return usuarioPendiente;
	}

	/**
	 * Guarda temporalmente el usuario de un registro de conductor en curso.
	 *
	 * @param usuario usuario pendiente de confirmar
	 */
	public void setUsuarioPendiente(Usuario usuario) {
	    this.usuarioPendiente = usuario;
	}

	/**
	 * Devuelve la licencia cargada en un registro de conductor que todavía no se confirmó.
	 *
	 * @return la licencia pendiente, o {@code null} si no hay
	 */
	public String getLicenciaPendiente() {
	    return licenciaPendiente;
	}

	/**
	 * Guarda temporalmente la licencia de un registro de conductor en curso.
	 *
	 * @param licencia licencia pendiente de confirmar
	 */
	public void setLicenciaPendiente(String licencia) {
	    this.licenciaPendiente = licencia;
	}

	/**
	 * Descarta los datos temporales (usuario y licencia) del registro de conductor en curso.
	 */
	public void limpiarRegistroPendiente() {
	    usuarioPendiente = null;
	    licenciaPendiente = null;
	}
	
}