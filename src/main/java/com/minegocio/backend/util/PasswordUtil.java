package com.minegocio.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para encriptar contraseñas
    public static String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Método para verificar si una contraseña es correcta
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
