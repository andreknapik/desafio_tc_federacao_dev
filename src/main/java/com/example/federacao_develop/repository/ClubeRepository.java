package com.example.federacao_develop.repository;

import com.example.federacao_develop.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Integer> {
    boolean existsByEstadio_EstadioId(Integer estadioId);
}