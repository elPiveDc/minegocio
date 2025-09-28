package com.minegocio.backend.dto;

public class UsuarioSesion {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private boolean esAdmin;
    private String franquicia;

    public UsuarioSesion(Integer idUsuario, String nombre, String correo, boolean esAdmin) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.esAdmin = esAdmin;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public String getfranquicia() {
        return franquicia;
    }

    public void setfranquicia(String Franquicia) {
        this.franquicia = Franquicia;
    }
}
