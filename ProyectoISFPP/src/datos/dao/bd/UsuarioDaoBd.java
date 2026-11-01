package datos.dao.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import datos.dao.UsuarioDAO;
import datos.dto.UsuarioDTO;

public class UsuarioDaoBd implements UsuarioDAO{

	//se agrego la conexion
	private final ConexionBD conexion;

    public UsuarioDaoBd(ConexionBD conexion) {
        this.conexion = conexion;
    }
	
	@Override
	public List<UsuarioDTO> obtenerTodos() {
		
		// Mapa para agrupar los usuarios por email
		Map<String, UsuarioDTO> usuarios = new LinkedHashMap<>();
		
		// Consulta de usuarios y sus patentes tengan o no 	vehiculos asociados
		// LEFT JOIN permite incluir usuarios que no tienen vehiculos 
		String sql = "SELECT u.email,u.nombre,u.telefono,u.licencia_conducir,c_vehiculo.patente"
				+ " FROM movilidad_digital.usuario AS u" //le asigno un alias
				+ " LEFT JOIN movilidad_digital.conductor_vehiculo AS c_vehiculo "
				+ "ON u.email = c_vehiculo.email"
				+ " ORDER BY u.email,c_vehiculo.patente";
		
		 try (Connection conex = conexion.obtenerConexion();
				    PreparedStatement ps = conex.prepareStatement(sql);
				    ResultSet rs = ps.executeQuery()) {
		
		// Recorre los registros obtenidos
			 while (rs.next()) {
				 
			 String email = rs.getString("email");
		
		// Busca si el usuario ya fue creado
	          UsuarioDTO usuario = usuarios.get(email);

	    // Si no existe, crea su DTO
	          if (usuario == null) {

	              usuario = new UsuarioDTO(
	                 rs.getString("nombre"),
	                 rs.getString("telefono"),
	                 email,
	                 rs.getString("licencia_conducir"),
	                 new ArrayList<>()
	                );
	                
	           // Lo agrega al mapa
	              usuarios.put(email, usuario);
	          }   
	            
	          // Obtiene la patente asociada al usuario
	          String patente = rs.getString("patente");

	          // Si tiene una patente, lo agrega a su lista
	          if (patente != null) {
	              usuario.getPatentes().add(patente);
	          }
	      }

	 } catch (SQLException e) {
		     throw new IllegalStateException("Error al obtener los usuarios de la BD", e);
		   }

		// Convierte los valores del mapa a una lista.
	    return new ArrayList<>(usuarios.values());
	}
		
	@Override
	public void guardarTodos(List<UsuarioDTO> usuarios) {
		// TODO Auto-generated method stub
		
	}
}
