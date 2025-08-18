package com.minegocio.backend.service;

import com.minegocio.backend.entity.Franquicia;
import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    public List<Franquicia> listarFranquicias() {
        return franquiciaRepository.findAll();
    }

    public Franquicia crearFranquicia(Usuario usuario, String nombreFranquicia) {
        Franquicia f = new Franquicia(usuario, nombreFranquicia);
        return franquiciaRepository.save(f);
    }

    public Optional<Franquicia> buscarPorId(Long id) {
        return franquiciaRepository.findById(id);
    }

    public Optional<Franquicia> buscarPorNombre(String nombreFranquicia) {
        return franquiciaRepository.findByNombreFranquicia(nombreFranquicia);
    }

    public void eliminarFranquicia(Long id) {
        franquiciaRepository.deleteById(id);
    }
}
