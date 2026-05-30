package modelo;

public abstract class Sensor implements IDispositivo {

    protected static final java.util.Random RND = new java.util.Random();

    private final String id;
    private final String nombre;
    private final String unidad;
    private double ultimoValor;

    protected Sensor(String id, String nombre, String unidad) {
        this.id = id;
        this.nombre = nombre;
        this.unidad = unidad;
    }

    public abstract void actualizarValor();

    protected void setValor(double valor) {
        this.ultimoValor = valor;
    }

    public double getValor() {
        return ultimoValor;
    }

    public String getUnidad() {
        return unidad;
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public String getEstadoActual() {
        return String.format("%.1f %s", ultimoValor, unidad);
    }
}