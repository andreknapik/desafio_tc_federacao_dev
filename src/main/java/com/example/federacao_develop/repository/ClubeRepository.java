package com.example.federacao_develop.repository;

import com.example.federacao_develop.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Integer> {
    boolean existsByEstadio_EstadioId(Integer estadioId);

    @Query("""
        SELECT c FROM Clube c
        WHERE (:nome IS NULL OR LOWER(c.nomeClube) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:uf IS NULL OR LOWER(c.ufClube) LIKE LOWER(CONCAT('%', :uf, '%')))
          AND (:ativo IS NULL OR c.ativo = :ativo)
        """)
    Page<Clube> findClubsFilter(
            @Param("nome") String nome,
            @Param("uf") String uf,
            @Param("ativo") Boolean ativo,
            Pageable pageable
    );
}