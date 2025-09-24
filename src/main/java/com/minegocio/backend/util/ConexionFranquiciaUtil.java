package com.minegocio.backend.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ConexionFranquiciaUtil {

    @Value("${franquicia.db.host}")
    private String dbHost;

    @Value("${franquicia.db.port}")
    private String dbPort;

    @Value("${franquicia.db.username}")
    private String dbUsername;

    @Value("${franquicia.db.password}")
    private String dbPassword;

    private String getServerUrl() {
        return String.format("jdbc:mysql://%s:%s/", dbHost, dbPort);
    }

    private String getDbUrl(String dbName) {
        return String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
    }

    public Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(getServerUrl(), dbUsername, dbPassword);
    }

    public Connection getConnectionToDb(String dbName) throws SQLException {
        return DriverManager.getConnection(getDbUrl(dbName), dbUsername, dbPassword);
    }

    /** Crea la BD de una franquicia y devuelve true si fue creada. */
    public String crearBaseDeDatos(String nombreBd, String nombreUsuario, String password) {
        // Sentencias SQL
        String sqlCreateDb = "CREATE DATABASE IF NOT EXISTS " + nombreBd
                + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        String sqlCreateUser = "CREATE USER IF NOT EXISTS '" + nombreUsuario
                + "'@'%' IDENTIFIED BY '" + password + "'";
        String sqlGrantPrivileges = "GRANT ALL PRIVILEGES ON " + nombreBd
                + ".* TO '" + nombreUsuario + "'@'%'";
        String sqlFlush = "FLUSH PRIVILEGES";
        try (Connection conn = getServerConnection(); Statement stmt = conn.createStatement()) {
            // 1. Crear la BD
            stmt.executeUpdate(sqlCreateDb);
            // 2. Crear usuario (si no existe)
            stmt.executeUpdate(sqlCreateUser);
            // 3. Asignar permisos solo dentro de esa BD
            stmt.executeUpdate(sqlGrantPrivileges);
            // 4. Refrescar privilegios
            stmt.executeUpdate(sqlFlush);
            return getDbUrl(nombreBd);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando BD o usuario: " + nombreBd, e);
        }
    }

    /**
     * Crea la tabla de usuarios de la franquicia y hace el primer insert con el
     * admin, ademas devuelve su estructura en JSON.
     */
    public String crearTablaUsuarios(String nombreBd, String nombreUsuario, String correo, String password) {
        String sqlCreate = """
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        correo VARCHAR(100) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        es_admin BOOLEAN DEFAULT FALSE,
                        fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB;
                """;

        String sqlInsert = "INSERT INTO usuarios (nombre, correo, password_hash, es_admin) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnectionToDb(nombreBd);
                Statement stmt = conn.createStatement();
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
            // 1. Crear la tabla si no existe
            stmt.executeUpdate(sqlCreate);

            // 2. Insertar el usuario
            psInsert.setString(1, nombreUsuario);
            psInsert.setString(2, correo);
            psInsert.setString(3, password);
            psInsert.setBoolean(4, true); // lo ponemos como admin inicial
            psInsert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creando tabla o insertando usuario en BD: " + nombreBd, e);
        }
        // 3. Generar JSON de la estructura de la tabla
        JSONArray columnas = new JSONArray();
        columnas.put(new JSONObject()
                .put("nombre", "id_usuario")
                .put("tipo", "INT")
                .put("auto_increment", true)
                .put("primary_key", true));

        columnas.put(new JSONObject().put("nombre", "nombre").put("tipo", "VARCHAR(100)"));
        columnas.put(new JSONObject().put("nombre", "correo").put("tipo", "VARCHAR(100)").put("unique", true));
        columnas.put(new JSONObject().put("nombre", "password_hash").put("tipo", "VARCHAR(255)"));
        columnas.put(new JSONObject().put("nombre", "es_admin").put("tipo", "BOOLEAN").put("default", false));
        columnas.put(new JSONObject().put("nombre", "fecha_registro").put("tipo", "TIMESTAMP").put("default",
                "CURRENT_TIMESTAMP"));

        JSONObject tablaJson = new JSONObject()
                .put("nombre_tabla", "usuarios")
                .put("tipo_objeto", "TABLA")
                .put("columnas", columnas);
        return tablaJson.toString();
    }
}
