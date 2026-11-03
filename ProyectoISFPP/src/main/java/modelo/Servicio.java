package modelo;

import enums.CategoriaVehiculo;
import enums.TipoServicio;
import enums.TipoVehiculo;

public class Servicio {

	private String nombre;
	private double tarifaBase;
	private double precioKm;
	private double precioMinuto;

	private TipoServicio tipoServicio;
	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;

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

	public double getTarifaBase() {
		return tarifaBase;
	}

	public void setTarifaBase(double tarifaBase) {
		this.tarifaBase = tarifaBase;
	}

	public double getPrecioKm() {
		return precioKm;
	}

	public void setPrecioKm(double precioKm) {
		this.precioKm = precioKm;
	}

	public double getPrecioMinuto() {
		return precioMinuto;
	}

	public void setPrecioMinuto(double precioMinuto) {
		this.precioMinuto = precioMinuto;
	}

	public String getNombre() {
		return nombre;
	}

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public CategoriaVehiculo getCategoriaVehiculo() {
		return categoriaVehiculo;
	}

	public void setCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {
		this.categoriaVehiculo = categoriaVehiculo;
	}

	public double calcularCosto(double km, double minutos) {

		return this.tarifaBase + (this.precioKm * km) + (this.precioMinuto * minutos);
	}

}
