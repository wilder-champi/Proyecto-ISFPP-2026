package modelo;

import java.util.ArrayList;
import java.util.List;

import enums.CategoriaVehiculo;
import enums.EstadoConductor;
import logica.GeneradorUbicacion;

/**
 * Rol de conductor de un usuario: guarda su licencia, sus vehículos, su historial de viajes,
 * su estado y la categoría mínima de viajes que acepta.
 */
public class Conductor {

	private String licenciaConducir;
	private List<Vehiculo> vehiculos; // Declaramos los vehiculos pertenecientes al conductor/usuario
	private List<Viaje> viajes; // Declaramos

	private Vehiculo vehiculoActivo;
	private EstadoConductor estadoconductor;
	private CategoriaVehiculo categoriaVehiculoActivo;
	private CategoriaVehiculo categoriaMinimaAceptada;

	/**
	 * Crea un conductor fuera de servicio con su primer vehículo como vehículo activo.
	 *
	 * @param licenciaConducir licencia de conducir (obligatoria)
	 * @param vehiculo primer vehículo del conductor
	 * @throws IllegalArgumentException si la licencia está vacía o el vehículo es {@code null}
	 */
	public Conductor(String licenciaConducir, Vehiculo vehiculo) {
		if (licenciaConducir == null || licenciaConducir.isBlank()) {//validamos que la licencia de conducir no sea nula o que no este vacia
			throw new IllegalArgumentException("la licencia de conducir es obligatoria");
		}
		if (vehiculo == null) {//validamos que el vehiculo no sea null
			throw new IllegalArgumentException("el conductor debe tener al menos un vehículo");
		}
		this.licenciaConducir = licenciaConducir;
		vehiculos = new ArrayList<Vehiculo>(); // Creamos donde se guardaran los vehiculos del conductor
		agregarVehiculo(vehiculo);
		viajes = new ArrayList<Viaje>(); // Creamos donde se guardara el historial de viajes del conductor
		this.vehiculoActivo = vehiculo;
		this.estadoconductor = EstadoConductor.FUERA_DE_SERVICIO;//asignamos que el conductor esta fuera de servicio
		this.categoriaVehiculoActivo = vehiculo.getCategoriaVehiculo();
		this.categoriaMinimaAceptada = vehiculo.getCategoriaVehiculo();

	}

	/**
	 * Devuelve la licencia de conducir.
	 *
	 * @return licencia de conducir
	 */
	public String getLicenciaConducir() {
		return licenciaConducir;
	}

	/**
	 * Devuelve una copia de la lista de vehículos del conductor.
	 *
	 * @return lista de vehículos
	 */
	public ArrayList<Vehiculo> getVehiculos() {
		return new ArrayList<>(vehiculos);
	}

	/**
	 * Devuelve una copia del historial de viajes del conductor.
	 *
	 * @return lista de viajes
	 */
	public ArrayList<Viaje> getViajes() {
		return new ArrayList<>(viajes);
	}

	/**
	 * Devuelve el vehículo con el que está trabajando el conductor.
	 *
	 * @return vehículo activo
	 */
	public Vehiculo getVehiculoActivo() {
		return vehiculoActivo;
	}

	/**
	 * Cambia el vehículo activo, que debe pertenecer al conductor, y ajusta las categorías.
	 * El conductor debe estar fuera de servicio.
	 *
	 * @param vehiculoActivo vehículo que pasa a ser el activo
	 * @throws IllegalArgumentException si el vehículo es {@code null} o no pertenece al conductor
	 * @throws IllegalStateException si el conductor no está fuera de servicio
	 */
	public void setVehiculoActivo(Vehiculo vehiculoActivo) {
		if (vehiculoActivo == null) {//validamos que el vehiculo que se va a cambiar  no se anull
			throw new IllegalArgumentException("el vehículo activo no puede ser null");
		}
		if (!vehiculos.contains(vehiculoActivo)) {// y validamos que dicho vehiculo pertenesca al conductor
			throw new IllegalArgumentException("el vehículo no pertenece a este conductor");
		}
		if (this.estadoconductor != EstadoConductor.FUERA_DE_SERVICIO) {//validamos que el conductor este fuera de servicio par arealisar el cambio
			throw new IllegalStateException("debe estar fuera de servicio para cambiar de vehículo");
		}
		//asignamos al conductor el vehiculo que va a usar y su categoria
		this.vehiculoActivo = vehiculoActivo;
		this.categoriaVehiculoActivo = vehiculoActivo.getCategoriaVehiculo();
		this.categoriaMinimaAceptada = vehiculoActivo.getCategoriaVehiculo();

	}

