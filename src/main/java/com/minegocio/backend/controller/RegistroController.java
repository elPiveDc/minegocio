package com.minegocio.backend.controller;

import com.minegocio.backend.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombreUsuario,
            @RequestParam String correo,
            @RequestParam String password,
            @RequestParam String nombreFranquicia,
            @RequestParam(name = "bd") String nombreBd) {

        try {
            registroService.registrarUsuarioConFranquicia(
                    nombreUsuario,
                    correo,
                    password,
                    nombreFranquicia,
                    nombreBd);

            // Si todo salió bien, redirige al login
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            // Si hay error de validación (por ejemplo, correo ya en uso)
            return "redirect:/registro?error=" + e.getMessage();
        } catch (Exception e) {
            // Si hay otro error inesperado
            return "redirect:/registro?error=Error inesperado, intenta de nuevo.";
        }
    }
}
