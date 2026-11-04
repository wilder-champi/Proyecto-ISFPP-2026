package test2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import enums.*;
import modelo.Vehiculo;

class VehiculoTest {

    @Test
    void rechazaDatosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo(null, "Fiat", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB1", "Fiat", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB-123C", "Fiat", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB123CD", " ", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB123CD", "Fiat", 0,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB123CD", "Fiat", 4,
                null, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB123CD", "Fiat", 4,
                TipoVehiculo.AUTO, null, TipoServicio.PASAJEROS));
        assertThrows(IllegalArgumentException.class, () -> new Vehiculo("AB123CD", "Fiat", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, null));
    }

    @Test
    void normalizaLaPatenteYElModelo() {
        Vehiculo v = new Vehiculo("  ab123cd ", "  Fiat Uno ", 4,
                TipoVehiculo.AUTO, CategoriaVehiculo.ESTANDAR, TipoServicio.PASAJEROS);
        assertEquals("AB123CD", v.getPatente());
        assertEquals("Fiat Uno", v.getModelo());
    }
}