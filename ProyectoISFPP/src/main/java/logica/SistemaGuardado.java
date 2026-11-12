	package logica;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import datos.dao.DAOFactory;
import datos.dao.DAOLoader;
import datos.dto.ServicioDTO;
import datos.dto.UsuarioDTO;
import datos.dto.VehiculoDTO;
import enums.TipoServicio;
import modelo.Servicio;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.Viaje;

import java.util.HashSet;
import java.util.Set;

/**
 * Sistema central de la aplicación. Carga los servicios, los vehículos y los usuarios desde la fuente de datos,
 * los convierte a objetos del modelo y se encarga de registrar usuarios y vehículos nuevos y de
 * guardar los viajes pendientes.
 */
public class SistemaGuardado {

	private final List<Servicio> servicios;
	private final Map<String, Vehiculo> vehiculos;
	private final List<Usuario> usuarios;
	private final DAOFactory fabrica;   // junto a los otros atributos
	
	private final List<Viaje> viajesPendientes;
	private final GeneradorUbicacion generadorUbicacion;

	/**
	 * Crea el sistema con la fábrica de DAO definida en el archivo de configuración.
	 */
	public SistemaGuardado() {
		this(DAOLoader.getFactory());
	}

	/**
	 * Crea el sistema cargando los datos desde la fábrica de DAO indicada. Los vehículos y usuarios con datos
	 * inválidos se omiten y la carga continúa con los demás.
	 *
	 * @param fabrica fábrica que entrega los DAO de servicios, vehículos y usuarios
	 */
	public SistemaGuardado(DAOFactory fabrica) {
		this.fabrica = fabrica;
		
		this.generadorUbicacion = new GeneradorUbicacion(-42.87689, -65.20872, -42.57303, -64.78616);
		
		this.viajesPendientes = new ArrayList<>();

		
		// Obtiene los dto mediante los DAO
		List<ServicioDTO> serviciosDTO = fabrica.getServicioDAO().obtenerTodos();
		Map<String, VehiculoDTO> vehiculosDTO = fabrica.getVehiculoDAO().obtenerTodos();
		List<UsuarioDTO> usuariosDTO = fabrica.getUsuarioDAO().obtenerTodos();

		// Convierte los servicios dto al modelo
		this.servicios = new ArrayList<>();

		for (ServicioDTO dto : serviciosDTO) {

			Servicio servicio = new Servicio(dto.getNombre(), dto.getTarifaBase(), dto.getPrecioKm(),
					dto.getPrecioMinuto(), dto.getTipoVehiculo(), dto.getCategoriaVehiculo(), dto.getTipoServicio());
			servicios.add(servicio);
		}
		// Convierte los vehiculos dto al modelo
		this.vehiculos = new LinkedHashMap<>();

		for (VehiculoDTO dto : vehiculosDTO.values()) {
			try {
				List<TipoServicio> tipos = dto.getTiposServicio();

				if (tipos == null || tipos.isEmpty()) {
					throw new IllegalArgumentException("El vehiculo no tiene servicios");
				}
				// Crea el vehiculo con su primer servicio
				Vehiculo vehiculo = new Vehiculo(dto.getPatente(), dto.getModelo(), dto.getCapacidadPasajeros(),
						dto.getTipoVehiculo(), dto.getCategoriaVehiculo(), tipos.get(0));
				// Agrega los otros servicios
				for (int i = 1; i < tipos.size(); i++) {
					vehiculo.agregarTipoServicio(tipos.get(i));
				}
				vehiculos.put(vehiculo.getPatente(), vehiculo);
			} catch (IllegalArgumentException e) {
				// un vehiculo con datos invalidos se saltea y la carga sigue con los demas
				System.out.println("vehiculo omitido (" + dto.getPatente() + "): " + e.getMessage());
			}
		}
		// Convierte los usuarios dto al modelo
		this.usuarios = new ArrayList<>();

		for (UsuarioDTO dto : usuariosDTO) {
			try {
				Usuario usuario = new Usuario(dto.getNombre(), dto.getTelefono(), dto.getEmail());

				// Si tiene licencia se registra como conductor
				if (dto.getLicencia() != null) {
					List<String> patentes = dto.getPatentes();

					if (patentes == null || patentes.isEmpty()) {
						throw new IllegalArgumentException("El conductor no tiene vehiculos");
					}
					Vehiculo primero = vehiculos.get(patentes.get(0));

					if (primero == null) {
						throw new IllegalArgumentException("Patente inexistente: " + patentes.get(0));
					}
					usuario.altaConductor(dto.getLicencia(), primero);

					for (int i = 1; i < patentes.size(); i++) {
						Vehiculo extra = vehiculos.get(patentes.get(i));

						if (extra == null) {
							throw new IllegalArgumentException("Patente inexistente: " + patentes.get(i));
						}
						usuario.getConductor().agregarVehiculo(extra);
					}
				}
				usuarios.add(usuario);
			} catch (IllegalArgumentException e) {
				// un usuario con datos invalidos se saltea y la carga sigue con los demas
				System.out.println("usuario omitido (" + dto.getEmail() + "): " + e.getMessage());
			}
		}
	}
	
	
	

