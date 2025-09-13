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

    // Metodo para generar un pasword segura
    public static String generarPasswordSegura(int length) {
        String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWER = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS = "0123456789";
        String SYMBOLS = "@#$%^&*_-+=";
        String ALL = UPPER + LOWER + DIGITS + SYMBOLS;
        java.security.SecureRandom random = new java.security.SecureRandom();
        if (length < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        StringBuilder password = new StringBuilder(length);
        // Garantizar al menos un carácter de cada tipo
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        // Completar el resto con caracteres aleatorios
        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }
        // Mezclar para que no quede predecible
        char[] array = password.toString().toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        return new String(array);
    }
}
