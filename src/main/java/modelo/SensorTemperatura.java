package modelo;

public class SensorTemperatura extends Sensor {

    // sensor de temperatura ambiente en grados celsius
    public SensorTemperatura() {
        super("temp", "Sensor de Temperatura", "°C");
    }

    @Override
    public void actualizarValor() {
        // simula una temperatura realista entre 16 y 30 grados
        setValor(16 + RND.nextDouble() * 14);
    }
}