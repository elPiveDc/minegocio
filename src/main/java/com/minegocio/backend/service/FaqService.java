package com.minegocio.backend.service;

import com.minegocio.backend.entity.Faq;
import com.minegocio.backend.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    /**
     * Busca preguntas:
     * 1) limpia el texto para fulltext
     * 2) intenta fulltext
     * 3) si no hay resultados o ocurre error, usa LIKE como fallback
     */
    public List<Faq> buscarPreguntas(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return List.of();
        }

        String original = texto.trim();
        String cleaned = sanitizeForFullText(original);

        // Intento fulltext
        try {
            List<Faq> results = faqRepository.searchByText(cleaned);
            if (results != null && !results.isEmpty()) {
                return results;
            }
        } catch (Exception ex) {
            // Opcional: loggear el error (logger)
            // logger.error("Fulltext search failed", ex);
        }

        // Fallback: búsqueda con LIKE sobre el texto original
        return faqRepository.searchLike(original);
    }

    /**
     * Sanitiza el término para usar en MATCH ... AGAINST:
     * - reemplaza caracteres no alfanuméricos por espacios
     * - compacta múltiples espacios
     * Esto evita que paréntesis, comillas, operadores, etc. rompan la consulta.
     */
    private String sanitizeForFullText(String input) {
        if (input == null)
            return "";
        // Reemplaza todo lo que NO sea letra, número o espacio por espacio
        String step1 = input.replaceAll("[^\\p{L}\\p{N}\\s]+", " ");
        // Compacta múltiples espacios y trim
        return step1.replaceAll("\\s+", " ").trim();
    }

    public Faq guardar(Faq faq) {
        return faqRepository.save(faq);
    }

    public List<Faq> obtenerTodas() {
        return faqRepository.findAll();
    }

    public void eliminarPregunta(Integer id) {
        faqRepository.deleteById(id);
    }

    public Faq obtenerPorId(Integer id) {
        return faqRepository.findById(id).orElseThrow(() -> new RuntimeException("FAQ no encontrada: " + id));
    }

}
