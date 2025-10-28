package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    public Consulta(Usuario usuario, TipoConsulta tipoConsulta, String descripcion) {
        this.usuario = usuario;
        this.tipoConsulta = tipoConsulta;
        this.descripcion = descripcion;
        this.estado = EstadoConsulta.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", nullable = false, length = 20)
    private TipoConsulta tipoConsulta = TipoConsulta.CONSULTA;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String respuesta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoConsulta estado = EstadoConsulta.PENDIENTE;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    // ==== ENUMS ====
    public enum TipoConsulta {
        CONSULTA, ACTUALIZACION, ERROR, OTRO
    }

    public enum EstadoConsulta {
        PENDIENTE, EN_PROCESO, RESUELTO, CERRADO
    }

    // ==== Auditoría ====
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.respuesta != null && this.estado == EstadoConsulta.PENDIENTE) {
            this.estado = EstadoConsulta.RESUELTO;
            this.fechaRespuesta = LocalDateTime.now();
        }
    }
}
