package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.mapper.PartidaMapper;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.model.Partida;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final EstadioRepository estadioRepository;
    private final ClubeRepository clubeRepository;
    private final PartidaMapper partidaMapper;

    public PartidaService(PartidaRepository partidaRepository, EstadioRepository estadioRepository,
                          ClubeRepository clubeRepository, PartidaMapper partidaMapper) {
        this.partidaRepository = partidaRepository;
        this.estadioRepository = estadioRepository;
        this.clubeRepository = clubeRepository;
        this.partidaMapper = partidaMapper;
    }

    public List<PartidaDTO> findAll() {
        return partidaRepository.findAll().stream()
                .map(partidaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PartidaDTO findById(Integer id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));
        return partidaMapper.toDTO(partida);
    }

    public PartidaDTO save(PartidaDTO dto) {
        Partida entity = partidaMapper.toEntity(dto);

        if (dto.getClubeMandanteId() != null) {
            entity.setClubeMandante(clubeRepository.findById(dto.getClubeMandanteId())
                    .orElseThrow(() -> new RuntimeException("Clube mandante não encontrado")));
        }
        if (dto.getClubeVisitanteId() != null) {
            entity.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId())
                    .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado")));
        }
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            entity.setEstadio(estadioRepository.findById(dto.getEstadio().getEstadioId())
                    .orElseThrow(() -> new RuntimeException("Estádio não encontrado")));
        }

        return partidaMapper.toDTO(partidaRepository.save(entity));
    }

    public PartidaDTO update(Integer id, PartidaDTO dto) {
        Partida entity = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        if (dto.getClubeMandanteId() != null) {
            entity.setClubeMandante(clubeRepository.findById(dto.getClubeMandanteId())
                    .orElseThrow(() -> new RuntimeException("Clube mandante não encontrado")));
        }
        if (dto.getClubeVisitanteId() != null) {
            entity.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId())
                    .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado")));
        }
        entity.setGolsMandante(dto.getGolsMandante());
        entity.setGolsVisitante(dto.getGolsVisitante());
        entity.setDataDaPartida(dto.getDataDaPartida());

        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            entity.setEstadio(estadioRepository.findById(dto.getEstadio().getEstadioId())
                    .orElseThrow(() -> new RuntimeException("Estádio não encontrado")));
        }

        return partidaMapper.toDTO(partidaRepository.save(entity));
    }

    public void delete(Integer id) {
        partidaRepository.deleteById(id);
    }
}