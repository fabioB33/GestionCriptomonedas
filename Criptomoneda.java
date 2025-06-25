package org.example.modelo;

import java.math.BigDecimal;

public class Criptomoneda {
    private String simbolo;
    private String nombre;
    private BigDecimal cantidad;
    private BigDecimal precioUSD;

    public Criptomoneda(String simbolo, String nombre, BigDecimal cantidad) {
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUSD = BigDecimal.ZERO;
    }

    // Getters y Setters
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUSD() { return precioUSD; }
    public void setPrecioUSD(BigDecimal precioUSD) { this.precioUSD = precioUSD; }

    public BigDecimal getValorTotal() {
        return cantidad.multiply(precioUSD);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.8f - $%.2f USD",
                nombre, simbolo, cantidad, getValorTotal());
    }
}