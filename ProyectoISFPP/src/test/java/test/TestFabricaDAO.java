package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import datos.dao.DAOFactory;
import datos.dao.ServicioDAO;
import datos.dao.UsuarioDAO;
import datos.dao.VehiculoDAO;
import datos.dao.txt.ServicioDaoTxt;
import datos.dao.txt.TxtDAOFactory;
import datos.dao.txt.UsuarioDaoTxt;
import datos.dao.txt.VehiculoDaoTxt;
import datos.dto.ServicioDTO;
import datos.dto.UsuarioDTO;
import datos.dto.VehiculoDTO;

class TestFabricaDAO {

	// Fabrica con archivos que NO existen: sirve para comprobar cuando se lee
	private DAOFactory fabricaSinArchivos() {
		return new TxtDAOFactory("no_existe_servicios.txt", "no_existe_vehiculos.txt", "no_existe_usuarios.txt");
	}

	// Fabrica con los archivos reales del proyecto
	private DAOFactory fabricaReal() {
		return new TxtDAOFactory("servicios.txt", "vehiculos.txt", "usuarios.txt");
	}

	@Test
	void crearLaFabricaNoLeeNingunArchivo() {
		// Si leyera algo, fallaria porque los archivos no existen
		assertDoesNotThrow(() -> fabricaSinArchivos());
	}

	@Test
	void pedirLosDAOTampocoLeeNingunArchivo() {
		DAOFactory fabrica = fabricaSinArchivos();

		assertDoesNotThrow(() -> fabrica.getServicioDAO());
		assertDoesNotThrow(() -> fabrica.getVehiculoDAO());
		assertDoesNotThrow(() -> fabrica.getUsuarioDAO());
	}

	@Test
	void el_archivo_se_lee_recien_al_pedir_los_datos() {
		DAOFactory fabrica = fabricaSinArchivos();

		// Recien aca se intenta abrir el archivo y falla porque no existe
		assertThrows(IllegalStateException.class, () -> fabrica.getServicioDAO().obtenerTodos());
		assertThrows(IllegalStateException.class, () -> fabrica.getVehiculoDAO().obtenerTodos());
	}

	@Test
	void la_fabrica_entrega_el_dao_de_txt_de_cada_entidad() {
		DAOFactory fabrica = fabricaReal(); // Se usa como la interfaz, no como TxtDAOFactory

		assertTrue(fabrica.getServicioDAO() instanceof ServicioDaoTxt);
		assertTrue(fabrica.getVehiculoDAO() instanceof VehiculoDaoTxt);
		assertTrue(fabrica.getUsuarioDAO() instanceof UsuarioDaoTxt);
	}

	@Test
	void cada_llamada_a_la_fabrica_entrega_un_dao_nuevo() {
		DAOFactory fabrica = fabricaReal();

		// La fabrica no guarda los DAO que crea
		assertNotSame(fabrica.getServicioDAO(), fabrica.getServicioDAO());
	}

	@Test
	void cada_llamada_a_obtenerTodos_vuelve_a_crear_los_objetos() {
		ServicioDAO dao = fabricaReal().getServicioDAO();

		ServicioDTO primero = dao.obtenerTodos().get(0);
		ServicioDTO segundo = dao.obtenerTodos().get(0);

		// Mismos datos pero objetos distintos: por eso SistemaGuardado carga una sola vez y guarda el resultado
		assertEquals(primero.getNombre(), segundo.getNombre());
		assertNotSame(primero, segundo);
	}

	@Test
	void recorrido_completo_fabrica_dao_objetos() {
		DAOFactory fabrica = fabricaReal();

		ServicioDAO servicioDAO = fabrica.getServicioDAO();
		VehiculoDAO vehiculoDAO = fabrica.getVehiculoDAO();
		UsuarioDAO usuarioDAO = fabrica.getUsuarioDAO();

		List<ServicioDTO> servicios = servicioDAO.obtenerTodos();
		Map<String, VehiculoDTO> vehiculos = vehiculoDAO.obtenerTodos();
		List<UsuarioDTO> usuarios = usuarioDAO.obtenerTodos();

		assertEquals(6, servicios.size());
		assertEquals(15, vehiculos.size());
		assertEquals(50, usuarios.size());
	}
}