//clase de Usuario
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

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

//Metodo para modificar cliente
public void modificarCliente() {
    Scanner entrada = new Scanner(System.in);

    System.out.println("Ingrese el nuevo nombre:");
    this.nombre = entrada.nextLine();

    System.out.println("Ingrese el nuevo apellido:");
    this.apellido = entrada.nextLine();

    System.out.println("Ingrese la nueva direccion:");
    this.direccion = entrada.nextLine();

    // Validar correo
    do {
        System.out.println("Ingrese el nuevo correo:");
        this.correo = entrada.nextLine();
        if (!this.correo.contains("@")) {
            System.out.println("Correo invalido. Debe contener al menos un '@'. Intente de nuevo.");
        }
    } while (!this.correo.contains("@"));

    // Validar telefono para que solo tenga numeros
    //no puede contener ni ´-¨ o espacios
    do {
        System.out.println("Ingrese el nuevo telefono:");
        this.telefono = entrada.nextLine();
        if (!this.telefono.matches("\\d+")) { //va a buscar el cadena solo numeros
            System.out.println("Telefono invalido. Debe contener solo numeros. Intente de nuevo.");
        }
    } while (!this.telefono.matches("\\d+")); //solo se sale del bucle cuando el telefono son solo numeros

    System.out.println("¿El cliente sigue activo? (true/false):");
    this.activo = Boolean.parseBoolean(entrada.nextLine());

    ConsultaBase.getInstance().actualizarDatosCliente(this.dni, this.nombre, this.apellido, this.direccion, this.correo, this.telefono, this.activo);
}


    // Metodo para mostrar informacion de un solo cliente
    public void mostrarInfoCliente(String dni) {
        Connection conn = ConsultaBase.getInstance().getConexion();

        //consulta SQL
        String listarSQL = "SELECT * FROM usuarios WHERE dni = ?";

        //se prepara la consulta y se ejecuta
        try (PreparedStatement solicitud = conn.prepareStatement(listarSQL)) {
            solicitud.setString(1, dni);
            var respuesta = solicitud.executeQuery();

            //se muestra toda la informacion de un solo cliente
            if (respuesta.next()) {

                System.out.println("=== Informacion del Cliente ===");
                System.out.println("ID: " + respuesta.getInt("id"));
                System.out.println("DNI: " + respuesta.getString("dni"));
                System.out.println("Nombre: " + respuesta.getString("nombre"));
                System.out.println("Apellido: " + respuesta.getString("apellido"));
                System.out.println("Dirección: " + respuesta.getString("direccion"));
                System.out.println("Correo: " + respuesta.getString("correo"));
                System.out.println("Telefono: " + respuesta.getString("telefono"));
                System.out.println("Activo: " + respuesta.getBoolean("activo"));

            } else {
                System.out.println("Cliente no encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Error al mostrar cliente: " + e.getMessage());
        }
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
/*
COMENTARIOS
-----------------------------------------------------------------------------------------------------------
A la hora de pasar el nuevo telefono, este se tiene que pasar sin espacios o guiones
ej: 54926398772346
Esto se podria modificar en un excel separando asi: 54 9 263 9877 2346

Hay que hacer lo mismo con el dni, ademas de encontrar una forma de pasar el id(int) que el dni(string)
para las busquedas. Por temas de eficiencia
------------------------------------------------------------------------------------------------------------
Elgi que el metodo de mostrarCliente se ubique en la clase usuario y no la clase de la Base
ya que siento que es delegar tareas adicionales a la base que la clase usuario puede resolver
asi que simplemente llamo a la conexion y no creo un metodo que llame a otro metodo y que hacen lo mismo
 */