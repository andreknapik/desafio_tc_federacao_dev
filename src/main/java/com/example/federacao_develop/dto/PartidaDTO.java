package com.example.federacao_develop.dto;

import java.time.LocalDateTime;

public class PartidaDTO {

    private Integer partidaId;
    private Integer clubeMandanteId;
    private Integer clubeVisitanteId;
    private int golsMandante;
    private int golsVisitante;
    private LocalDateTime dataDaPartida;
    private EstadioDTO estadio;

    public PartidaDTO() {}

    public Integer getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Integer partidaId) {
        this.partidaId = partidaId;
    }

    public Integer getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Integer clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public Integer getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public void setClubeVisitanteId(Integer clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public LocalDateTime getDataDaPartida() {
        return dataDaPartida;
    }

    public void setDataDaPartida(LocalDateTime dataDaPartida) {
        this.dataDaPartida = dataDaPartida;
    }

    public EstadioDTO getEstadio() {
        return estadio;
    }

    public void setEstadio(EstadioDTO estadio) {
        this.estadio = estadio;
    }
}