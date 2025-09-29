package com.minegocio.backend.config;

import com.minegocio.backend.dto.UsuarioSesion;
import com.minegocio.backend.exception.AutenticacionException;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Este método se ejecuta antes de renderizar cualquier vista
    @ModelAttribute("usuarioSesion")
    public UsuarioSesion addUserToModel(HttpSession session) {
        // Obtiene el usuario guardado en sesión en LoginController
        return (UsuarioSesion) session.getAttribute("usuarioLogueado");
    }

    // Manejo centralizado de errores de autenticación
    @ExceptionHandler(AutenticacionException.class)
    public String manejarAutenticacion(AutenticacionException ex, Model model) {
        model.addAttribute("errorMensaje", ex.getMessage());
        return "login"; // login.html
    }
}
