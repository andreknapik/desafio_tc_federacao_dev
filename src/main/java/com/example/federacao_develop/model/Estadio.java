package com.example.federacao_develop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estadio")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estadioId;

    @Column(nullable = false, length = 100)
    private String nome;

    // Getters e Setters
    public Integer getEstadioId() {
        return estadioId;
    }
    public void setEstadioId(Integer estadioId) {
        this.estadioId = estadioId;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}