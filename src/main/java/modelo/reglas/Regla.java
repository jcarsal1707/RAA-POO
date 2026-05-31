package modelo.reglas;

import modelo.Sensor;
import modelo.Actuador;
import java.util.List;

public interface Regla {

    // nombre de la regla, se usa también como clave en el mapa de smarttecnohouse
    String getNombre();

    // cada regla decide qué hacer con los sensores y actuadores, sin que el motor sepa cómo
    void aplicar(List<Sensor> sensores, List<Actuador> actuadores);
}