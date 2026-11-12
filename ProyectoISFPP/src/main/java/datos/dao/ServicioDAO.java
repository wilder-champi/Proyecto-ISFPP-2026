package datos.dao;

import java.util.List;

import datos.dto.ServicioDTO;

/**
 * Acceso a los datos de los servicios.
 */
public interface ServicioDAO {

	/**
	 * Obtiene todos los servicios.
	 *
	 * @return lista de servicios
	 */
	List<ServicioDTO> obtenerTodos();
}