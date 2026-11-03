package datos.dao.txt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LectorArchivo {

	public static List<Linea> leer(String nombreArchivo) {
		List<Linea> lineas = new ArrayList<>();	//Creamos una lista para guardar los objetos linea o las lineas que se pudieron guardar
		try {
			Scanner lector = new Scanner(new File(nombreArchivo));	//Se crea un Scanner para explorar y empezar a leer el archivo desde el inicio
			int numero = 0;	//Inicializamos numero que nos servira para ver en que linea del archivo vamos o cual estamos leyendo
			while (lector.hasNextLine()) {	//Este while sigue mientras haya otra linea para leer en el archivo
				numero++;	//Incremetamos el indice de en que linea estamos
				String texto = lector.nextLine().trim(); //Lee la siguiente linea del archivo y elimina los espacios
				//Ignora las lineas vacias y los comentarios
				if (!texto.isEmpty() && !texto.startsWith("#")) {
					//Si esta es una linea valida se almacena dentro de la lista de manera, guardando que linea del archivo es y su contenido almacenado de manera separada dentro de otra lista
					lineas.add(new Linea(numero, separarCampos(texto)));
				}
			}
			lector.close();	//Una ves terminamos de leer el archivo cerramos el lector
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("No se encontro el archivo " + nombreArchivo);
		}
		return lineas;	//Devolvemos la lista que contiene todas las lineas que se pudieron almacenar de manera correcta
	}
	
	
	private static List<String> separarCampos(String texto) {
		
		List<String> campos = new ArrayList<>();	//Crea una lista para guardar lo valores de la linea
		for (String campo : texto.split(";")) {	//Con este for separa los datos atra vez de los ;
			campo = campo.trim();	//Elimina los espacios de dicho campo
			if (!campo.isEmpty()) {	//Pregunta si dicho campo esta vacio y si no es asi los añade ala lista
				campos.add(campo);
			}
		}
		return campos;
	}
	
	
}