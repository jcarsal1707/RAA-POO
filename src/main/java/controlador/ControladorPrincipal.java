package controlador;

import modelo.Actuador;
import modelo.SmartTecnoHouse;
import modelo.persistencia.GestorPersistencia;
import vista.VentanaPrincipal;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ControladorPrincipal {

    // el controlador es el único que conoce a la vez al modelo y a la vista
    private final SmartTecnoHouse modelo;
    private final VentanaPrincipal vista;

    private static final Path LOG = Paths.get("logs", "actuators.log");

    public ControladorPrincipal(SmartTecnoHouse modelo, VentanaPrincipal vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        // pintamos el estado inicial al arrancar la aplicación
        refrescarTodo();
    }

    public void onActualizarLecturas() {
        modelo.actualizarSensores();
        refrescarTodo();
    }

    public void onAplicarReglas() {
        modelo.aplicarReglasActivas();
        refrescarTodo();
    }

    public void onAccionActuador(String idActuador, String accion) {
        Actuador a = modelo.buscarActuador(idActuador);
        if (a != null) {
            try {
                a.ejecutarAccion(accion);
            } catch (IllegalArgumentException ex) {
                // avisamos al usuario si la acción no es válida
                JOptionPane.showMessageDialog(vista, ex.getMessage(),
                        "Acción no válida", JOptionPane.WARNING_MESSAGE);
            }
        }
        refrescarTodo();
    }

    public void onToggleRegla(String nombre, boolean activa) {
        modelo.setReglaActiva(nombre, activa);
    }

    public void onGuardar() {
        GestorPersistencia.guardar(modelo);
        JOptionPane.showMessageDialog(vista, "Estado guardado en data/estado.json",
                "Guardado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onCerrar() {
        // guardamos automáticamente al cerrar la ventana
        GestorPersistencia.guardar(modelo);
        System.exit(0);
    }

    private void refrescarTodo() {
        vista.refrescar();
        vista.mostrarLog(leerUltimasLineasLog(15));
    }

    private String leerUltimasLineasLog(int n) {
        if (!Files.exists(LOG)) return "(sin actividad registrada todavía)";
        try {
            List<String> lineas = Files.readAllLines(LOG, StandardCharsets.UTF_8);
            // mostramos solo las últimas n líneas para no saturar el área de texto
            int desde = Math.max(0, lineas.size() - n);
            return String.join("\n", lineas.subList(desde, lineas.size()));
        } catch (IOException ex) {
            return "(no se pudo leer el log: " + ex.getMessage() + ")";
        }
    }
}