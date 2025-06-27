package com.example.federacao_develop.dto;

import java.util.List;

public class ConfrontoDiretoDTO {

    private List<PartidaDTO> partidas;
    private RetrospectoDTO retrospectoClubeA;
    private RetrospectoDTO retrospectoClubeB;

    public ConfrontoDiretoDTO() {}

    public List<PartidaDTO> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<PartidaDTO> partidas) {
        this.partidas = partidas;
    }

    public RetrospectoDTO getRetrospectoClubeA() {
        return retrospectoClubeA;
    }

    public void setRetrospectoClubeA(RetrospectoDTO retrospectoClubeA) {
        this.retrospectoClubeA = retrospectoClubeA;
    }

    public RetrospectoDTO getRetrospectoClubeB() {
        return retrospectoClubeB;
    }

    public void setRetrospectoClubeB(RetrospectoDTO retrospectoClubeB) {
        this.retrospectoClubeB = retrospectoClubeB;
    }
}