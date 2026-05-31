package modelo.reglas;

import modelo.Sensor;
import modelo.Actuador;
import java.util.List;

public abstract class ReglaBase implements Regla {

    // evita repetir el mismo bucle de búsqueda en cada regla concreta
    protected Sensor buscarSensor(List<Sensor> sensores, String id) {
        for (Sensor s : sensores) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    protected Actuador buscarActuador(List<Actuador> actuadores, String id) {
        for (Actuador a : actuadores) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }
}