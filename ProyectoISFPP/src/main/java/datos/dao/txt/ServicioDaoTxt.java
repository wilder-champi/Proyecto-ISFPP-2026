package datos.dao.txt;

import java.util.ArrayList;
import java.util.List;

import datos.dao.ServicioDAO;
import datos.dto.ServicioDTO;
import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;

public class ServicioDaoTxt implements ServicioDAO {

	private static final int CAMPOS = 7;// Se usa como parametro de los campos recibidos de parte del txt
	private final String archivo;// Donde se almacena el nombre del archivo

	//constructor
	public ServicioDaoTxt(String archivo) {
		this.archivo = archivo;// Se le asigna el nombre del archivo
	}

	@Override
	public List<ServicioDTO> obtenerTodos() {
		List<ServicioDTO> servicios = new ArrayList<>();// Se crea una lista vacia para almacenar todos los servicios permitidos del archivo
		
		for (Linea linea : LectorArchivo.leer(archivo)) {	// Se lee una linea de la list de lineas que almacena cada linea del archivo txt
			servicios.add(crear(linea));// Se fija si se pudo crear dicho servicio apartir de la linea si es asi se le agrega ala list
		}
		return servicios;	// Retorna la lista de servicios validos del txt
	}

	private ServicioDTO crear(Linea linea) {
		List<String> c = linea.getCampos(); // Se le asigna ala list la list que contiene los campos de una linea del txt
		
		if (c.size() != CAMPOS) {	// Verifica que contenga dicha linea el tamaño o campos de informacion correcta
			throw new ArchivoInvalidoException(archivo, linea.getNumero(),	// Si no es asi suelta una excepcion que faltan campos
					"Se esperaban " + CAMPOS + " campos y hay " + c.size());
		}
		try { // Se usa un try catch para validar que no haya errores ala hora de crear el objeto servicio y si es asi que no rompa el programa y solo tire una excepcion
			 ServicioDTO servi = new ServicioDTO(
					 c.get(0),
					 Double.parseDouble(c.get(1)),
					 Double.parseDouble(c.get(2)),
					Double.parseDouble(c.get(3)),
					TipoVehiculo.valueOf(c.get(4)),
					CategoriaVehiculo.valueOf(c.get(5)),
					TipoServicio.valueOf(c.get(6)));
			 
			 return servi;
			 
		} catch (IllegalArgumentException e) {
			// Se convierte en una excepcion que indica archivo y linea del error
			throw new ArchivoInvalidoException(archivo, linea.getNumero(), "valor invalido" + e.getMessage());
		}
	}
}