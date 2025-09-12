package com.minegocio.backend.repository;

import com.minegocio.backend.entity.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranquiciaRepository extends JpaRepository<Franquicia, Integer> {
    boolean existsByNombreFranquicia(String nombreFranquicia);
}
