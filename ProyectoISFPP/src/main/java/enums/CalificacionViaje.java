package enums;

public enum CalificacionViaje {
	NO_CALIFICADO(0),MALO(1),REGULAR(2),BUENO(3),MUY_BUENO(4),EXCELENTE(5);
	
	
	private final int valor;

	private CalificacionViaje(int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}
	
	
	

}
 