package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.exception.BusinessException;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.mapper.EstadioMapper;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<EstadioDTO> findAllPage(Pageable pageable) {
        return estadioRepository.findAll(pageable)
                .map(estadioMapper::toDTO);
    }

    public EstadioDTO findById(Integer id) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO));
        return estadioMapper.toDTO(estadio);
    }

    public EstadioDTO update(Integer id, EstadioDTO dto) {
        Estadio entity = estadioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO));
        entity.setNomeEstadio(dto.getNomeEstadio());
        return estadioMapper.toDTO(estadioRepository.save(entity));
    }

    public void delete(Integer id) {
        boolean exists = estadioRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO);
        }
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

    public EstadioDTO save(EstadioDTO dto) {
        Estadio entity = estadioMapper.toEntity(dto);
        return estadioMapper.toDTO(estadioRepository.save(entity));
    }
}