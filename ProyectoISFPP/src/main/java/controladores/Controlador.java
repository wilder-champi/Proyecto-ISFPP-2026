package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;

/**
 * Interfaz que implementan los controladores de las vistas. Permite entregarles el {@link Navegador}
 * (que maneja el cambio de pantallas) y el {@link Contexto} (que guarda los datos compartidos entre controladores).
 */
public interface Controlador {
	/**
	 * Inicializa el controlador una vez cargada su vista (funciona como un constructor que recibe los datos compartidos).
	 *
	 * @param navegador navegador que cambia de pantalla
	 * @param contexto datos compartidos entre los controladores
	 */
//interfaz que se utiliza en si para que cada controlador sepa cual es el navegador o la clase que dirige cada vista o panel y tambien contiene coontexto que guarda o almacena la informacion o datos comunes entre controladores
	void init(Navegador navegador, Contexto contexto);
}