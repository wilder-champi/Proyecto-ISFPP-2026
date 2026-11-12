package enums;

/**
 * Calificaciones posibles de un viaje, cada una con su valor numérico (de 0 a 5).
 */
public enum CalificacionViaje {
	NO_CALIFICADO(0),MALO(1),REGULAR(2),BUENO(3),MUY_BUENO(4),EXCELENTE(5);
	
	
	private final int valor;

	/**
	 * Crea una calificación con su valor numérico.
	 *
	 * @param valor valor numérico de la calificación
	 */
	private CalificacionViaje(int valor) {
		this.valor = valor;
	}

	/**
	 * Devuelve el valor numérico de la calificación.
	 *
	 * @return valor entre 0 ({@code NO_CALIFICADO}) y 5 ({@code EXCELENTE})
	 */
	public int getValor() {
		return valor;
	}
	
	
	

}
 