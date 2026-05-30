package modelo;

import modelo.persistencia.GestorLog;
import java.util.Arrays;

public abstract class Actuador implements IDispositivo {

    private final String id;
    private final String nombre;
    private String estado;

    protected Actuador(String id, String nombre, String estadoInicial) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estadoInicial;
    }

    public abstract void ejecutarAccion(String accion);

    public abstract String[] getAccionesPosibles();

    protected boolean esAccionValida(String accion) {
        return Arrays.asList(getAccionesPosibles()).contains(accion);
    }

    protected void cambiarEstado(String nuevoEstado) {
        if (!nuevoEstado.equals(this.estado)) {
            this.estado = nuevoEstado;
            GestorLog.getInstance().registrar(id, nuevoEstado);
        }
    }

    public void restaurarEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() { return estado; }

    @Override
    public String getId() { return id; }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public String getEstadoActual() { return estado; }
}