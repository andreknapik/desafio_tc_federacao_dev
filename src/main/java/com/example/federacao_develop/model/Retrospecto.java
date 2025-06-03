package com.example.federacao_develop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "retrospecto")
public class Retrospecto {

    @Id
    @ManyToOne
    @JoinColumn(name = "clube_id", nullable = false)
    private Clube clube;

    @Id
    @ManyToOne
    @JoinColumn(name = "clube_oponente_id", nullable = false)
    private Clube clubeOponente;

    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsFeitos;
    private Integer golsLevados;

    // Getters e Setters
    public Clube getClube() {
        return clube;
    }
    public void setClube(Clube clube) {
        this.clube = clube;
    }
    public Clube getClubeOponente() {
        return clubeOponente;
    }
    public void setClubeOponente(Clube clubeOponente) {
        this.clubeOponente = clubeOponente;
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
    public Integer getGolsLevados() {
        return golsLevados;
    }
    public void setGolsLevados(Integer golsLevados) {
        this.golsLevados = golsLevados;
    }
}