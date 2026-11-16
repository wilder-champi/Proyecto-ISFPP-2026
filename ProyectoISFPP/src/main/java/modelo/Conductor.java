package modelo;

import java.util.ArrayList;
import java.util.List;

public class Conductor {

	private String licenciaConducir;
	private EstadoConductor estadoConductor;
	private Vehiculo vehiculoActivo;
	private CategoriaVehiculo categoriaVehiculoActivo;
	private List<Viaje> viajes;
	private List<Vehiculo> vehiculos;

	public Conductor(String licenciaConducir, Vehiculo vehiculo) {
		this.estadoConductor = EstadoConductor.FUERA_DE_SERVICIO;
		this.licenciaConducir = licenciaConducir;
		this.vehiculoActivo = vehiculo;
		this.categoriaVehiculoActivo = vehiculo.getCategoriaVehiculo();
		vehiculos = new ArrayList<Vehiculo>();
		viajes = new ArrayList<Viaje>();
		agregarVehiculo(vehiculo);
	}

	public void agregarViaje(Viaje viaje) {
		viajes.add(viaje);
	}

	public void agregarVehiculo(Vehiculo vehiculo) {
		vehiculos.add(vehiculo);
	}

	public String getLicenciaConducir() {
		return licenciaConducir;
	}

	public void setLicenciaConducir(String licenciaConducir) {
		this.licenciaConducir = licenciaConducir;
	}

	public EstadoConductor getEstadoConductor() {
		return estadoConductor;
	}

	public void setEstado(EstadoConductor estadoConductor) {
		this.estadoConductor = estadoConductor;
	}

	public Vehiculo getVehiculoActivo() {
		return vehiculoActivo;
	}

	public void setVehiculoActivo(Vehiculo vehiculoActivo) {
		this.vehiculoActivo = vehiculoActivo;
	}

	public List<Vehiculo> getVehiculos() {
		return new ArrayList<Vehiculo>(vehiculos);
	}

	public List<Viaje> getViajes() {
		return new ArrayList<Viaje>(viajes);
	}

	public CategoriaVehiculo getCategoriaVehiculoActivo() {
		return categoriaVehiculoActivo;
	}

	public void setCategoriaVehiculoActivo(CategoriaVehiculo categoriaVehiculoActivo) {
		this.categoriaVehiculoActivo = categoriaVehiculoActivo;
	}

}
