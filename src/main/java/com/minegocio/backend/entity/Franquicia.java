package com.minegocio.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "franquicias")
public class Franquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFranquicia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;  // dueño de la franquicia

    @Column(nullable = false, unique = true)
    private String nombreFranquicia;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVA;

    public enum Estado {
        ACTIVA, INACTIVA, ELIMINADA
    }

    //Constructores
    public Franquicia() {}

    public Franquicia(Usuario usuario, String nombreFranquicia) {
        this.usuario = usuario;
        this.nombreFranquicia = nombreFranquicia;
    }

    //Getters y Setters
    public Long getIdFranquicia() { return idFranquicia; }
    public void setIdFranquicia(Long idFranquicia) { this.idFranquicia = idFranquicia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNombreFranquicia() { return nombreFranquicia; }
    public void setNombreFranquicia(String nombreFranquicia) { this.nombreFranquicia = nombreFranquicia; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
