package datos.dao;

import java.util.List;

import datos.dto.ServicioDTO;

public interface ServicioDAO {

	List<ServicioDTO> obtenerTodos();
}