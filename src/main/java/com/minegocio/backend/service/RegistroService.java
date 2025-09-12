package com.minegocio.backend.service;

import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.entity.Franquicia;
import com.minegocio.backend.entity.BaseDatosFranquicia;
import com.minegocio.backend.repository.UsuarioRepository;
import com.minegocio.backend.repository.FranquiciaRepository;
import com.minegocio.backend.repository.BaseDatosFranquiciaRepository;
import com.minegocio.backend.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final FranquiciaRepository franquiciaRepository;
    private final BaseDatosFranquiciaRepository bdRepository;

    public RegistroService(UsuarioRepository usuarioRepository,
            FranquiciaRepository franquiciaRepository,
            BaseDatosFranquiciaRepository bdRepository) {
        this.usuarioRepository = usuarioRepository;
        this.franquiciaRepository = franquiciaRepository;
        this.bdRepository = bdRepository;
    }

    @Transactional
    public Usuario registrarUsuarioConFranquicia(
            String nombreUsuario,
            String correo,
            String password,
            String nombreFranquicia,
            String nombreBd) {

        // Verificar que el correo no esté en uso
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya está en uso.");
        }

        // Crear usuario y encriptar contraseña
        Usuario usuario = new Usuario();
        usuario.setNombre(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setPasswordHash(PasswordUtil.hashPassword(password));

        usuario = usuarioRepository.save(usuario);

        // Crear franquicia asociada
        Franquicia franquicia = new Franquicia();
        franquicia.setUsuario(usuario);
        franquicia.setNombreFranquicia(nombreFranquicia);

        franquicia = franquiciaRepository.save(franquicia);

        // Crear base de datos asociada a la franquicia
        BaseDatosFranquicia bd = new BaseDatosFranquicia();
        bd.setFranquicia(franquicia);
        bd.setNombreBd(nombreBd);

        bdRepository.save(bd);

        return usuario;
    }
}
