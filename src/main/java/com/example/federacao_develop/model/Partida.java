package com.example.federacao_develop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partidaId;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    private Clube mandante;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    private Clube visitante;

    @ManyToOne
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

    @Column(name = "data_da_partida", nullable = false)
    private LocalDateTime dataDaPartida;

    @Column(nullable = false)
    private String resultado;

    public Integer getPartidaId() {
        return partidaId;
    }
    public void setPartidaId(Integer partidaId) {
        this.partidaId = partidaId;
    }
    public Clube getMandante() {
        return mandante;
    }
    public void setMandante(Clube mandante) {
        this.mandante = mandante;
    }
    public Clube getVisitante() {
        return visitante;
    }
    public void setVisitante(Clube visitante) {
        this.visitante = visitante;
    }
    public Estadio getEstadio() {
        return estadio;
    }
    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }
    public LocalDateTime getDataDaPartida() {
        return dataDaPartida;
    }
    public void setDataDaPartida(LocalDateTime dataDaPartida) {
        this.dataDaPartida = dataDaPartida;
    }
    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}