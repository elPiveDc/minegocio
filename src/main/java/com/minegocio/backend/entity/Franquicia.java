package com.minegocio.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franquicias")
public class Franquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_franquicia")
    private Integer idFranquicia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_franquicia", nullable = false, unique = true, length = 100)
    private String nombreFranquicia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFranquicia estado = EstadoFranquicia.ACTIVA;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "franquicia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaseDatosFranquicia> basesDatos = new ArrayList<>();

    public enum EstadoFranquicia {
        ACTIVA, INACTIVA, ELIMINADA
    }

    public Franquicia() {
    }

    public Franquicia(Usuario usuario, String nombreFranquicia) {
        this.usuario = usuario;
        this.nombreFranquicia = nombreFranquicia;
    }

    // ==== Getters & Setters ====
    public Integer getIdFranquicia() {
        return idFranquicia;
    }

    public void setIdFranquicia(Integer idFranquicia) {
        this.idFranquicia = idFranquicia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreFranquicia() {
        return nombreFranquicia;
    }

    public void setNombreFranquicia(String nombreFranquicia) {
        this.nombreFranquicia = nombreFranquicia;
    }

    public EstadoFranquicia getEstado() {
        return estado;
    }

    public void setEstado(EstadoFranquicia estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<BaseDatosFranquicia> getBasesDatos() {
        return basesDatos;
    }

    public void setBasesDatos(List<BaseDatosFranquicia> basesDatos) {
        this.basesDatos = basesDatos;
    }
}
