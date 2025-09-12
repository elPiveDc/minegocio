package com.minegocio.backend.controller;

import com.minegocio.backend.entity.Usuario;
import com.minegocio.backend.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String usuario, // este es el correo
            @RequestParam String password,
            @RequestParam String franquicia,
            HttpSession session) {
        try {
            Usuario user = loginService.autenticar(usuario, password, franquicia);

            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", user);

            return "redirect:/dashboard"; // redirige al dashboard único del usuario

        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=" + e.getMessage();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
