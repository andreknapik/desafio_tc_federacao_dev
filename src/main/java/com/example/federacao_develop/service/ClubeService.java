package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.mapper.ClubeMapper;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;
    private final ClubeMapper clubeMapper;

    public ClubeService(ClubeRepository clubeRepository, EstadioRepository estadioRepository, ClubeMapper clubeMapper) {
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
        this.clubeMapper = clubeMapper;
    }

    public List<ClubeDTO> findAll() {
        return clubeRepository.findAll()
                .stream()
                .map(clubeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClubeDTO findById(Integer id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        return clubeMapper.toDTO(clube);
    }

    public ClubeDTO save(ClubeDTO dto) {
        Clube entity = clubeMapper.toEntity(dto);

        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Optional<Estadio> estadio = estadioRepository.findById(dto.getEstadio().getEstadioId());
            estadio.ifPresent(entity::setEstadio);
        }
        return clubeMapper.toDTO(clubeRepository.save(entity));
    }

    public ClubeDTO update(Integer id, ClubeDTO dto) {
        Clube clubebd = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        Clube updatedEntity = clubeMapper.toEntity(dto);
        clubebd.setNomeClube(updatedEntity.getNomeClube());
        clubebd.setUfClube(updatedEntity.getUfClube());
        clubebd.setDataFundacao(updatedEntity.getDataFundacao());
        clubebd.setAtivo(updatedEntity.getAtivo());
        if (dto.getEstadio() != null && dto.getEstadio().getEstadioId() != null) {
            Optional<Estadio> estadio = estadioRepository.findById(dto.getEstadio().getEstadioId());
            estadio.ifPresent(clubebd::setEstadio);
        }
        return clubeMapper.toDTO(clubeRepository.save(clubebd));
    }

    public void delete(Integer id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }
}