package org.example.modelo;

import java.sql.*;


        public class ConexionBD {
            private static final String URL_SIN_DB = "jdbc:mysql://localhost:3306/";
            private static final String URL_CON_DB = "jdbc:mysql://localhost:3306/crypto_db";
            private static final String USUARIO = "root"; // tu usuario
            private static final String PASSWORD = "r00t"; // tu contraseña

            private static Connection conexion;
            public static Connection getConexion() {
                try {
                    // Primero conecta sin especificar base de datos
                    Connection connTemp = DriverManager.getConnection(URL_SIN_DB, USUARIO, PASSWORD);

                    // Crea la base de datos si no existe
                    Statement stmt = connTemp.createStatement();
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS crypto_db");
                    stmt.close();
                    connTemp.close();

                    // Ahora conecta a la base de datos específica
                    return DriverManager.getConnection(URL_CON_DB, USUARIO, PASSWORD);

                } catch (SQLException e) {
                    System.err.println("Error al conectar: " + e.getMessage());
                    return null;
                }
            }


    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión");
            e.printStackTrace();
        }
    }

    public static void crearTablas() {
        String[] tablas = {
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "nombre VARCHAR(100) NOT NULL," +
                        "email VARCHAR(100) UNIQUE NOT NULL," +
                        "password VARCHAR(255) NOT NULL," +
                        "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",

                "CREATE TABLE IF NOT EXISTS billeteras (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "nombre VARCHAR(100) NOT NULL," +
                        "usuario_id INT," +
                        "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))",

                "CREATE TABLE IF NOT EXISTS criptomonedas (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "billetera_id INT," +
                        "simbolo VARCHAR(10) NOT NULL," +
                        "nombre VARCHAR(100) NOT NULL," +
                        "cantidad DECIMAL(20,8) NOT NULL," +
                        "FOREIGN KEY (billetera_id) REFERENCES billeteras(id))",

                "CREATE TABLE IF NOT EXISTS transacciones (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "tipo VARCHAR(20) NOT NULL," +
                        "simbolo_cripto VARCHAR(10) NOT NULL," +
                        "cantidad DECIMAL(20,8) NOT NULL," +
                        "billetera_origen_id INT," +
                        "billetera_destino_id INT," +
                        "descripcion TEXT," +
                        "estado VARCHAR(20) DEFAULT 'COMPLETADA'," +
                        "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (billetera_origen_id) REFERENCES billeteras(id)," +
                        "FOREIGN KEY (billetera_destino_id) REFERENCES billeteras(id))"
        };

        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {

            for (String sql : tablas) {
                stmt.execute(sql);
            }
            System.out.println("Tablas creadas exitosamente");

        } catch (SQLException e) {
            System.err.println("Error al crear tablas");
            e.printStackTrace();
        }
    }
}

