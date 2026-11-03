package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import datos.dao.txt.LectorArchivo;
import datos.dao.txt.Linea;

public class TestLectorArchivo {

	@Test
	void archivoServicios() {
		assertEquals(6, LectorArchivo.leer("servicios.txt").size());
	}

	@Test
	void archivoVehiculos() {
		assertEquals(15, LectorArchivo.leer("vehiculos.txt").size());
	}

	@Test
	void archivoUsuarios() {
		List<Linea> lineas = LectorArchivo.leer("usuarios.txt");

		assertEquals(50, lineas.size());
		// La linea 1 es el comentario asi que juan perez es la linea 2
		assertEquals(2, lineas.get(0).getNumero());
		assertEquals("Juan Perez", lineas.get(0).getCampos().get(0));
		// Nombre, telefono, email, licencia y 3 patentes el ";" final no cuenta como campo
		assertEquals(7, lineas.get(0).getCampos().size());
	}

}