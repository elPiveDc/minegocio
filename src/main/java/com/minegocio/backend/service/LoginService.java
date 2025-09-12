package com.minegocio.backend.service;

import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.repository.UsuarioRepository;
import com.minegocio.backend.repository.FranquiciaRepository;
import com.minegocio.backend.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final FranquiciaRepository franquiciaRepository;

    public LoginService(UsuarioRepository usuarioRepository, FranquiciaRepository franquiciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public Usuario autenticar(String correo, String password, String nombreFranquicia) {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar contraseña con PasswordUtil
        if (!PasswordUtil.checkPassword(password, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // Validar que la franquicia exista
        boolean franquiciaValida = franquiciaRepository.existsByNombreFranquicia(nombreFranquicia);
        if (!franquiciaValida) {
            throw new IllegalArgumentException("Franquicia no encontrada");
        }

        return usuario;
    }
}
