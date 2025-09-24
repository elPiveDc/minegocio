package com.minegocio.backend.controller;

import com.minegocio.backend.dto.UsuarioSesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }

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
