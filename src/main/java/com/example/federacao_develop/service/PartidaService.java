package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.model.Partida;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.repository.PartidaRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private EstadioRepository estadioRepository;
    @Autowired
    private ClubeRepository clubeRepository;

    private EstadioDTO toEstadioDTO(Estadio estadio) {
        if (estadio == null) return null;
        EstadioDTO dto = new EstadioDTO();
        dto.setEstadioId(estadio.getEstadioId());
        dto.setNomeEstadio(estadio.getNomeEstadio());
        return dto;
    }

    private PartidaDTO toDTO(Partida entity) {
        PartidaDTO dto = new PartidaDTO();
        dto.setPartidaId(entity.getPartidaId());
        dto.setClubeMandanteId(entity.getClubeMandante().getClubeId());
        dto.setClubeVisitanteId(entity.getClubeVisitante().getClubeId());
        dto.setGolsMandante(entity.getGolsMandante());
        dto.setGolsVisitante(entity.getGolsVisitante());
        dto.setDataDaPartida(entity.getDataDaPartida());
        dto.setEstadio(toEstadioDTO(entity.getEstadio()));
        if (entity.getEstadio() != null) {
            EstadioDTO estadioDTO = new EstadioDTO();
            estadioDTO.setEstadioId(entity.getEstadio().getEstadioId());
            estadioDTO.setNomeEstadio(entity.getEstadio().getNomeEstadio());
            dto.setEstadio(estadioDTO);
        }
        return dto;
    }

    private Partida toEntity(PartidaDTO dto) {
        Partida entity = new Partida();
        entity.setPartidaId(dto.getPartidaId());
        entity.setGolsMandante(dto.getGolsMandante());
        entity.setGolsVisitante(dto.getGolsVisitante());
        entity.setDataDaPartida(dto.getDataDaPartida());

        if (dto.getClubeMandanteId() != null) {
            Optional<Clube> mandante = clubeRepository.findById(dto.getClubeMandanteId());
            mandante.ifPresent(entity::setClubeMandante);
        }
        if (dto.getClubeVisitanteId() != null) {
            Optional<Clube> visitante = clubeRepository.findById(dto.getClubeVisitanteId());
            visitante.ifPresent(entity::setClubeVisitante);
        }
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Optional<Estadio> estadio = estadioRepository.findById(dto.getEstadio().getEstadioId());
            estadio.ifPresent(entity::setEstadio);
        }
        return entity;
    }

    public List<PartidaDTO> findAll() {
        return partidaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PartidaDTO findById(Integer id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));
        return toDTO(partida);
    }

    public PartidaDTO save(PartidaDTO dto) {
        Partida entity = toEntity(dto);
        return toDTO(partidaRepository.save(entity));
    }

    public PartidaDTO update(Integer id, PartidaDTO dto) {
        Partida entity = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        if(dto.getClubeMandanteId() != null) {
            Clube clubeMandante = clubeRepository.findById(dto.getClubeMandanteId())
                    .orElseThrow(() -> new RuntimeException("Clube mandante não encontrado"));
            entity.setClubeMandante(clubeMandante);
        }
        if(dto.getClubeVisitanteId() != null) {
            Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitanteId())
                    .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado"));
            entity.setClubeVisitante(clubeVisitante);
        }
        entity.setGolsMandante(dto.getGolsMandante());
        entity.setGolsVisitante(dto.getGolsVisitante());
        entity.setDataDaPartida(dto.getDataDaPartida());
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Estadio estadio = estadioRepository.findById(dto.getEstadio().getEstadioId())
                    .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
            entity.setEstadio(estadio);
        }
        return toDTO(partidaRepository.save(entity));
    }

    public void delete(Integer id) {
        partidaRepository.deleteById(id);
    }
}