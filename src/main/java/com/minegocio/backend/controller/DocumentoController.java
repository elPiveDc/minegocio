package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Documento;
import com.minegocio.backend.service.DocumentoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // Página HTML (ej: /documentos/terminos -> documento.html)
    @GetMapping("/{slug}")
    public String paginaDocumento(@PathVariable String slug, Model model) {
        Documento doc = documentoService.obtenerPorSlug(slug);
        model.addAttribute("titulo", doc.getTitulo());
        model.addAttribute("slug", slug);
        return "documento"; // templates/documento.html
    }

    // Ver en navegador (inline)
    @GetMapping("/{slug}/ver")
    public ResponseEntity<byte[]> verDocumento(@PathVariable String slug) {
        Documento doc = documentoService.obtenerPorSlug(slug);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + doc.getTitulo())
                .contentType(MediaType.parseMediaType(doc.getTipoContenido()))
                .body(doc.getArchivo());
    }

    // Descargar
    @GetMapping("/{slug}/descargar")
    public ResponseEntity<byte[]> descargarDocumento(@PathVariable String slug) {
        Documento doc = documentoService.obtenerPorSlug(slug);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + doc.getTitulo())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(doc.getArchivo());
    }
}
