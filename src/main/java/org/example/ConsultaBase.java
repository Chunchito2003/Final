/*Esta clase es la conexion de la base de Datos
y la principal, ya que de ella depende la mayoria de metodos
*/

/*Tareas
--Modificar los datos de la conexion con los adecuados
--
*/
package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConsultaBase {
    //atributos
    private static ConsultaBase instance;
    private Connection conexion;

    // Datos necesarios para conectar la base
    private final String url = "jdbc:mysql://localhost:3306/mi_base_datos";
    private final String usuario = "root";
    private final String contrasena = "tu_contraseña";

    // Constructor privado
    private ConsultaBase() {
        try {
            conexion = DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    // Metodo Singleton
    public static ConsultaBase getInstance() {
        if (instance == null) {
            instance = new ConsultaBase();
        }
        return instance;
    }

    //Metodos
    public boolean buscarCliente(String dni){
        // Consulta SQL
        String verificarSql = "SELECT COUNT(*) FROM usuarios WHERE dni = ?";
        try (var solicitud = conexion.prepareStatement(verificarSql)) {

            // Reemplazamos el signo de interrogación (?) por el valor real del DNI
            solicitud.setString(1, dni);

            var respuesta = solicitud.executeQuery();
            // Si hay una fila en el resultado (respuesta.next() devuelve true), leemos el valor del conteo
            if (respuesta.next()) {
            // respuesta.getInt(1) obtiene el numero de coincidencias encontradas (COUNT(*))
                return respuesta.getInt(1) > 0; //si es mayor que 0 se encontro el cliente y es TRUE
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        //si hubo un error o no se encontro el cliente, devolvemos FALSE
        return false;
        /*
        Mandamos la solicitud
        Buscamos en la tabla
        Contamos los resultados
        0 = no se encontro
        0 > si se encontro
         */
    }

    public void actualizarDatosClinte(String nombre, String apellido, String direccion, String correo, String telefono){
        //aca se deberia llamar al metodo de buscarCliente
        //y luego empezar a pasar los valores nuevos
    }

    // Getter para la conexion
    public Connection getConexion() {
        return conexion;
    }
}