	/**
	 * Devuelve los servicios cargados.
	 *
	 * @return lista de servicios
	 */
	public List<Servicio> getServicios() {
		return servicios;
	}

	/**
	 * Devuelve los vehículos cargados.
	 *
	 * @return mapa de vehículos cuya clave es la patente
	 */
	public Map<String, Vehiculo> getVehiculos() {
		return vehiculos;
	}

	/**
	 * Devuelve los usuarios cargados.
	 *
	 * @return lista de usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	/*
	// Pasa los usuarios del modelo a DTO y le pide al DAO que los escriba
	public void guardarUsuarios() {
		List<UsuarioDTO> dtos = new ArrayList<>();

		for (Usuario u : usuarios) {
			String licencia = null;
			List<String> patentes = new ArrayList<>();

			if (u.getConductor() != null) {
				licencia = u.getConductor().getLicenciaConducir();
				for (Vehiculo v : u.getConductor().getVehiculos()) {
					patentes.add(v.getPatente());
				}
			}
			dtos.add(new UsuarioDTO(u.getNombre(), u.getTelefono(), u.getEmail(), licencia, patentes));
		}
		fabrica.getUsuarioDAO().guardarTodos(dtos);
	}*/

	/**
	 * Registra un usuario nuevo y, si es conductor, también sus vehículos. Primero se guardan los vehículos
	 * y el usuario en la fuente de datos y recién después se agregan a la memoria.
	 *
	 * @param nuevo usuario a registrar
	 * @throws IllegalArgumentException si el email ya existe o alguna patente ya está registrada
	 */
	public void registrarUsuario(Usuario nuevo) {
		//se valida que el mail del usuario no este repetido ya que es algo unico para cada usuario
		for (Usuario u : usuarios) {
			if (u.getEmail().equalsIgnoreCase(nuevo.getEmail())) {
				throw new IllegalArgumentException("ya existe un usuario con ese email");
			}
		}
		//se valida que las patentes nuevas no esten oreviamente cargadas en el sistema o por el mismo usuario
		List<Vehiculo> vehiculosNuevos = new ArrayList<>();
		if (nuevo.getConductor() != null) {
			Set<String> patentes = new HashSet<>();
			for (Vehiculo v : nuevo.getConductor().getVehiculos()) {
				if (vehiculos.containsKey(v.getPatente()) || !patentes.add(v.getPatente())) {//
					throw new IllegalArgumentException("la patente " + v.getPatente() + " ya esta registrada");
				}
				vehiculosNuevos.add(v);
			}
		}
		//primero cargo los vehiculos asi no queda un usuario con un vehiculo no registrado
		for (Vehiculo v : vehiculosNuevos) {
			fabrica.getVehiculoDAO().guardar(creeDTO(v));
		}
		fabrica.getUsuarioDAO().guardar(creeDTO(nuevo));
		//si no se llegoa guardar algunos de los dichos datos la lista sigue como hasta ahora tanto como en vehiculos como en usuarios
		for (Vehiculo v : vehiculosNuevos) {
			vehiculos.put(v.getPatente(), v);
		}
		usuarios.add(nuevo);
	}

	/**
	 * Convierte un usuario del modelo en un DTO, con su licencia y las patentes de sus vehículos si es conductor.
	 *
	 * @param u usuario a convertir
	 * @return el DTO del usuario
	 */
	private UsuarioDTO creeDTO(Usuario u) {
		String licencia = null;
		List<String> patentes = new ArrayList<>();

		if (u.getConductor() != null) {//si el usuario no es un conductor este solo se guardara con la u¿informacion basica y la licencia en null y una lista de vehiculos vacia ya que no contaria con ninguno
		
		//si el usuario es un conductor entonces se le carga tanto su licencia como los vehiculos que le pertenecen	
			licencia = u.getConductor().getLicenciaConducir();
			for (Vehiculo v : u.getConductor().getVehiculos()) {
				patentes.add(v.getPatente());
			}
		}
		//de vuelvo un usuario dto con todos los atributos del usuario cargado 
		return new UsuarioDTO(u.getNombre(), u.getTelefono(), u.getEmail(), licencia, patentes);
	}
	
