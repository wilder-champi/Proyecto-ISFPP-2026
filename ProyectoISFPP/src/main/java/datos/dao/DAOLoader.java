package datos.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import datos.dao.bd.BdDAOFactory;
import datos.dao.txt.TxtDAOFactory;

//import logica.GeneradorUbicacion;

/**
 * Lee el archivo de configuración y crea la fábrica de DAO correspondiente al tipo de persistencia
 * indicado (archivos de texto o base de datos).
 */
public class DAOLoader {

	/**
	 * Nombre del archivo de configuración que se usa por defecto.
	 */
	public static final String ARCHIVO_POR_DEFECTO = "config.properties"; // Asigno al atributo el nombre del archivo

	private static DAOFactory fabrica; // Guarda la fabrica ya creada para no volver a leer el archivo

	/**
	 * Devuelve la fábrica de DAO. La primera vez la crea con el archivo de configuración por defecto
	 * y luego reutiliza la misma.
	 *
	 * @return la fábrica de DAO configurada
	 * @throws IllegalStateException si no se puede leer la configuración o falta algún valor
	 */
	// Si todavia no hay fabrica le delega a crearFactory el archivo por defecto para que la cree
	public static DAOFactory getFactory() {
		if (fabrica == null) {
			fabrica = crearFactory(ARCHIVO_POR_DEFECTO);
		}
		return fabrica;
	}

	/**
	 * Crea una fábrica de DAO a partir de un archivo de configuración. Con {@code persistencia=TXT}
	 * (valor por defecto) usa archivos de texto y con {@code persistencia=BD} usa la base de datos PostgreSQL.
	 *
	 * @param archivo ruta del archivo de configuración
	 * @return la fábrica de DAO correspondiente
	 * @throws IllegalStateException si no se puede leer el archivo, falta una clave o el tipo de persistencia no está soportado
	 */
	// Recibe el nombre del archivo (el por defecto o uno desde un test) y con lo que lee crea la fabrica de txt
	public static DAOFactory crearFactory(String archivo) {
		Properties diccionario = cargar(archivo);
		// retorna el valor de las latitudes y longitudes que son el parametro de madryn
		/*GeneradorUbicacion generador = new GeneradorUbicacion(numero(diccionario, "latitud1"),
				numero(diccionario, "longitud1"), numero(diccionario, "latitud2"), numero(diccionario, "longitud2"));*/

		 // Obtiene el tipo de persistencia configurado.
        // Si no se indica, utiliza TXT por defecto.
        String tipo = diccionario.getProperty("persistencia", "TXT")
                .trim().toUpperCase(Locale.ROOT);
        
        switch (tipo) {

        case "TXT":
		
		// Le pasa a la fabrica los nombres de los archivos txt
		return new TxtDAOFactory(texto(diccionario, "servicio"),
				texto(diccionario, "vehiculo"),
				texto(diccionario, "usuario") /*, generador*/);
		
        case "BD":
            // Crea los DAO que trabajan con PostgreSQL
            return new BdDAOFactory(
                texto(diccionario, "bd.url"),
                texto(diccionario, "bd.usuario"),
                texto(diccionario, "bd.password")
            );
		
        default:
            throw new IllegalStateException(
                "Tipo de persistencia no soportado: " + tipo);
    }   
            
 }

	/**
	 * Carga el archivo de configuración en un conjunto de propiedades.
	 *
	 * @param archivo ruta del archivo de configuración
	 * @return las propiedades leídas del archivo
	 * @throws IllegalStateException si no se puede leer el archivo
	 */
	private static Properties cargar(String archivo) {
		Properties diccionario = new Properties(); // Creo un diccionario que almacena clave valor que se usara para que lea el archivo
		try (FileInputStream entrada = new FileInputStream(archivo)) { // Abre el archivo que se le pase o desde getFactory o desde un test
			diccionario.load(entrada); // Lee el archivo abierto y carga en el diccionario por clave valor ignorando comentarios o lineas vacias
		} catch (IOException e) {
			throw new IllegalStateException("No se pudo leer la configuracion " + archivo);
		}	
		return diccionario;
	}

	/**
	 * Devuelve el valor de una propiedad, sin espacios en los extremos.
	 *
	 * @param diccionario propiedades cargadas de la configuración
	 * @param clave nombre de la propiedad buscada
	 * @return el valor de la propiedad
	 * @throws IllegalStateException si la propiedad falta o está vacía
	 */
	// Devuelve el valor de una propiedad o avisa cual falta
	private static String texto(Properties diccionario, String clave) {
		String valor = diccionario.getProperty(clave); // Se le asigna el valor que esta guardado en el diccionario a partir de la clave
		if (valor == null || valor.trim().isEmpty()) { // Valida que el valor no sea null y que el valor no este vacio
			throw new IllegalStateException("Falta el valor de la clave '" + clave + "' en la configuracion");
		}
		return valor.trim(); // Retorna el valor quitandole los espacios
	}

	/*private static double numero(Properties diccionario, String clave) {
		try {
			return Double.parseDouble(texto(diccionario, clave)); // se le delega al metodo texto la validacion y busqueda del valor con dicha clave y se hace un parse para que el valor sea un numero double
		} catch (NumberFormatException e) {
			throw new IllegalStateException("el valor de la clave '" + clave + "' no es un numero");
		} // si este no pudo ser asignado porque no era un numero se lanza una excepcion
	}*/
}