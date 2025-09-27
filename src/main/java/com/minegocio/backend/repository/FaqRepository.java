package com.minegocio.backend.repository;

import com.minegocio.backend.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Integer> {

        /**
         * Búsqueda usando FULLTEXT. Usamos parámetros posicionales (?1) para evitar
         * problemas con named params en nativeQuery.
         */
        @Query(value = ""
                        + "SELECT * FROM faq "
                        + "WHERE MATCH(question, answer) AGAINST (?1 IN NATURAL LANGUAGE MODE) "
                        + "ORDER BY MATCH(question, answer) AGAINST (?1 IN NATURAL LANGUAGE MODE) DESC "
                        + "LIMIT 5", nativeQuery = true)
        List<Faq> searchByText(String text);

        /**
         * Fallback JPQL con LIKE (case-insensitive).
         */
        @Query("SELECT f FROM Faq f WHERE LOWER(f.question) LIKE LOWER(CONCAT('%', :term, '%')) "
                        + "OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :term, '%'))")
        List<Faq> searchLike(@Param("term") String term);
}
