package modelo;

public class SensorLuz extends Sensor {

    // sensor de luminosidad, valores entre 0 (oscuridad) y 1000 lux
    public SensorLuz() {
        super("light", "Sensor de Luz", "lux");
    }

    @Override
    public void actualizarValor() {
        // genera un valor entero aleatorio simulando la luz ambiente
        setValor(RND.nextInt(1001));
    }

    @Override
    public String getEstadoActual() {
        // mostramos sin decimales porque los lux no necesitan tanta precisión
        return String.format("%.0f %s", getValor(), getUnidad());
    }
}