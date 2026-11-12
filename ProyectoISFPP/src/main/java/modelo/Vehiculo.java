package modelo;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import java.util.EnumSet;
import java.util.Set;

/**
 * Vehículo de un conductor, identificado por su patente, con su modelo, capacidad,
 * tipo, categoría, servicios que presta y ubicación.
 */
public class Vehiculo {

	private String patente;
	private String modelo;
	private int capacidadPasajeros;

	private CategoriaVehiculo categoriaVehiculo;
	private TipoVehiculo tipoVehiculo;
	private Set<TipoServicio> tipoServicio;
	private Ubicacion ubicacion;

	/**
	 * Crea un vehículo con su ubicación inicial.
	 *
	 * @param patente patente (6 o 7 caracteres, solo letras y números)
	 * @param modelo modelo del vehículo
	 * @param capacidadPasajeros cantidad de pasajeros (al menos 1)
	 * @param tipo tipo de vehículo
	 * @param categoria categoría del vehículo
	 * @param servicio1 primer tipo de servicio que presta
	 * @param ubicacion ubicación inicial
	 * @throws IllegalArgumentException si algún dato no es válido o la ubicación es {@code null}
	 */
	public Vehiculo(String patente, String modelo, int capacidadPasajeros, TipoVehiculo tipo,
			CategoriaVehiculo categoria, TipoServicio servicio1, Ubicacion ubicacion) {
	this(patente,modelo,capacidadPasajeros,tipo,categoria,servicio1);
		setUbicacion(ubicacion); // Valida y guarda la ubicacion resivida
	}

	/**
	 * Crea un vehículo sin ubicación inicial. La patente se guarda en mayúsculas y sin espacios en los extremos.
	 *
	 * @param patente patente (6 o 7 caracteres, solo letras y números)
	 * @param modelo modelo del vehículo
	 * @param capacidadPasajeros cantidad de pasajeros (al menos 1)
	 * @param tipo tipo de vehículo
	 * @param categoria categoría del vehículo
	 * @param servicio1 primer tipo de servicio que presta
	 * @throws IllegalArgumentException si algún dato no es válido
	 */
	public Vehiculo(String patente, String modelo, int capacidadPasajeros, TipoVehiculo tipo,
			CategoriaVehiculo categoria, TipoServicio servicio1) {

		if(patente == null || patente.isBlank()) {
			throw new IllegalArgumentException("la patente es obligatoria");
		}
		String patenteLimpia = patente.trim().toUpperCase();
		if (patenteLimpia.length() < 6 || patenteLimpia.length() > 7) {
			throw new IllegalArgumentException("la patente debe contar con almenos 6 o 7 caracteres");
		}
		for (int i = 0; i < patenteLimpia.length(); i++) {
			char c = patenteLimpia.charAt(i);//devueve elc aracter de la posicion i
			boolean esLetra = c >= 'A' && c <= 'Z';//pregunta si ese caracter esta entre la A y la Z
			boolean esNumero = c >= '0' && c <= '9';//pregunta si ese caracter esta entre el 0 y el 9
			if (!esLetra && !esNumero) {
				throw new IllegalArgumentException("la patente solo puede tener letras y numeros");
			}
		}
		if (modelo == null || modelo.isBlank()) {
			throw new IllegalArgumentException("el modelo es obligatorio");
		}
		if (capacidadPasajeros < 1) {
			throw new IllegalArgumentException("la capacidad de pasajeros debe ser al menos 1");
		}
		if (tipo == null) {
			throw new IllegalArgumentException("el tipo de vehiculo es obligatorio");
		}
		if (categoria == null) {
			throw new IllegalArgumentException("la categoria es obligatoria");
		}
		if (servicio1 == null) {
			throw new IllegalArgumentException("El vehiculo debe contar con al menos un tipo de servicio");
		}
		
		Set<TipoServicio> setServicios = EnumSet.noneOf(TipoServicio.class);
		if (servicio1 != null) {
			setServicios.add(servicio1);
		}
		if (setServicios.isEmpty()) {
			throw new IllegalArgumentException("El vehiculo debe contar con al menos un tipo de servicio");
		}
		this.patente = patenteLimpia;
		this.modelo = modelo.trim();
		this.capacidadPasajeros = capacidadPasajeros;
		this.categoriaVehiculo = categoria;
		this.tipoVehiculo = tipo;
		this.tipoServicio = setServicios;
	}

	/**
	 * Agrega un tipo de servicio que puede prestar el vehículo.
	 *
	 * @param tipoServicio tipo de servicio a agregar
	 * @throws IllegalArgumentException si el tipo de servicio es {@code null}
	 */
	public void agregarTipoServicio(TipoServicio tipoServicio) {

		if (tipoServicio == null) {
			throw new IllegalArgumentException("El tipo de servicio no puede ser null");
		}

		this.tipoServicio.add(tipoServicio);
	}
	
	/**
	 * Quita un tipo de servicio del vehículo. No hace nada si el vehículo no lo tenía.
	 *
	 * @param tipo tipo de servicio a quitar
	 * @throws IllegalArgumentException si es {@code null} o si es el único tipo de servicio del vehículo
	 */
	public void quitarTipoServicio(TipoServicio tipo) {
		if (tipo == null) {
			throw new IllegalArgumentException("El tipo de servicio no puede ser null");
		}
		if (!this.tipoServicio.contains(tipo)) {
			return; // ya no lo tenía no hay nada que quitar
		}
		if (this.tipoServicio.size() == 1) {
			throw new IllegalArgumentException("El vehiculo debe contar con al menos un tipo de servicio");
		}
		this.tipoServicio.remove(tipo);
	}

	/**
	 * Devuelve una copia de los tipos de servicio que presta el vehículo.
	 *
	 * @return conjunto de tipos de servicio (siempre tiene al menos uno)
	 */
	// Este metodo en si hace una copia asi no perdierdo la condicion del viaje que
	// mayuscula
	// como minimo tiene que tener un tipo de servicio
	public Set<TipoServicio> getTipoServicios() {
		return EnumSet.copyOf(tipoServicio);
	}

	/**
	 * Devuelve la ubicación del vehículo.
	 *
	 * @return ubicación actual, o {@code null} si todavía no tiene
	 */
	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	/**
	 * Establece la ubicación del vehículo.
	 *
	 * @param ubicacion nueva ubicación
	 * @throws IllegalArgumentException si la ubicación es {@code null}
	 */
	public void setUbicacion(Ubicacion ubicacion) {
		if (ubicacion == null) {
			throw new IllegalArgumentException("la ubicacion no puede ser null");
		}
		this.ubicacion = ubicacion;
	}

	/**
	 * Devuelve la patente.
	 *
	 * @return patente del vehículo
	 */
	public String getPatente() {
		return patente;
	}

	/**
	 * Devuelve el modelo.
	 *
	 * @return modelo del vehículo
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * Devuelve la capacidad de pasajeros.
	 *
	 * @return cantidad de pasajeros
	 */
	public int getCapacidadPasajeros() {
		return capacidadPasajeros;
	}

	/**
	 * Devuelve la categoría del vehículo.
	 *
	 * @return categoría del vehículo
	 */
	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	/**
	 * Devuelve el tipo de vehículo.
	 *
	 * @return tipo de vehículo
	 */
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

}
