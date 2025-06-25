package org.example.modelo;

import java.math.BigDecimal;

public class RecepcionCripto extends CriptOperacion {
    private String direccionOrigen;

    public RecepcionCripto(String simboloCripto, BigDecimal cantidad,
                           int billeteraOrigenId, int billeteraDestinoId, String direccionOrigen) {
        super("RECEPCION", simboloCripto, cantidad, billeteraOrigenId, billeteraDestinoId);
        this.direccionOrigen = direccionOrigen;
    }

    @Override
    public boolean ejecutar() {
        // Lógica específica para recepción
        System.out.println("Ejecutando recepción de " + cantidad + " " + simboloCripto +
                " desde dirección: " + direccionOrigen);
        return true;
    }

    public String getDireccionOrigen() { return direccionOrigen; }
    public void setDireccionOrigen(String direccionOrigen) { this.direccionOrigen = direccionOrigen; }

    @Override
    public String toString() {
        return super.toString() + " <- " + direccionOrigen;
    }
}