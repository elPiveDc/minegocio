package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Franquicia;
import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.service.FranquiciaService;
import com.minegocio.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;
    private final UsuarioService usuarioService;

    public FranquiciaController(FranquiciaService franquiciaService, UsuarioService usuarioService) {
        this.franquiciaService = franquiciaService;
        this.usuarioService = usuarioService;
    }

    // Listar todas
    @GetMapping
    public List<Franquicia> listar() {
        return franquiciaService.listarFranquicias();
    }

    // Crear franquicia asociada a un usuario (dueño)
    @PostMapping("/{idUsuario}")
    public Franquicia crear(@PathVariable Long idUsuario, @RequestParam String nombreFranquicia) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(idUsuario);
        if (usuario.isPresent()) {
            return franquiciaService.crearFranquicia(usuario.get(), nombreFranquicia);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    //Buscar por nombre
    @GetMapping("/buscar")
    public Optional<Franquicia> buscarPorNombre(@RequestParam String nombre) {
        return franquiciaService.buscarPorNombre(nombre);
    }

    //Eliminar por ID
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        franquiciaService.eliminarFranquicia(id);
    }
}
