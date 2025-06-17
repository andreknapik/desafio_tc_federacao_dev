package com.example.federacao_develop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EstadioDTO {

    private Integer estadioId;

    @NotBlank(message = "O nome do estádio é obrigatório.")
    @Size(max = 100, message = "O nome do estádio deve ter no máximo 100 caracteres.")
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