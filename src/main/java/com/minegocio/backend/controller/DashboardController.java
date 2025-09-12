package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }

    // Agregar entidades, respositorios y servicios especializados

    @GetMapping("/dashboard/usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/dashboard/basedatos")
    public String basedatos() {
        return "basedatos";
    }

    @GetMapping("/dashboard/modulos")
    public String modulos() {
        return "modulos";
    }
}
