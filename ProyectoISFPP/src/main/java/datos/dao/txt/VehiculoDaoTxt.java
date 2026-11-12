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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import logica.GeneradorUbicacion;


/**
 * DAO que obtiene y guarda los vehículos en un archivo de texto.
 */
public class VehiculoDaoTxt implements VehiculoDAO {

	private static final int CAMPOS_MINIMOS = 6;	// Son los datos minimos ya que dicho vehiculo debe tener almenos un servicio

	private final String archivo;	// Se usa para almacenar el nombre del archivo
	//private final GeneradorUbicacion generador;//se usara para la cracion de la ubcacion de dicho vehiculo ya que este no cuenta con una propia desde el txt

	/**
	 * Crea el DAO con el archivo a utilizar.
	 *
	 * @param archivo ruta del archivo de vehículos
	 */
	public VehiculoDaoTxt(String archivo /*, GeneradorUbicacion generador*/) {
		this.archivo = archivo;
		//this.generador = generador;
	}

	/**
	 * Lee todos los vehículos del archivo.
	 *
	 * @return mapa de vehículos cuya clave es la patente
	 * @throws ArchivoInvalidoException si una línea no es válida o hay una patente repetida
	 */
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

	/**
	 * Convierte una línea del archivo en un vehículo.
	 *
	 * @param linea línea con los campos del vehículo
	 * @return el vehículo creado
	 * @throws ArchivoInvalidoException si faltan campos o algún valor no es válido
	 */
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
	
	/**
	 * Agrega un vehículo al final del archivo.
	 *
	 * @param vehiculo vehículo a guardar
	 * @throws IllegalArgumentException si los datos contienen {@code ;} o ya existe un vehículo con esa patente
	 * @throws IllegalStateException si no se puede escribir el archivo
	 */
	@Override
	public void guardar(VehiculoDTO vehiculo) {
		if (vehiculo.getPatente().contains(";") || vehiculo.getModelo().contains(";")) {
			throw new IllegalArgumentException("no se permite el caracter ; en los datos del vehiculo");
		}
		if (obtenerTodos().containsKey(vehiculo.getPatente())) {
			throw new IllegalArgumentException("ya existe un vehiculo con la patente " + vehiculo.getPatente());
		}

		StringBuilder linea = new StringBuilder();
		linea.append(vehiculo.getPatente()).append(";").append(vehiculo.getModelo()).append(";")
				.append(vehiculo.getCapacidadPasajeros()).append(";").append(vehiculo.getTipoVehiculo()).append(";")
				.append(vehiculo.getCategoriaVehiculo()).append(";");
		for (TipoServicio tipo : vehiculo.getTiposServicio()) {
			linea.append(tipo).append(";");
		}

		// con append en true la linea se agrega al final sin borrar lo que ya habia
		try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo, true))) {
			escritor.println(linea);
		} catch (IOException e) {
			throw new IllegalStateException("no se pudo guardar el archivo " + archivo);
		}
	}
}