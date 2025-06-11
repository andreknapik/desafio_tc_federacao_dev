package com.example.federacao_develop.dto;

import java.time.LocalDate;

public class ClubeDTO {

    private Integer clubeId;
    private String nomeClube;
    private String ufClube;
    private LocalDate dataFundacao;
    private Boolean ativo;
    private EstadioDTO estadio;
    public ClubeDTO() {}


    public Integer getClubeId() {
        return clubeId;
    }

    public void setClubeId(Integer clubeId) {
        this.clubeId = clubeId;
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public void setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
    }

    public String getUfClube() {
        return ufClube;
    }

    public void setUfClube(String ufClube) {
        this.ufClube = ufClube;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EstadioDTO getEstadio() {
        return estadio;
    }

    public void setEstadio(EstadioDTO estadio) {
        this.estadio = estadio;
    }

}