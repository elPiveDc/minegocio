package com.minegocio.backend.controller;

import com.minegocio.backend.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            @RequestParam(name = "bd") String nombreBd,
            Model model) {

        try {
            registroService.registrarUsuarioConFranquicia(
                    nombreUsuario, correo, password, nombreFranquicia, nombreBd);

            model.addAttribute("successMessage", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error inesperado, por favor intenta de nuevo.");
        }

        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("correo", correo);
        model.addAttribute("nombreFranquicia", nombreFranquicia);
        model.addAttribute("bd", nombreBd);

        return "registro";
    }
}
