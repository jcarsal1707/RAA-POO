package modelo;

public class ActuadorBombilla extends Actuador {

    // bombilla inteligente, solo puede estar encendida o apagada
    public ActuadorBombilla() {
        super("bulb", "Bombilla", "OFF");
    }

    @Override
    public void ejecutarAccion(String accion) {
        // validamos que la acción sea válida antes de cambiar el estado
        if (!esAccionValida(accion)) {
            throw new IllegalArgumentException("acción no válida para la bombilla: " + accion);
        }
        cambiarEstado(accion);
    }

    @Override
    public String[] getAccionesPosibles() {
        return new String[]{"ON", "OFF"};
    }
}