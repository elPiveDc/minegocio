package com.minegocio.backend.service;

import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepo.deleteById(id);
    }

    // Métodos que faltaban
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepo.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepo.findByCorreo(correo);
    }
}
