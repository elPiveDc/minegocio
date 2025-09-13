package com.minegocio.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "objetos_bd_franquicia")
public class ObjetoBdFranquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objeto")
    private Integer idObjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bd", nullable = false)
    private BaseDatosFranquicia baseDatosFranquicia;

    @Column(name = "nombre_tabla", nullable = false, length = 100)
    private String nombreTabla;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_objeto", nullable = false, length = 20)
    private TipoObjeto tipoObjeto = TipoObjeto.TABLA;

    @Column(name = "es_tabla_usuarios", nullable = false)
    private Boolean esTablaUsuarios = false;

    /**
     * Guardamos la definición de columnas en formato JSON.
     * Ejemplo:
     * [
     * {"nombre": "id", "tipo": "INT", "pk": true},
     * {"nombre": "nombre", "tipo": "VARCHAR(100)"},
     * {"nombre": "correo", "tipo": "VARCHAR(100)"}
     * ]
     */

    @Column(name = "columnas", columnDefinition = "JSON", nullable = false)
    private String columnas;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoObjeto {
        TABLA, VISTA, FUNCION
    }

    public ObjetoBdFranquicia() {
    }

    public ObjetoBdFranquicia(BaseDatosFranquicia baseDatosFranquicia, String nombreTabla, String columnasJson) {
        this.baseDatosFranquicia = baseDatosFranquicia;
        this.nombreTabla = nombreTabla;
        this.columnas = columnasJson;
    }

    // ==== Getters & Setters ====

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public BaseDatosFranquicia getBaseDatosFranquicia() {
        return baseDatosFranquicia;
    }

    public void setBaseDatosFranquicia(BaseDatosFranquicia baseDatosFranquicia) {
        this.baseDatosFranquicia = baseDatosFranquicia;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public Boolean getEsTablaUsuarios() {
        return esTablaUsuarios;
    }

    public void setEsTablaUsuarios(Boolean esTablaUsuarios) {
        this.esTablaUsuarios = esTablaUsuarios;
    }

    public String getColumnas() {
        return columnas;
    }

    public void setColumnas(String columnas) {
        this.columnas = columnas;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
