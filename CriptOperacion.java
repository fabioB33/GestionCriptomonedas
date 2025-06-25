package org.example.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class CriptOperacion {
    protected int id;
    protected String tipo;
    protected String simboloCripto;
    protected BigDecimal cantidad;
    protected LocalDateTime fecha;
    protected int billeteraOrigenId;
    protected int billeteraDestinoId;

    public CriptOperacion(String tipo, String simboloCripto, BigDecimal cantidad,
                           int billeteraOrigenId, int billeteraDestinoId) {
        this.tipo = tipo;
        this.simboloCripto = simboloCripto;
        this.cantidad = cantidad;
        this.billeteraOrigenId = billeteraOrigenId;
        this.billeteraDestinoId = billeteraDestinoId;
        this.fecha = LocalDateTime.now();
    }

  
    public abstract boolean ejecutar();

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public String getSimboloCripto() { return simboloCripto; }
    public BigDecimal getCantidad() { return cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public int getBilleteraOrigenId() { return billeteraOrigenId; }
    public int getBilleteraDestinoId() { return billeteraDestinoId; }

    @Override
    public String toString() {
        return String.format("%s: %.8f %s - %s", tipo, cantidad, simboloCripto, fecha);
    }
}
