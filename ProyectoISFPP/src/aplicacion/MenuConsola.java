package aplicacion;

import interfaz_consola.interfazConsola;

import java.util.Scanner;

import enums.RolUsuario;
import logica.SistemaGuardado;
import modelo.Servicio;
import modelo.Usuario;

public class MenuConsola {

	private Scanner teclado;
	private SistemaGuardado sistema;
	private Usuario usuarioActual;

	public MenuConsola() {
		sistema = new SistemaGuardado();
		teclado = new Scanner(System.in);
		usuarioActual = null;
	}

	//Aplicacion
	public static void main(String[] args) {
		new MenuConsola().iniciar();
	}

	// ---------- MENU PRINCIPAL ----------

	public void iniciar() {
		int opcion;
		do {
			interfazConsola.mostrarMensaje("\n===== MENU PRINCIPAL =====");
			interfazConsola.mostrarMensaje("1. Registrarse");
			interfazConsola.mostrarMensaje("2. Iniciar seccion");
			interfazConsola.mostrarMensaje("0. Salir");
			opcion = leerEntero("Opcion: ");

			switch (opcion) {
			case 1:
				registrarse();
				break;
			case 2:
				iniciarSesion();
				break;
			case 0:
				interfazConsola.mostrarMensaje("Hasta luego!");
				break;
			default:
				interfazConsola.mostrarError("Opcion Invalida!");
			}
		} while (opcion != 0);
	}

//Por ahora solo registro usuarios en memoria.
//#nombre;telefono;email;licenciaConducir;patente;...
	private void registrarse() {
		interfazConsola.mostrarMensaje("Ingrese su nombre:");
		String nombre = teclado.nextLine().trim();
		
		interfazConsola.mostrarMensaje("Ingrese un email:");
		String email = teclado.nextLine().trim();
		
		interfazConsola.mostrarMensaje("Ingrese un telefono:");
		String telefono = teclado.nextLine().trim();
		
		if(nombre.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
			interfazConsola.mostrarError("Los datos ingresados estan vacios!");
			return;
		}
		
		if (nombre.contains(";") || email.contains(";") || telefono.contains(";")) {
			interfazConsola.mostrarError("No se permite el caracter ;");
			return;
		}
		
		Usuario nuevoUsuario = new Usuario(nombre, telefono, email);
		try {
			sistema.registrarUsuario(nuevoUsuario);
			interfazConsola.mostrarMensaje("Usuario Registrado Exitosamente.");
		} catch (IllegalArgumentException | UnsupportedOperationException e) {
			interfazConsola.mostrarError(e.getMessage());
		}
		
		/*	Esto guardaba los usuarios en memoria
		for (int i = 0; i < sistema.getUsuarios().size(); i++) {
			Usuario u = sistema.getUsuarios().get(i);
			//Verifico si este usuario esta en la lista
			//equalsIgnoreCase() Distinge en mayusculas
			if(email.equalsIgnoreCase(u.getEmail()) ) {
				interfazConsola.mostrarError("Este usuario ya existe!");
				return;
			}
		}
			
		//Creo un nuevo usuario y lo cargo a la lista  
		Usuario nuevoUsuario = new Usuario(nombre, telefono, email);
		sistema.getUsuarios().add(nuevoUsuario);
		interfazConsola.mostrarMensaje("Usuario Registrado Exitosamente.");
		*/
	}
	
	//Este metodo lo que hace es buscar si el usuario existe y si no avisar que no esta registrado!
	private void iniciarSesion() {
		interfazConsola.mostrarMensaje("Ingrese un email:");
		String email = teclado.nextLine().trim();
		for (int i = 0; i < sistema.getUsuarios().size(); i++) {
			Usuario u = sistema.getUsuarios().get(i);
			if(email.equalsIgnoreCase(u.getEmail())) {
				usuarioActual = u;
				interfazConsola.mostrarMensaje("Bienvenido "+ u.getNombre());
				menuUsuario();
				return;
			}
		}
		interfazConsola.mostrarError("No se encontro ningun usuario con ese Email.");
	}
	
/* Estos metodos los deje para el tema de hacer pruebas!! 
 * No borres nada wilder ojito!! // le quitas lp divertido a todo mano
	private void listarUsuarios() {
		for (int i = 0; i < sistema.getUsuarios().size(); i++) {
			Usuario u = sistema.getUsuarios().get(i);
			System.out.println((i + 1) + ". " + u.getNombre() + " (" + u.getEmail() + ")");
		}
	}

	private void elegirUsuario() {
		//listarUsuarios();
		int num = leerEntero("Ingrese el numero de usuario (0 para volver): ");
		if (num < 1 || num > sistema.getUsuarios().size()) {
			return;
		}
		usuarioActual = sistema.getUsuarios().get(num - 1);
		menuUsuario();
	}
*/

