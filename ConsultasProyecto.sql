CREATE DATABASE crypto_db;
USE crypto_db;

CREATE TABLE IF NOT EXISTS usuarios (" +
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
