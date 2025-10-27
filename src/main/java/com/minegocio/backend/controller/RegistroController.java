package com.minegocio.backend.controller;

import com.minegocio.backend.dto.UsuarioCreacion;
import com.minegocio.backend.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // 🔹 Muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        // Crea un objeto vacío para vincular con el formulario
        model.addAttribute("usuario", new UsuarioCreacion());
        return "registro";
    }

    // 🔹 Procesa el envío del formulario
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioCreacion dto, Model model) {
        try {
            registroService.registrarUsuarioConFranquicia(
                    dto.getNombreUsuario(),
                    dto.getCorreo(),
                    dto.getPassword(),
                    dto.getNombreFranquicia(),
                    dto.getNombreBd()
            );

            model.addAttribute("successMessage", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "login"; // Redirige a login.html

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error inesperado, por favor intenta de nuevo.");
        }

        // Mantiene los datos ingresados si ocurre un error
        model.addAttribute("usuario", dto);
        return "registro";
    }
}
