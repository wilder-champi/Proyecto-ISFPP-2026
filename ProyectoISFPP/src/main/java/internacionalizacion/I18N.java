package internacionalizacion;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Maneja la internacionalización de la aplicación: guarda el idioma seleccionado
 * y entrega los textos traducidos desde los archivos {@code i18n/labels}.
 */
public final class I18N {

    // Idioma inicial: español de Argentina.
    private static Locale idioma = Locale.forLanguageTag("es-ES");

    /**
     * Constructor privado: evita que se creen objetos de esta clase.
     */
    // Evita que se creen objetos de esta clase.
    private I18N() {
    }

    /**
     * Devuelve el idioma seleccionado.
     *
     * @return el idioma actual
     */
    // Devuelve el idioma seleccionado.
    public static Locale getIdioma() {
        return idioma;
    }

    /**
     * Cambia el idioma de la aplicación.
     *
     * @param nuevoIdioma nuevo idioma a utilizar
     * @throws IllegalArgumentException si el idioma es {@code null}
     */
    // Cambia el idioma de la aplicación.
    public static void cambiarIdioma(Locale nuevoIdioma) {

        if (nuevoIdioma == null) {
            throw new IllegalArgumentException("El idioma no puede ser null");
        }

        idioma = nuevoIdioma;
    }

    /**
     * Obtiene el archivo de traducciones correspondiente al idioma actual.
     *
     * @return el {@link ResourceBundle} del idioma seleccionado
     */
    // Obtiene el archivo de traducciones según el idioma.
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(
            "i18n.labels",
            idioma
        );
    }

    /**
     * Devuelve un texto traducido a partir de su clave.
     *
     * @param clave clave del texto en el archivo de traducciones
     * @return el texto traducido al idioma actual
     */
    // Devuelve un texto traducido mediante una clave.
    public static String texto(String clave) {
        return getBundle().getString(clave);
    }
}