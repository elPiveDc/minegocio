package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Consulta;
import com.minegocio.backend.service.LibroDeReclamacionesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/libro-reclamaciones")
public class LibroDeReclamacionesController {

    private final LibroDeReclamacionesService libroService;

    public LibroDeReclamacionesController(LibroDeReclamacionesService libroService) {
        this.libroService = libroService;
    }

    // === 1. Mostrar vista del libro de reclamaciones ===
    @GetMapping
    public String libroReclamaciones() {
        return "libro-reclamaciones"; // Thymeleaf: templates/libro-reclamaciones.html
    }

    // === 2. Recibir formulario de reclamación ===
    @PostMapping("/enviar")
    public String enviarReclamacion(
            @RequestParam("correo") String correo,
            @RequestParam("tipoConsulta") Consulta.TipoConsulta tipoConsulta,
            @RequestParam("descripcion") String descripcion,
            Model model) {

        String resultado = libroService.registrarReclamacion(correo, tipoConsulta, descripcion);
        model.addAttribute("mensaje", resultado);

        return "libro-reclamaciones";
    }

    // === 3. Validar correo ===
    @GetMapping("/validar")
    @ResponseBody
    public String validarCorreo(@RequestParam("correo") String correo) {
        return libroService.validarCorreoEnLibroReclamaciones(correo);
    }
}