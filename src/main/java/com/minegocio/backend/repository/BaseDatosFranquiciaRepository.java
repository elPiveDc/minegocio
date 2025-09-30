package com.minegocio.backend.repository;

import com.minegocio.backend.dto.ConexionBdProjection;
import com.minegocio.backend.entity.BaseDatosFranquicia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BaseDatosFranquiciaRepository extends JpaRepository<BaseDatosFranquicia, Integer> {

       @Query("""
                     SELECT b.idBd AS idBd,
                            b.urlConexion AS urlConexion,
                            b.usuarioConexion AS usuarioConexion,
                            b.passConexionHash AS passConexionHash
                     FROM BaseDatosFranquicia b
                     JOIN b.franquicia f
                     WHERE f.nombreFranquicia = :nombreFranquicia
                     """)
       Optional<ConexionBdProjection> obtenerDatosConexionPorNombreFranquicia(
                     @Param("nombreFranquicia") String nombreFranquicia);

       @Query("""
                      SELECT b
                      FROM BaseDatosFranquicia b
                      JOIN b.franquicia f
                      WHERE f.nombreFranquicia = :nombreFranquicia
                     """)
       BaseDatosFranquicia ObtenerBDporNombreFranquicia(
                     @Param("nombreFranquicia") String nombreFranquicia);

}