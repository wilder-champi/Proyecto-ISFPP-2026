package logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Ciudad {
    private Ciudad() { }

    public static GeneradorUbicacion cargarGenerador(String archivo) {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(archivo)) {
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la ciudad " + archivo, e);
        }
        return new GeneradorUbicacion(num(p, "latitud1"), num(p, "longitud1"),
                num(p, "latitud2"), num(p, "longitud2"));
    }

    private static double num(Properties p, String clave) {
        try {
            return Double.parseDouble(p.getProperty(clave).trim());
        } catch (RuntimeException e) {
            throw new IllegalStateException("Valor inválido o faltante: " + clave, e);
        }
    }
}