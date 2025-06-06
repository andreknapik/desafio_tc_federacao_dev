package com.example.federacao_develop.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clubeId;

    @Column(name = "nome_clube", nullable = false, length = 100)
    private String nomeClube;

    @Column(name = "uf_clube", nullable = false, length = 2)
    private String ufClube;

    @Column(name = "data_fundacao", nullable = false)
    private LocalDate dataFundacao;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

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

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }
}