package datos.dto;

import java.util.List;

public class UsuarioDTO {

	private String nombre;
	private String telefono;
	private String email;
	private String licencia;// licencia del usuario que esta registrado como conductor
	private List<String> patentes;// patentes de los vehiculos asociados al conductor

	public UsuarioDTO(String nombre, String telefono, String email, String licencia, List<String> patentes) {

		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.licencia = licencia;
		this.patentes = patentes;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getEmail() {
		return email;
	}

	public String getLicencia() {
		return licencia;
	}

	public List<String> getPatentes() {
		return patentes;
	}
}