package com.example.federacao_develop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PartidaDTO {

    private Integer partidaId;

    @NotNull(message = "O clube mandante é obrigatório.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer clubeMandanteId;

    @NotNull(message = "O clube visitante é obrigatório.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer clubeVisitanteId;

    private ClubeResumoDTO clubeMandante;
    private ClubeResumoDTO clubeVisitante;

    @Min(value = 0, message = "Gols do mandante não pode ser negativo.")
    private int golsMandante;

    @Min(value = 0, message = "Gols do visitante não pode ser negativo.")
    private int golsVisitante;

    @NotNull(message = "A data da partida é obrigatória.")
    @FutureOrPresent(message = "A data da partida deve ser presente ou futura.")
    private LocalDateTime dataDaPartida;

    @Valid
    @NotNull(message = "O estádio da partida é obrigatório.")
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

    public ClubeResumoDTO getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(ClubeResumoDTO clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public ClubeResumoDTO getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(ClubeResumoDTO clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
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