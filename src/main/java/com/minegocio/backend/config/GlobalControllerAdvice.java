package com.minegocio.backend.config;

import com.minegocio.backend.dto.UsuarioSesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Este método se ejecuta antes de renderizar cualquier vista
    @ModelAttribute("usuarioSesion")
    public UsuarioSesion addUserToModel(HttpSession session) {
        // Obtiene el usuario guardado en sesión en LoginController
        return (UsuarioSesion) session.getAttribute("usuarioLogueado");
    }
}
