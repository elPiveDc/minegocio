package com.minegocio.backend.repository;

import com.minegocio.backend.entity.ObjetoBdFranquicia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjetoBdFranquiciaRepository extends JpaRepository<ObjetoBdFranquicia, Integer> {

    List<ObjetoBdFranquicia> findByBaseDatosFranquiciaIdBd(Integer idBd);

    Optional<ObjetoBdFranquicia> findByBaseDatosFranquiciaIdBdAndNombreTabla(Integer idBd, String nombreTabla);

    boolean existsByBaseDatosFranquiciaIdBdAndNombreTabla(Integer idBd, String nombreTabla);

    // devuelve la tabla de usuarios si existe en esta BD
    Optional<ObjetoBdFranquicia> findFirstByBaseDatosFranquiciaIdBdAndEsTablaUsuariosTrue(Integer idBd);
}
