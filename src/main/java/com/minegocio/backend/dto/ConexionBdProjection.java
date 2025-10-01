package com.minegocio.backend.dto;

public interface ConexionBdProjection {
    Integer getIdBd();

    String getUrlConexion();

    String getUsuarioConexion();

    String getPassConexionHash();
}
