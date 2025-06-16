package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.exception.BusinessException;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.repository.ClubeRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstadioServiceTest {

    @Mock private EstadioRepository estadioRepository;
    @Mock private ClubeRepository clubeRepository;
    @Mock private PartidaRepository partidaRepository;

    @InjectMocks private EstadioService estadioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Estadio criarEstadio() {
        Estadio estadio = new Estadio();
        estadio.setEstadioId(1);
        estadio.setNomeEstadio("Maracanã");
        return estadio;
    }

    private EstadioDTO criarEstadioDTO() {
        EstadioDTO dto = new EstadioDTO();
        dto.setEstadioId(1);
        dto.setNomeEstadio("Maracanã");
        return dto;
    }

    @Test
    void findAll_deveRetornarLista() {
        when(estadioRepository.findAll()).thenReturn(Arrays.asList(criarEstadio()));

        List<EstadioDTO> lista = estadioService.findAll();

        assertEquals(1, lista.size());
        assertEquals("Maracanã", lista.get(0).getNomeEstadio());
        verify(estadioRepository).findAll();
    }

    @Test
    void findById_quandoExiste_deveRetornarDTO() {
        when(estadioRepository.findById(1)).thenReturn(Optional.of(criarEstadio()));

        EstadioDTO dto = estadioService.findById(1);

        assertNotNull(dto);
        assertEquals("Maracanã", dto.getNomeEstadio());
        verify(estadioRepository).findById(1);
    }

    @Test
    void findById_quandoNaoExiste_deveLancarRuntimeException() {
        when(estadioRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadioService.findById(1));
        assertEquals("Estádio não encontrado", ex.getMessage());
    }

    @Test
    void save_deveSalvarEConverterParaDTO() {
        EstadioDTO dto = criarEstadioDTO();
        Estadio estadio = criarEstadio();
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadio);

        EstadioDTO criado = estadioService.save(dto);

        assertEquals(dto.getNomeEstadio(), criado.getNomeEstadio());
        verify(estadioRepository).save(any(Estadio.class));
    }

    @Test
    void update_deveAlterarENomeEConverterParaDTO() {
        EstadioDTO dto = criarEstadioDTO();
        dto.setNomeEstadio("Novo Nome");
        Estadio estadio = criarEstadio();
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadio);

        EstadioDTO atualizado = estadioService.update(1, dto);

        assertNotNull(atualizado);
        verify(estadioRepository).save(any(Estadio.class));
        verify(estadioRepository).findById(1);
    }

    @Test
    void update_quandoNaoExiste_lancaExcecao() {
        EstadioDTO dto = criarEstadioDTO();
        when(estadioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> estadioService.update(1, dto));
    }

    @Test
    void delete_quandoTemClube_lancaBusinessException() {
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> estadioService.delete(1));
        assertEquals(MensagemExceptionEnum.ESTADIO_COM_CLUBES, ex.getMensagemEnum());
    }

    @Test
    void delete_quandoTemPartida_lancaBusinessException() {
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(false);
        when(partidaRepository.existsByEstadio_EstadioId(1)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> estadioService.delete(1));
        assertEquals(MensagemExceptionEnum.ESTADIO_COM_PARTIDAS, ex.getMensagemEnum());
    }

    @Test
    void delete_sucesso_chamaDeleteById() {
        when(clubeRepository.existsByEstadio_EstadioId(1)).thenReturn(false);
        when(partidaRepository.existsByEstadio_EstadioId(1)).thenReturn(false);

        estadioService.delete(1);

        verify(estadioRepository).deleteById(1);
    }
}