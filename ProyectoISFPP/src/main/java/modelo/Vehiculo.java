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

		Set<TipoServicio> setServicios = EnumSet.noneOf(TipoServicio.class);

		if (servicio1 != null) {
			setServicios.add(servicio1);
		}

		if (setServicios.isEmpty()) {
			throw new IllegalArgumentException("El viaje debe contar con al menos un tipo de servicio");
		}
		this.patente = patente;
		this.modelo = modelo;
		this.capacidadPasajeros = capacidadPasajeros;
		this.categoriaVehiculo = categoria;
		this.tipoVehiculo = tipo;
		this.tipoServicio = setServicios;
		setUbicacion(ubicacion); // Valida y guarda la ubicacion resivida
	}

	public Vehiculo(String patente, String modelo, int capacidadPasajeros, TipoVehiculo tipo,
			CategoriaVehiculo categoria, TipoServicio servicio1) {

		Set<TipoServicio> setServicios = EnumSet.noneOf(TipoServicio.class);

		if (servicio1 != null) {
			setServicios.add(servicio1);
		}

		if (setServicios.isEmpty()) {
			throw new IllegalArgumentException("El vehiculo debe contar con al menos un tipo de servicio");
		}

		this.patente = patente;
		this.modelo = modelo;
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
