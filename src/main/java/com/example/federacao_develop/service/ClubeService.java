package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private EstadioRepository estadioRepository;

    private ClubeDTO toDTO(Clube entity) {
        ClubeDTO dto = new ClubeDTO();
        dto.setClubeId(entity.getClubeId());
        dto.setNomeClube(entity.getNomeClube());
        dto.setUfClube(entity.getUfClube());
        dto.setDataFundacao(entity.getDataFundacao());
        dto.setAtivo(entity.getAtivo());
        if (entity.getEstadio() != null) {
            EstadioDTO estadioDTO = new EstadioDTO();
            estadioDTO.setEstadioId(entity.getEstadio().getEstadioId());
            estadioDTO.setNomeEstadio(entity.getEstadio().getNomeEstadio());
            dto.setEstadio(estadioDTO);
        }
        return dto;
    }
    private Clube toEntity(ClubeDTO dto) {
        Clube entity = new Clube();
        entity.setClubeId(dto.getClubeId());
        entity.setNomeClube(dto.getNomeClube());
        entity.setUfClube(dto.getUfClube());
        entity.setDataFundacao(dto.getDataFundacao());
        entity.setAtivo(dto.getAtivo());
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Optional<Estadio> estadio = estadioRepository.findById(dto.getEstadio().getEstadioId());
            estadio.ifPresent(entity::setEstadio);
        }
        return entity;
    }

    public List<ClubeDTO> findAll() {
        return clubeRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ClubeDTO findById(Integer id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        return toDTO(clube);
    }

    public ClubeDTO save(ClubeDTO dto) {
        Clube entity = toEntity(dto);
        return toDTO(clubeRepository.save(entity));
    }

    public ClubeDTO update(Integer id, ClubeDTO dto) {
        Clube clubebd = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        clubebd.setNomeClube(dto.getNomeClube());
        clubebd.setUfClube(dto.getUfClube());
        clubebd.setDataFundacao(dto.getDataFundacao());
        clubebd.setAtivo(dto.getAtivo());
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Optional<Estadio> estadio = estadioRepository.findById(dto.getEstadio().getEstadioId());
            estadio.ifPresent(clubebd::setEstadio);
        }
        return toDTO(clubeRepository.save(clubebd));
    }

    public void delete(Integer id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }
}