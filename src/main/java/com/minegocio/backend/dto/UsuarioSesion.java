package com.minegocio.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSesion {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private boolean esAdmin;
    private String franquicia;
    private ConexionBdProjection conexion;
}
