package modelo.reglas;

import modelo.Sensor;
import modelo.Actuador;
import java.util.List;

public class ReglaPersianaSolar extends ReglaBase {

    // regla extra que usa los dos dispositivos nuevos: sensor de luz y persiana
    // demuestra que añadir una regla nueva no toca nada del código existente
    @Override
    public String getNombre() {
        return "Control Solar de Persiana";
    }

    @Override
    public void aplicar(List<Sensor> sensores, List<Actuador> actuadores) {
        Sensor luz = buscarSensor(sensores, "light");
        Actuador persiana = buscarActuador(actuadores, "persiana");
        if (luz == null || persiana == null) return;

        double lux = luz.getValor();
        // con mucha luz bajamos la persiana del todo para evitar deslumbramiento
        if (lux > 800) {
            persiana.ejecutarAccion("CERRADA");
        // con luz media la dejamos a medias
        } else if (lux > 400) {
            persiana.ejecutarAccion("MEDIO");
        // con poca luz la abrimos del todo
        } else {
            persiana.ejecutarAccion("ABIERTA");
        }
    }
}