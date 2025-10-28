package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "franquicias",
    indexes = {
        @Index(name = "idx_franquicias_usuario", columnList = "id_usuario")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Franquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_franquicia")
    private Integer idFranquicia;

    // Relación N:1 con Usuario (dueño)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    @Column(name = "nombre_franquicia", nullable = false, unique = true, length = 100)
    private String nombreFranquicia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFranquicia estado = EstadoFranquicia.ACTIVA;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Campos de auditoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id_usuario")
    @ToString.Exclude
    private Usuario createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id_usuario")
    @ToString.Exclude
    private Usuario updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relación 1:N con BaseDatosFranquicia
    @OneToMany(
        mappedBy = "franquicia",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @Builder.Default
    private List<BaseDatosFranquicia> basesDatos = new ArrayList<>();

    // === ENUM ===
    public enum EstadoFranquicia {
        ACTIVA, INACTIVA, ELIMINADA
    }

    // === Métodos auxiliares ===
    public void addBaseDatos(BaseDatosFranquicia bd) {
        basesDatos.add(bd);
        bd.setFranquicia(this);
    }

    public void removeBaseDatos(BaseDatosFranquicia bd) {
        basesDatos.remove(bd);
        bd.setFranquicia(null);
    }
}
