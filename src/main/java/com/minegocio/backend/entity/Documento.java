package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "documento",
    indexes = {
        @Index(name = "idx_documento_slug", columnList = "slug", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, unique = true, length = 100)
    private String slug; // Ej: 'terminos', 'privacidad', etc.

    @Column(name = "tipo_contenido", nullable = false, length = 100)
    private String tipoContenido; // Ej: 'application/pdf'

    @Lob
    @Basic(fetch = FetchType.LAZY) // mejora rendimiento
    @Column(name = "archivo", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] archivo;

    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;

    // === Auditoría automática ===
    @PrePersist
    protected void onCreate() {
        this.fechaSubida = LocalDateTime.now();
    }
}
