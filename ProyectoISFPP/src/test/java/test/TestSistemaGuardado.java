package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import logica.SistemaGuardado;

class TestSistemaGuardado {

	@Test
	void cargaLosDatos() {
		SistemaGuardado sistema = new SistemaGuardado();

		assertEquals(6, sistema.getServicios().size());
		assertEquals(15, sistema.getVehiculos().size());
		assertEquals(50, sistema.getUsuarios().size());
	}
}	