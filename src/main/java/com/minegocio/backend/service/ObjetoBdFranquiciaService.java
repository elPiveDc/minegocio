package com.minegocio.backend.service;

import com.minegocio.backend.dto.UsuarioSesion;
import com.minegocio.backend.entity.BaseDatosFranquicia;
import com.minegocio.backend.entity.ObjetoBdFranquicia;
import com.minegocio.backend.repository.BaseDatosFranquiciaRepository;
import com.minegocio.backend.repository.ObjetoBdFranquiciaRepository;
import com.minegocio.backend.util.SqlBuilderUtil;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ObjetoBdFranquiciaService {

    private final ObjetoBdFranquiciaRepository objetoBdFranquiciaRepository;
    private final BaseDatosFranquiciaRepository baseDatosFranquiciaRepository;

    /**
     * Ejecuta solo consultas de tipo SELECT y devuelve los datos en lista.
     */
    public List<Object[]> ejecutarConsultaLectura(UsuarioSesion sesion,
            String nombreTabla,
            SqlBuilderUtil.TipoConsulta tipoConsulta) throws Exception {

        if (!tipoConsulta.isSoloLectura()) {
            throw new IllegalArgumentException("Para operaciones de escritura use ejecutarConsultaEscritura()");
        }

        ObjetoBdFranquicia objeto = objetoBdFranquiciaRepository
                .findByBaseDatosFranquiciaIdBdAndNombreTabla(
                        sesion.getConexion().getIdBd(), nombreTabla)
                .orElseThrow(
                        () -> new IllegalArgumentException("No se encontró el objeto para la tabla: " + nombreTabla));

        String sql = SqlBuilderUtil.construirConsulta(objeto.getColumnas(), tipoConsulta);

        try (Connection conn = DriverManager.getConnection(
                sesion.getConexion().getUrlConexion(),
                sesion.getConexion().getUsuarioConexion(),
                sesion.getConexion().getPassConexionHash());
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            int colCount = rs.getMetaData().getColumnCount();
            List<Object[]> result = new ArrayList<>();
            while (rs.next()) {
                Object[] fila = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    fila[i - 1] = rs.getObject(i);
                }
                result.add(fila);
            }
            return result;
        }
    }

    /**
     * Ejecuta INSERT, UPDATE o DELETE con parámetros dinámicos.
     */
    public int ejecutarConsultaEscritura(UsuarioSesion sesion,
            String nombreTabla,
            SqlBuilderUtil.TipoConsulta tipoConsulta,
            Map<String, Object> valores) throws Exception {

        if (tipoConsulta.isSoloLectura()) {
            throw new IllegalArgumentException("Para SELECT use ejecutarConsultaLectura()");
        }

        ObjetoBdFranquicia objeto = objetoBdFranquiciaRepository
                .findByBaseDatosFranquiciaIdBdAndNombreTabla(
                        sesion.getConexion().getIdBd(), nombreTabla)
                .orElseThrow(
                        () -> new IllegalArgumentException("No se encontró el objeto para la tabla: " + nombreTabla));

        String sql = SqlBuilderUtil.construirConsulta(objeto.getColumnas(), tipoConsulta);
        List<String> columnas = SqlBuilderUtil.obtenerColumnasParaParametros(objeto.getColumnas(), tipoConsulta);

        try (Connection conn = DriverManager.getConnection(
                sesion.getConexion().getUrlConexion(),
                sesion.getConexion().getUsuarioConexion(),
                sesion.getConexion().getPassConexionHash());
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (String columna : columnas) {
                ps.setObject(index++, valores.get(columna));
            }

            // Para UPDATE/DELETE agregamos la PK al final si aplica
            if (tipoConsulta == SqlBuilderUtil.TipoConsulta.UPDATE
                    || tipoConsulta == SqlBuilderUtil.TipoConsulta.DELETE) {
                ps.setObject(index, valores.get("id")); // Asumimos que la PK viene como "id"
            }

            return ps.executeUpdate();
        }
    }

    public void crearTabla(UsuarioSesion sesion, String jsonDefinicion) throws Exception {
        JSONObject tablaJson = new JSONObject(jsonDefinicion);
        // 1. Registrar en ObjetoBdFranquicia
        ObjetoBdFranquicia objeto = new ObjetoBdFranquicia();
        // solo quiero insertar el id de las franquicia
        BaseDatosFranquicia bd = baseDatosFranquiciaRepository
                .ObtenerBDporNombreFranquicia(sesion.getFranquicia());
        objeto.setBaseDatosFranquicia(bd);
        objeto.setNombreTabla(tablaJson.getString("nombre_tabla"));
        objeto.setEsTablaUsuarios(
                "usuarios".equalsIgnoreCase(tablaJson.getString("nombre_tabla")));
        objeto.setColumnas(tablaJson.toString());

        objetoBdFranquiciaRepository.save(objeto);

        // 2. Construir SQL
        String sql = SqlBuilderUtil.construirCreateTable(tablaJson);

        // 3. Ejecutar DDL
        ejecutarDDL(sesion, sql);
    }

    // metodo directo

    public int ejecutarDDL(UsuarioSesion sesion, String sql) throws Exception {
        try (Connection conn = DriverManager.getConnection(
                sesion.getConexion().getUrlConexion(),
                sesion.getConexion().getUsuarioConexion(),
                sesion.getConexion().getPassConexionHash());
                Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    /**
     * Lista todas las tablas registradas para la BD de la franquicia del usuario.
     */
    public List<ObjetoBdFranquicia> listarTablas(UsuarioSesion sesion) {
        return objetoBdFranquiciaRepository.findByBaseDatosFranquiciaIdBd(
                sesion.getConexion().getIdBd());
    }

    /**
     * Devuelve la definición de columnas (JSON) de una tabla específica.
     */
    public JSONObject obtenerEstructuraTabla(UsuarioSesion sesion, String nombreTabla) {
        ObjetoBdFranquicia objeto = objetoBdFranquiciaRepository
                .findByBaseDatosFranquiciaIdBdAndNombreTabla(
                        sesion.getConexion().getIdBd(), nombreTabla)
                .orElseThrow(() -> new IllegalArgumentException("Tabla no encontrada: " + nombreTabla));

        return new JSONObject(objeto.getColumnas());
    }

}
