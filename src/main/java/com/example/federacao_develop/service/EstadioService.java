package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private PartidaRepository partidaRepository;

    private EstadioDTO toDTO(Estadio estadio) {
        EstadioDTO dto = new EstadioDTO();
        dto.setEstadioId(estadio.getEstadioId());
        dto.setNomeEstadio(estadio.getNomeEstadio());
        return dto;
    }

    private Estadio toEntity(EstadioDTO dto) {
        Estadio estadio = new Estadio();
        estadio.setEstadioId(dto.getEstadioId());
        estadio.setNomeEstadio(dto.getNomeEstadio());
        return estadio;
    }

    public List<EstadioDTO> findAll() {
        return estadioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EstadioDTO findById(Integer id) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
        return toDTO(estadio);
    }

    public EstadioDTO save(EstadioDTO dto) {
        Estadio entity = toEntity(dto);
        return toDTO(estadioRepository.save(entity));
    }

    public EstadioDTO update(Integer id, EstadioDTO dto) {
        Estadio entity = estadioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
        entity.setNomeEstadio(dto.getNomeEstadio());
        return toDTO(estadioRepository.save(entity));
    }

    public void delete(Integer id) {
        boolean estadioTemClubes = clubeRepository.existsByEstadio_EstadioId(id);
        boolean estadioTemPartidas = partidaRepository.existsByEstadio_EstadioId(id);
        if (estadioTemClubes) {
            throw new RuntimeException("Não é possível deletar o estádio pois existem clubes vinculados a ele. Para deletar este estádio, você precisa primeiro deletar os clubes associados a ele.");
        }
        if (estadioTemPartidas) {
            throw new RuntimeException("Não é possível deletar o estádio pois existem partidas marcadas para ele. Para deletar este est[adio, você precisa primeiro deletar as partidas associadas a ele.");
        }
        estadioRepository.deleteById(id);
    }
}