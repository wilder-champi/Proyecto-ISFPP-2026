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

/**
 * DAO que obtiene los usuarios desde la base de datos. Por el momento es de solo lectura.
 */
public class UsuarioDaoBd implements UsuarioDAO {

	// se agrego la conexion
	private final ConexionBD conexion;

	/**
	 * Crea el DAO con la conexión a utilizar.
	 *
	 * @param conexion conexión a la base de datos
	 */
	public UsuarioDaoBd(ConexionBD conexion) {
		this.conexion = conexion;
	}

	/**
	 * Consulta todos los usuarios de la base de datos junto con las patentes de sus vehículos.
	 *
	 * @return lista de usuarios, cada uno con sus patentes
	 * @throws IllegalStateException si falla la consulta
	 */
	@Override
	public List<UsuarioDTO> obtenerTodos() {

		// Mapa para agrupar los usuarios por email
		Map<String, UsuarioDTO> usuarios = new LinkedHashMap<>();

		// Consulta de usuarios y sus patentes tengan o no vehiculos asociados
		// LEFT JOIN permite incluir usuarios que no tienen vehiculos
		String sql = "SELECT u.email,u.nombre,u.telefono,u.licencia_conducir,c_vehiculo.patente"
				+ " FROM movilidad_digital.usuario AS u" // le asigno un alias
				+ " LEFT JOIN movilidad_digital.conductor_vehiculo AS c_vehiculo " + "ON u.email = c_vehiculo.email"
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

					usuario = new UsuarioDTO(rs.getString("nombre"), rs.getString("telefono"), email,
							rs.getString("licencia_conducir"), new ArrayList<>());

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

	/**
	 * No está soportado: la base de datos es de solo lectura.
	 *
	 * @param usuario usuario a guardar
	 * @throws UnsupportedOperationException siempre
	 */
	@Override
	public void guardar(UsuarioDTO usuario) {
		// como aclaracion este metodo no se puede realisar ya que en si como la bd es
		// compartida con los otros equipos asi que se decide hacer de solo lectura sin
		// ahcer modificaciones alas dichas tablas
		throw new UnsupportedOperationException("la base de datos es de solo lectura por el momento");
	}

	/**
	 * No está soportado: la base de datos es de solo lectura.
	 *
	 * @param usuarios usuarios a guardar
	 * @throws UnsupportedOperationException siempre
	 */
	@Override
	public void guardarTodos(List<UsuarioDTO> usuarios) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("la base de datos es de solo lectura por el momento");
	}
}
