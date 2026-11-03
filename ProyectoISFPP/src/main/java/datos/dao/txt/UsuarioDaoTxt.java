package datos.dao.txt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import datos.dao.UsuarioDAO;
import datos.dto.UsuarioDTO;

//Estos son para cargar los archivos txt 
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class UsuarioDaoTxt implements UsuarioDAO {

	private static final int CAMPOS_USUARIO = 3; // Nombre, telefono y email
	private static final int POSICION_LICENCIA = 3; // Indice de la posicion de la licencia de conducir
	private static final int POSICION_PRIMERA_PATENTE = 4; // Indice de la posicion de la primer patente del vehiculo
															// que le pertenece

	private final String archivo;

	public UsuarioDaoTxt(String archivo) {
		this.archivo = archivo;
	}

	// Recibe el mapa de vehiculos para poder buscar cada patente
	@Override
	public List<UsuarioDTO> obtenerTodos() {

		List<UsuarioDTO> usuarios = new ArrayList<>(); // Crea una list para guardar los usuarios del archivo.txt
		Set<String> emails = new HashSet<>(); // Para detectar emails repetidos ya que set solo es una coleccion de
												// elementos unicos
		Set<String> patentesAsignadas = new HashSet<>(); // Para detectar vehiculos con dos dueños

		for (Linea linea : LectorArchivo.leer(archivo)) { // Con un for each recorremos la list con todas las lineas del
															// archivo
			UsuarioDTO usuario = crear(linea, patentesAsignadas); // Crea un objeto de tipo usuario
																	// pidiendole que lo haga el metodo
																	// crear que hace unas verificaciones
																	// antes de crearlo

			if (!emails.add(usuario.getEmail())) { // Add devuelve false si ya estaba
				throw new ArchivoInvalidoException(archivo, linea.getNumero(), "Email repetido " + usuario.getEmail());
			}
			usuarios.add(usuario); // Como es un usuario valido lo agrego a la list
		}
		return usuarios; // Retorno la list de usuarios con conductores en alta y usuarios normales
	}

	private UsuarioDTO crear(Linea linea, Set<String> patentesAsignadas) {

		List<String> c = linea.getCampos(); // Asigno la linea de datos del archivo

		int numero = linea.getNumero(); // Asigno de que numero de linea del archivo son dichos datos

		if (c.size() < CAMPOS_USUARIO) { // Me fijo si dicha linea cuenta con la cantidad minima de datos para ser un
											// usuario, si no es asi lanzo una excepcion
			throw new ArchivoInvalidoException(archivo, numero, "Faltan datos (nombre, telefono y email)");
		}

		// Lista para almacenar las patentes del conductor
		List<String> patentes = new ArrayList<>();

		// Si tiene solo 3 campos, es un usuario sin conductor
		if (c.size() == CAMPOS_USUARIO) {
			return new UsuarioDTO(c.get(0), c.get(1), c.get(2), null, patentes);
		}

		// Si tiene licencia pero ninguna patente
		if (c.size() == POSICION_PRIMERA_PATENTE) {
			throw new ArchivoInvalidoException(archivo, numero, "Tiene licencia pero ninguna patente");
		}

		// Recorre las patentes desde la posicion 4
		for (String patente : c.subList(POSICION_PRIMERA_PATENTE, c.size())) {

			// Verifica que no haya sido asignada anteriormente
			if (!patentesAsignadas.add(patente)) {
				throw new ArchivoInvalidoException(archivo, numero, "La patente " + patente + " ya fue asignada");
			}

			// Agrega la patente a la lista del usuario
			patentes.add(patente);
		}
		// Crea el dto con licencia y patentes
		UsuarioDTO usuario = new UsuarioDTO(c.get(0), c.get(1), c.get(2), c.get(POSICION_LICENCIA), patentes);
		return usuario;
	}

	@Override
	public void guardar(UsuarioDTO usuario) {
		// Con append en true la linea se agrega al final sin borrar lo que ya habia en el archivo
		try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo, true))) {
			//se crea una nueva linea de texto basicas de un usuario con sus tres casillas
			StringBuilder linea = new StringBuilder();//StringBuilder sirve para ir pegando partes sin tener que crear sin tener que crear un texto nuevo cada ves 
			linea.append(usuario.getNombre()).append(";").append(usuario.getTelefono()).append(";")
					.append(usuario.getEmail());

			// Si tiene licencia es conductor: agrego licencia y patentes
			if (usuario.getLicencia() != null) {
				linea.append(";").append(usuario.getLicencia());
				for (String patente : usuario.getPatentes()) {
					linea.append(";").append(patente);
				}
			}
			//escribe la linea completa
			escritor.println(linea);
		} catch (IOException e) {
			throw new IllegalStateException("no se pudo guardar el archivo " + archivo);
		}
	}

}