	/**
	 * Devuelve el estado actual del conductor.
	 *
	 * @return estado del conductor
	 */
	public EstadoConductor getEstadoConductor() {
		return estadoconductor;
	}

	/**
	 * Establece el estado del conductor.
	 *
	 * @param estadoconductor nuevo estado
	 */
	public void setEstado(EstadoConductor estadoconductor) {
		this.estadoconductor = estadoconductor;
	}

	/**
	 * Devuelve la categoría del vehículo activo.
	 *
	 * @return categoría del vehículo activo
	 */
	public CategoriaVehiculo getCategoriaVehiculoActivo() {
		return categoriaVehiculoActivo;
	}

	/**
	 * Establece la categoría del vehículo activo.
	 *
	 * @param categoriaVehiculoActivo categoría del vehículo activo
	 */
	public void setCategoriaVehiculoActivo(CategoriaVehiculo categoriaVehiculoActivo) {
		this.categoriaVehiculoActivo = categoriaVehiculoActivo;
	}

	/**
	 * Devuelve la categoría mínima de viaje que acepta el conductor.
	 *
	 * @return categoría mínima aceptada
	 */
	public CategoriaVehiculo getCategoriaMinimaAceptada() {
		return categoriaMinimaAceptada;
	}
	

	/**
	 * Establece la categoría mínima de viaje que acepta el conductor.
	 *
	 * @param categoriaMinimaAceptada categoría mínima aceptada
	 * @throws IllegalArgumentException si es {@code null} o mayor a la categoría del vehículo activo
	 */
	public void setCategoriaMinimaAceptada(CategoriaVehiculo categoriaMinimaAceptada) {
		if (categoriaMinimaAceptada == null) {
			throw new IllegalArgumentException("La categoría mínima no puede ser null");
		}
		if (categoriaMinimaAceptada.getValor() > this.categoriaVehiculoActivo.getValor()) {
			throw new IllegalArgumentException("No puedes aceptar viajes de una categoría mayor a tu vehículo activo");
		}
		this.categoriaMinimaAceptada = categoriaMinimaAceptada;
	}
	

	/**
	 * Agrega un vehículo a la lista del conductor si todavía no estaba cargado.
	 *
	 * @param vehiculo vehículo a agregar
	 * @throws IllegalArgumentException si el vehículo es {@code null}
	 */
	public void agregarVehiculo(Vehiculo vehiculo) {

		if (vehiculo == null) {// revisa que el vehiculo no sea null
			throw new IllegalArgumentException("el conductor tiene que tener al menos un vehiculo");
		}
		if (!vehiculos.contains(vehiculo)) {// verifiac si el vehiculo no fue previamente cargado ala lista
			vehiculos.add(vehiculo);// si es asi lo carga
		}
	}

	/**
	 * Agrega un viaje al historial del conductor.
	 *
	 * @param viaje viaje a agregar
	 * @throws IllegalArgumentException si el viaje es {@code null}
	 */
	public void agregarViaje(Viaje viaje) {

		if (viaje == null) {// verifiac que el viaje no sea null
			throw new IllegalArgumentException("el viaje no puede ser null");
		}
		viajes.add(viaje);// guarda el historial de viajes del conductor
	}

	/**
	 * Pone al conductor disponible y ubica su vehículo activo en un punto aleatorio.
	 *
	 * @param generador generador que entrega la ubicación del vehículo
	 * @throws IllegalArgumentException si el generador es {@code null}
	 * @throws IllegalStateException si no hay un vehículo activo seleccionado
	 */
	public void ponerDisponible(GeneradorUbicacion generador) {
		if (generador == null) {
			throw new IllegalArgumentException("el generador de ubicaciones no puede ser null");
		}
		if (this.vehiculoActivo == null) {
			throw new IllegalStateException("no hay un vehículo activo seleccionado");
		}

		Ubicacion ubicacionAleatoria = generador.generar();
		this.vehiculoActivo.setUbicacion(ubicacionAleatoria);
		this.estadoconductor = EstadoConductor.DISPONIBLE;

	}


}
