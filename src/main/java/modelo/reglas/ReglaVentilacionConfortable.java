package modelo.reglas;

import modelo.Sensor;
import modelo.Actuador;
import java.util.List;

public class ReglaVentilacionConfortable extends ReglaBase {

    // regla r1 del enunciado: ajusta el ventilador según la temperatura
    @Override
    public String getNombre() {
        return "Ventilación Confortable";
    }

    @Override
    public void aplicar(List<Sensor> sensores, List<Actuador> actuadores) {
        Sensor temp = buscarSensor(sensores, "temp");
        Actuador fan = buscarActuador(actuadores, "fan");
        if (temp == null || fan == null) return;

        double t = temp.getValor();
        // por encima de 26 grados ponemos el ventilador a tope
        if (t >= 26) {
            fan.ejecutarAccion("HIGH");
        // entre 24 y 26 grados velocidad baja
        } else if (t >= 24) {
            fan.ejecutarAccion("LOW");
        // por debajo de 24 lo apagamos
        } else {
            fan.ejecutarAccion("OFF");
        }
    }
}