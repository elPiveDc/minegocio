package com.minegocio.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {

        return "index";
    }

    // Enlaces (header)
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Páginas de enlaces rápidos (footer)

    @GetMapping("/libro-reclamaciones")
    public String libroReclamaciones() {
        return "libro-reclamaciones";
    }

    @GetMapping("/terminos")
    public String terminos() {
        return "terminos";
    }

    @GetMapping("/privacidad")
    public String privacidad() {
        return "privacidad";
    }

}
