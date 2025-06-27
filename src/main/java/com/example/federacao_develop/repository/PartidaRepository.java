package com.example.federacao_develop.repository;

import com.example.federacao_develop.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Integer> {
    boolean existsByEstadio_EstadioId(Integer estadioId);

    @Query("""
        SELECT p FROM Partida p
        WHERE
            (
                (:clubeId IS NULL) OR 
                (p.clubeMandante.clubeId = :clubeId) OR
                (p.clubeVisitante.clubeId = :clubeId)
            )
            AND (:estadioId IS NULL OR p.estadio.estadioId = :estadioId)
        """)
    Page<Partida> findPartidasFilter(
            @Param("clubeId") Integer clubeId,
            @Param("estadioId") Integer estadioId,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Partida p
        WHERE p.clubeMandante.clubeId = :clubeId OR p.clubeVisitante.clubeId = :clubeId
    """)
    List<Partida> findByClube(@Param("clubeId") Integer clubeId);

    @Query("""
        SELECT p FROM Partida p
        WHERE (p.clubeMandante.clubeId = :clubeA AND p.clubeVisitante.clubeId = :clubeB)
           OR (p.clubeMandante.clubeId = :clubeB AND p.clubeVisitante.clubeId = :clubeA)
    """)
    List<Partida> findConfrontosEntreDoisClubes(
            @Param("clubeA") Integer clubeA,
            @Param("clubeB") Integer clubeB
    );
}