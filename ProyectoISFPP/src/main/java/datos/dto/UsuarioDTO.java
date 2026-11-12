package datos.dto;

import java.util.List;

/**
 * Objeto de transferencia con los datos de un usuario, tal como se leen de la fuente de datos.
 * Si el usuario es conductor incluye su licencia y las patentes de sus vehículos.
 */
public class UsuarioDTO {

	private String nombre;
	private String telefono;
	private String email;
	private String licencia;// licencia del usuario que esta registrado como conductor
	private List<String> patentes;// patentes de los vehiculos asociados al conductor

	/**
	 * Crea el DTO de un usuario.
	 *
	 * @param nombre nombre del usuario
	 * @param telefono teléfono del usuario
	 * @param email email del usuario
	 * @param licencia licencia de conducir, o {@code null} si no es conductor
	 * @param patentes patentes de los vehículos del conductor
	 */
	public UsuarioDTO(String nombre, String telefono, String email, String licencia, List<String> patentes) {

		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.licencia = licencia;
		this.patentes = patentes;
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
	 * Devuelve la licencia de conducir.
	 *
	 * @return licencia, o {@code null} si el usuario no es conductor
	 */
	public String getLicencia() {
		return licencia;
	}

	/**
	 * Devuelve las patentes de los vehículos del conductor.
	 *
	 * @return lista de patentes (vacía si no es conductor)
	 */
	public List<String> getPatentes() {
		return patentes;
	}
}