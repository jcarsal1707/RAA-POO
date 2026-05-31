package modelo.persistencia;

import modelo.Actuador;
import modelo.SmartTecnoHouse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GestorPersistencia {

    // ruta donde se guarda el estado de la vivienda
    private static final Path ARCHIVO = Paths.get("data", "estado.json");

    private GestorPersistencia() {}

    public static void guardar(SmartTecnoHouse casa) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // guardamos el estado de cada actuador
        sb.append("  \"actuadores\": {\n");
        List<Actuador> acts = casa.getActuadores();
        for (int k = 0; k < acts.size(); k++) {
            Actuador a = acts.get(k);
            sb.append("    \"").append(JsonMini.escape(a.getId())).append("\": \"")
              .append(JsonMini.escape(a.getEstado())).append("\"")
              .append(k < acts.size() - 1 ? "," : "").append("\n");
        }
        sb.append("  },\n");

        // guardamos qué reglas están activas y cuáles no
        sb.append("  \"reglas\": {\n");
        List<Map.Entry<String, Boolean>> reglas = new ArrayList<>(casa.getEstadoReglas().entrySet());
        for (int k = 0; k < reglas.size(); k++) {
            Map.Entry<String, Boolean> e = reglas.get(k);
            sb.append("    \"").append(JsonMini.escape(e.getKey())).append("\": ")
              .append(e.getValue())
              .append(k < reglas.size() - 1 ? "," : "").append("\n");
        }
        sb.append("  }\n}\n");

        try {
            Files.createDirectories(ARCHIVO.getParent());
            Files.write(ARCHIVO, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.err.println("no se pudo guardar el estado: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargar(SmartTecnoHouse casa) {
        // si no existe el archivo es la primera ejecución, usamos valores por defecto
        if (!Files.exists(ARCHIVO)) return;
        try {
            String txt = new String(Files.readAllBytes(ARCHIVO), StandardCharsets.UTF_8);
            Map<String, Object> raiz = (Map<String, Object>) JsonMini.parse(txt);

            // restauramos el estado de los actuadores sin escribir en el log
            Map<String, Object> acts = (Map<String, Object>) raiz.get("actuadores");
            if (acts != null) {
                for (Map.Entry<String, Object> e : acts.entrySet()) {
                    Actuador a = casa.buscarActuador(e.getKey());
                    if (a != null) a.restaurarEstado(String.valueOf(e.getValue()));
                }
            }

            // restauramos qué reglas estaban activas
            Map<String, Object> reglas = (Map<String, Object>) raiz.get("reglas");
            if (reglas != null) {
                for (Map.Entry<String, Object> e : reglas.entrySet())
                    casa.setReglaActiva(e.getKey(), Boolean.TRUE.equals(e.getValue()));
            }
        } catch (Exception ex) {
            System.err.println("no se pudo cargar el estado: " + ex.getMessage());
        }
    }
}