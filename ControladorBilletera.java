package org.example.controlador;


import org.example.modelo.Billetera;
import org.example.modelo.ConexionBD;
import org.example.modelo.Criptomoneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ControladorBilletera {

    public boolean crearBilletera(String nombre, int usuarioId) {
        String sql = "INSERT INTO billeteras (nombre, usuario_id) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setInt(2, usuarioId);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear billetera: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarBilletera(int billeteraId, int usuarioId) {
        String sql = "DELETE FROM billeteras WHERE id = ? AND usuario_id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billeteraId);
            pstmt.setInt(2, usuarioId);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar billetera: " + e.getMessage());
            return false;
        }
    }

    public List<Billetera> listarBilleterasPorUsuario(int usuarioId) {
        List<Billetera> billeteras = new ArrayList<>();
        String sql = "SELECT * FROM billeteras WHERE usuario_id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Billetera billetera = new Billetera(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("usuario_id")
                );

                // Cargar criptomonedas de la billetera
                cargarCriptomonedas(billetera);
                billeteras.add(billetera);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar billeteras: " + e.getMessage());
        }

        return billeteras;
    }

    private void cargarCriptomonedas(Billetera billetera) {
        String sql = "SELECT * FROM criptomonedas WHERE billetera_id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billetera.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Criptomoneda cripto = new Criptomoneda(
                        rs.getString("simbolo"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("cantidad")
                );
                billetera.agregarCriptomoneda(cripto);
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar criptomonedas: " + e.getMessage());
        }
    }

    public boolean agregarCriptomoneda(int billeteraId, String simbolo, String nombre, BigDecimal cantidad) {
        // Verificar si ya existe
        String verificarSql = "SELECT cantidad FROM criptomonedas WHERE billetera_id = ? AND simbolo = ?";
        String insertarSql = "INSERT INTO criptomonedas (billetera_id, simbolo, nombre, cantidad) VALUES (?, ?, ?, ?)";
        String actualizarSql = "UPDATE criptomonedas SET cantidad = cantidad + ? WHERE billetera_id = ? AND simbolo = ?";

        try (Connection conn = ConexionBD.getConexion()) {

            // Verificar si existe
            try (PreparedStatement pstmt = conn.prepareStatement(verificarSql)) {
                pstmt.setInt(1, billeteraId);
                pstmt.setString(2, simbolo);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Actualizar cantidad existente
                    try (PreparedStatement updateStmt = conn.prepareStatement(actualizarSql)) {
                        updateStmt.setBigDecimal(1, cantidad);
                        updateStmt.setInt(2, billeteraId);
                        updateStmt.setString(3, simbolo);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Insertar nueva criptomoneda
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertarSql)) {
                        insertStmt.setInt(1, billeteraId);
                        insertStmt.setString(2, simbolo);
                        insertStmt.setString(3, nombre);
                        insertStmt.setBigDecimal(4, cantidad);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar criptomoneda: " + e.getMessage());
            return false;
        }
    }

    public Billetera obtenerBilletera(int billeteraId) {
        String sql = "SELECT * FROM billeteras WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billeteraId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Billetera billetera = new Billetera(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("usuario_id")
                );
                cargarCriptomonedas(billetera);
                return billetera;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener billetera: " + e.getMessage());
        }

        return null;
    }
}
