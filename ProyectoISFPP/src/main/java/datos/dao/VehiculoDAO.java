package datos.dao;

import java.util.Map;

import datos.dto.VehiculoDTO;

public interface VehiculoDAO {

	Map<String, VehiculoDTO> obtenerTodos();
	//guarda un vehiculo nuevo
	void guardar(VehiculoDTO vehiculo);
}