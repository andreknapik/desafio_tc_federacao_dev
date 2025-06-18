package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.exception.BusinessException;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.mapper.EstadioMapper;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;
    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final EstadioMapper estadioMapper;

    public EstadioService(EstadioRepository estadioRepository, ClubeRepository clubeRepository,
                          PartidaRepository partidaRepository, EstadioMapper estadioMapper) {
        this.estadioRepository = estadioRepository;
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
        this.estadioMapper = estadioMapper;
    }

    public List<EstadioDTO> findAll() {
        return estadioRepository.findAll().stream()
                .map(estadioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EstadioDTO findById(Integer id) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
        return estadioMapper.toDTO(estadio);
    }

    public EstadioDTO save(EstadioDTO dto) {
        Estadio entity = estadioMapper.toEntity(dto);
        return estadioMapper.toDTO(estadioRepository.save(entity));
    }

    public EstadioDTO update(Integer id, EstadioDTO dto) {
        Estadio entity = estadioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
        entity.setNomeEstadio(dto.getNomeEstadio());
        return estadioMapper.toDTO(estadioRepository.save(entity));
    }

    public void delete(Integer id) {
        boolean estadioTemClubes = clubeRepository.existsByEstadio_EstadioId(id);
        boolean estadioTemPartidas = partidaRepository.existsByEstadio_EstadioId(id);
        if (estadioTemClubes) {
            throw new BusinessException(MensagemExceptionEnum.ESTADIO_COM_CLUBES);
        }
        if (estadioTemPartidas) {
            throw new BusinessException(MensagemExceptionEnum.ESTADIO_COM_PARTIDAS);
        }
        estadioRepository.deleteById(id);
    }
}