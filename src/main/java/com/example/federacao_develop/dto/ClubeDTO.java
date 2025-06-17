package com.example.federacao_develop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.Valid;

import java.time.LocalDate;

public class ClubeDTO {

    private Integer clubeId;

    @NotBlank(message = "O nome do clube é obrigatório.")
    @Size(max = 100, message = "O nome do clube deve ter no máximo 100 caracteres.")
    private String nomeClube;

    @NotBlank(message = "A UF do clube é obrigatória.")
    @Size(min = 2, max = 2, message = "A UF deve ter exatamente 2 caracteres.")
    private String ufClube;

    @NotNull(message = "A data de fundação é obrigatória.")
    @PastOrPresent(message = "A data de fundação não pode ser no futuro.")
    private LocalDate dataFundacao;

    @NotNull(message = "O campo 'ativo' é obrigatório.")
    private Boolean ativo;

    @Valid
    @NotNull(message = "O estádio é obrigatório.")
    private EstadioDTO estadio;

    public ClubeDTO() {}

    public Integer getClubeId() { return clubeId; }
    public void setClubeId(Integer clubeId) { this.clubeId = clubeId; }

    public String getNomeClube() { return nomeClube; }
    public void setNomeClube(String nomeClube) { this.nomeClube = nomeClube; }

    public String getUfClube() { return ufClube; }
    public void setUfClube(String ufClube) { this.ufClube = ufClube; }

    public LocalDate getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(LocalDate dataFundacao) { this.dataFundacao = dataFundacao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public EstadioDTO getEstadio() { return estadio; }
    public void setEstadio(EstadioDTO estadio) { this.estadio = estadio; }
}