package com.minegocio.backend.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SqlBuilderUtil {

    public enum TipoConsulta {
        SELECT(true), // Solo lectura
        INSERT(false), // Escritura
        UPDATE(false),
        DELETE(false);

        private final boolean soloLectura;

        TipoConsulta(boolean soloLectura) {
            this.soloLectura = soloLectura;
        }

        public boolean isSoloLectura() {
            return soloLectura;
        }
    }

    /**
     * Construye el SQL dinámico a partir del JSON de columnas.
     */
    public static String construirConsulta(String jsonColumnas, TipoConsulta tipoConsulta) {
        JSONObject tablaJson = new JSONObject(jsonColumnas);
        String nombreTabla = tablaJson.getString("nombre_tabla");
        JSONArray columnas = tablaJson.getJSONArray("columnas");

        return switch (tipoConsulta) {
            case SELECT -> construirSelect(nombreTabla, columnas);
            case INSERT -> construirInsert(nombreTabla, columnas);
            case UPDATE -> construirUpdate(nombreTabla, columnas);
            case DELETE -> construirDelete(nombreTabla, columnas);
        };
    }

    private static String construirSelect(String nombreTabla, JSONArray columnas) {
        String columnasSql = IntStream.range(0, columnas.length())
                .mapToObj(i -> columnas.getJSONObject(i).getString("nombre"))
                .collect(Collectors.joining(", "));
        return "SELECT " + columnasSql + " FROM " + nombreTabla;
    }

    private static String construirInsert(String nombreTabla, JSONArray columnas) {
        var columnasValidas = columnas.toList().stream()
                .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                .filter(c -> !c.optBoolean("auto_increment", false))
                .toList();

        String columnasSql = columnasValidas.stream()
                .map(c -> c.getString("nombre"))
                .collect(Collectors.joining(", "));

        String valuesSql = columnasValidas.stream()
                .map(c -> "?")
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + nombreTabla + " (" + columnasSql + ") VALUES (" + valuesSql + ")";
    }

    private static String construirUpdate(String nombreTabla, JSONArray columnas) {
        var columnasValidas = columnas.toList().stream()
                .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                .filter(c -> !c.optBoolean("primary_key", false)) // No actualizamos PK
                .toList();

        String setSql = columnasValidas.stream()
                .map(c -> c.getString("nombre") + " = ?")
                .collect(Collectors.joining(", "));

        String pk = columnas.toList().stream()
                .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                .filter(c -> c.optBoolean("primary_key", false))
                .findFirst()
                .map(c -> c.getString("nombre"))
                .orElseThrow(() -> new IllegalArgumentException("No se encontró PK en el JSON"));

        return "UPDATE " + nombreTabla + " SET " + setSql + " WHERE " + pk + " = ?";
    }

    private static String construirDelete(String nombreTabla, JSONArray columnas) {
        String pk = columnas.toList().stream()
                .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                .filter(c -> c.optBoolean("primary_key", false))
                .findFirst()
                .map(c -> c.getString("nombre"))
                .orElseThrow(() -> new IllegalArgumentException("No se encontró PK en el JSON"));

        return "DELETE FROM " + nombreTabla + " WHERE " + pk + " = ?";
    }

    /**
     * Obtiene los nombres de columnas que requieren valores dinámicos.
     */
    public static List<String> obtenerColumnasParaParametros(String jsonColumnas, TipoConsulta tipoConsulta) {
        JSONObject tablaJson = new JSONObject(jsonColumnas);
        JSONArray columnas = tablaJson.getJSONArray("columnas");

        return switch (tipoConsulta) {
            case INSERT -> columnas.toList().stream()
                    .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                    .filter(c -> !c.optBoolean("auto_increment", false))
                    .map(c -> c.getString("nombre"))
                    .toList();
            case UPDATE -> columnas.toList().stream()
                    .map(obj -> new JSONObject((java.util.Map<?, ?>) obj))
                    .filter(c -> !c.optBoolean("primary_key", false))
                    .map(c -> c.getString("nombre"))
                    .toList();
            case DELETE, SELECT -> List.of(); // No tienen columnas dinámicas (salvo WHERE)
        };
    }

    /**
     * Contruccion y eliminacion dinamica
     */

    public static String construirCreateTable(JSONObject tablaJson) {
        String nombreTabla = tablaJson.getString("nombre_tabla");
        JSONArray columnas = tablaJson.getJSONArray("columnas");

        String columnasSql = IntStream.range(0, columnas.length())
                .mapToObj(i -> {
                    JSONObject col = columnas.getJSONObject(i);
                    StringBuilder sb = new StringBuilder(col.getString("nombre"))
                            .append(" ")
                            .append(col.getString("tipo"));

                    if (col.optBoolean("primary_key", false)) {
                        sb.append(" PRIMARY KEY");
                    }
                    if (col.optBoolean("auto_increment", false)) {
                        sb.append(" AUTO_INCREMENT");
                    }
                    if (col.has("unique") && col.getBoolean("unique")) {
                        sb.append(" UNIQUE");
                    }
                    if (col.has("default")) {
                        Object def = col.get("default");
                        if (def instanceof Boolean) {
                            sb.append(" DEFAULT ").append((Boolean) def ? 1 : 0);
                        } else {
                            sb.append(" DEFAULT ").append(def.toString().equalsIgnoreCase("CURRENT_TIMESTAMP")
                                    ? "CURRENT_TIMESTAMP"
                                    : "'" + def + "'");
                        }
                    }
                    return sb.toString();
                })
                .collect(Collectors.joining(", "));

        return "CREATE TABLE " + nombreTabla + " (" + columnasSql + ")";
    }

    public static String construirDropTable(String nombreTabla) {
        return "DROP TABLE IF EXISTS " + nombreTabla;
    }

}
