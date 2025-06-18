package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.mapper.EstadioMapperImpl;
import com.example.federacao_develop.mapper.PartidaMapperImpl;
import com.example.federacao_develop.model.Clube;
import com.example.federacao_develop.model.Estadio;
import com.example.federacao_develop.model.Partida;
import com.example.federacao_develop.repository.PartidaRepository;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.repository.ClubeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidaServiceTest {

    private PartidaRepository partidaRepository;
    private EstadioRepository estadioRepository;
    private ClubeRepository clubeRepository;
    private PartidaService partidaService;

    @BeforeEach
    void setUp() {
        partidaRepository = mock(PartidaRepository.class);
        estadioRepository = mock(EstadioRepository.class);
        clubeRepository = mock(ClubeRepository.class);

        PartidaMapperImpl partidaMapper = new PartidaMapperImpl();

        partidaService = new PartidaService(partidaRepository, estadioRepository, clubeRepository, partidaMapper);
    }
    private Partida criarPartida() {
        Partida partida = new Partida();
        partida.setPartidaId(1);
        Clube clubeMandante = new Clube();
        clubeMandante.setClubeId(10);
        Clube clubeVisitante = new Clube();
        clubeVisitante.setClubeId(20);
        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setGolsMandante(2);
        partida.setGolsVisitante(1);
        partida.setDataDaPartida(LocalDate.of(2024, 6, 9).atStartOfDay());
        Estadio estadio = new Estadio();
        estadio.setEstadioId(15);
        estadio.setNomeEstadio("Morumbi");
        partida.setEstadio(estadio);
        return partida;
    }

    private PartidaDTO criarPartidaDTO() {
        PartidaDTO dto = new PartidaDTO();
        dto.setPartidaId(1);
        dto.setClubeMandanteId(10);
        dto.setClubeVisitanteId(20);
        dto.setGolsMandante(2);
        dto.setGolsVisitante(1);
        dto.setDataDaPartida(LocalDate.of(2024, 6, 9).atStartOfDay());
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setEstadioId(15);
        estadioDTO.setNomeEstadio("Morumbi");
        dto.setEstadio(estadioDTO);
        return dto;
    }

    @Test
    void findAll_deveRetornarListaDTO() {
        when(partidaRepository.findAll()).thenReturn(Arrays.asList(criarPartida()));
        List<PartidaDTO> lista = partidaService.findAll();
        assertEquals(1, lista.size());
        assertEquals(2, lista.get(0).getGolsMandante());
        verify(partidaRepository).findAll();
    }

    @Test
    void findById_quandoExiste_deveRetornarDTO() {
        when(partidaRepository.findById(1)).thenReturn(Optional.of(criarPartida()));
        PartidaDTO dto = partidaService.findById(1);
        assertNotNull(dto);
        assertEquals(2, dto.getGolsMandante());
        verify(partidaRepository).findById(1);
    }

    @Test
    void findById_quandoNaoExiste_lancaExcecao() {
        when(partidaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> partidaService.findById(1));
    }

    @Test
    void save_deveSalvarEConverterParaDTO() {
        PartidaDTO dto = criarPartidaDTO();
        Partida entity = criarPartida();

        when(clubeRepository.findById(10)).thenReturn(Optional.of(entity.getClubeMandante()));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(entity.getClubeVisitante()));
        when(estadioRepository.findById(15)).thenReturn(Optional.of(entity.getEstadio()));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invoc -> {
            Partida p = invoc.getArgument(0);
            p.setPartidaId(1);
            return p;
        });

        PartidaDTO criado = partidaService.save(dto);
        assertEquals(dto.getGolsMandante(), criado.getGolsMandante());
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void update_deveAlterarCamposEConverterParaDTO() {
        PartidaDTO dto = criarPartidaDTO();
        dto.setGolsMandante(3);

        Partida entity = criarPartida();
        Clube clubeMandante = entity.getClubeMandante();
        Clube clubeVisitante = entity.getClubeVisitante();
        Estadio estadio = entity.getEstadio();

        when(partidaRepository.findById(1)).thenReturn(Optional.of(entity));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(clubeMandante));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(15)).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invoc -> invoc.getArgument(0));

        PartidaDTO atualizado = partidaService.update(1, dto);

        assertNotNull(atualizado);
        assertEquals(3, atualizado.getGolsMandante());
        verify(partidaRepository).findById(1);
        verify(partidaRepository).save(entity);
    }

    @Test
    void update_quandoNaoExiste_lancaExcecao() {
        PartidaDTO dto = criarPartidaDTO();
        when(partidaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> partidaService.update(1, dto));
    }

    @Test
    void update_quandoMandanteNaoExiste_lancaExcecao() {
        PartidaDTO dto = criarPartidaDTO();
        Partida entity = criarPartida();

        when(partidaRepository.findById(1)).thenReturn(Optional.of(entity));
        when(clubeRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> partidaService.update(1, dto));
    }

    @Test
    void update_quandoVisitanteNaoExiste_lancaExcecao() {
        PartidaDTO dto = criarPartidaDTO();
        Partida entity = criarPartida();

        when(partidaRepository.findById(1)).thenReturn(Optional.of(entity));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(entity.getClubeMandante()));
        when(clubeRepository.findById(20)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> partidaService.update(1, dto));
    }

    @Test
    void update_quandoEstadioNaoExiste_lancaExcecao() {
        PartidaDTO dto = criarPartidaDTO();
        Partida entity = criarPartida();

        when(partidaRepository.findById(1)).thenReturn(Optional.of(entity));
        when(clubeRepository.findById(10)).thenReturn(Optional.of(entity.getClubeMandante()));
        when(clubeRepository.findById(20)).thenReturn(Optional.of(entity.getClubeVisitante()));
        when(estadioRepository.findById(15)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> partidaService.update(1, dto));
    }

    @Test
    void delete_chamaDeleteById() {
        partidaService.delete(1);
        verify(partidaRepository).deleteById(1);
    }
}