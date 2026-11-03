package logica;

import java.util.Random;

import modelo.Ubicacion;

public class GeneradorUbicacion {
	//
	private double latitudMinima;//la latitud mas al sur
	private double latitudMaxima;//la latitud mas al norte
	private double longitudMinima;//la longitud mas al oeste
	private double longitudMaxima;//la longitud mas al este
	private Random aleatorio;

	public GeneradorUbicacion(double latitud1, double longitud1, double latitud2, double longitud2) {
		//utilizando los metodos de Math saco el minimo y los maximo de las logitudes y latitudes
		this.latitudMinima = Math.min(latitud1, latitud2);//calcula la minima latitud o en otras palabra la latitud mas al sur
		this.latitudMaxima = Math.max(latitud1, latitud2);//calcula la latitud maxima o la que se encuentra mas al norte
		this.longitudMinima = Math.min(longitud1, longitud2);//calcula la longitud minma o la que se encuentra mas al oeste
		this.longitudMaxima = Math.max(longitud1, longitud2);//calcula la longitud maxima o la que se encuentra mas al este
		
		this.aleatorio = new Random();//se calculara con un ramdom cosa que cada compilacion sea de manera aleatoria sin un patron previo
	}
	
	//genera ubicaciones con una semilla predeterminada
	public GeneradorUbicacion(double latitud1, double longitud1, double latitud2, double longitud2, Random random) {
		if (random == null) {
			throw new IllegalArgumentException("El random no puede ser null");
		}
		//utilizando los metodos de Math saco el minimo y los maximo de las logitudes y latitudes
		this.latitudMinima = Math.min(latitud1, latitud2);//calcula la minima latitud o en otras palabra la latitud mas al sur
		this.latitudMaxima = Math.max(latitud1, latitud2);//calcula la latitud maxima o la que se encuentra mas al norte
		this.longitudMinima = Math.min(longitud1, longitud2);//calcula la longitud minma o la que se encuentra mas al oeste
		this.longitudMaxima = Math.max(longitud1, longitud2);//calcula la longitud maxima o la que se encuentra mas al este
		this.aleatorio = random;//se le pasa un valor previo como semilla para que al momento de sacar un valor del 0 al 1 sea el mismo patron ya pre hecho
	}
	
	
	public Ubicacion generar() {
		double latitud = valorAleatorio(this.latitudMinima, this.latitudMaxima);//se le asigna una nueva latitud de manera ramdom que se calcula con el metodo valor aleatorio
		double longitud = valorAleatorio(this.longitudMinima, this.longitudMaxima);//se le asigna una nueva longitud de manera random que se calcula con el metodo valor aleatorio
		Ubicacion nuevaUbicacion = new Ubicacion(latitud, longitud);//con esa nueva latitud y logitud se crea una nueva ubicacion
		return nuevaUbicacion;//retorna la nueva ubicacion
	}

	public double valorAleatorio(double minimo, double maximo) {
		double ancho = maximo - minimo;//se calcula el ancho de lso puntos dentro de la ubicacion que se le pasa dentro de madryn
		double aleatorio = this.aleatorio.nextDouble();//se le asigan un valor aleatorio entre 0 y 1 que luego se ocupa para saber que tan separados de las esquenas o que tan al centro esat esta nueva coordenada
		double resultado = minimo + (aleatorio * ancho);//al valor minimo osea dependiendo si se habla de latitud que el minimo seria la coordenada que se encuentra ams al norte y en la longitud seria la coordenda que se encuentra mas al oeste en si ala coordenada minima se le suma dicho desplasamiento
		return resultado;//retorna la nueva cordenada latitud/longitud
	}

}
