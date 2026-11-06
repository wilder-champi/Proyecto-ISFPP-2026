package aplicacion;

import logica.SistemaGuardado;
import modelo.Usuario;

//se usa para almacenar todos lo objetos que lo usaran dichos controladores
public class Contexto {

	private final SistemaGuardado sistema;

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

}