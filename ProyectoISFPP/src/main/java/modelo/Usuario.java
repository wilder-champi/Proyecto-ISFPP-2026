package modelo;

import enums.EstadoConductor;
import enums.RolUsuario;

public class Usuario {

	private String nombre;
	private String telefono;
	private String email;

	private RolUsuario rolActivo;
	private Cliente cliente;
	private Conductor conductor;

	public Usuario(String nombre, String telefono, String email) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;

		this.cliente = new Cliente();
		this.rolActivo = RolUsuario.CLIENTE;
		this.conductor = null;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public RolUsuario getRolActivo() {
		return rolActivo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Conductor getConductor() {
		return conductor;
	}

	public void altaConductor(String licencia, Vehiculo vehiculo) {
		if (licencia == null || vehiculo == null) {
			throw new IllegalArgumentException("Se debe registrar la licencia y vehiculo");
		}
		if (conductor != null) {
			throw new IllegalArgumentException("El usuario ya esta registrado como conductor");
		}
		conductor = new Conductor(licencia, vehiculo);
	}

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