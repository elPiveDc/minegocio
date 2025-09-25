package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Faq;
import com.minegocio.backend.service.FaqService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/preguntas")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    /**
     * Página principal de Preguntas.
     * Renderiza preguntas.html vacío inicialmente.
     */
    @GetMapping
    public String preguntasPage(Model model) {
        model.addAttribute("pregunta", "");
        model.addAttribute("resultados", List.of());
        return "preguntas"; // preguntas.html
    }

    /**
     * Buscar preguntas similares a lo que escribe el usuario.
     */
    @PostMapping("/buscar")
    public String buscarPreguntas(@RequestParam("pregunta") String pregunta, Model model) {
        List<Faq> resultados = faqService.buscarPreguntas(pregunta);

        model.addAttribute("pregunta", pregunta);
        model.addAttribute("resultados", resultados);

        return "preguntas"; // -> templates/preguntas.html
    }

    /**
     * Guardar nueva pregunta frecuente.
     */
    @PostMapping("/guardar")
    public String guardarPregunta(@RequestParam("question") String question,
            @RequestParam("answer") String answer,
            Model model) {
        faqService.guardarPregunta(question, answer);
        model.addAttribute("successMessage", "Pregunta registrada con éxito.");
        return "redirect:/preguntas";
    }
}
