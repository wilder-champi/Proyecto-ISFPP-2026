package enums;

/**
 * Categorías de vehículo disponibles, cada una con su valor numérico de nivel.
 */
public enum CategoriaVehiculo {
	ESTANDAR(1),CONFORT(2),PREMIUM(3);
	

    private final int valor;

	/**
	 * Crea una categoría con su valor numérico.
	 *
	 * @param valor nivel de la categoría
	 */
	private CategoriaVehiculo(int valor) {
		this.valor = valor;
	}

	/**
	 * Devuelve el valor numérico de la categoría.
	 *
	 * @return nivel de la categoría (1 estándar, 2 confort, 3 premium)
	 */
	public int getValor() {
		return valor;
	}
	
}
