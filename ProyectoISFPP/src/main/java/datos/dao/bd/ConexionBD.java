package datos.dao.bd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Datos de conexión JDBC compartidos por los DAO de la familia SQL.
 * Abrir una conexión requiere URL, credenciales y un driver compatibles con el
 * servidor. MySQL y PostgreSQL utilizan esta misma clase y DAO JDBC. Otro gestor exige
 * comprobar también la compatibilidad del esquema, las consultas y los tipos;
 * la presencia de un driver por sí sola no garantiza esa compatibilidad.
 * 
 * JDBC permite que Java ejecute consultas SQL en una base de datos.***
 */
public class ConexionBD {

    private final String url;
    private final String usuario;
    private final String password;

    //recibe los datos de conexion
    public ConexionBD(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    //habre y devuelve una conexion a posgrest 
    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
}
