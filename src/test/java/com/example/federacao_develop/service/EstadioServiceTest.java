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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadioServiceTest {

    @Mock private EstadioRepository estadioRepository;
    @Mock private ClubeRepository clubeRepository;
    @Mock private PartidaRepository partidaRepository;
    @Mock private EstadioMapper estadioMapper;

    @InjectMocks
    private EstadioService estadioService;

    private Estadio estadio;
    private EstadioDTO estadioDTO;

    @BeforeEach
    void setUp() {
        estadio = new Estadio();
        estadio.setEstadioId(1);
        estadio.setNomeEstadio("Morumbi");

        estadioDTO = new EstadioDTO();
        estadioDTO.setEstadioId(1);
        estadioDTO.setNomeEstadio("Morumbi");
    }

    @Test
    void findAllPage_returnsDTOs() {
        Pageable pageable = mock(Pageable.class);
        Page<Estadio> page = new PageImpl<>(Collections.singletonList(estadio));
        when(estadioRepository.findAll(pageable)).thenReturn(page);
        when(estadioMapper.toDTO(estadio)).thenReturn(estadioDTO);

        Page<EstadioDTO> result = estadioService.findAllPage(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(estadioDTO.getNomeEstadio(), result.getContent().get(0).getNomeEstadio());
        verify(estadioRepository).findAll(pageable);
        verify(estadioMapper).toDTO(estadio);
    }

    @Test
    void findById_success() {
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));
        when(estadioMapper.toDTO(estadio)).thenReturn(estadioDTO);

        EstadioDTO dto = estadioService.findById(1);

        assertNotNull(dto);
        assertEquals(estadio.getNomeEstadio(), dto.getNomeEstadio());
    }

    @Test
    void findById_throws_ifNotFound() {
        when(estadioRepository.findById(1)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> estadioService.findById(1));
        assertEquals("Estádio não encontrado", ex.getMessage());
    }

    @Test
    void save_persistsEntity() {
        when(estadioMapper.toEntity(estadioDTO)).thenReturn(estadio);
        when(estadioRepository.save(estadio)).thenReturn(estadio);
        when(estadioMapper.toDTO(estadio)).thenReturn(estadioDTO);

        EstadioDTO result = estadioService.save(estadioDTO);

        assertNotNull(result);
        verify(estadioRepository).save(estadio);
        verify(estadioMapper).toEntity(estadioDTO);
        verify(estadioMapper).toDTO(estadio);
    }

    @Test
    void update_success_andNewNome_set() {
        EstadioDTO newDTO = new EstadioDTO();
        newDTO.setEstadioId(1);
        newDTO.setNomeEstadio("Novo Nome");
        Estadio novoEstadio = new Estadio();
        novoEstadio.setEstadioId(1);
        novoEstadio.setNomeEstadio("Novo Nome");

        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(novoEstadio);
        when(estadioMapper.toDTO(novoEstadio)).thenReturn(newDTO);

        EstadioDTO result = estadioService.update(1, newDTO);

        assertEquals("Novo Nome", result.getNomeEstadio());
        ArgumentCaptor<Estadio> captor = ArgumentCaptor.forClass(Estadio.class);
        verify(estadioRepository).save(captor.capture());
        assertEquals("Novo Nome", captor.getValue().getNomeEstadio());
        verify(estadioRepository).findById(1);
    }

    @Test
    void update_throws_ifEstadioNotFound() {
        EstadioDTO newDTO = new EstadioDTO();
        when(estadioRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadioService.update(1, newDTO));
        assertEquals("Estádio não encontrado", ex.getMessage());
        verify(estadioRepository, never()).save(any());
    }

    @Test
    void delete_throws_ifEstadioNaoExiste() {
        when(estadioRepository.existsById(1)).thenReturn(false);

        var ex = assertThrows(NotFoundException.class, () -> estadioService.delete(1));
        assertEquals(MensagemExceptionEnum.ESTADIO_NAO_ENCONTRADO.getMessage(), ex.getMessage());
        verify(estadioRepository, never()).deleteById(any());
    }

    @Test
    void delete_throws_ifEstadioTemClubes() {
        when(estadioRepository.existsById(1)).thenReturn(true);
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> estadioService.delete(1));
        assertEquals(MensagemExceptionEnum.ESTADIO_COM_CLUBES, ex.getMensagemEnum());
        verify(estadioRepository, never()).deleteById(any());
    }

    @Test
    void delete_throws_ifEstadioTemPartidas() {
        when(estadioRepository.existsById(1)).thenReturn(true);
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(false);
        when(partidaRepository.existsByEstadio_EstadioId(1)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> estadioService.delete(1));
        assertEquals(MensagemExceptionEnum.ESTADIO_COM_PARTIDAS, ex.getMensagemEnum());
        verify(estadioRepository, never()).deleteById(any());
    }

    @Test
    void delete_successful() {
        when(estadioRepository.existsById(1)).thenReturn(true);
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(false);
        when(partidaRepository.existsByEstadio_EstadioId(1)).thenReturn(false);

        estadioService.delete(1);

        verify(estadioRepository).deleteById(1);
    }
}