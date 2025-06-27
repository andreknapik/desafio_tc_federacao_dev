package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.exception.BusinessException;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.mapper.PartidaMapper;
import com.example.federacao_develop.model.Partida;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<PartidaDTO> findPartidasFilter(Integer clubeId, Integer estadioId, Pageable pageable) {
        return partidaRepository.findPartidasFilter(clubeId, estadioId, pageable)
                .map(partidaMapper::toDTO);
    }

    public PartidaDTO findById(Integer id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.PARTIDA_NAO_ENCONTRADA));
        return partidaMapper.toDTO(partida);
    }

    public PartidaDTO save(PartidaDTO dto) {
        Partida entity = partidaMapper.toEntity(dto);

        if (dto.getClubeMandanteId() != null) {
            entity.setClubeMandante(clubeRepository.findById(dto.getClubeMandanteId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_MANDANTE_NAO_ENCONTRADO)));
        } else {
            throw new BusinessException(MensagemExceptionEnum.CLUBE_MANDANTE_NAO_ENCONTRADO);
        }
        if (dto.getClubeVisitanteId() != null) {
            entity.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_VISITANTE_NAO_ENCONTRADO)));
        } else {
            throw new BusinessException(MensagemExceptionEnum.CLUBE_VISITANTE_NAO_ENCONTRADO);
        }
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            entity.setEstadio(estadioRepository.findById(dto.getEstadio().getEstadioId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO)));
        } else {
            throw new BusinessException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO);
        }

        return partidaMapper.toDTO(partidaRepository.save(entity));
    }

    public PartidaDTO update(Integer id, PartidaDTO dto) {
        Partida entity = partidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.PARTIDA_NAO_ENCONTRADA));

        if (dto.getClubeMandanteId() != null) {
            entity.setClubeMandante(clubeRepository.findById(dto.getClubeMandanteId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_MANDANTE_NAO_ENCONTRADO)));
        }
        if (dto.getClubeVisitanteId() != null) {
            entity.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_VISITANTE_NAO_ENCONTRADO)));
        }
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            entity.setEstadio(estadioRepository.findById(dto.getEstadio().getEstadioId())
                    .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO)));
        }

        entity.setGolsMandante(dto.getGolsMandante());
        entity.setGolsVisitante(dto.getGolsVisitante());
        entity.setDataDaPartida(dto.getDataDaPartida());

        return partidaMapper.toDTO(partidaRepository.save(entity));
    }

    public void delete(Integer id) {
        boolean exists = partidaRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException(MensagemExceptionEnum.PARTIDA_NAO_ENCONTRADA);
        }
        partidaRepository.deleteById(id);
    }
}