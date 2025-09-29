package com.minegocio.backend.service;

import com.minegocio.backend.entity.Consulta;
import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.repository.ConsultaRepository;
import com.minegocio.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroDeReclamacionesService {

    private final UsuarioRepository usuarioRepository;
    private final ConsultaRepository consultaRepository;

    public LibroDeReclamacionesService(UsuarioRepository usuarioRepository,
            ConsultaRepository consultaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.consultaRepository = consultaRepository;
    }

    public String registrarReclamacion(String correo, Consulta.TipoConsulta tipoConsulta, String descripcion) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario == null) {
            return "El correo no está registrado en usuarios.";
        }

        Consulta consulta = new Consulta(usuario, tipoConsulta, descripcion);
        consultaRepository.save(consulta);

        return "Reclamación registrada correctamente.";
    }

    /**
     * Verifica si un correo pertenece a un usuario dueño de franquicia.
     */
    public boolean existeEnUsuarios(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    /**
     * Verifica si un correo está vinculado con consultas realizadas.
     * Nota: Asumimos que "consulta" siempre tiene un usuario asociado.
     */
    public boolean existeEnConsultas(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) {
            return false;
        }
        List<Consulta> consultas = consultaRepository.findByUsuario(usuario);
        return !consultas.isEmpty();
    }

    /**
     * Determina si el correo pertenece a un usuario que es dueño de franquicia
     * y además ha realizado consultas/reclamaciones.
     */
    public String validarCorreoEnLibroReclamaciones(String correo) {
        boolean enUsuarios = existeEnUsuarios(correo);
        boolean enConsultas = existeEnConsultas(correo);

        if (enUsuarios && enConsultas) {
            return "El correo pertenece a un usuario dueño de franquicia y tiene consultas registradas.";
        } else if (enUsuarios) {
            return "El correo pertenece a un usuario dueño de franquicia, pero no tiene consultas.";
        } else if (enConsultas) {
            return "El correo tiene consultas registradas, pero no pertenece a un usuario dueño de franquicia.";
        } else {
            return "El correo no está registrado en usuarios ni en consultas.";
        }
    }

    // mas metodos para admin:
    public List<Consulta> obtenerTodasConsultas() {
        return consultaRepository.findAll();
    }

    public Consulta obtenerPorId(int id) {
        return consultaRepository.findById(id).orElse(null);
    }

    public void eliminarConsulta(int id) {
        consultaRepository.deleteById(id);
    }

    public void guardar(Consulta consulta) {
        consultaRepository.save(consulta);
    }

}