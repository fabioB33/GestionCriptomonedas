package org.example.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaccion {
    private int id;
    private String tipo;
    private String simboloCripto;
    private BigDecimal cantidad;
    private int billeteraOrigenId;
    private int billeteraDestinoId;
    private LocalDateTime fecha;
    private String estado;
    private String descripcion;

    public Transaccion(String tipo, String simboloCripto, BigDecimal cantidad,
                       int billeteraOrigenId, int billeteraDestinoId, String descripcion) {
        this.tipo = tipo;
        this.simboloCripto = simboloCripto;
        this.cantidad = cantidad;
        this.billeteraOrigenId = billeteraOrigenId;
        this.billeteraDestinoId = billeteraDestinoId;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.estado = "COMPLETADA";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSimboloCripto() { return simboloCripto; }
    public void setSimboloCripto(String simboloCripto) { this.simboloCripto = simboloCripto; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public int getBilleteraOrigenId() { return billeteraOrigenId; }
    public void setBilleteraOrigenId(int billeteraOrigenId) { this.billeteraOrigenId = billeteraOrigenId; }

    public int getBilleteraDestinoId() { return billeteraDestinoId; }
    public void setBilleteraDestinoId(int billeteraDestinoId) { this.billeteraDestinoId = billeteraDestinoId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.8f %s - %s (%s)",
                fecha.toString(), tipo, cantidad, simboloCripto, descripcion, estado);
    }  }