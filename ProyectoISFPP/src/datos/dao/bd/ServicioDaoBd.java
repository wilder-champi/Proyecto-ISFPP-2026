package datos.dao.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datos.dao.ServicioDAO;
import datos.dto.ServicioDTO;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;

public class ServicioDaoBd implements ServicioDAO{

	//se agrego la conexion
	private final ConexionBD conexion;

    public ServicioDaoBd(ConexionBD conexion) {
        this.conexion = conexion;
    }
					
	@Override
	public List<ServicioDTO> obtenerTodos() {
		 // Lista donde se almacenan los servicios obtenidos de la BD
		List<ServicioDTO> lista_servicios = new ArrayList<>();
		
		//consulta SQL que obtiene las columnas de la tabla , 7 columnas que necesita el constructor de ServicioDTO
        String sql = "SELECT nombre,tarifa_base,precio_km,"
        		+ "precio_minuto,tipo_vehiculo,categoria_vehiculo,"
        		+ "tipo_servicio FROM movilidad_digital.servicio ORDER BY nombre";
        
        //abre la conexion y ejecuta la consulta SQL
        try (Connection conex = conexion.obtenerConexion();//establece la conexion con PostgreSQL
                PreparedStatement ps = conex.prepareStatement(sql);//prepara la instruccion SQL que queremos ejecutar
                ResultSet rs = ps.executeQuery()) {//contiene los registros obtenidos al ejecutar la consulta
        
        	//para leer los datos de cada registro 
      while(rs.next()) {
    	  
    	  // Obtiene el entero de la BD que representa el tipo de vehiculo. 
    	  int numero = rs.getInt("tipo_vehiculo");

    	  TipoVehiculo tipoVehiculo;

    	// Convierte los enteros de la BD a los enum
    	  if (numero == 0) {
    	      tipoVehiculo = TipoVehiculo.AUTO;
    	  } else if (numero == 1) {
    	      tipoVehiculo = TipoVehiculo.MOTO;
    	  } else {
    	      throw new IllegalArgumentException("Tipo de vehículo inválido");
    	  }
    	  
    	  int categoria = rs.getInt("categoria_vehiculo");

    	  CategoriaVehiculo categoriaVehiculo;

    	  // Convierte el entero a su correspondiente enum de categoria vehiculo
    	  if (categoria == 0) {
    	      categoriaVehiculo = CategoriaVehiculo.ESTANDAR;

    	  } else if (categoria == 1) {
    	      categoriaVehiculo = CategoriaVehiculo.CONFORT;

    	  } else if (categoria == 2) {
    	      categoriaVehiculo = CategoriaVehiculo.PREMIUM;

    	  } else {
    	      throw new IllegalArgumentException("Categoria invalida");
    	  }
    	  
    	  int tipo = rs.getInt("tipo_servicio");

    	  TipoServicio tipoServicio;

    	  // Convierte el entero al enum de tipo de servicio
    	  if (tipo == 0) {
    	      tipoServicio = TipoServicio.PASAJEROS;

    	  } else if (tipo == 1) {
    	      tipoServicio = TipoServicio.ENVIOS;

    	  } else {
    	      throw new IllegalArgumentException("Tipo de servicio invalido");
    	  }

              // Creo un DTO con los datos del registro actual
             ServicioDTO servicio = new ServicioDTO(
                rs.getString("nombre"),
                rs.getDouble("tarifa_base"),
                rs.getDouble("precio_km"),
                rs.getDouble("precio_minuto"),
                tipoVehiculo,
                categoriaVehiculo,
                tipoServicio
            );     
           // Agrego a la lista
           lista_servicios.add(servicio);
      }
       
	} catch (SQLException e) {
            throw new IllegalStateException("Error al obtener los servicios de la BD", e);
      	}
   
 //devuelve la lista de servicios 
       return lista_servicios;
	}
}
