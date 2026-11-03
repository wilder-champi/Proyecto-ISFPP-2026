package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import datos.dao.DAOFactory;
import datos.dao.DAOLoader;
import datos.dao.txt.ArchivoInvalidoException;
import datos.dao.txt.ServicioDaoTxt;
import datos.dao.txt.UsuarioDaoTxt;
import datos.dao.txt.VehiculoDaoTxt;
import datos.dto.ServicioDTO;
import datos.dto.UsuarioDTO;
import datos.dto.VehiculoDTO;
import enums.TipoServicio;

// Prueba los DAO de txt: lo que leen de los archivos y como detectan los errores
class TestDaoTxt {

	private static List<ServicioDTO> servicios;
	private static Map<String, VehiculoDTO> vehiculos;
	private static List<UsuarioDTO> usuarios;

	@BeforeAll
	static void cargarArchivosReales() {
		DAOFactory fabrica = DAOLoader.getFactory();
		servicios = fabrica.getServicioDAO().obtenerTodos();
		vehiculos = fabrica.getVehiculoDAO().obtenerTodos();
		usuarios = fabrica.getUsuarioDAO().obtenerTodos();
	}

	// crea un archivo temporal con el contenido indicado
	private Path archivo(Path carpeta, String contenido) throws IOException {
		return Files.writeString(carpeta.resolve("datos.txt"), contenido);
	}

	// ---------- lectura de los archivos reales ----------

	@Test
	void leeLosServicios() {
		assertEquals(6, servicios.size());
		assertEquals("Estandar", servicios.get(0).getNombre());
		assertEquals(2000, servicios.get(0).getTarifaBase(), 0.001);
	}

	@Test
	void leeLosVehiculosConUnoYDosTiposDeServicio() {
		assertEquals(15, vehiculos.size());

		List<TipoServicio> doble = vehiculos.get("AB456DE").getTiposServicio();
		assertEquals(2, doble.size());
		assertTrue(doble.contains(TipoServicio.ENVIOS));

		assertEquals(1, vehiculos.get("AD123BC").getTiposServicio().size());
	}

	@Test
	void leeLosUsuariosConYSinLicencia() {
		assertEquals(50, usuarios.size());
		assertEquals(10, usuarios.stream().filter(u -> u.getLicencia() != null).count());

		UsuarioDTO juan = usuarios.get(0); // conductor con tres patentes
		assertEquals("A13-B1-D1-E1", juan.getLicencia());
		assertEquals(List.of("AD123BC", "123ABC", "AD012HI"), juan.getPatentes());

		UsuarioDTO maria = usuarios.get(1); // solo cliente
		assertNull(maria.getLicencia());
		assertTrue(maria.getPatentes().isEmpty());
	}

	// ---------- lectura de archivos temporales ----------

	@Test
	void vehiculoConDosTiposDeServicio(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "AB123CD;Ford Ka;4;AUTO;ESTANDAR;PASAJEROS;ENVIOS;\n");

		VehiculoDTO v = new VehiculoDaoTxt(f.toString()).obtenerTodos().get("AB123CD");
		assertEquals(List.of(TipoServicio.PASAJEROS, TipoServicio.ENVIOS), v.getTiposServicio());
	}

	@Test
	void conductorConVariasPatentes(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "Ana;+54911;ana@email.com;B1;AB456DE;AD123BC;\n");

		UsuarioDTO ana = new UsuarioDaoTxt(f.toString()).obtenerTodos().get(0);
		assertEquals("B1", ana.getLicencia());
		assertEquals(List.of("AB456DE", "AD123BC"), ana.getPatentes());
	}

	// ---------- errores en los archivos ----------

	@Test
	void servicioConValorInvalidoIndicaLaLinea(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "# comentario\nEstandar;2000;200;100;AUTO;LUJO;PASAJEROS;\n");

		ArchivoInvalidoException e = assertThrows(ArchivoInvalidoException.class,
				() -> new ServicioDaoTxt(f.toString()).obtenerTodos());
		assertEquals(2, e.getNumeroLinea());
	}

	@Test
	void vehiculoConCamposDeMenos(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "AB123CD;Ford Ka;4;AUTO;\n");

		assertThrows(ArchivoInvalidoException.class, () -> new VehiculoDaoTxt(f.toString()).obtenerTodos());
	}

	@Test
	void vehiculoConPatenteRepetida(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta,
				"AB123CD;Ford Ka;4;AUTO;ESTANDAR;PASAJEROS;\n" + "AB123CD;Fiat Uno;4;AUTO;ESTANDAR;PASAJEROS;\n");

		assertThrows(ArchivoInvalidoException.class, () -> new VehiculoDaoTxt(f.toString()).obtenerTodos());
	}

	@Test
	void usuarioConLicenciaSinPatente(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "Ana;+54911;ana@email.com;B1;\n");

		assertThrows(ArchivoInvalidoException.class, () -> new UsuarioDaoTxt(f.toString()).obtenerTodos());
	}

	@Test
	void patenteAsignadaADosUsuarios(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "Ana;+54911;ana@email.com;B1;AB456DE;\nLuis;+54912;luis@email.com;B1;AB456DE;\n");

		assertThrows(ArchivoInvalidoException.class, () -> new UsuarioDaoTxt(f.toString()).obtenerTodos());
	}

	@Test
	void emailRepetido(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "Ana;+54911;mismo@email.com;\nLuis;+54912;mismo@email.com;\n");

		assertThrows(ArchivoInvalidoException.class, () -> new UsuarioDaoTxt(f.toString()).obtenerTodos());
	}

	// ---------- configuracion (DAOLoader) ----------

	@Test
	void configuracionInexistente() {
		assertThrows(IllegalStateException.class, () -> DAOLoader.crearFactory("noexiste.properties"));
	}

	@Test
	void configuracionSinUnaPropiedad(@TempDir Path carpeta) throws IOException {
		Path f = archivo(carpeta, "usuario=usuarios.txt\n");

		assertThrows(IllegalStateException.class, () -> DAOLoader.crearFactory(f.toString()));
	}
}