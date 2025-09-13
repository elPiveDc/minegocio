# Gestor de Franquicias

Aplicación web desarrollada en Spring Boot (JDK 23) con Thymeleaf, HTML y Bootstrap, que permite gestionar franquicias (negocios) y su configuración sobre una base de datos MySQL.

Actualmente el sistema ya cuenta con registro de usuarios y franquicias con contraseñas encriptadas, así como inicio de sesión con validación en base de datos y manejo de sesión.

Estado actual del proyecto

El proyecto cuenta con:

✅ Frontend completo con HTML + Bootstrap + Thymeleaf
✅ Registro de usuario + franquicia + base de datos asociada
✅ Encriptación de contraseñas usando BCryptPasswordEncoder
✅ Inicio de sesión funcional, con sesión HTTP y redirección al dashboard
✅ Repositorios JPA para usuarios y franquicias
✅ Controladores MVC para navegación, registro, login y dashboard

Controladores implementados

HomeController – Maneja navegación básica entre vistas:

@Controller
public class HomeController {
    @GetMapping("/") public String index() { return "index"; }
    @GetMapping("/registro") public String registro() { return "registro"; }
    @GetMapping("/login") public String login() { return "login"; }
}


RegistroController – Registra usuario, franquicia y base de datos:

@Controller
public class RegistroController {
    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registro")
    public String registrarUsuario(
        @RequestParam String nombreUsuario,
        @RequestParam String correo,
        @RequestParam String password,
        @RequestParam String nombreFranquicia,
        @RequestParam(name = "bd") String nombreBd) {

        try {
            registroService.registrarUsuarioConFranquicia(
                    nombreUsuario, correo, password,
                    nombreFranquicia, nombreBd);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/registro?error=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/registro?error=Error inesperado, intenta de nuevo.";
        }
    }
}


LoginController – Autenticación con verificación de contraseña encriptada y sesión:

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String usuario,
            @RequestParam String password,
            @RequestParam String franquicia,
            HttpSession session) {
        try {
            Usuario user = loginService.autenticar(usuario, password, franquicia);
            session.setAttribute("usuarioLogueado", user);
            return "redirect:/dashboard";
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


DashboardController – Protege la vista, solo usuarios autenticados pueden acceder:

@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}

Repositorios JPA
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}

public interface FranquiciaRepository extends JpaRepository<Franquicia, Integer> {
    boolean existsByNombreFranquicia(String nombreFranquicia);
}

Utilidad para encriptación de contraseñas
public class PasswordUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

Configuración (application.properties)

Configuración para MySQL y Hibernate:

spring.application.name=MiNegocio

spring.datasource.url=jdbc:mysql://localhost:3306/sistema_franquicias
spring.datasource.username=Prueba
spring.datasource.password=Prueba123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

Script SQL de la base de datos central

Incluye tablas de usuarios, franquicias, y configuraciones de BD (ver versión previa del README para el script completo).

Requisitos del sistema

Java 23 (JDK)

Maven

Spring Boot

MySQL 8.0+

Navegador web moderno (Thymeleaf + Bootstrap)

Instalación y ejecución
git clone https://github.com/tu_usuario/gestor-franquicias.git
cd gestor-franquicias
mvn spring-boot:run


Acceder en navegador: http://localhost:8080

Funcionalidades actuales

✅ Registro de usuario + franquicia + BD
✅ Contraseñas encriptadas con BCrypt
✅ Validación de credenciales y login
✅ Sesión activa con HttpSession
✅ Redirección automática al dashboard
✅ Desconexión con /logout

Próximos pasos

🔜 Implementar manejo de errores en frontend (mensajes visibles en /login y /registro)
🔜 CRUD completo de usuarios y franquicias desde el dashboard
🔜 Creación dinámica de base de datos de cada franquicia
🔜 Integración con Spring Security (roles y permisos)
