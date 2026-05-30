package modelo;

import modelo.reglas.Regla;
import modelo.reglas.ReglaIluminacionAutomatica;
import modelo.reglas.ReglaPersianaSolar;
import modelo.reglas.ReglaVentilacionConfortable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SmartTecnoHouse {

    // patrón singleton: una única instancia del sistema en toda la aplicación
    private static SmartTecnoHouse instancia;

    private final List<Sensor> sensores = new ArrayList<>();
    private final List<Actuador> actuadores = new ArrayList<>();

    // mapa de reglas indexadas por nombre para acceder fácilmente
    private final Map<String, Regla> reglas = new LinkedHashMap<>();

    // controla qué reglas están activas en cada momento
    private final Map<String, Boolean> reglasActivas = new LinkedHashMap<>();

    private SmartTecnoHouse() {
        inicializarDispositivos();
        inicializarReglas();
    }

    // devuelve siempre la misma instancia, synchronized para evitar problemas con hilos
    public static synchronized SmartTecnoHouse getInstance() {
        if (instancia == null) {
            instancia = new SmartTecnoHouse();
        }
        return instancia;
    }

    private void inicializarDispositivos() {
        // 3 sensores base + humedad (nuevo)
        sensores.add(new SensorTemperatura());
        sensores.add(new SensorLuz());
        sensores.add(new SensorPresencia());
        sensores.add(new SensorHumedad());
        // 2 actuadores base + persiana (nuevo)
        actuadores.add(new ActuadorBombilla());
        actuadores.add(new ActuadorVentilador());
        actuadores.add(new ActuadorPersiana());
    }

    private void inicializarReglas() {
        registrarRegla(new ReglaVentilacionConfortable());
        registrarRegla(new ReglaIluminacionAutomatica());
        registrarRegla(new ReglaPersianaSolar());
    }

    private void registrarRegla(Regla r) {
        // todas las reglas arrancan activas por defecto
        reglas.put(r.getNombre(), r);
        reglasActivas.put(r.getNombre(), true);
    }

    public void actualizarSensores() {
        for (Sensor s : sensores) {
            s.actualizarValor();
        }
    }

    public void aplicarReglasActivas() {
        // recorre las reglas activas y llama a aplicar() en cada una (strategy)
        for (Regla r : reglas.values()) {
            if (isReglaActiva(r.getNombre())) {
                r.aplicar(sensores, actuadores);
            }
        }
    }

    public Actuador buscarActuador(String id) {
        for (Actuador a : actuadores) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    public Sensor buscarSensor(String id) {
        for (Sensor s : sensores) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    public List<Sensor> getSensores() { return sensores; }
    public List<Actuador> getActuadores() { return actuadores; }
    public Collection<Regla> getReglas() { return reglas.values(); }
    public boolean isReglaActiva(String nombre) { return Boolean.TRUE.equals(reglasActivas.get(nombre)); }
    public void setReglaActiva(String nombre, boolean activa) { reglasActivas.put(nombre, activa); }
    public Map<String, Boolean> getEstadoReglas() { return reglasActivas; }
}