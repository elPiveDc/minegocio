package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@Setter
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", nullable = false, length = 20)
    private TipoConsulta tipoConsulta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoConsulta estado = EstadoConsulta.PENDIENTE;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // ==== ENUMS ====
    public enum TipoConsulta {
        CONSULTA, ACTUALIZACION, ERROR, OTRO
    }

    public enum EstadoConsulta {
        PENDIENTE, EN_PROCESO, RESUELTO, CERRADO
    }

    // ==== CONSTRUCTORES ====
    public Consulta() {
    }

    public Consulta(Usuario usuario, TipoConsulta tipoConsulta, String descripcion) {
        this.usuario = usuario;
        this.tipoConsulta = tipoConsulta;
        this.descripcion = descripcion;
    }
}