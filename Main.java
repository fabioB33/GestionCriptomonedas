package org.example;

import org.example.vista.MenuPrincipal;

public class Main {
    public static void main(String[] args) {

                try {
                    MenuPrincipal menu = new MenuPrincipal();
                    menu.iniciar();
                } catch (Exception e) {
                    System.err.println("Error crítico en la aplicación: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

