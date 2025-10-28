package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "objetos_bd_franquicia",
    indexes = {
        @Index(name = "idx_objetos_bd", columnList = "id_bd")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjetoBdFranquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objeto")
    private Integer idObjeto;

    // Relación N:1 con BaseDatosFranquicia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bd", nullable = false)
    @ToString.Exclude
    private BaseDatosFranquicia baseDatosFranquicia;

    @Column(name = "nombre_tabla", nullable = false, length = 100)
    private String nombreTabla;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_objeto", nullable = false, length = 20)
    private TipoObjeto tipoObjeto = TipoObjeto.TABLA;

    @Column(name = "es_tabla_usuarios", nullable = false)
    private Boolean esTablaUsuarios = false;

    @Column(name = "columnas", columnDefinition = "JSON", nullable = false)
    private String columnas;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Auditoría
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

    // === ENUM ===
    public enum TipoObjeto {
        TABLA, VISTA, FUNCION
    }
}
