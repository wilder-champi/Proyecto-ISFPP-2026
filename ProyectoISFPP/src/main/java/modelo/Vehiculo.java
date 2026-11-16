package modelo;

import java.util.HashSet;
import java.util.Set;

public class Vehiculo {
	private String patente;
	private String modelo;
	private int capacidadPasajeros;
	private Ubicacion ubicacion;
	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;
	private Set<TipoServicio> tipoServicios;
	
	public Vehiculo(String patente, String modelo, int capacidadPasajeros, TipoVehiculo tipoVehiculo, CategoriaVehiculo categoriaVehiculo, TipoServicio tipoServicio) {
		this.patente = patente;
		this.modelo = modelo;
		this.capacidadPasajeros = capacidadPasajeros;
		this.tipoVehiculo = tipoVehiculo;
		this.categoriaVehiculo = categoriaVehiculo;
		tipoServicios = new HashSet<TipoServicio>();
		tipoServicios.add(tipoServicio);
	}
	
	public void agregarTipoServicio(TipoServicio tipoServicio) {
		tipoServicios.add(tipoServicio);
	}
	
	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public int getCapacidadPasajeros() {
		return capacidadPasajeros;
	}

	public void setCapacidadPasajeros(int capacidadPasajeros) {
		this.capacidadPasajeros = capacidadPasajeros;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	public void setCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {
		this.categoriaVehiculo = categoriaVehiculo;
	}

	public Set<TipoServicio> getTipoServicios() {
		return new HashSet<TipoServicio>(tipoServicios);
	}
	
	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}
	
}
