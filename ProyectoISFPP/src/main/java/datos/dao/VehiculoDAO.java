package datos.dao;

import java.util.Map;

import datos.dto.VehiculoDTO;

public interface VehiculoDAO {

	Map<String, VehiculoDTO> obtenerTodos();
}