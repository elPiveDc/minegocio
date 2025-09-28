package com.minegocio.backend.controller;

import com.minegocio.backend.dto.UsuarioSesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {

        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");

        if (usuario != null) {
            // Elimina el usuario de la sesión
            session.removeAttribute("usuarioLogueado");

            // Sobrescribe en el modelo para que el header no lo muestre
            model.addAttribute("usuarioSesion", null);

            // Envía el mensaje
            model.addAttribute("mensajeCierre", "Tu sesión ha sido cerrada");
        }

        return "index";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
