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

import java.util.HashSet;
import java.util.Set;

public class SistemaGuardado {

	private final List<Servicio> servicios;
	private final Map<String, Vehiculo> vehiculos;
	private final List<Usuario> usuarios;
	private final DAOFactory fabrica;   // junto a los otros atributos

	public SistemaGuardado() {
		this(DAOLoader.getFactory());
	}

	public SistemaGuardado(DAOFactory fabrica) {
		this.fabrica = fabrica;
		
		// Obtiene los dto mediante los DAO
		List<ServicioDTO> serviciosDTO = fabrica.getServicioDAO().obtenerTodos();
		Map<String, VehiculoDTO> vehiculosDTO = fabrica.getVehiculoDAO().obtenerTodos();
		List<UsuarioDTO> usuariosDTO = fabrica.getUsuarioDAO().obtenerTodos();

		// Convierte los servicios dto al modelo
		servicios = new ArrayList<>();

		for (ServicioDTO dto : serviciosDTO) {

			Servicio servicio = new Servicio(dto.getNombre(), dto.getTarifaBase(), dto.getPrecioKm(),
					dto.getPrecioMinuto(), dto.getTipoVehiculo(), dto.getCategoriaVehiculo(), dto.getTipoServicio());
			servicios.add(servicio);
		}
		// Convierte los vehiculos dto al modelo
		vehiculos = new LinkedHashMap<>();

		for (VehiculoDTO dto : vehiculosDTO.values()) {
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
		}
		// Convierte los usuarios dto al modelo
		usuarios = new ArrayList<>();

		for (UsuarioDTO dto : usuariosDTO) {
			Usuario usuario = new Usuario(dto.getNombre(), dto.getTelefono(), dto.getEmail());

			// Si tiene lisencia, se registra como conductor
			if (dto.getLicencia() != null) {

				List<String> patentes = dto.getPatentes();

				if (patentes == null || patentes.isEmpty()) {
					throw new IllegalArgumentException("El conductor no tiene vehiculos");
				}
				// Busca el primer vehiculo en el mapa
				Vehiculo primero = vehiculos.get(patentes.get(0));

				if (primero == null) {
					throw new IllegalArgumentException("Patente inexistente: " + patentes.get(0));
				}

				usuario.altaConductor(dto.getLicencia(), primero);

				// Agrega los demas vehiculos
				for (int i = 1; i < patentes.size(); i++) {
					Vehiculo extra = vehiculos.get(patentes.get(i));

					if (extra == null) {
						throw new IllegalArgumentException("Patente inexistente: " + patentes.get(i));
					}
					usuario.getConductor().agregarVehiculo(extra);
				}
			}
			usuarios.add(usuario);
		}
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	public Map<String, Vehiculo> getVehiculos() {
		return vehiculos;
	}

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
				if (vehiculos.containsKey(v.getPatente()) || !patentes.add(v.getPatente())) {
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
	
	private VehiculoDTO creeDTO(Vehiculo v) {
		//retorna un vehiculo dto con todos los atributos del vehiculo cargado
		return new VehiculoDTO(v.getPatente(), v.getModelo(), v.getCapacidadPasajeros(), v.getTipoVehiculo(),
				v.getCategoriaVehiculo(), new ArrayList<>(v.getTipoServicios()));
	}
}