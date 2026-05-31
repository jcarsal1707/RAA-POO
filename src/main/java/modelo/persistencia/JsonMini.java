package modelo.persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonMini {

    // utilidad json propia sin dependencias externas
    // suficiente para leer y escribir el estado simple que necesitamos guardar
    private final String s;
    private int i;

    private JsonMini(String s) {
        this.s = s;
        this.i = 0;
    }

    public static Object parse(String texto) {
        JsonMini p = new JsonMini(texto);
        p.ws();
        return p.value();
    }

    // escapa caracteres especiales al escribir strings en json
    public static String escape(String valor) {
        StringBuilder sb = new StringBuilder();
        for (char c : valor.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }

    private void ws() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
    }

    private Object value() {
        ws();
        char c = s.charAt(i);
        switch (c) {
            case '{': return obj();
            case '[': return arr();
            case '"': return str();
            case 't': i += 4; return Boolean.TRUE;
            case 'f': i += 5; return Boolean.FALSE;
            case 'n': i += 4; return null;
            default:  return num();
        }
    }

    private Map<String, Object> obj() {
        Map<String, Object> m = new LinkedHashMap<>();
        i++; ws();
        if (s.charAt(i) == '}') { i++; return m; }
        while (true) {
            ws();
            String k = str(); ws();
            i++; // ':'
            Object v = value();
            m.put(k, v); ws();
            char c = s.charAt(i++);
            if (c == '}') break;
        }
        return m;
    }

    private List<Object> arr() {
        List<Object> l = new ArrayList<>();
        i++; ws();
        if (s.charAt(i) == ']') { i++; return l; }
        while (true) {
            l.add(value()); ws();
            char c = s.charAt(i++);
            if (c == ']') break;
        }
        return l;
    }

    private String str() {
        StringBuilder sb = new StringBuilder();
        i++; // comilla de apertura
        while (true) {
            char c = s.charAt(i++);
            if (c == '"') break;
            if (c == '\\') {
                char e = s.charAt(i++);
                switch (e) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"');  break;
                    case '\\': sb.append('\\'); break;
                    default:  sb.append(e);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Double num() {
        int start = i;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c=='-'||c=='+'||c=='.'||c=='e'||c=='E'||(c>='0'&&c<='9')) i++;
            else break;
        }
        return Double.parseDouble(s.substring(start, i));
    }
}