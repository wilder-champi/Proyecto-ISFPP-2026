package controladores;

import aplicacion.Contexto;
import aplicacion.Navegador;

public interface Controlador {
//interfaz que se utiliza en si para que cada controlador sepa cual es el navegador o la clase que dirige cada vista o panel y tambien contiene coontexto que guarda o almacena la informacion o datos comunes entre controladores
	void init(Navegador navegador, Contexto contexto);
}