package modelo;

public class Servicio {
	private String nombre;
	private double tarifaBase;
	private double precioKm;
	private double precioMinuto;
	private TipoVehiculo tipoVehiculo;
	private CategoriaVehiculo categoriaVehiculo;
	private TipoServicio tipoServicio;

	public Servicio(String nombre, double tarifaBase, double precioKm, double precioMinuto, TipoVehiculo tipoVehiculo,
			CategoriaVehiculo categoriaVehiculo, TipoServicio tipoServicio) {
		super();
		this.nombre = nombre;
		this.tarifaBase = tarifaBase;
		this.precioKm = precioKm;
		this.precioMinuto = precioMinuto;
		this.tipoVehiculo = tipoVehiculo;
		this.tipoServicio = tipoServicio;
		this.categoriaVehiculo = categoriaVehiculo;
	}

	public double calcularCosto(double km, double minutos) {
		return this.tarifaBase + (km * this.precioKm) + (minutos * this.precioMinuto);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public TipoServicio getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicio tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

}
