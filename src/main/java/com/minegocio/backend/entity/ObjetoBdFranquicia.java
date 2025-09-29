package com.minegocio.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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
}
