package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Documento;
import com.minegocio.backend.entity.Faq;
import com.minegocio.backend.service.DocumentoService;
import com.minegocio.backend.service.FaqService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DocumentoService documentoService;
    private final FaqService faqService;

    public AdminController(DocumentoService documentoService, FaqService faqService) {
        this.documentoService = documentoService;
        this.faqService = faqService;
    }

    /**
     * Página principal del admin. `section` controla qué fragmento se muestra.
     * Default -> "faqs"
     */
    @GetMapping
    public String adminPage(@RequestParam(name = "section", required = false, defaultValue = "faqs") String section,
            Model model,
            @ModelAttribute("faqForm") Faq faqFlash,
            @ModelAttribute("documentoForm") Documento docFlash) {

        // listas para las dos secciones
        model.addAttribute("section", section);
        model.addAttribute("faqs", faqService.obtenerTodas());
        model.addAttribute("documentos", documentoService.obtenerTodos());

        // formularios: si vienen en flash (edición), los usamos; si no, ponemos
        // instancias por defecto
        if (faqFlash == null || faqFlash.getQuestion() == null && faqFlash.getAnswer() == null) {
            model.addAttribute("faqForm", new Faq());
        } else {
            model.addAttribute("faqForm", faqFlash);
        }

        if (docFlash == null || docFlash.getTitulo() == null) {
            model.addAttribute("documentoForm", new Documento());
        } else {
            model.addAttribute("documentoForm", docFlash);
        }

        return "admin";
    }

    // -------------------------
    // FAQ CRUD (crear/editar/eliminar)
    // -------------------------
    @PostMapping("/faq/guardar")
    public String guardarFaq(@RequestParam(required = false) Integer id,
            @RequestParam String question,
            @RequestParam String answer,
            RedirectAttributes ra) {

        Faq faq;
        if (id != null) {
            faq = faqService.obtenerPorId(id);
            faq.setQuestion(question);
            faq.setAnswer(answer);
        } else {
            faq = new Faq();
            faq.setQuestion(question);
            faq.setAnswer(answer);
        }
        faqService.guardar(faq);
        ra.addFlashAttribute("successMessage", "FAQ guardada correctamente.");
        return "redirect:/admin?section=faqs";
    }

    @GetMapping("/faq/editar/{id}")
    public String editarFaq(@PathVariable Integer id, RedirectAttributes ra) {
        Faq faq = faqService.obtenerPorId(id);
        // pasamos el objeto en flash para que aparezca en el formulario
        ra.addFlashAttribute("faqForm", faq);
        return "redirect:/admin?section=faqs";
    }

    @GetMapping("/faq/eliminar/{id}")
    public String eliminarFaq(@PathVariable Integer id, RedirectAttributes ra) {
        faqService.eliminarPregunta(id);
        ra.addFlashAttribute("successMessage", "FAQ eliminada.");
        return "redirect:/admin?section=faqs";
    }

    // -------------------------
    // DOCUMENTOS CRUD (subir/reemplazar/eliminar)
    // -------------------------
    @PostMapping("/documentos/guardar")
    public String guardarDocumento(@RequestParam(required = false) Long id,
            @RequestParam String titulo,
            @RequestParam String slug,
            @RequestParam(name = "file", required = false) MultipartFile file,
            RedirectAttributes ra) throws IOException {

        Documento doc;
        if (id != null) {
            doc = documentoService.obtenerPorId(id);
            doc.setTitulo(titulo);
            doc.setSlug(slug);
        } else {
            doc = new Documento();
            doc.setTitulo(titulo);
            doc.setSlug(slug);
        }

        if (file != null && !file.isEmpty()) {
            doc.setArchivo(file.getBytes());
            doc.setTipoContenido(file.getContentType());
        }

        documentoService.guardarDocumento(doc);
        ra.addFlashAttribute("successMessage", "Documento guardado correctamente.");
        return "redirect:/admin?section=documentos";
    }

    @GetMapping("/documentos/editar/{id}")
    public String editarDocumento(@PathVariable Long id, Model model) {
        Documento doc = documentoService.obtenerPorId(id);

        model.addAttribute("section", "documentos");
        // Formulario precargado con el documento seleccionado
        model.addAttribute("documentoForm", doc);

        // Recargamos listas necesarias para que la vista no falle
        model.addAttribute("documentos", documentoService.obtenerTodos());
        model.addAttribute("faqs", faqService.obtenerTodas());
        model.addAttribute("faqForm", new Faq());

        return "admin";
    }

    @GetMapping("/documentos/eliminar/{id}")
    public String eliminarDocumento(@PathVariable Long id, RedirectAttributes ra) {
        documentoService.eliminarDocumento(id);
        ra.addFlashAttribute("successMessage", "Documento eliminado.");
        return "redirect:/admin?section=documentos";
    }
}
