package modelo;

import java.util.ArrayList;
import java.util.List;

import enums.CategoriaVehiculo;
import enums.EstadoConductor;

public class Conductor {
	
	private String licenciaConducir;
	private List<Vehiculo> vehiculos;	//Declaramos
	private List<Viaje> viajes;	//Declaramos
	
	private Vehiculo vehiculoActivo;
	private EstadoConductor	estadoconductor;
	private CategoriaVehiculo categoriaVehiculoActivo;
	
	public Conductor(String licenciaConducir,Vehiculo vehiculo) {
		this.licenciaConducir = licenciaConducir;
		vehiculos = new ArrayList<Vehiculo>();	//Creamos
		agregarVehiculo(vehiculo);
		viajes = new ArrayList<Viaje>();	//Creamos
		this.vehiculoActivo = vehiculo;
		this.estadoconductor = EstadoConductor.FUERA_DE_SERVICIO;
		this.categoriaVehiculoActivo = vehiculo.getCategoriaVehiculo();
	}

	public String getLicenciaConducir() {
		return licenciaConducir;
	}

	public ArrayList<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public ArrayList<Viaje> getViajes() {
        return new ArrayList<>(viajes);
    }
	
	public Vehiculo getVehiculoActivo() {
		return vehiculoActivo;
	}

	public void setVehiculoActivo(Vehiculo vehiculoActivo) {
		this.vehiculoActivo = vehiculoActivo;
	}
	
	public EstadoConductor getEstadoConductor() {
		return estadoconductor;
	}

	public void setEstado(EstadoConductor estadoconductor) {
		this.estadoconductor = estadoconductor;
	}
	
	public CategoriaVehiculo getCategoriaVehiculoActivo() {
		return categoriaVehiculoActivo;
	}

	public void setCategoriaVehiculoActivo(CategoriaVehiculo categoriaVehiculoActivo) {
		this.categoriaVehiculoActivo = categoriaVehiculoActivo;
	}
	
	public void agregarVehiculo(Vehiculo vehiculo) {

		if (vehiculo == null) {//revisa que el vehiculo no sea null
			throw new IllegalArgumentException("el conductor tiene que tener al menos un vehiculo");
		}
		if (!vehiculos.contains(vehiculo)) {//verifiac si el vehiculo no fue previamente cargado ala lista
			vehiculos.add(vehiculo);//si es asi lo carga
		}
	}
	
	public void agregarViaje(Viaje viaje) {
		
		if(viaje== null) {//verifiac que el viaje no sea null
			throw new IllegalArgumentException("el viaje no puede ser null");
		}
		viajes.add(viaje);//guarda el historial de viajes del conductor
	}

}
