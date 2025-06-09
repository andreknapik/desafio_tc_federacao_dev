package com.example.federacao_develop.dto;

public class EstadioDTO {

    private Integer estadioId;
    private String nomeEstadio;

    public EstadioDTO() {}

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