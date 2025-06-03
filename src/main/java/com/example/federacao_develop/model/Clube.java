package com.example.federacao_develop.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clube")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clubeId;

    @Column(nullable = false)
    private String nome;

    @Column(name = "sigla_estado", nullable = false, length = 2)
    private String siglaEstado;

    @Column(name = "data_fundacao", nullable = false)
    private LocalDate dataFundacao;

    @Column(nullable = false)
    private Boolean ativo;

    public Integer getClubeId() {
        return clubeId;
    }
    public void setClubeId(Integer clubeId) {
        this.clubeId = clubeId;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSiglaEstado() {
        return siglaEstado;
    }
    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
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
}