package datos.dao.bd;

import datos.dao.DAOFactory;
import datos.dao.ServicioDAO;
import datos.dao.UsuarioDAO;
import datos.dao.VehiculoDAO;

/**
 * Fábrica de DAO que trabajan con la base de datos.
 */
public class BdDAOFactory implements DAOFactory{
	
	private final ConexionBD conexion;

    /**
     * Crea la fábrica con los datos de conexión a la base de datos.
     *
     * @param url dirección JDBC de la base de datos
     * @param usuario usuario de la base de datos
     * @param password contraseña de la base de datos
     */
	//constructor que recibe la configuracion de la BD
    public BdDAOFactory(String url, String usuario, String password) {
        this.conexion = new ConexionBD(url, usuario, password);
    }
    
	/**
	 * Crea el DAO de servicios sobre la base de datos.
	 *
	 * @return DAO de servicios
	 */
	@Override
	public ServicioDAO getServicioDAO() {
		// TODO Auto-generated method stub
		return new ServicioDaoBd(conexion);
	}

	/**
	 * Crea el DAO de vehículos sobre la base de datos.
	 *
	 * @return DAO de vehículos
	 */
	@Override
	public VehiculoDAO getVehiculoDAO() {
		// TODO Auto-generated method stub
		return new VehiculoDaoBd(conexion);
	}

	/**
	 * Crea el DAO de usuarios sobre la base de datos.
	 *
	 * @return DAO de usuarios
	 */
	@Override
	public UsuarioDAO getUsuarioDAO() {
		// TODO Auto-generated method stub
		return new UsuarioDaoBd(conexion);
	}
	

}