	// ---------- MENU DEL USUARIO ----------

	private void menuUsuario() {
		int opcion;
		do {
			interfazConsola.mostrarMensaje("\n===== " + usuarioActual.getNombre() + " | Rol activo: "
					+ usuarioActual.getRolActivo() + " =====");
			interfazConsola.mostrarMensaje("1. Cambiar rol");
			interfazConsola.mostrarMensaje("2. Ir al menu de mi rol");
			interfazConsola.mostrarMensaje("0. Cerra sesion");
			opcion = leerEntero("Opcion: ");

			switch (opcion) {
			case 1:
				cambiarRol();
				break;
			case 2:
				if (usuarioActual.getRolActivo() == RolUsuario.CLIENTE) {
					menuCliente();
				} else {
					menuConductor();
				}
				break;
			case 0:
				break;
			default:
				interfazConsola.mostrarError("Opcion invalida");
			}
		} while (opcion != 0);
		usuarioActual = null;
	}

	private void cambiarRol() {
		// Si esta como CLIENTE intenta pasar a CONDUCTOR y viceversa
		RolUsuario nuevo = (usuarioActual.getRolActivo() == RolUsuario.CLIENTE) ? RolUsuario.CONDUCTOR
				: RolUsuario.CLIENTE;
		try {
			usuarioActual.cambiarRolActivo(nuevo);
			interfazConsola.mostrarMensaje("Ahora sos " + nuevo);
		} catch (IllegalStateException | IllegalArgumentException e) {
			interfazConsola.mostrarError("No se pudo cambiar: " + e.getMessage());
		}
	}

	// ---------- MENU CLIENTE ----------

	private void menuCliente() {
		int opcion;
		do {
			interfazConsola.mostrarMensaje("\n--- MENU CLIENTE ---");
			interfazConsola.mostrarMensaje("1. Solicitar viaje");
			interfazConsola.mostrarMensaje("2. Cancelar viaje");
			interfazConsola.mostrarMensaje("3. Ver mis viajes");
			interfazConsola.mostrarMensaje("0. Volver");
			opcion = leerEntero("Opcion: ");

			switch (opcion) {
			case 1:
				solicitarViaje();
				break;
			case 2:
				// TODO: buscar el viaje activo del cliente y llamar a cancelar(...)
				break;
			case 3:
				// TODO: recorrer usuarioActual.getCliente().getViajes() y mostrar estado
				break;
			case 0:
				break;
			default:
				interfazConsola.mostrarError("Opcion Invalida!");
			}
		} while (opcion != 0);
	}

	private void solicitarViaje() {
		// 1) mostrar servicios numerados
		for (int i = 0; i < sistema.getServicios().size(); i++) {
			Servicio s = sistema.getServicios().get(i);
			System.out.println((i + 1) + ". " + s.getNombre());
		}
		int num = leerEntero("Servicio: ");
		if (num < 1 || num > sistema.getServicios().size()) {
			interfazConsola.mostrarError("Servicio invalido");
			return;
		}
		Servicio servicio = sistema.getServicios().get(num - 1);

		// 2) TODO: generar origen y destino con GeneradorUbicacion
		// 3) TODO: crear el Viaje (new Viaje(usuarioActual, origen, destino, servicio))
		// 4) TODO: llamar viaje.solicitar(LocalDateTime.now()) dentro de un try/catch
		// 5) TODO: guardar el viaje en una lista de pendientes para que un conductor lo vea
	}

	// ---------- MENU CONDUCTOR ----------

	private void menuConductor() {
		int opcion;
		do {
			interfazConsola.mostrarMensaje("\n--- MENU CONDUCTOR ---");
			interfazConsola.mostrarMensaje("1. Ponerme disponible");
			interfazConsola.mostrarMensaje("2. Ver viajes pendientes y aceptar uno");
			interfazConsola.mostrarMensaje("3. Iniciar viaje");
			interfazConsola.mostrarMensaje("4. Finalizar viaje");
			interfazConsola.mostrarMensaje("0. Volver");
			opcion = leerEntero("Opcion: ");

			switch (opcion) {
			case 1:
				// TODO: usuarioActual.getConductor().setEstado(EstadoConductor.DISPONIBLE);
				break;
			case 2:
				// TODO: listar pendientes, elegir uno, viaje.aceptar(ahora, usuarioActual)
				break;
			case 3:
				// TODO: viaje.iniciar(ahora)
				break;
			case 4:
				// TODO: viaje.finalizar(ahora, calificacionCliente, calificacionConductor)
				break;
			case 0:
				break;
			default:
				interfazConsola.mostrarError("Opcion invalida");
			}
		} while (opcion != 0);
	}


	// Lee un numero sin que explote si escriben letras
	private int leerEntero(String mensaje) {
		System.out.print(mensaje);
		try {
			return Integer.parseInt(teclado.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}