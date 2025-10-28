package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "bases_datos_franquicia",
    indexes = {
        @Index(name = "idx_bd_franquicia", columnList = "id_franquicia")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseDatosFranquicia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bd")
    private Integer idBd;

    // Relación N:1 con Franquicia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_franquicia", nullable = false)
    @ToString.Exclude
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

    // Relación 1:N con objetos_bd_franquicia
    @OneToMany(
        mappedBy = "baseDatosFranquicia",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @Builder.Default
    private List<ObjetoBdFranquicia> objetos = new ArrayList<>();

    // === ENUMs ===
    public enum TipoBd {
        MYSQL, POSTGRESQL, ORACLE, MONGODB, CASSANDRA
    }

    public enum EstadoBd {
        CONFIGURADA, NO_CONFIGURADA, ERROR
    }

    // === Métodos auxiliares ===
    public void addObjeto(ObjetoBdFranquicia objeto) {
        objetos.add(objeto);
        objeto.setBaseDatosFranquicia(this);
    }

    public void removeObjeto(ObjetoBdFranquicia objeto) {
        objetos.remove(objeto);
        objeto.setBaseDatosFranquicia(null);
    }
}
