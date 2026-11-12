package datos.dao;

import java.util.List;

import datos.dto.UsuarioDTO;

/**
 * Acceso a los datos de los usuarios.
 */
public interface UsuarioDAO {

	/**
	 * Obtiene todos los usuarios.
	 *
	 * @return lista de usuarios
	 */
	List<UsuarioDTO> obtenerTodos();
	
		/**
		 * Guarda un usuario nuevo.
		 *
		 * @param usuario usuario a guardar
		 */
	// Guarda un usuario nuevo
		void guardar(UsuarioDTO usuario);
		/**
		 * Guarda la lista completa de usuarios, reemplazando los datos existentes.
		 *
		 * @param usuarios usuarios a guardar
		 */
		void guardarTodos(List<UsuarioDTO> usuarios);
}