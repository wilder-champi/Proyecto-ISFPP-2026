package logica;

import modelo.Usuario;
import modelo.Vehiculo;

public final class ValidadorUsuario {

	private ValidadorUsuario() {
	}

	public static Usuario crear(String nombre, String telefono, String email) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("el nombre es obligatorio");
		}
		for (int digito = 0; digito <= 9; digito++) {
			if (nombre.contains(String.valueOf(digito))) {
				throw new IllegalArgumentException("el nombre no puede contener numeros");
			}
		}
		if (telefono == null || telefono.isBlank()) {
			throw new IllegalArgumentException("el telefono es obligatorio");
		}
		if (email == null || !email.trim().endsWith("@email.com")) {
			throw new IllegalArgumentException("el email debe terminar en @email.com");
		}
		// el caracter ; separa los campos en los archivos de texto
		if (nombre.contains(";") || telefono.contains(";") || email.contains(";")) {
			throw new IllegalArgumentException("no se permite el caracter ;");
		}
		return new Usuario(nombre.trim(), telefono.trim(), email.trim());
	}

	public static void altaConductor(Usuario usuario, String licencia, Vehiculo vehiculo) {
		if (usuario == null) {
			throw new IllegalArgumentException("el usuario no puede ser null");
		}
		if (licencia == null || licencia.isBlank()) {
			throw new IllegalArgumentException("la licencia de conducir es obligatoria");
		}
		if (vehiculo == null) {
			throw new IllegalArgumentException("el conductor debe tener al menos un vehiculo");
		}
		if (usuario.getConductor() != null) {
			throw new IllegalArgumentException("el usuario ya esta registrado como conductor");
		}
		usuario.altaConductor(licencia.trim(), vehiculo);
	}
}