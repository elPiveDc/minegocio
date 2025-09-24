package com.minegocio.backend.controller;

import com.minegocio.backend.dto.UsuarioSesion;
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
            // Validar usuario en la BD de la franquicia
            UsuarioSesion user = loginService.autenticar(usuario, password, franquicia);

            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", user);

            // Redirigir según si es admin o no
            if (user.isEsAdmin()) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/dashboard";
            }

        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=" + e.getMessage();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
