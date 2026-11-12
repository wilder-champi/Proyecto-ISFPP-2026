package modelo;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;

/**
 * Servicio que se ofrece a los clientes (por ejemplo un viaje en auto estándar), con su tarifa base,
 * precio por kilómetro y precio por minuto.
 */
public class Servicio {

	private String nombre;
	private double tarifaBase;
	private double precioKm;
	private double precioMinuto;

	private TipoServicio tipoServicio;
	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;

	/**
	 * Crea un servicio.
	 *
	 * @param nombre nombre del servicio
	 * @param tarifaBase monto fijo que se cobra al iniciar el viaje
	 * @param precioKm precio por cada kilómetro recorrido
	 * @param precioMinuto precio por cada minuto de viaje
	 * @param tipoVehiculo tipo de vehículo que presta el servicio
	 * @param categoriaVehiculo categoría del vehículo que presta el servicio
	 * @param tipoServicio tipo de servicio (pasajeros o envíos)
	 */
	public Servicio(String nombre, double tarifaBase, double precioKm, double precioMinuto, 
			TipoVehiculo tipoVehiculo, CategoriaVehiculo categoriaVehiculo,TipoServicio tipoServicio) {
		super();
		this.nombre = nombre;
		this.tarifaBase = tarifaBase;
		this.precioKm = precioKm;
		this.precioMinuto = precioMinuto;
		this.tipoServicio = tipoServicio;
		this.tipoVehiculo = tipoVehiculo;
		this.categoriaVehiculo = categoriaVehiculo;
	}

	/**
	 * Devuelve la tarifa base.
	 *
	 * @return monto fijo del servicio
	 */
	public double getTarifaBase() {
		return tarifaBase;
	}

	/**
	 * Establece la tarifa base.
	 *
	 * @param tarifaBase monto fijo del servicio
	 */
	public void setTarifaBase(double tarifaBase) {
		this.tarifaBase = tarifaBase;
	}

	/**
	 * Devuelve el precio por kilómetro.
	 *
	 * @return precio por cada kilómetro
	 */
	public double getPrecioKm() {
		return precioKm;
	}

	/**
	 * Establece el precio por kilómetro.
	 *
	 * @param precioKm precio por cada kilómetro
	 */
	public void setPrecioKm(double precioKm) {
		this.precioKm = precioKm;
	}

	/**
	 * Devuelve el precio por minuto.
	 *
	 * @return precio por cada minuto
	 */
	public double getPrecioMinuto() {
		return precioMinuto;
	}

	/**
	 * Establece el precio por minuto.
	 *
	 * @param precioMinuto precio por cada minuto
	 */
	public void setPrecioMinuto(double precioMinuto) {
		this.precioMinuto = precioMinuto;
	}

	/**
	 * Devuelve el nombre del servicio.
	 *
	 * @return nombre del servicio
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve el tipo de servicio.
	 *
	 * @return tipo de servicio
	 */
	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * Devuelve el tipo de vehículo del servicio.
	 *
	 * @return tipo de vehículo
	 */
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	/**
	 * Devuelve la categoría de vehículo del servicio.
	 *
	 * @return categoría de vehículo
	 */
	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	/**
	 * Establece la categoría de vehículo del servicio.
	 *
	 * @param categoriaVehiculo categoría de vehículo
	 */
	public void setCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {
		this.categoriaVehiculo = categoriaVehiculo;
	}

	/**
	 * Calcula el costo de un viaje: tarifa base más el precio por kilómetro y por minuto.
	 *
	 * @param km distancia del viaje en kilómetros
	 * @param minutos duración del viaje en minutos
	 * @return costo total del viaje
	 */
	public double calcularCosto(double km, double minutos) {

		return this.tarifaBase + (this.precioKm * km) + (this.precioMinuto * minutos);
	}

}
