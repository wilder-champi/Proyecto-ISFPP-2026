package modelo;

public enum CategoriaVehiculo {
	ESTANDAR(0), CONFORT(1), PREMIUM(2);

	private final int valorNumerico;

	CategoriaVehiculo(int valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	public int getValorNumerico() {
		return this.valorNumerico;
	}
}
