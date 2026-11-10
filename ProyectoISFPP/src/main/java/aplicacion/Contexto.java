package aplicacion;

import logica.SistemaGuardado;
import modelo.Usuario;
import modelo.Vehiculo;

//se usa para almacenar todos lo objetos que lo usaran dichos controladores
public class Contexto {

	private final SistemaGuardado sistema;
	
	// Datos temporales del registro de conductor
	private Usuario usuarioPendiente;
	private String licenciaPendiente;

	public Contexto(SistemaGuardado sistema) {
		if (sistema == null) {
			throw new IllegalArgumentException("el sistema no puede ser null");
		}
		this.sistema = sistema;
	}

	public SistemaGuardado getSistema() {
		return sistema;
	}
	
	private Usuario usuarioActual;
	public Usuario getUsuarioActual() { return usuarioActual; }
	public void setUsuarioActual(Usuario usuario) { this.usuarioActual = usuario; }
	
	
	private Vehiculo vehiculoActual;
	
	public Vehiculo getVehiculoActual() {
		return vehiculoActual;
	}

	public void setVehiculoActual(Vehiculo vehiculoActual) {
		this.vehiculoActual = vehiculoActual;
	}

	private Vehiculo vehiculoPendiente;
	
	
	public Vehiculo getVehiculoPendiente() {
		return vehiculoPendiente;
	}

	public void setVehiculoPendiente(Vehiculo vehiculoPendiente) {
		this.vehiculoPendiente = vehiculoPendiente;
	}

	
	public Usuario getUsuarioPendiente() {
	    return usuarioPendiente;
	}

	public void setUsuarioPendiente(Usuario usuario) {
	    this.usuarioPendiente = usuario;
	}

	public String getLicenciaPendiente() {
	    return licenciaPendiente;
	}

	public void setLicenciaPendiente(String licencia) {
	    this.licenciaPendiente = licencia;
	}

	public void limpiarRegistroPendiente() {
	    usuarioPendiente = null;
	    licenciaPendiente = null;
	}
	
}