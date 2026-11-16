package modelo;

public enum CalificacionViaje {
	NO_CALIFICADO(0),
    MALO(1),
    REGULAR(2),
    BUENO(3),
    MUY_BUENO(4),
    EXCELENTE(5);

    private final int valorNumerico;

    CalificacionViaje(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return this.valorNumerico;
    }
}
