package modelo;

import java.util.ArrayList;
import java.util.List;

import enums.CategoriaVehiculo;
import enums.EstadoConductor;
import logica.GeneradorUbicacion;

public class Conductor {

	private String licenciaConducir;
	private List<Vehiculo> vehiculos; // Declaramos los vehiculos pertenecientes al conductor/usuario
	private List<Viaje> viajes; // Declaramos

	private Vehiculo vehiculoActivo;
	private EstadoConductor estadoconductor;
	private CategoriaVehiculo categoriaVehiculoActivo;
	private CategoriaVehiculo categoriaMinimaAceptada;

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

	public CategoriaVehiculo getCategoriaMinimaAceptada() {
		return categoriaMinimaAceptada;
	}
	

	public void setCategoriaMinimaAceptada(CategoriaVehiculo categoriaMinimaAceptada) {
		if (categoriaMinimaAceptada == null) {
			throw new IllegalArgumentException("La categoría mínima no puede ser null");
		}
		if (categoriaMinimaAceptada.getValor() > this.categoriaVehiculoActivo.getValor()) {
			throw new IllegalArgumentException("No puedes aceptar viajes de una categoría mayor a tu vehículo activo");
		}
		this.categoriaMinimaAceptada = categoriaMinimaAceptada;
	}
	

	public void agregarVehiculo(Vehiculo vehiculo) {

		if (vehiculo == null) {// revisa que el vehiculo no sea null
			throw new IllegalArgumentException("el conductor tiene que tener al menos un vehiculo");
		}
		if (!vehiculos.contains(vehiculo)) {// verifiac si el vehiculo no fue previamente cargado ala lista
			vehiculos.add(vehiculo);// si es asi lo carga
		}
	}

	public void agregarViaje(Viaje viaje) {

		if (viaje == null) {// verifiac que el viaje no sea null
			throw new IllegalArgumentException("el viaje no puede ser null");
		}
		viajes.add(viaje);// guarda el historial de viajes del conductor
	}

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
