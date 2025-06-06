package com.example.federacao_develop.model;
import jakarta.persistence.*;


@Entity
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estadioId;

    @Column(name = "nome_estadio", nullable = false, length = 100)
    private String nomeEstadio;

    public Integer getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(Integer estadioId) {
        this.estadioId = estadioId;
    }

    public String getNomeEstadio() {
        return nomeEstadio;
    }

    public void setNomeEstadio(String nomeEstadio) {
        this.nomeEstadio = nomeEstadio;
    }
}
