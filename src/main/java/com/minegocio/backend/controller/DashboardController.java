package com.minegocio.backend.controller;

import com.minegocio.backend.dto.UsuarioSesion;
import com.minegocio.backend.service.ObjetoBdFranquiciaService;
import com.minegocio.backend.util.PasswordUtil;
import com.minegocio.backend.util.SqlBuilderUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final ObjetoBdFranquiciaService objetoBdFranquiciaService;

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");
        if (usuario == null)
            return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }

    // vista, creacion y actualizacion de usuarios

    @GetMapping("/usuarios")
    public String usuarios(HttpSession session, Model model) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");
        if (usuario == null)
            return "redirect:/login";

        try {
            List<Object[]> listaUsuarios = objetoBdFranquiciaService.ejecutarConsultaLectura(
                    usuario,
                    "usuarios",
                    SqlBuilderUtil.TipoConsulta.SELECT);

            model.addAttribute("usuarioSesion", usuario);
            model.addAttribute("usuarios", listaUsuarios);
        } catch (Exception e) {
            model.addAttribute("error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(HttpSession session,
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String password) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");
        if (usuario == null)
            return "redirect:/login";

        try {
            String contraseñaHash = PasswordUtil.hashPassword(password);
            Map<String, Object> valores = new HashMap<>();
            valores.put("nombre", nombre);
            valores.put("correo", correo);
            valores.put("password_hash", contraseñaHash);
            valores.put("es_admin", false); // valor por defecto
            valores.put("fecha_registro", Timestamp.from(Instant.now()));

            objetoBdFranquiciaService.ejecutarConsultaEscritura(
                    usuario,
                    "usuarios",
                    SqlBuilderUtil.TipoConsulta.INSERT,
                    valores);
        } catch (Exception e) {
            // Podrías loguear el error o redirigir con mensaje de error
        }

        return "redirect:/dashboard/usuarios";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String editarUsuario(HttpSession session,
            @PathVariable Integer id,
            @RequestParam String nombre,
            @RequestParam String correo) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");
        if (usuario == null)
            return "redirect:/login";

        try {
            Map<String, Object> valores = new HashMap<>();
            valores.put("id", id);
            valores.put("nombre", nombre);
            valores.put("correo", correo);

            objetoBdFranquiciaService.ejecutarConsultaEscritura(
                    usuario,
                    "usuarios",
                    SqlBuilderUtil.TipoConsulta.UPDATE,
                    valores);
        } catch (Exception e) {
            // log o manejo de error
        }

        return "redirect:/dashboard/usuarios";
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(HttpSession session,
            @PathVariable Integer id) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuarioLogueado");
        if (usuario == null)
            return "redirect:/login";

        try {
            Map<String, Object> valores = new HashMap<>();
            valores.put("id", id);

            objetoBdFranquiciaService.ejecutarConsultaEscritura(
                    usuario,
                    "usuarios",
                    SqlBuilderUtil.TipoConsulta.DELETE,
                    valores);
        } catch (Exception e) {
            // log o manejo de error
        }

        return "redirect:/dashboard/usuarios";
    }

    @GetMapping("/basedatos")
    public String basedatos() {
        return "basedatos";
    }

    @GetMapping("/modulos")
    public String modulos() {
        return "modulos";
    }
}
