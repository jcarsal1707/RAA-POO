package modelo.persistencia;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorLog {

    // singleton: un único punto de escritura del log en toda la aplicación
    private static GestorLog instancia;

    private static final Path LOG = Paths.get("logs", "actuators.log");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private GestorLog() {
        try {
            Files.createDirectories(LOG.getParent());
        } catch (IOException ignorado) {}
    }

    public static synchronized GestorLog getInstance() {
        if (instancia == null) {
            instancia = new GestorLog();
        }
        return instancia;
    }

    public synchronized void registrar(String idActuador, String accion) {
        // formato: [2026-05-30 17:00:00] ACTUATOR bulb -> ON
        String linea = String.format("[%s] ACTUATOR %s -> %s%n",
                LocalDateTime.now().format(fmt), idActuador, accion);
        try (BufferedWriter w = Files.newBufferedWriter(LOG, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(linea);
        } catch (IOException ex) {
            System.err.println("error escribiendo en el log: " + ex.getMessage());
        }
    }
}