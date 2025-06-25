package org.example.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Billetera {
    private int id;
    private String nombre;
    private int usuarioId;
    private List<Criptomoneda> criptomonedas;

    public Billetera(String nombre, int usuarioId) {
        this.nombre = nombre;
        this.usuarioId = usuarioId;
        this.criptomonedas = new ArrayList<>();
    }

    public Billetera(int id, String nombre, int usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioId = usuarioId;
        this.criptomonedas = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public List<Criptomoneda> getCriptomonedas() { return criptomonedas; }
    public void setCriptomonedas(List<Criptomoneda> criptomonedas) { this.criptomonedas = criptomonedas; }

    // Métodos de negocio
    public void agregarCriptomoneda(Criptomoneda cripto) {
        // Buscar si ya existe la criptomoneda
        for (Criptomoneda c : criptomonedas) {
            if (c.getSimbolo().equals(cripto.getSimbolo())) {
                c.setCantidad(c.getCantidad().add(cripto.getCantidad()));
                return;
            }
        }
        criptomonedas.add(cripto);
    }

    public boolean tieneSaldoSuficiente(String simbolo, BigDecimal cantidad) {
        for (Criptomoneda c : criptomonedas) {
            if (c.getSimbolo().equals(simbolo)) {
                return c.getCantidad().compareTo(cantidad) >= 0;
            }
        }
        return false;
    }

    public void reducirSaldo(String simbolo, BigDecimal cantidad) {
        for (Criptomoneda c : criptomonedas) {
            if (c.getSimbolo().equals(simbolo)) {
                c.setCantidad(c.getCantidad().subtract(cantidad));
                if (c.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    criptomonedas.remove(c);
                }
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Billetera{" + "id=" + id + ", nombre='" + nombre + '\'' + ", criptomonedas=" + criptomonedas.size() + '}';
    }
}
