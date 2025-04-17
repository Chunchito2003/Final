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
import java.sql.PreparedStatement;
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

    public void actualizarDatosCliente(String dni, String nombre, String apellido, String direccion, String correo, String telefono, boolean activo) {
        //aca se llama al metodo de buscarCliente
        //y luego empezar a pasar los valores nuevos
        if (buscarCliente(dni)) {
            String actualizarSQL = "UPDATE usuarios SET nombre = ?, apellido = ?, direccion = ?, correo = ?, telefono = ?, activo = ? WHERE dni = ?";

            //si encuentra el cliente pasa los datos a actualizar
            try (PreparedStatement updateSQL = conexion.prepareStatement(actualizarSQL)) {
                updateSQL.setString(1, nombre);
                updateSQL.setString(2, apellido);
                updateSQL.setString(3, direccion);
                updateSQL.setString(4, correo);
                updateSQL.setString(5, telefono);
                updateSQL.setBoolean(6, activo); // aca usamos setBoolean
                updateSQL.setString(7, dni);

                //verficacion acerca si se actualizo el cliente
                int filasActualizadas = updateSQL.executeUpdate();
                //si las filas son mas de 1, se actualizo
                if (filasActualizadas > 0) {
                    System.out.println("Cliente actualizado correctamente.");
                } else {
                    System.out.println("No se encontró cliente con ese DNI.");
                }

            } catch (Exception e) {
                System.out.println("Error al actualizar cliente: " + e.getMessage());
            }
        }
    }


    // Getter para la conexion
    public Connection getConexion() {
        return conexion;
    }
}

/*
COMENTARIOS
-----------------------------------------------------------------------------------------------------------
A esta clase luego hay que agregarle metodos que listen todos los clientes, prestamos y pagos
ademas en esta clase solo va a hacer búsquedas y actualizaciones
El registro y listar individualmente los haran las clases correspondientes
------------------------------------------------------------------------------------------------------------

 */
