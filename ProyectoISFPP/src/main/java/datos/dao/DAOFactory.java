package datos.dao;

/**
 * Fábrica de DAO: entrega los objetos de acceso a datos de servicios, vehículos y usuarios
 * sin que el resto del sistema conozca el origen de los datos (archivos de texto o base de datos).
 */
public interface DAOFactory {

	/**
	 * Devuelve el DAO de servicios.
	 *
	 * @return DAO para acceder a los servicios
	 */
	ServicioDAO getServicioDAO();

	/**
	 * Devuelve el DAO de vehículos.
	 *
	 * @return DAO para acceder a los vehículos
	 */
	VehiculoDAO getVehiculoDAO();

	/**
	 * Devuelve el DAO de usuarios.
	 *
	 * @return DAO para acceder a los usuarios
	 */
	UsuarioDAO getUsuarioDAO();
}