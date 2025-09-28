package com.minegocio.backend.repository;

import com.minegocio.backend.entity.Consulta;
import com.minegocio.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    // === MÉTODOS PERSONALIZADOS ===

    // Buscar todas las consultas de un usuario
    List<Consulta> findByUsuario(Usuario usuario);

    // Buscar por tipo de consulta
    List<Consulta> findByTipoConsulta(Consulta.TipoConsulta tipoConsulta);

    // Buscar por estado de la consulta
    List<Consulta> findByEstado(Consulta.EstadoConsulta estado);

    // Buscar todas las consultas de un usuario en un estado específico
    List<Consulta> findByUsuarioAndEstado(Usuario usuario, Consulta.EstadoConsulta estado);

    // Buscar todas las consultas de un usuario según tipo
    List<Consulta> findByUsuarioAndTipoConsulta(Usuario usuario, Consulta.TipoConsulta tipoConsulta);
}