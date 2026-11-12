package datos.dto;

import enums.CategoriaVehiculo;
import enums.TipoVehiculo;
import enums.TipoServicio;

/**
 * Objeto de transferencia con los datos de un servicio, tal como se leen de la fuente de datos.
 */
public class ServicioDTO {

	private String nombre;
	private double tarifaBase;
	private double precioKm;
	private double precioMinuto;

	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;
	private TipoServicio tipoServicio;

	/**
	 * Crea el DTO de un servicio.
	 *
	 * @param nombre nombre del servicio
	 * @param tarifaBase monto fijo del servicio
	 * @param precioKm precio por kilómetro
	 * @param precioMinuto precio por minuto
	 * @param tipoVehiculo tipo de vehículo del servicio
	 * @param categoriaVehiculo categoría de vehículo del servicio
	 * @param tipoServicio tipo de servicio (pasajeros o envíos)
	 */
	public ServicioDTO(String nombre, double tarifaBase, double precioKm, double precioMinuto,
			TipoVehiculo tipoVehiculo, CategoriaVehiculo categoriaVehiculo, TipoServicio tipoServicio) {

		this.nombre = nombre;
		this.tarifaBase = tarifaBase;
		this.precioKm = precioKm;
		this.precioMinuto = precioMinuto;
		this.tipoVehiculo = tipoVehiculo;
		this.categoriaVehiculo = categoriaVehiculo;
		this.tipoServicio = tipoServicio;
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
	 * Devuelve la tarifa base.
	 *
	 * @return monto fijo del servicio
	 */
	public double getTarifaBase() {
		return tarifaBase;
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
	 * Devuelve el precio por minuto.
	 *
	 * @return precio por cada minuto
	 */
	public double getPrecioMinuto() {
		return precioMinuto;
	}

	/**
	 * Devuelve el tipo de vehículo.
	 *
	 * @return tipo de vehículo
	 */
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	/**
	 * Devuelve la categoría de vehículo.
	 *
	 * @return categoría de vehículo
	 */
	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	/**
	 * Devuelve el tipo de servicio.
	 *
	 * @return tipo de servicio
	 */
	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}
}