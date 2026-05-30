package modelo;

public class SensorPresencia extends Sensor {

    // sensor pir, detecta si hay alguien en la habitación (0 o 1)
    public SensorPresencia() {
        super("pir", "Sensor de Presencia", "");
    }

    @Override
    public void actualizarValor() {
        // devuelve 1 si hay movimiento, 0 si no
        setValor(RND.nextBoolean() ? 1 : 0);
    }

    // método útil para que las reglas pregunten directamente si hay presencia
    public boolean hayPresencia() {
        return getValor() >= 1;
    }

    @Override
    public String getEstadoActual() {
        // texto legible en lugar de mostrar un 0 o un 1
        return hayPresencia() ? "Movimiento detectado" : "Sin movimiento";
    }
}