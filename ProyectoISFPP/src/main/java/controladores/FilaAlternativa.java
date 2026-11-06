package controladores;

import logica.Alternativa;

// una fila de la tabla de alternativas: lo que se ve en cada columna
public class FilaAlternativa {

    private final String servicio;
    private final String precio;
    private final String tiempo;

    public FilaAlternativa(Alternativa alternativa) {
        this.servicio = alternativa.getServicio().getNombre();
        this.precio = String.format("$%.2f", alternativa.getCosto());
        this.tiempo = String.format("%.0f", alternativa.getMinutos());
    }

    public String getServicio() {
        return servicio;
    }

    public String getPrecio() {
        return precio;
    }

    public String getTiempo() {
        return tiempo;
    }
}