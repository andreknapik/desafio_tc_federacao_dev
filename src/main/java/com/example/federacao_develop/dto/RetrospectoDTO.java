package com.example.federacao_develop.dto;

public class RetrospectoDTO {

    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsFeitos;
    private Integer golsSofridos;

    public RetrospectoDTO() {}

    public RetrospectoDTO(Integer vitorias, Integer empates, Integer derrotas, Integer golsFeitos, Integer golsSofridos) {
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
    }

    public Integer getVitorias() {
        return vitorias;
    }
    public void setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
    }

    public Integer getEmpates() {
        return empates;
    }
    public void setEmpates(Integer empates) {
        this.empates = empates;
    }

    public Integer getDerrotas() {
        return derrotas;
    }
    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getGolsFeitos() {
        return golsFeitos;
    }
    public void setGolsFeitos(Integer golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }
    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
    }
}