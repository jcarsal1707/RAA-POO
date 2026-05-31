package modelo.reglas;

import modelo.Sensor;
import modelo.Actuador;
import modelo.SensorPresencia;
import java.util.List;

public class ReglaIluminacionAutomatica extends ReglaBase {

    // regla r2 del enunciado: enciende la luz si hay poca luz y alguien presente
    @Override
    public String getNombre() {
        return "Iluminación Automática";
    }

    @Override
    public void aplicar(List<Sensor> sensores, List<Actuador> actuadores) {
        Sensor luz = buscarSensor(sensores, "light");
        Sensor pir = buscarSensor(sensores, "pir");
        Actuador bombilla = buscarActuador(actuadores, "bulb");
        if (luz == null || pir == null || bombilla == null) return;

        // comprobamos presencia usando el método propio de sensorpresencia
        boolean hayPresencia = (pir instanceof SensorPresencia)
                ? ((SensorPresencia) pir).hayPresencia()
                : pir.getValor() >= 1;

        // menos de 300 lux y alguien en casa: encendemos
        if (luz.getValor() < 300 && hayPresencia) {
            bombilla.ejecutarAccion("ON");
        } else {
            bombilla.ejecutarAccion("OFF");
        }
    }
}