	/**
	 * Convierte un vehículo del modelo en un DTO.
	 *
	 * @param v vehículo a convertir
	 * @return el DTO del vehículo
	 */
	private VehiculoDTO creeDTO(Vehiculo v) {
		//retorna un vehiculo dto con todos los atributos del vehiculo cargado
		return new VehiculoDTO(v.getPatente(), v.getModelo(), v.getCapacidadPasajeros(), v.getTipoVehiculo(),
				v.getCategoriaVehiculo(), new ArrayList<>(v.getTipoServicios()));
	}
	
	/**
	 * Agrega un viaje a la lista de viajes pendientes si todavía no estaba.
	 *
	 * @param viaje viaje a agregar
	 * @throws IllegalArgumentException si el viaje es {@code null}
	 */
	public void agregarViajePendiente(Viaje viaje) {
		if (viaje == null) {
			throw new IllegalArgumentException("el viaje no puede ser null");
		}
		if (!viajesPendientes.contains(viaje)) {
			viajesPendientes.add(viaje);
		}
	}
	
	/**
	 * Devuelve una copia de la lista de viajes pendientes.
	 *
	 * @return lista de viajes pendientes
	 */
	public List<Viaje> getViajesPendientes() {
		return new ArrayList<>(viajesPendientes);
	}
	/**
	 * Quita un viaje de la lista de viajes pendientes.
	 *
	 * @param viaje viaje a quitar
	 */
	public void removerViajePendiente(Viaje viaje) {
		viajesPendientes.remove(viaje);
	}
	/**
	 * Devuelve el generador de ubicaciones aleatorias, que cubre la zona de Puerto Madryn.
	 *
	 * @return generador de ubicaciones
	 */
	public GeneradorUbicacion getGeneradorUbicacion() {
		return generadorUbicacion;
	}
	
	/**
	 * Busca un usuario por su email, sin distinguir mayúsculas de minúsculas.
	 *
	 * @param email email del usuario buscado
	 * @return el usuario encontrado, o {@code null} si el email está vacío o no existe
	 */
	public Usuario buscarPorEmail(String email) {
		if (email == null || email.isBlank()) {
			return null;
		}
		for (Usuario u : getUsuarios()) {
			if (u.getEmail().equalsIgnoreCase(email.trim())) {
				return u;
			}
		}
		return null;
	}

	/**
	 * Agrega un vehículo nuevo a un conductor. Primero se guarda en la fuente de datos y, si salió bien,
	 * se actualiza la memoria.
	 *
	 * @param usuario conductor al que se le agrega el vehículo
	 * @param vehiculo vehículo a agregar
	 * @throws IllegalArgumentException si el usuario no es conductor o la patente ya está registrada
	 */
	public void agregarVehiculoAConductor(Usuario usuario, Vehiculo vehiculo) {
		if (usuario == null || usuario.getConductor() == null) {
			throw new IllegalArgumentException("el usuario no es conductor");
		}
		if (vehiculos.containsKey(vehiculo.getPatente())) {
			throw new IllegalArgumentException("la patente " + vehiculo.getPatente() + " ya esta registrada");
		}

		// armo todos los usuarios, con la patente nueva en el que corresponde
		List<UsuarioDTO> dtos = new ArrayList<>();
		for (Usuario u : usuarios) {
			dtos.add(u == usuario ? creeDTO(u, vehiculo) : creeDTO(u));
		}

		// primero se guardan los datos y recién si salió bien se cambia la memoria
		fabrica.getVehiculoDAO().guardar(creeDTO(vehiculo));
		fabrica.getUsuarioDAO().guardarTodos(dtos);

		usuario.getConductor().agregarVehiculo(vehiculo);
		vehiculos.put(vehiculo.getPatente(), vehiculo);
	}

	/**
	 * Convierte un usuario en un DTO agregando a sus patentes la de un vehículo extra.
	 *
	 * @param u usuario a convertir
	 * @param extra vehículo cuya patente se suma a las del usuario
	 * @return el DTO del usuario con la patente nueva
	 */
	private UsuarioDTO creeDTO(Usuario u, Vehiculo extra) {
		UsuarioDTO base = creeDTO(u);
		List<String> patentes = new ArrayList<>(base.getPatentes());
		patentes.add(extra.getPatente());
		return new UsuarioDTO(base.getNombre(), base.getTelefono(), base.getEmail(), base.getLicencia(), patentes);
	}
	
	
	
	
	
}