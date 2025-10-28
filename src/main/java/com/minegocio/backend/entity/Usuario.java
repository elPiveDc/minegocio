package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "usuarios",
    indexes = {
        @Index(name = "idx_usuarios_correo", columnList = "correo")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // Relación 1:N con Franquicia
    @OneToMany(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @ToString.Exclude // evita recursión infinita en Lombok
    @Builder.Default
    private List<Franquicia> franquicias = new ArrayList<>();

    // === ENUM ===
    public enum EstadoUsuario {
        ACTIVO, INACTIVO, BLOQUEADO
    }

    // === Métodos auxiliares ===
    public void addFranquicia(Franquicia franquicia) {
        franquicias.add(franquicia);
        franquicia.setUsuario(this);
    }

    public void removeFranquicia(Franquicia franquicia) {
        franquicias.remove(franquicia);
        franquicia.setUsuario(null);
    }
}
