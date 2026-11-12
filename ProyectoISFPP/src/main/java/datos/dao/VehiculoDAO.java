package datos.dao;

import java.util.Map;

import datos.dto.VehiculoDTO;

/**
 * Acceso a los datos de los vehículos.
 */
public interface VehiculoDAO {

	/**
	 * Obtiene todos los vehículos.
	 *
	 * @return mapa de vehículos cuya clave es la patente
	 */
	Map<String, VehiculoDTO> obtenerTodos();
	/**
	 * Guarda un vehículo nuevo.
	 *
	 * @param vehiculo vehículo a guardar
	 */
	//guarda un vehiculo nuevo
	void guardar(VehiculoDTO vehiculo);
}