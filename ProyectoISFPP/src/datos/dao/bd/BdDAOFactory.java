package datos.dao.bd;

import datos.dao.DAOFactory;
import datos.dao.ServicioDAO;
import datos.dao.UsuarioDAO;
import datos.dao.VehiculoDAO;

public class BdDAOFactory implements DAOFactory{
	
	private final ConexionBD conexion;

	//constructor que recibe la configuracion de la BD
    public BdDAOFactory(String url, String usuario, String password) {
        this.conexion = new ConexionBD(url, usuario, password);
    }
    
	@Override
	public ServicioDAO getServicioDAO() {
		// TODO Auto-generated method stub
		return new ServicioDaoBd(conexion);
	}

	@Override
	public VehiculoDAO getVehiculoDAO() {
		// TODO Auto-generated method stub
		return new VehiculoDaoBd(conexion);
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		// TODO Auto-generated method stub
		return new UsuarioDaoBd(conexion);
	}
	

}
