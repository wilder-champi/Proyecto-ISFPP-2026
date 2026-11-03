package datos.dao.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import datos.dao.VehiculoDAO;
import datos.dto.VehiculoDTO;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;

public class VehiculoDaoBd implements VehiculoDAO{
	
	// se agrego la conexion
    private final ConexionBD conexion;
    
    public VehiculoDaoBd(ConexionBD conexion) {
    	this.conexion = conexion;
    }
    
	@Override
	public Map<String, VehiculoDTO> obtenerTodos() {
		
		//Se crea un map para almacenar todos los vehiculos de manera que se pueda buscar usando como llave su matricula
		Map<String, VehiculoDTO> vehiculos = new LinkedHashMap<>();
		
		// Consulta SQL para obtener los datos de los vehiculos
		 String sql = "SELECT patente, modelo, capacidad_pasajeros, "
	                + "tipo_vehiculo, categoria_vehiculo, tipo_servicios "
	                + "FROM movilidad_digital.vehiculo ORDER BY patente";
		 
		 // Abre la conexion y ejecuta la consulta SQL
	        try (Connection conex = conexion.obtenerConexion();
	             PreparedStatement ps = conex.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	     // Recorre los registros obtenidos
	       while (rs.next()) {   
	            	
	       // Convierte los enteros de la base de datos a enum
	        TipoVehiculo tipoVehiculo = TipoVehiculo.values()[rs.getInt("tipo_vehiculo")];

	        CategoriaVehiculo categoriaVehiculo = CategoriaVehiculo.values()[rs.getInt("categoria_vehiculo")];
	                

	     // La base de datos  almacena los servicios mediante una mascara de bits
	     // 1 = PASAJEROS, 2 = ENVIOS, 3 = AMBOS
	      int mascara_bits = rs.getInt("tipo_servicios");

	   List<TipoServicio> tiposServicio = new ArrayList<>();

   switch (mascara_bits) {

       case 1: // Solo pasajeros
           tiposServicio.add(TipoServicio.PASAJEROS);
           break;

       case 2: // Solo envios
           tiposServicio.add(TipoServicio.ENVIOS);
           break;

       case 3: // Ambos servicios
           tiposServicio.add(TipoServicio.PASAJEROS);
           tiposServicio.add(TipoServicio.ENVIOS);
           break;

       default:
           throw new IllegalArgumentException(
               "Tipo de servicios invalido: " + mascara_bits);
   }

   //Crea un DTO con los datos del registro
   VehiculoDTO vehiculo = new VehiculoDTO(
       rs.getString("patente"),
       rs.getString("modelo"),
       rs.getInt("capacidad_pasajeros"),
       tipoVehiculo,
       categoriaVehiculo,
       tiposServicio
   );

   //Agrega el vehiculo al mapa usando su patente
   vehiculos.put(vehiculo.getPatente(), vehiculo);

  }
		
	} catch (SQLException e) {
            throw new IllegalStateException("Error al obtener los vehiculos de la BD", e);
        }

	// Devuelve el mapa con los vehiculos obtenidos
	  return vehiculos;   	        
	}
}
