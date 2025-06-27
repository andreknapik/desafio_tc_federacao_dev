package com.example.federacao_develop.dto;

public class ClubeResumoDTO {
    private Integer clubeId;
    private String nomeClube;

    public ClubeResumoDTO() {}
    public ClubeResumoDTO(Integer clubeId, String nomeClube) {
        this.clubeId = clubeId;
        this.nomeClube = nomeClube;
    }

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
}