package com.minegocio.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCreacion {

    private String nombreUsuario;
    private String correo;
    private String password;
    private String nombreFranquicia;
    private String nombreBd;

    // Constructor vacío para Spring
    public UsuarioCreacion() {}

    // Constructor útil para pruebas o creaciones manuales
    public UsuarioCreacion(String nombreUsuario, String correo, String password,
                              String nombreFranquicia, String nombreBd) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.password = password;
        this.nombreFranquicia = nombreFranquicia;
        this.nombreBd = nombreBd;
    }
}
