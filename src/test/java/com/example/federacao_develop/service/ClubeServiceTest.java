package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClubeServiceTest {

    @Mock
    ClubeRepository clubeRepository;

    @Mock
    EstadioRepository estadioRepository;

    @InjectMocks
    ClubeService clubeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Clube criarClube() {
        Clube clube = new Clube();
        clube.setClubeId(1);
        clube.setNomeClube("Clube A");
        clube.setUfClube("RJ");
        clube.setDataFundacao(LocalDate.of(2000,1,1));
        clube.setAtivo(true);

        Estadio estadio = new Estadio();
        estadio.setEstadioId(10);
        estadio.setNomeEstadio("Estadio A");
        clube.setEstadio(estadio);

        return clube;
    }

    private ClubeDTO criarClubeDTO() {
        ClubeDTO dto = new ClubeDTO();
        dto.setClubeId(1);
        dto.setNomeClube("Clube A");
        dto.setUfClube("RJ");
        dto.setDataFundacao(LocalDate.of(2000,1,1));
        dto.setAtivo(true);

        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setEstadioId(10);
        estadioDTO.setNomeEstadio("Estadio A");
        dto.setEstadio(estadioDTO);

        return dto;
    }

    @Test
    void findAll_deveRetornarListaDeClubeDTO() {
        Clube clube = criarClube();
        when(clubeRepository.findAll()).thenReturn(Arrays.asList(clube));

        List<ClubeDTO> lista = clubeService.findAll();

        assertEquals(1, lista.size());
        assertEquals("Clube A", lista.get(0).getNomeClube());
        verify(clubeRepository, times(1)).findAll();
    }

    @Test
    void findById_quandoExiste_deveRetornarDTO() {
        Clube clube = criarClube();
        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));

        ClubeDTO dto = clubeService.findById(1);

        assertNotNull(dto);
        assertEquals("Clube A", dto.getNomeClube());
        verify(clubeRepository).findById(1);
    }

    @Test
    void findById_quandoNaoExiste_deveLancarNotFound() {
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> clubeService.findById(1));
        assertEquals(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO, ex.getMensagemEnum());
    }

    @Test
    void save_deveSalvarEConverterParaDTO() {
        ClubeDTO dto = criarClubeDTO();
        Clube clube = criarClube();

        // Simular busca do estadio ao converter DTO em entidade
        when(estadioRepository.findById(10)).thenReturn(Optional.of(clube.getEstadio()));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        ClubeDTO criado = clubeService.save(dto);

        assertEquals(dto.getNomeClube(), criado.getNomeClube());
        verify(clubeRepository).save(any(Clube.class));
    }

    @Test
    void update_deveAlterarCamposEConverterParaDTO() {
        ClubeDTO dto = criarClubeDTO();
        Clube clubeExistente = criarClube();

        when(clubeRepository.findById(1)).thenReturn(Optional.of(clubeExistente));
        when(estadioRepository.findById(10)).thenReturn(Optional.of(clubeExistente.getEstadio()));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeExistente);

        ClubeDTO atualizado = clubeService.update(1, dto);

        assertEquals(dto.getNomeClube(), atualizado.getNomeClube());
        verify(clubeRepository).findById(1);
        verify(clubeRepository).save(any(Clube.class));
    }

    @Test
    void update_quandoNaoExiste_deveLancarNotFound() {
        ClubeDTO dto = criarClubeDTO();
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clubeService.update(1, dto));
    }

    @Test
    void delete_deveDesativarClube() {
        Clube clube = criarClube();
        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        clubeService.delete(1);

        assertFalse(clube.getAtivo());
        verify(clubeRepository).save(clube);
    }

    @Test
    void delete_quandoNaoExiste_deveLancarNotFound() {
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clubeService.delete(1));
    }
}