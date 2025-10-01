package com.minegocio.backend.service;

import com.minegocio.backend.dto.ConexionBdProjection;
import com.minegocio.backend.dto.UsuarioSesion;
import com.minegocio.backend.repository.BaseDatosFranquiciaRepository;
import com.minegocio.backend.util.PasswordUtil;
import org.springframework.stereotype.Service;
import com.minegocio.backend.exception.AutenticacionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Service
public class LoginService {

    private final BaseDatosFranquiciaRepository baseDatosFranquiciaRepository;

    public LoginService(BaseDatosFranquiciaRepository baseDatosFranquiciaRepository) {
        this.baseDatosFranquiciaRepository = baseDatosFranquiciaRepository;
    }

    public UsuarioSesion autenticar(String correo, String password, String nombreFranquicia) {
        ConexionBdProjection datosConexion = baseDatosFranquiciaRepository
                .obtenerDatosConexionPorNombreFranquicia(nombreFranquicia)
                .orElseThrow(() -> new AutenticacionException("Franquicia no encontrada"));

        try (Connection conn = DriverManager.getConnection(
                datosConexion.getUrlConexion(),
                datosConexion.getUsuarioConexion(),
                datosConexion.getPassConexionHash())) {

            String sql = "SELECT id_usuario, nombre, correo, password_hash, es_admin FROM usuarios WHERE correo = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, correo);
                var rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new AutenticacionException("Usuario no encontrado en la franquicia");
                }

                Integer idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String correoDb = rs.getString("correo");
                String passwordHash = rs.getString("password_hash");
                boolean esAdmin = rs.getBoolean("es_admin");

                if (!PasswordUtil.checkPassword(password, passwordHash)) {
                    throw new AutenticacionException("Contraseña incorrecta");
                }

                return new UsuarioSesion(idUsuario, nombre, correoDb, esAdmin, datosConexion, nombreFranquicia);
            }
        } catch (AutenticacionException e) {
            throw e; // re-lanza las excepciones de autenticación tal cual
        } catch (Exception e) {
            throw new AutenticacionException("Error autenticando en la BD de la franquicia: " + e.getMessage());
        }
    }

}
