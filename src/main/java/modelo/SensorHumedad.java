package modelo;

public class SensorHumedad extends Sensor {

    // sensor nuevo añadido para demostrar que la jerarquía es extensible
    // sin tocar la clase base Sensor ni ninguna otra clase existente
    public SensorHumedad() {
        super("hum", "Sensor de Humedad", "%");
    }

    @Override
    public void actualizarValor() {
        // simula humedad relativa entre 25% y 80%
        setValor(25 + RND.nextDouble() * 55);
    }
}