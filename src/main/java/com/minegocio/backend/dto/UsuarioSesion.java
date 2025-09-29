package com.minegocio.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSesion {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private boolean esAdmin;
    private String franquicia;
    private ConexionBdProjection conexion;

    public UsuarioSesion(Integer idUsuario, String nombre, String correo, boolean esAdmin,
            ConexionBdProjection conexion, String franquicia) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.esAdmin = esAdmin;
        this.conexion = conexion;
        this.franquicia = franquicia;
    }

}
