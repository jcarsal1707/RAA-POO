import controlador.ControladorPrincipal;
import modelo.SmartTecnoHouse;
import modelo.persistencia.GestorPersistencia;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class App {

    private App() {}

    public static void main(String[] args) {
        // lanzamos la interfaz en el hilo de eventos de swing (EDT)
        SwingUtilities.invokeLater(App::arrancar);
    }

    private static void arrancar() {
        try {
            // usamos el look and feel del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorado) {}

        // 1. obtenemos el modelo singleton y cargamos el estado guardado
        SmartTecnoHouse modelo = SmartTecnoHouse.getInstance();
        GestorPersistencia.cargar(modelo);
        modelo.actualizarSensores();

        // 2. creamos la vista
        VentanaPrincipal vista = new VentanaPrincipal(modelo);

        // 3. creamos el controlador y conectamos vista y modelo
        ControladorPrincipal controlador = new ControladorPrincipal(modelo, vista);
        vista.setControlador(controlador);

        // 4. guardamos al cerrar la ventana
        vista.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.onCerrar();
            }
        });

        // 5. pintamos el estado inicial y mostramos la ventana
        controlador.iniciar();
        vista.setVisible(true);
    }
}