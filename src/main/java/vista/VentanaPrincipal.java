package vista;

import controlador.ControladorPrincipal;
import modelo.Actuador;
import modelo.Sensor;
import modelo.SmartTecnoHouse;
import modelo.reglas.Regla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    // referencias al modelo y al controlador
    private final transient SmartTecnoHouse modelo;
    private transient ControladorPrincipal controlador;

    // paneles principales de la interfaz
    private final JPanel panelSensores = new JPanel();
    private final JPanel panelActuadores = new JPanel();
    private final JPanel panelReglas = new JPanel();
    private final JTextArea areaLog = new JTextArea(8, 30);

    // botones de la barra superior
    private final JButton btnActualizar = new JButton("Actualizar lecturas");
    private final JButton btnAplicarReglas = new JButton("Aplicar reglas");
    private final JButton btnGuardar = new JButton("Guardar estado");

    public VentanaPrincipal(SmartTecnoHouse modelo) {
        super("Smart TecnoHouse - Panel de Control Domótico");
        this.modelo = modelo;
        construirInterfaz();
    }

    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
        // conectamos los botones al controlador una vez que lo tenemos
        btnActualizar.addActionListener(e -> controlador.onActualizarLecturas());
        btnAplicarReglas.addActionListener(e -> controlador.onAplicarReglas());
        btnGuardar.addActionListener(e -> controlador.onGuardar());
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // configuramos los tres paneles centrales
        panelSensores.setLayout(new BoxLayout(panelSensores, BoxLayout.Y_AXIS));
        panelSensores.setBorder(BorderFactory.createTitledBorder("Sensores"));

        panelActuadores.setLayout(new BoxLayout(panelActuadores, BoxLayout.Y_AXIS));
        panelActuadores.setBorder(BorderFactory.createTitledBorder("Actuadores"));

        panelReglas.setLayout(new BoxLayout(panelReglas, BoxLayout.Y_AXIS));
        panelReglas.setBorder(BorderFactory.createTitledBorder("Reglas (Strategy)"));

        // los tres paneles en columnas
        JPanel centro = new JPanel(new GridLayout(1, 3, 10, 10));
        centro.add(new JScrollPane(panelSensores));
        centro.add(new JScrollPane(panelActuadores));
        centro.add(new JScrollPane(panelReglas));

        // barra superior con los tres botones
        JPanel barraSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraSuperior.add(btnActualizar);
        barraSuperior.add(btnAplicarReglas);
        barraSuperior.add(btnGuardar);

        // área de log en la parte inferior
        areaLog.setEditable(false);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel panelLog = new JPanel(new BorderLayout());
        panelLog.setBorder(BorderFactory.createTitledBorder("Registro de actividad (actuators.log)"));
        panelLog.add(new JScrollPane(areaLog), BorderLayout.CENTER);

        add(barraSuperior, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(panelLog, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);
    }

    // el controlador llama a este método cada vez que algo cambia
    public void refrescar() {
        pintarSensores();
        pintarActuadores();
        pintarReglas();
        revalidate();
        repaint();
    }

    private void pintarSensores() {
        panelSensores.removeAll();
        for (Sensor s : modelo.getSensores()) {
            JLabel etiqueta = new JLabel(s.getNombre() + ":  " + s.getEstadoActual());
            etiqueta.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            panelSensores.add(etiqueta);
        }
        panelSensores.revalidate();
    }

    private void pintarActuadores() {
        panelActuadores.removeAll();
        for (Actuador a : modelo.getActuadores()) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
            // mostramos nombre y estado actual entre corchetes
            JLabel titulo = new JLabel(a.getNombre() + " [" + a.getEstadoActual() + "]");
            titulo.setPreferredSize(new Dimension(230, 22));
            fila.add(titulo);
            // generamos un botón por cada acción posible del actuador
            for (String accion : a.getAccionesPosibles()) {
                JButton boton = new JButton(accion);
                String idActuador = a.getId();
                boton.addActionListener(e -> {
                    if (controlador != null) controlador.onAccionActuador(idActuador, accion);
                });
                fila.add(boton);
            }
            panelActuadores.add(fila);
        }
        panelActuadores.revalidate();
    }

    private void pintarReglas() {
        panelReglas.removeAll();
        for (Regla r : modelo.getReglas()) {
            String nombre = r.getNombre();
            JCheckBox check = new JCheckBox(nombre, modelo.isReglaActiva(nombre));
            // al marcar o desmarcar avisamos al controlador
            check.addItemListener(e -> {
                if (controlador != null) controlador.onToggleRegla(nombre, check.isSelected());
            });
            panelReglas.add(check);
        }
        panelReglas.add(Box.createVerticalGlue());
        panelReglas.revalidate();
    }

    public void mostrarLog(String contenido) {
        areaLog.setText(contenido);
        // scroll automático al final del log
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}