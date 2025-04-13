//clase de Usuario
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Usuario {

    // Propiedades
    private final int id;
    private final String dni;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo;
    private String telefono;
    private boolean activo; //este atributo evita que se borre el cliente y evitar problemas en la base

    // Variable para IDs automaticos
    private static int contadorIds = 1;

    // Constructor
    public Usuario(String dni, String nombre, String apellido, String direccion, String correo, String telefono) {
        this.id = contadorIds++;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.activo = true; //si el usuario deja de ser cliente se pone "false"
    }

    // Metodo para registrar cliente
    public void registrarCliente() {
        //Obtiene la conexion a la base de datos desde la clase Singleton ConsultaBase
        Connection conn = ConsultaBase.getInstance().getConexion();

        // Solo registrar si NO existe
        if (!ConsultaBase.getInstance().buscarCliente(this.dni)) { //se llama al metodo de busqueda
            // Consulta SQL
            String insertarSql = "INSERT INTO usuarios (dni, nombre, apellido, direccion, correo, telefono, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";

            //si se encuentra se inserta los datos
            try (PreparedStatement insertarStmt = conn.prepareStatement(insertarSql)) {
                insertarStmt.setString(1, this.dni);
                insertarStmt.setString(2, this.nombre);
                insertarStmt.setString(3, this.apellido);
                insertarStmt.setString(4, this.direccion);
                insertarStmt.setString(5, this.correo);
                insertarStmt.setString(6, this.telefono);
                insertarStmt.setBoolean(7, this.activo);

                //se ejecuta la consulta
                int filasInsertadas = insertarStmt.executeUpdate();
                if (filasInsertadas > 0) { //si las filas Insertdas son mas que 1, se ingreso
                    System.out.println("Cliente registrado correctamente.");
                }
            } catch (Exception e) {
                System.out.println("Error al registrar cliente:");
            }
        } else {
            System.out.println("Cliente con DNI " + this.dni + " ya existe."); //se encontro el cliente y por ello no se ingresa de vuelta
        }
    }

    // Metodo para modificar cliente (por implementar)
    public void modificarCliente() {
        // Llamar a ConsultaBase.actualizarDatosCliente cuando este implementado
    }

    // Metodo para mostrar informacion (por implementar)
    public void mostrarInfoCliente() {
        // Traer info de la base y mostrar
    }


    // Getters y Setters
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public static int getContadorIds() {
        return contadorIds;
    }

    public static void setContadorIds(int contadorIds) {
        Usuario.contadorIds = contadorIds;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
