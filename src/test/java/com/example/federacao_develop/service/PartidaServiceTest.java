package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.mapper.PartidaMapper;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.model.Partida;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock private PartidaRepository partidaRepository;
    @Mock private EstadioRepository estadioRepository;
    @Mock private ClubeRepository clubeRepository;
    @Mock private PartidaMapper partidaMapper;

    @InjectMocks
    private PartidaService partidaService;

    private Partida partida;
    private PartidaDTO partidaDTO;
    private Estadio estadio;
    private EstadioDTO estadioDTO;
    private Clube mandante;
    private Clube visitante;

    @BeforeEach
    void setUp() {
        estadio = new Estadio();
        estadio.setEstadioId(2);
        estadio.setNomeEstadio("Castelão");

        estadioDTO = new EstadioDTO();
        estadioDTO.setEstadioId(2);
        estadioDTO.setNomeEstadio("Castelão");

        mandante = new Clube();
        mandante.setClubeId(10);

        visitante = new Clube();
        visitante.setClubeId(20);

        partida = new Partida();
        partida.setPartidaId(99);
        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
        partida.setGolsMandante(3);
        partida.setGolsVisitante(1);
        partida.setDataDaPartida(LocalDateTime.of(2024, 6, 1, 16, 0));
        partida.setEstadio(estadio);

        partidaDTO = new PartidaDTO();
        partidaDTO.setPartidaId(99);
        partidaDTO.setClubeMandanteId(10);
        partidaDTO.setClubeVisitanteId(20);
        partidaDTO.setGolsMandante(3);
        partidaDTO.setGolsVisitante(1);
        partidaDTO.setDataDaPartida(LocalDateTime.of(2024, 6, 1, 16, 0));
        partidaDTO.setEstadio(estadioDTO);
    }

    @Test
    void findPartidasFilter_returnsDTOs() {
        Pageable pageable = mock(Pageable.class);
        Page<Partida> page = new PageImpl<>(Collections.singletonList(partida));
        when(partidaRepository.findPartidasFilter(10, 2, pageable)).thenReturn(page);
        when(partidaMapper.toDTO(partida)).thenReturn(partidaDTO);

        Page<PartidaDTO> result = partidaService.findPartidasFilter(10, 2, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(99, result.getContent().get(0).getPartidaId());
        verify(partidaRepository).findPartidasFilter(10, 2, pageable);
        verify(partidaMapper).toDTO(partida);
    }

    @Test
    void findById_success() {
        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(partidaMapper.toDTO(partida)).thenReturn(partidaDTO);

        PartidaDTO result = partidaService.findById(99);

        assertNotNull(result);
        assertEquals(99, result.getPartidaId());
    }

    @Test
    void findById_throws_ifNotFound() {
        when(partidaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> partidaService.findById(99));
        assertEquals("Partida não encontrada", ex.getMessage());
    }

    @Test
    void save_success_allPresent() {
        when(partidaMapper.toEntity(partidaDTO)).thenReturn(partida);
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(2)).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(partida)).thenReturn(partida);
        when(partidaMapper.toDTO(partida)).thenReturn(partidaDTO);

        PartidaDTO result = partidaService.save(partidaDTO);

        assertNotNull(result);
        verify(clubeRepository).findById(10);
        verify(clubeRepository).findById(20);
        verify(estadioRepository).findById(2);
        verify(partidaRepository).save(partida);
        verify(partidaMapper).toDTO(partida);
    }

    @Test
    void save_throws_ifMandanteNotFound() {
        when(partidaMapper.toEntity(partidaDTO)).thenReturn(partida);
        when(clubeRepository.findById(10)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> partidaService.save(partidaDTO));
        assertEquals("Clube mandante não encontrado", ex.getMessage());
    }

    @Test
    void save_throws_ifVisitanteNotFound() {
        when(partidaMapper.toEntity(partidaDTO)).thenReturn(partida);
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> partidaService.save(partidaDTO));
        assertEquals("Clube visitante não encontrado", ex.getMessage());
    }

    @Test
    void save_throws_ifEstadioNotFound() {
        when(partidaMapper.toEntity(partidaDTO)).thenReturn(partida);
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(2)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> partidaService.save(partidaDTO));
        assertEquals("Estádio não encontrado", ex.getMessage());
    }

    @Test
    void update_success_allPresent() {
        PartidaDTO updateDTO = new PartidaDTO();
        updateDTO.setClubeMandanteId(10);
        updateDTO.setClubeVisitanteId(20);
        updateDTO.setGolsMandante(2);
        updateDTO.setGolsVisitante(2);
        updateDTO.setDataDaPartida(LocalDateTime.of(2024, 7, 1, 19, 0));
        updateDTO.setEstadio(estadioDTO);

        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(2)).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(partida)).thenReturn(partida);
        when(partidaMapper.toDTO(partida)).thenReturn(updateDTO);

        PartidaDTO result = partidaService.update(99, updateDTO);

        assertEquals(2, result.getGolsMandante());
        assertEquals(2, result.getGolsVisitante());

        verify(clubeRepository).findById(10);
        verify(clubeRepository).findById(20);
        verify(estadioRepository).findById(2);
        verify(partidaRepository).save(partida);
        verify(partidaMapper).toDTO(partida);
    }

    @Test
    void update_throws_ifNotFound() {
        when(partidaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> partidaService.update(99, partidaDTO));
        assertEquals("Partida não encontrada", ex.getMessage());
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void update_throws_ifMandanteNotFound() {
        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(10)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> partidaService.update(99, partidaDTO));
        assertEquals("Clube mandante não encontrado", ex.getMessage());
    }

    @Test
    void update_throws_ifVisitanteNotFound() {
        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> partidaService.update(99, partidaDTO));
        assertEquals("Clube visitante não encontrado", ex.getMessage());
    }

    @Test
    void update_throws_ifEstadioNotFound() {
        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(2)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> partidaService.update(99, partidaDTO));
        assertEquals("Estádio não encontrado", ex.getMessage());
    }

    @Test
    void update_onlyUpdatesProvidedFields() {
        // Caso: DTO não traz estadio, não sobrescreve
        PartidaDTO updateDTO = new PartidaDTO();
        updateDTO.setClubeMandanteId(10);
        updateDTO.setClubeVisitanteId(20);
        updateDTO.setGolsMandante(5);
        updateDTO.setGolsVisitante(0);
        updateDTO.setDataDaPartida(LocalDateTime.of(2025, 1, 1, 12, 0));
        updateDTO.setEstadio(null);

        when(partidaRepository.findById(99)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(visitante));
        when(partidaRepository.save(partida)).thenReturn(partida);
        when(partidaMapper.toDTO(partida)).thenReturn(updateDTO);

        PartidaDTO result = partidaService.update(99, updateDTO);

        assertEquals(5, result.getGolsMandante());
        verify(estadioRepository, never()).findById(any());
    }

    @Test
    void delete_callsRepository() {
        when(partidaRepository.existsById(99)).thenReturn(true);
        doNothing().when(partidaRepository).deleteById(99);
        partidaService.delete(99);
        verify(partidaRepository).deleteById(99);
    }

    @Test
    void delete_throwsException_whenNotFound() {
        when(partidaRepository.existsById(99)).thenReturn(false);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> partidaService.delete(99));
        assertEquals(MensagemExceptionEnum.PARTIDA_NAO_ENCONTRADA.getMessage(), ex.getMessage());
        verify(partidaRepository, never()).deleteById(any());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(partidaRepository.findById(1234)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> partidaService.findById(1234));
        assertEquals(MensagemExceptionEnum.PARTIDA_NAO_ENCONTRADA.getMessage(), ex.getMessage());
    }
}