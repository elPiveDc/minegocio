package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "tipo_contenido", nullable = false, length = 100)
    private String tipoContenido;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] archivo;

    @Column(name = "fecha_subida", updatable = false, insertable = false)
    private LocalDateTime fechaSubida;
}
