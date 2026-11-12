package datos.dao.txt;

import datos.dao.DAOFactory;
import datos.dao.ServicioDAO;
import datos.dao.UsuarioDAO;
import datos.dao.VehiculoDAO;

//import logica.GeneradorUbicacion;

/**
 * Fábrica de DAO que trabajan con archivos de texto.
 */
// Fabrica de daos para txt: crea los tres DAO que leen archivos de texto
public class TxtDAOFactory implements DAOFactory {

	private final String archivoServicios; // Nombre del archivo txt de servicios
	private final String archivoVehiculos; // Nombre del archivo txt de vehiculos
	private final String archivoUsuarios; // Nombre del archivo txt de usuarios
	//private final GeneradorUbicacion generador; // genera las ubicaciones al azar de los vehiculos

	/**
	 * Crea la fábrica con los nombres de los archivos de datos.
	 *
	 * @param archivoServicios archivo de servicios
	 * @param archivoVehiculos archivo de vehículos
	 * @param archivoUsuarios archivo de usuarios
	 */
	// Los nombres de archivo entran desde afuera la fabrica
	public TxtDAOFactory(String archivoServicios, String archivoVehiculos, String archivoUsuarios /*,
			GeneradorUbicacion generador*/) {
		this.archivoServicios = archivoServicios;
		this.archivoVehiculos = archivoVehiculos;
		this.archivoUsuarios = archivoUsuarios;
		//this.generador = generador;
	}

	/**
	 * Crea el DAO de servicios sobre su archivo de texto.
	 *
	 * @return DAO de servicios
	 */
	// Crea el dao de servicios indicandole que archivo leer
	@Override
	public ServicioDAO getServicioDAO() {
		return new ServicioDaoTxt(archivoServicios);
	}

	/**
	 * Crea el DAO de vehículos sobre su archivo de texto.
	 *
	 * @return DAO de vehículos
	 */
	// Crea el dao de vehiculos con su archivo y el generador de ubicaciones
	@Override
	public VehiculoDAO getVehiculoDAO() {
		return new VehiculoDaoTxt(archivoVehiculos /*, generador*/);
	}

	/**
	 * Crea el DAO de usuarios sobre su archivo de texto.
	 *
	 * @return DAO de usuarios
	 */
	// Crea el dao de usuarios indicandole que archivo leer
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new UsuarioDaoTxt(archivoUsuarios);
	}
}