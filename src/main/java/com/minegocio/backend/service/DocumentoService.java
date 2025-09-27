package com.minegocio.backend.service;

import com.minegocio.backend.entity.Documento;
import com.minegocio.backend.repository.DocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    public Documento obtenerPorSlug(String slug) {
        return documentoRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado: " + slug));
    }

    public Documento obtenerPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado: " + id));
    }

    public Documento guardarDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    public void eliminarDocumento(Long id) {
        documentoRepository.deleteById(id);
    }

    public List<Documento> obtenerTodos() {
        return documentoRepository.findAll();
    }
}
