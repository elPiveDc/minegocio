package com.minegocio.backend.repository;

import com.minegocio.backend.entity.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranquiciaRepository extends JpaRepository<Franquicia, Long> {
    Optional<Franquicia> findByNombreFranquicia(String nombreFranquicia);
    
}
