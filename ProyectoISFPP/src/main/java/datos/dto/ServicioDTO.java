package datos.dto;

import enums.CategoriaVehiculo;
import enums.TipoVehiculo;
import enums.TipoServicio;

public class ServicioDTO {

	private String nombre;
	private double tarifaBase;
	private double precioKm;
	private double precioMinuto;

	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;
	private TipoServicio tipoServicio;

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

	public String getNombre() {
		return nombre;
	}

	public double getTarifaBase() {
		return tarifaBase;
	}

	public double getPrecioKm() {
		return precioKm;
	}

	public double getPrecioMinuto() {
		return precioMinuto;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}
}