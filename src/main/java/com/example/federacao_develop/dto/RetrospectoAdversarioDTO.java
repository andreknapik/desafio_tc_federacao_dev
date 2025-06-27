package com.example.federacao_develop.dto;

public class RetrospectoAdversarioDTO {

    private Integer adversarioId;
    private String adversarioNome;
    private RetrospectoDTO retrospecto;

    public RetrospectoAdversarioDTO() {}

    public Integer getAdversarioId() {
        return adversarioId;
    }

    public void setAdversarioId(Integer adversarioId) {
        this.adversarioId = adversarioId;
    }

    public String getAdversarioNome() {
        return adversarioNome;
    }

    public void setAdversarioNome(String adversarioNome) {
        this.adversarioNome = adversarioNome;
    }

    public RetrospectoDTO getRetrospecto() {
        return retrospecto;
    }

    public void setRetrospecto(RetrospectoDTO retrospecto) {
        this.retrospecto = retrospecto;
    }
}