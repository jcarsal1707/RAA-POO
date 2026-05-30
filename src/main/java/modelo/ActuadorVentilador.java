package modelo;

public class ActuadorVentilador extends Actuador {

    // ventilador con tres velocidades: apagado, baja y alta
    public ActuadorVentilador() {
        super("fan", "Ventilador", "OFF");
    }

    @Override
    public void ejecutarAccion(String accion) {
        // comprobamos que la velocidad pedida sea una de las tres válidas
        if (!esAccionValida(accion)) {
            throw new IllegalArgumentException("acción no válida para el ventilador: " + accion);
        }
        cambiarEstado(accion);
    }

    @Override
    public String[] getAccionesPosibles() {
        // off para apagado, low para velocidad baja, high para velocidad alta
        return new String[]{"OFF", "LOW", "HIGH"};
    }
}