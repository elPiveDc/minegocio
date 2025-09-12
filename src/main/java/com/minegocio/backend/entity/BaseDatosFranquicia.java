package com.minegocio.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bases_datos_franquicia")
public class BaseDatosFranquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bd")
    private Integer idBd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_franquicia", nullable = false)
    private Franquicia franquicia;

    @Column(name = "nombre_bd", nullable = false, length = 100)
    private String nombreBd;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bd", nullable = false, length = 20)
    private TipoBd tipoBd = TipoBd.MYSQL;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoBd estado = EstadoBd.NO_CONFIGURADA;

    @Column(name = "url_conexion", columnDefinition = "TEXT")
    private String urlConexion;

    @Column(name = "usuario_conexion", length = 100)
    private String usuarioConexion;

    @Column(name = "pass_conexion_hash", length = 255)
    private String passConexionHash;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoBd {
        MYSQL, POSTGRESQL, ORACLE, MONGODB, CASSANDRA
    }

    public enum EstadoBd {
        CONFIGURADA, NO_CONFIGURADA, ERROR
    }

    public BaseDatosFranquicia() {
    }

    public BaseDatosFranquicia(Franquicia franquicia, String nombreBd) {
        this.franquicia = franquicia;
        this.nombreBd = nombreBd;
    }

    // ==== Getters & Setters ====
    public Integer getIdBd() {
        return idBd;
    }

    public void setIdBd(Integer idBd) {
        this.idBd = idBd;
    }

    public Franquicia getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(Franquicia franquicia) {
        this.franquicia = franquicia;
    }

    public String getNombreBd() {
        return nombreBd;
    }

    public void setNombreBd(String nombreBd) {
        this.nombreBd = nombreBd;
    }

    public TipoBd getTipoBd() {
        return tipoBd;
    }

    public void setTipoBd(TipoBd tipoBd) {
        this.tipoBd = tipoBd;
    }

    public EstadoBd getEstado() {
        return estado;
    }

    public void setEstado(EstadoBd estado) {
        this.estado = estado;
    }

    public String getUrlConexion() {
        return urlConexion;
    }

    public void setUrlConexion(String urlConexion) {
        this.urlConexion = urlConexion;
    }

    public String getUsuarioConexion() {
        return usuarioConexion;
    }

    public void setUsuarioConexion(String usuarioConexion) {
        this.usuarioConexion = usuarioConexion;
    }

    public String getPassConexionHash() {
        return passConexionHash;
    }

    public void setPassConexionHash(String passConexionHash) {
        this.passConexionHash = passConexionHash;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
