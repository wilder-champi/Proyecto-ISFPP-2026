package datos.dto;

import java.util.List;

import enums.CategoriaVehiculo;
import enums.TipoVehiculo;
import enums.TipoServicio;

/**
 * Objeto de transferencia con los datos de un vehículo, tal como se leen de la fuente de datos.
 */
public class VehiculoDTO {

    private String patente;
    private String modelo;
    private int capacidadPasajeros;

    private CategoriaVehiculo categoriaVehiculo;
    private TipoVehiculo tipoVehiculo;
    private List<TipoServicio> tiposServicio;//lista de serbicios que puede ofrecer un vehiculo

    /**
     * Crea el DTO de un vehículo.
     *
     * @param patente patente del vehículo
     * @param modelo modelo del vehículo
     * @param capacidadPasajeros cantidad de pasajeros
     * @param tipoVehiculo tipo de vehículo
     * @param categoriaVehiculo categoría del vehículo
     * @param tiposServicio tipos de servicio que presta
     */
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

    /**
     * Devuelve la patente.
     *
     * @return patente del vehículo
     */
    public String getPatente() {
        return patente;
    }

    /**
     * Devuelve el modelo.
     *
     * @return modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Devuelve la capacidad de pasajeros.
     *
     * @return cantidad de pasajeros
     */
    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    /**
     * Devuelve el tipo de vehículo.
     *
     * @return tipo de vehículo
     */
    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }
    
    /**
     * Devuelve la categoría del vehículo.
     *
     * @return categoría del vehículo
     */
    public CategoriaVehiculo getCategoriaVehiculo() {
        return categoriaVehiculo;
    }

    /**
     * Devuelve los tipos de servicio que presta el vehículo.
     *
     * @return lista de tipos de servicio
     */
    public List<TipoServicio> getTiposServicio() {
        return tiposServicio;
    }
}