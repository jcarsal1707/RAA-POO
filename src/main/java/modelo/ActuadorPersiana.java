package modelo;

public class ActuadorPersiana extends Actuador {

    // actuador nuevo añadido para demostrar extensibilidad, igual que sensorhumedad
    // no ha hecho falta tocar la clase base actuador para añadirlo
    public ActuadorPersiana() {
        super("persiana", "Persiana Motorizada", "ABIERTA");
    }

    @Override
    public void ejecutarAccion(String accion) {
        // validamos que la posición pedida sea una de las tres válidas
        if (!esAccionValida(accion)) {
            throw new IllegalArgumentException("acción no válida para la persiana: " + accion);
        }
        cambiarEstado(accion);
    }

    @Override
    public String[] getAccionesPosibles() {
        // tres posiciones: abierta del todo, a medias o cerrada del todo
        return new String[]{"ABIERTA", "MEDIO", "CERRADA"};
    }
}
