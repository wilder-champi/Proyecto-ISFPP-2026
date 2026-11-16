package modelo;

public class Usuario {

	private String nombre;
	private String telefono;
	private String email;

	private Cliente cliente;
	private Conductor conductor;

	private RolUsuario rolActivo;

	public Usuario(String nombre, String telefono, String email) {
		super();
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.cliente = new Cliente();
		this.rolActivo = RolUsuario.CLIENTE;
	}

	public void altaConductor(String licenciaConducir, Vehiculo vehiculo) {
		this.conductor = new Conductor(licenciaConducir, vehiculo);
	}

	public void cambiarRolActivo(RolUsuario rolNuevo) {
		if (rolNuevo == RolUsuario.CONDUCTOR) {
			if (this.conductor == null)
				throw new IllegalStateException(
						"No se puede cambiar a conductor porque el usuario no está dado de alta como tal.");

			if (this.conductor.getEstadoConductor() != EstadoConductor.FUERA_DE_SERVICIO)
				throw new IllegalStateException(
						"Solo se puede cambiar al rol de conductor si se encuentra actualmente FUERA_SERVICIO.");
			
			if (this.getCliente().enViaje())
				throw new IllegalStateException(
						"No se puede cambiar de rol porque actualmente tenés un viaje activo como cliente.");

			
		}

		if (rolNuevo == RolUsuario.CLIENTE) {
			if (this.getCliente().enViaje())
				throw new IllegalStateException(
						"No se puede cambiar de rol porque actualmente tenés un viaje activo como cliente.");

			if (this.conductor != null)
				this.conductor.setEstado(EstadoConductor.FUERA_DE_SERVICIO);

		}
		this.rolActivo = rolNuevo;
	}
	
	public RolUsuario getRolActivo() {
		return rolActivo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Conductor getConductor() {
		return conductor;
	}

}
