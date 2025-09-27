package com.minegocio.backend.repository;

import com.minegocio.backend.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    Optional<Documento> findBySlug(String slug);
}
