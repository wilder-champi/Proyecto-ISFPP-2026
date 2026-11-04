package modelo;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;
import java.util.EnumSet;
import java.util.Set;

public class Vehiculo {

	private String patente;
	private String modelo;
	private int capacidadPasajeros;

	private CategoriaVehiculo categoriaVehiculo;
	private TipoVehiculo tipoVehiculo;
	private Set<TipoServicio> tipoServicio;
	private Ubicacion ubicacion;

	public Vehiculo(String patente, String modelo, int capacidadPasajeros, TipoVehiculo tipo,
			CategoriaVehiculo categoria, TipoServicio servicio1, Ubicacion ubicacion) {
	this(patente,modelo,capacidadPasajeros,tipo,categoria,servicio1);
		setUbicacion(ubicacion); // Valida y guarda la ubicacion resivida
	}

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

	public void agregarTipoServicio(TipoServicio tipoServicio) {

		if (tipoServicio == null) {
			throw new IllegalArgumentException("El tipo de servicio no puede ser null");
		}

		this.tipoServicio.add(tipoServicio);
	}

	// Este metodo en si hace una copia asi no perdierdo la condicion del viaje que
	// mayuscula
	// como minimo tiene que tener un tipo de servicio
	public Set<TipoServicio> getTipoServicios() {
		return EnumSet.copyOf(tipoServicio);
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		if (ubicacion == null) {
			throw new IllegalArgumentException("la ubicacion no puede ser null");
		}
		this.ubicacion = ubicacion;
	}

	public String getPatente() {
		return patente;
	}

	public String getModelo() {
		return modelo;
	}

	public int getCapacidadPasajeros() {
		return capacidadPasajeros;
	}

	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

}
