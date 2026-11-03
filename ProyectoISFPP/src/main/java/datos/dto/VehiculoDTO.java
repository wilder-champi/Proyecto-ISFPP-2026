package datos.dto;

import java.util.List;

import enums.CategoriaVehiculo;
import enums.TipoVehiculo;
import enums.TipoServicio;

public class VehiculoDTO {

    private String patente;
    private String modelo;
    private int capacidadPasajeros;

    private CategoriaVehiculo categoriaVehiculo;
    private TipoVehiculo tipoVehiculo;
    private List<TipoServicio> tiposServicio;//lista de serbicios que puede ofrecer un vehiculo

    public VehiculoDTO(String patente, String modelo,
            int capacidadPasajeros,
            TipoVehiculo tipoVehiculo,
            CategoriaVehiculo categoriaVehiculo,
            List<TipoServicio> tiposServicio) {

        this.patente = patente;
        this.modelo = modelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.tipoVehiculo = tipoVehiculo;
        this.categoriaVehiculo = categoriaVehiculo;
        this.tiposServicio = tiposServicio;
    }

    public String getPatente() {
        return patente;
    }

    public String getModelo() {
        return modelo;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }
    
    public CategoriaVehiculo getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    public List<TipoServicio> getTiposServicio() {
        return tiposServicio;
    }
}