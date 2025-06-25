package org.example.vista;


import org.example.controlador.ControladorBilletera;
import org.example.controlador.ControladorCotizacion;
import org.example.controlador.ControladorTransaccion;
import org.example.controlador.ControladorUsuario;
import org.example.modelo.*;

import java.util.*;
import java.math.BigDecimal;

public class MenuPrincipal {
    private Scanner scanner;
    private ControladorUsuario controladorUsuario;
    private ControladorBilletera controladorBilletera;
    private ControladorTransaccion controladorTransaccion;
    private ControladorCotizacion controladorCotizacion;
    private Usuario usuarioActual;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.controladorUsuario = new ControladorUsuario();
        this.controladorBilletera = new ControladorBilletera();
        this.controladorTransaccion = new ControladorTransaccion();
        this.controladorCotizacion = new ControladorCotizacion();
        this.usuarioActual = null;
    }

    public void iniciar() {
        System.out.println("========================================");
        System.out.println("  SISTEMA DE GESTIÓN DE CRIPTOMONEDAS  ");
        System.out.println("========================================");

        // Inicializar base de datos
        ConexionBD.crearTablas();

        boolean continuar = true;

        while (continuar) {
            if (usuarioActual == null) {
                mostrarMenuLogin();
                int opcion = leerOpcion();
                continuar = procesarMenuLogin(opcion);
            } else {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();
                continuar = procesarMenuPrincipal(opcion);
            }
        }

        ConexionBD.cerrarConexion();
        System.out.println("¡Gracias por usar el sistema!");
    }

    private void mostrarMenuLogin() {
        System.out.println("\n========== MENÚ DE ACCESO ==========");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar Sesión");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private boolean procesarMenuLogin(int opcion) {
        switch (opcion) {
            case 1:
                registrarUsuario();
                break;
            case 2:
                iniciarSesion();
                break;
            case 3:
                return false;
            default:
                System.out.println("Opción no válida");
        }
        return true;
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("Usuario: " + usuarioActual.getNombre());
        System.out.println("1. Gestión de Billeteras");
        System.out.println("2. Operaciones con Criptomonedas");
        System.out.println("3. Ver Cotizaciones");
        System.out.println("4. Historial de Transacciones");
        System.out.println("5. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
    }

    private boolean procesarMenuPrincipal(int opcion) {
        switch (opcion) {
            case 1:
                menuGestionBilleteras();
                break;
            case 2:
                menuOperacionesCripto();
                break;
            case 3:
                verCotizaciones();
                break;
            case 4:
                verHistorialTransacciones();
                break;
            case 5:
                usuarioActual = null;
                System.out.println("Sesión cerrada exitosamente");
                break;
            default:
                System.out.println("Opción no válida");
        }
        return true;
    }

    private void registrarUsuario() {
        System.out.println("\n========== REGISTRO DE USUARIO ==========");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (controladorUsuario.registrarUsuario(nombre, email, password)) {
            System.out.println("Usuario registrado exitosamente");
        } else {
            System.out.println("Error al registrar usuario");
        }
    }

    private void iniciarSesion() {
        System.out.println("\n========== INICIAR SESIÓN ==========");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        usuarioActual = controladorUsuario.iniciarSesion(email, password);

        if (usuarioActual != null) {
            System.out.println("¡Bienvenido " + usuarioActual.getNombre() + "!");
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    private void menuGestionBilleteras() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== GESTIÓN DE BILLETERAS ==========");
            System.out.println("1. Crear Billetera");
            System.out.println("2. Listar Mis Billeteras");
            System.out.println("3. Agregar Criptomoneda a Billetera");
            System.out.println("4. Eliminar Billetera");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    crearBilletera();
                    break;
                case 2:
                    listarBilleteras();
                    break;
                case 3:
                    agregarCriptomoneda();
                    break;
                case 4:
                    eliminarBilletera();
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private void crearBilletera() {
        System.out.println("\n========== CREAR BILLETERA ==========");
        System.out.print("Nombre de la billetera: ");
        String nombre = scanner.nextLine();

        if (controladorBilletera.crearBilletera(nombre, usuarioActual.getId())) {
            System.out.println("Billetera creada exitosamente");
        } else {
            System.out.println("Error al crear billetera");
        }
    }

    private void listarBilleteras() {
        System.out.println("\n========== MIS BILLETERAS ==========");
        List<Billetera> billeteras = controladorBilletera.listarBilleterasPorUsuario(usuarioActual.getId());

        if (billeteras.isEmpty()) {
            System.out.println("No tienes billeteras creadas");
            return;
        }

        for (int i = 0; i < billeteras.size(); i++) {
            Billetera billetera = billeteras.get(i);
            System.out.println((i + 1) + ". " + billetera.getNombre() + " (ID: " + billetera.getId() + ")");

            if (billetera.getCriptomonedas().isEmpty()) {
                System.out.println("   - No tiene criptomonedas");
            } else {
                for (Criptomoneda cripto : billetera.getCriptomonedas()) {
                    System.out.println("   - " + cripto.toString());
                }
            }
            System.out.println();
        }
    }

    private void agregarCriptomoneda() {
        System.out.println("\n========== AGREGAR CRIPTOMONEDA ==========");

        List<Billetera> billeteras = controladorBilletera.listarBilleterasPorUsuario(usuarioActual.getId());
        if (billeteras.isEmpty()) {
            System.out.println("Primero debes crear una billetera");
            return;
        }

        System.out.println("Selecciona una billetera:");
        for (int i = 0; i < billeteras.size(); i++) {
            System.out.println((i + 1) + ". " + billeteras.get(i).getNombre());
        }

        System.out.print("Opción: ");
        int opcionBilletera = leerOpcion() - 1;

        if (opcionBilletera < 0 || opcionBilletera >= billeteras.size()) {
            System.out.println("Billetera no válida");
            return;
        }

        Billetera billeteraSeleccionada = billeteras.get(opcionBilletera);

        System.out.print("Símbolo de la criptomoneda (ej: BTC): ");
        String simbolo = scanner.nextLine().toUpperCase();
        System.out.print("Nombre completo (ej: Bitcoin): ");
        String nombre = scanner.nextLine();
        System.out.print("Cantidad: ");

        try {
            BigDecimal cantidad = new BigDecimal(scanner.nextLine());

            if (controladorBilletera.agregarCriptomoneda(billeteraSeleccionada.getId(), simbolo, nombre, cantidad)) {
                System.out.println("Criptomoneda agregada exitosamente");
            } else {
                System.out.println("Error al agregar criptomoneda");
            }

        } catch (NumberFormatException e) {
            System.out.println("Cantidad no válida");
        }
    }

    private void eliminarBilletera() {
        System.out.println("\n========== ELIMINAR BILLETERA ==========");

        List<Billetera> billeteras = controladorBilletera.listarBilleterasPorUsuario(usuarioActual.getId());
        if (billeteras.isEmpty()) {
            System.out.println("No tienes billeteras para eliminar");
            return;
        }

        System.out.println("Selecciona la billetera a eliminar:");
        for (int i = 0; i < billeteras.size(); i++) {
            System.out.println((i + 1) + ". " + billeteras.get(i).getNombre());
        }

        System.out.print("Opción: ");
        int opcion = leerOpcion() - 1;

        if (opcion < 0 || opcion >= billeteras.size()) {
            System.out.println("Billetera no válida");
            return;
        }

        Billetera billeteraSeleccionada = billeteras.get(opcion);

        System.out.print("¿Estás seguro de eliminar la billetera '" + billeteraSeleccionada.getNombre() + "'? (s/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("s") || confirmacion.equalsIgnoreCase("si")) {
            if (controladorBilletera.eliminarBilletera(billeteraSeleccionada.getId(), usuarioActual.getId())) {
                System.out.println("Billetera eliminada exitosamente");
            } else {
                System.out.println("Error al eliminar billetera");
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void menuOperacionesCripto() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== OPERACIONES CRIPTO ==========");
            System.out.println("1. Enviar Criptomonedas");
            System.out.println("2. Recibir Criptomonedas");
            System.out.println("3. Ver Saldos");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    enviarCriptomonedas();
                    break;
                case 2:
                    recibirCriptomonedas();
                    break;
                case 3:
                    verSaldos();
                    break;
                case 4:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private void enviarCriptomonedas() {
        System.out.println("\n========== ENVIAR CRIPTOMONEDAS ==========");

        List<Billetera> billeteras = controladorBilletera.listarBilleterasPorUsuario(usuarioActual.getId());
        if (billeteras.isEmpty()) {
            System.out.println("No tienes billeteras");
            return;
        }

        System.out.println("Billetera origen:");
        for (int i = 0; i < billeteras.size(); i++) {
            System.out.println((i + 1) + ". " + billeteras.get(i).getNombre());
        }

        System.out.print("Selecciona billetera origen: ");
        int origenIdx = leerOpcion() - 1;

        if (origenIdx < 0 || origenIdx >= billeteras.size()) {
            System.out.println("Billetera no válida");
            return;
        }

        System.out.print("Selecciona billetera destino: ");
        int destinoIdx = leerOpcion() - 1;

        if (destinoIdx < 0 || destinoIdx >= billeteras.size()) {
            System.out.println("Billetera no válida");
            return;
        }

        if (origenIdx == destinoIdx) {
            System.out.println("No puedes enviar a la misma billetera");
            return;
        }

        Billetera billeteraOrigen = billeteras.get(origenIdx);
        Billetera billeteraDestino = billeteras.get(destinoIdx);

        if (billeteraOrigen.getCriptomonedas().isEmpty()) {
            System.out.println("La billetera origen no tiene criptomonedas");
            return;
        }

        System.out.println("Criptomonedas disponibles:");
        for (int i = 0; i < billeteraOrigen.getCriptomonedas().size(); i++) {
            Criptomoneda cripto = billeteraOrigen.getCriptomonedas().get(i);
            System.out.println((i + 1) + ". " + cripto.getSimbolo() + " - Disponible: " + cripto.getCantidad());
        }

        System.out.print("Selecciona criptomoneda: ");
        int criptoIdx = leerOpcion() - 1;

        if (criptoIdx < 0 || criptoIdx >= billeteraOrigen.getCriptomonedas().size()) {
            System.out.println("Criptomoneda no válida");
            return;
        }

        Criptomoneda criptoSeleccionada = billeteraOrigen.getCriptomonedas().get(criptoIdx);

        System.out.print("Cantidad a enviar: ");
        try {
            BigDecimal cantidad = new BigDecimal(scanner.nextLine());
            System.out.print("Descripción (opcional): ");
            String descripcion = scanner.nextLine();

            if (controladorTransaccion.enviarCriptomonedas(
                    billeteraOrigen.getId(),
                    billeteraDestino.getId(),
                    criptoSeleccionada.getSimbolo(),
                    cantidad,
                    descripcion)) {
                System.out.println("Envío realizado exitosamente");
            } else {
                System.out.println("Error en el envío");
            }

        } catch (NumberFormatException e) {
            System.out.println("Cantidad no válida");
        }
    }

    private void recibirCriptomonedas() {
        System.out.println("\n========== RECIBIR CRIPTOMONEDAS ==========");

        List<Billetera> billeteras = controladorBilletera.listarBilleterasPorUsuario(usuarioActual.getId());
        if (billeteras.isEmpty()) {
            System.out.println("Primero debes crear una billetera");
            return;
        }

        System.out.println("Selecciona billetera destino:");
        for (int i = 0; i < billeteras.size(); i++) {
            System.out.println((i + 1) + ". " + billeteras.get(i).getNombre());
        }

        System.out.print("Opción: ");
        int opcion = leerOpcion() - 1;

        if (opcion < 0 || opcion >= billeteras.size()) {
            System.out.println("Billetera no válida");
            return;
        }

        Billetera billeteraDestino = billeteras.get(opcion);

        System.out.print("Símbolo de la criptomoneda: ");
        String simbolo = scanner.nextLine().toUpperCase();
        System.out.print("Cantidad recibida: ");

        try {
            BigDecimal cantidad = new BigDecimal(scanner.nextLine());
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();

            if (controladorTransaccion.recibirCriptomonedas(billeteraDestino.getId(), simbolo, cantidad, descripcion)) {
                System.out.println("Recepción registrada exitosamente");
            } else {
                System.out.println("Error al registrar recepción");
            }

        } catch (NumberFormatException e) {
            System.out.println("Cantidad no válida");
        }
    }

    private void verSaldos() {
        System.out.println("\n========== RESUMEN DE SALDOS ==========");
        listarBilleteras();
    }

    private void verCotizaciones() {
        System.out.println("\n========== COTIZACIONES ACTUALES ==========");

        String[] criptos = {"bitcoin", "ethereum", "cardano", "solana", "dogecoin"};

        for (String cripto : criptos) {
            try {
                BigDecimal precio = controladorCotizacion.obtenerPrecio(cripto);
                if (precio != null) {
                    System.out.printf("%s: $%.2f USD%n",
                            cripto.toUpperCase(), precio);
                }
            } catch (Exception e) {
                System.out.println("Error al obtener precio de " + cripto);
            }
        }

        System.out.println("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }

    private void verHistorialTransacciones() {
        System.out.println("\n========== HISTORIAL DE TRANSACCIONES ==========");

        try {
            List<Transaccion> transacciones = controladorTransaccion.obtenerHistorialTransacciones(usuarioActual.getId());

            if (transacciones.isEmpty()) {
                System.out.println("No hay transacciones registradas");
                return;
            }

            // Ordenar por fecha (más recientes primero)
            transacciones.sort((t1, t2) -> t2.getFecha().compareTo(t1.getFecha()));

            for (Transaccion t : transacciones) {
                System.out.println(t.toString());
            }

        } catch (Exception e) {
            System.out.println("Error al obtener historial: " + e.getMessage());
        }

        System.out.println("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}