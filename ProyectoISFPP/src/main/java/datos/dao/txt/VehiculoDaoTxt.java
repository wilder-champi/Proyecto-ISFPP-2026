package datos.dao.txt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import datos.dao.VehiculoDAO;
import datos.dto.VehiculoDTO;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
//import logica.GeneradorUbicacion;


public class VehiculoDaoTxt implements VehiculoDAO {

	private static final int CAMPOS_MINIMOS = 6;	// Son los datos minimos ya que dicho vehiculo debe tener almenos un servicio

	private final String archivo;	// Se usa para almacenar el nombre del archivo
	//private final GeneradorUbicacion generador;//se usara para la cracion de la ubcacion de dicho vehiculo ya que este no cuenta con una propia desde el txt

	public VehiculoDaoTxt(String archivo /*, GeneradorUbicacion generador*/) {
		this.archivo = archivo;
		//this.generador = generador;
	}

	@Override
	public Map<String, VehiculoDTO> obtenerTodos() {
		Map<String, VehiculoDTO> vehiculos = new LinkedHashMap<>(); // Se crea un map para almacenar todos los vehiculos de manera que se pueda buscar usando como llave su matricula
		
		for (Linea linea : LectorArchivo.leer(archivo)) { // Lee cada linea de la list que contiene todas las lineas del txt
			VehiculoDTO vehiculo = crear(linea);	// Se crea un nuevo vehiculo dada dicha linea y el metodo crear valida si se puede crear
			
			if (vehiculos.containsKey(vehiculo.getPatente())) {	// Verifica que dicha patente del vehiculo no haya sido registrada antes
				
				throw new ArchivoInvalidoException(archivo, linea.getNumero(),	// Si es asi lanza una excepcion que la patente esta repetida
						"Patente repetida " + vehiculo.getPatente());
			}
			vehiculos.put(vehiculo.getPatente(), vehiculo);	// Carga el vehiculo creado al mapa usando como llave su patente
		}
		return vehiculos;	// Retorna el map de vehiculos cargados del txt o almenos los que son validos
	}

	private VehiculoDTO crear(Linea linea) {
		List<String> c = linea.getCampos();	// Se le asigna dicha linea del txt a una nueva list
		if (c.size() < CAMPOS_MINIMOS) {	// Valida que tenga los campos minimos requeridos
			
			throw new ArchivoInvalidoException(archivo, linea.getNumero(),
					// Si no es asi lanza una excepcion diciendo que dicha linea no cumple con los campos minimos
					"Se esperaban al menos " + CAMPOS_MINIMOS + " campos y hay " + c.size());
		}
		try {
			
			 // Lista para guardar los servicios del vehiculo
            List<TipoServicio> tiposServicio = new ArrayList<>();
            
            // Recorre los campos desde la posicion 5
            // y agrega cada tipo de servicio a la lista
            for (int i = 5; i < c.size(); i++) {
                tiposServicio.add(TipoServicio.valueOf(c.get(i)));
            }
			
			VehiculoDTO vehiculo = new VehiculoDTO(
					c.get(0),
					c.get(1),
					Integer.parseInt(c.get(2)),
					TipoVehiculo.valueOf(c.get(3)),
					CategoriaVehiculo.valueOf(c.get(4)),
					tiposServicio);			
			
			// Devuelve el vehiculo creado
            return vehiculo;
			
		} catch (IllegalArgumentException e) {
			throw new ArchivoInvalidoException(archivo, linea.getNumero(), "Valor invalido (" + e.getMessage() + ")");
		}
	}
}