package org.example.controlador;


import org.example.modelo.Billetera;
import org.example.modelo.ConexionBD;
import org.example.modelo.Transaccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ControladorTransaccion {
    private ControladorBilletera controladorBilletera;

    public ControladorTransaccion() {
        this.controladorBilletera = new ControladorBilletera();
    }

    public boolean enviarCriptomonedas(int billeteraOrigenId, int billeteraDestinoId,
                                       String simbolo, BigDecimal cantidad, String descripcion) {

        // Validaciones
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a cero");
            return false;
        }

        Billetera billeteraOrigen = controladorBilletera.obtenerBilletera(billeteraOrigenId);
        if (billeteraOrigen == null) {
            System.out.println("Error: Billetera origen no encontrada");
            return false;
        }

        if (!billeteraOrigen.tieneSaldoSuficiente(simbolo, cantidad)) {
            System.out.println("Error: Saldo insuficiente");
            return false;
        }

        try (Connection conn = ConexionBD.getConexion()) {
            conn.setAutoCommit(false);

            // Reducir saldo en billetera origen
            String reducirSql = "UPDATE criptomonedas SET cantidad = cantidad - ? WHERE billetera_id = ? AND simbolo = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(reducirSql)) {
                pstmt.setBigDecimal(1, cantidad);
                pstmt.setInt(2, billeteraOrigenId);
                pstmt.setString(3, simbolo);
                pstmt.executeUpdate();
            }

            // Aumentar saldo en billetera destino
            controladorBilletera.agregarCriptomoneda(billeteraDestinoId, simbolo, simbolo, cantidad);

            // Registrar transacción
            String transaccionSql = "INSERT INTO transacciones (tipo, simbolo_cripto, cantidad, billetera_origen_id, billetera_destino_id, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(transaccionSql)) {
                pstmt.setString(1, "ENVIO");
                pstmt.setString(2, simbolo);
                pstmt.setBigDecimal(3, cantidad);
                pstmt.setInt(4, billeteraOrigenId);
                pstmt.setInt(5, billeteraDestinoId);
                pstmt.setString(6, descripcion);
                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Transacción completada exitosamente");
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transacción: " + e.getMessage());
            try {
                Connection conn = null;
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            return false;
        }
    }

    public boolean recibirCriptomonedas(int billeteraDestinoId, String simbolo,
                                        BigDecimal cantidad, String descripcion) {

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a cero");
            return false;
        }

        // Agregar criptomoneda a la billetera
        boolean resultado = controladorBilletera.agregarCriptomoneda(billeteraDestinoId, simbolo, simbolo, cantidad);

        if (resultado) {
            // Registrar transacción
            String sql = "INSERT INTO transacciones (tipo, simbolo_cripto, cantidad, billetera_destino_id, descripcion) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, "RECEPCION");
                pstmt.setString(2, simbolo);
                pstmt.setBigDecimal(3, cantidad);
                pstmt.setInt(4, billeteraDestinoId);
                pstmt.setString(5, descripcion);

                pstmt.executeUpdate();
                System.out.println("Recepción completada exitosamente");
                return true;

            } catch (SQLException e) {
                System.err.println("Error al registrar recepción: " + e.getMessage());
            }
        }

        return false;
    }

    public List<Transaccion> obtenerHistorialTransacciones(int usuarioId) {
        List<Transaccion> transacciones = new ArrayList<>();
        String sql = """
            SELECT t.*, b1.nombre as billetera_origen, b2.nombre as billetera_destino
            FROM transacciones t
            LEFT JOIN billeteras b1 ON t.billetera_origen_id = b1.id
            LEFT JOIN billeteras b2 ON t.billetera_destino_id = b2.id
            WHERE b1.usuario_id = ? OR b2.usuario_id = ?
            ORDER BY t.fecha DESC
            """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaccion transaccion = new Transaccion(
                        rs.getString("tipo"),
                        rs.getString("simbolo_cripto"),
                        rs.getBigDecimal("cantidad"),
                        rs.getInt("billetera_origen_id"),
                        rs.getInt("billetera_destino_id"),
                        rs.getString("descripcion")
                );
                transaccion.setId(rs.getInt("id"));
                transaccion.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                transaccion.setEstado(rs.getString("estado"));

                transacciones.add(transaccion);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }

        return transacciones;
    }

    // Algoritmo de ordenamiento por fecha (más recientes primero)
    public List<Transaccion> ordenarTransaccionesPorFecha(List<Transaccion> transacciones) {
        // Implementación de ordenamiento burbuja
        for (int i = 0; i < transacciones.size() - 1; i++) {
            for (int j = 0; j < transacciones.size() - 1 - i; j++) {
                if (transacciones.get(j).getFecha().isBefore(transacciones.get(j + 1).getFecha())) {
                    // Intercambiar
                    Transaccion temp = transacciones.get(j);
                    transacciones.set(j, transacciones.get(j + 1));
                    transacciones.set(j + 1, temp);
                }
            }
        }
        return transacciones;
    }

    // Algoritmo de búsqueda por símbolo de criptomoneda
    public List<Transaccion> buscarTransaccionesPorSimbolo(List<Transaccion> transacciones, String simbolo) {
        List<Transaccion> resultado = new ArrayList<>();

        for (Transaccion t : transacciones) {
            if (t.getSimboloCripto().equalsIgnoreCase(simbolo)) {
                resultado.add(t);
            }
        }

        return resultado;
    }
}