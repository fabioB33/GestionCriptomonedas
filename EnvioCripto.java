package org.example.modelo;

import java.math.BigDecimal;

public class EnvioCripto extends CriptOperacion {
    private String direccionDestino;

    public EnvioCripto(String simboloCripto, BigDecimal cantidad,
                       int billeteraOrigenId, int billeteraDestinoId, String direccionDestino) {
        super("ENVIO", simboloCripto, cantidad, billeteraOrigenId, billeteraDestinoId);
        this.direccionDestino = direccionDestino;
    }

    @Override
    public boolean ejecutar() {
        // Lógica específica para envío
        System.out.println("Ejecutando envío de " + cantidad + " " + simboloCripto +
                " a dirección: " + direccionDestino);
        return true;
    }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    @Override
    public String toString() {
        return super.toString() + " -> " + direccionDestino;
    }
}