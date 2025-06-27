package com.example.federacao_develop.service;

import com.example.federacao_develop.dto.*;
import com.example.federacao_develop.exception.MensagemExceptionEnum;
import com.example.federacao_develop.exception.NotFoundException;
import com.example.federacao_develop.mapper.ClubeMapper;
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

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    @Mock private ClubeRepository clubeRepository;
    @Mock private EstadioRepository estadioRepository;
    @Mock private PartidaRepository partidaRepository;
    @Mock private ClubeMapper clubeMapper;
    @Mock private PartidaMapper partidaMapper;

    @InjectMocks
    private ClubeService clubeService;

    private Clube clube;
    private ClubeDTO clubeDTO;
    private Estadio estadio;
    private EstadioDTO estadioDTO;

    @BeforeEach
    void setup() {
        estadio = new Estadio();
        estadio.setEstadioId(1);
        estadio.setNomeEstadio("Maracanã");

        estadioDTO = new EstadioDTO();
        estadioDTO.setEstadioId(1);
        estadioDTO.setNomeEstadio("Maracanã");

        clube = new Clube();
        clube.setClubeId(1);
        clube.setNomeClube("Flamengo");
        clube.setUfClube("RJ");
        clube.setDataFundacao(LocalDate.of(1895, 11, 15));
        clube.setAtivo(true);
        clube.setEstadio(estadio);

        clubeDTO = new ClubeDTO();
        clubeDTO.setClubeId(1);
        clubeDTO.setNomeClube("Flamengo");
        clubeDTO.setUfClube("RJ");
        clubeDTO.setDataFundacao(LocalDate.of(1895, 11, 15));
        clubeDTO.setAtivo(true);
        clubeDTO.setEstadio(estadioDTO);
    }

    @Test
    void testFindById_Success() {
        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));
        when(clubeMapper.toDTO(clube)).thenReturn(clubeDTO);

        ClubeDTO result = clubeService.findById(1);

        assertNotNull(result);
        assertEquals(clubeDTO.getNomeClube(), result.getNomeClube());
        verify(clubeRepository).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> clubeService.findById(1));
        assertEquals(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO, exception.getMensagemEnum());
    }

    @Test
    void testFindAll() {
        when(clubeRepository.findAll()).thenReturn(Collections.singletonList(clube));
        when(clubeMapper.toDTO(clube)).thenReturn(clubeDTO);

        List<ClubeDTO> result = clubeService.findAll();

        assertEquals(1, result.size());
        assertEquals(clubeDTO.getNomeClube(), result.get(0).getNomeClube());
    }

    @Test
    void testSave_WithEstadio() {
        when(clubeMapper.toEntity(clubeDTO)).thenReturn(clube);
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));
        when(clubeRepository.save(clube)).thenReturn(clube);
        when(clubeMapper.toDTO(clube)).thenReturn(clubeDTO);

        ClubeDTO result = clubeService.save(clubeDTO);

        assertNotNull(result);
        assertEquals("Flamengo", result.getNomeClube());
        verify(estadioRepository).findById(1);
        verify(clubeRepository).save(clube);
    }

    @Test
    void testUpdate_Success() {
        Clube updatedClube = new Clube();
        updatedClube.setNomeClube("Fluminense");
        updatedClube.setUfClube("RJ");
        updatedClube.setDataFundacao(LocalDate.of(1902, 7, 21));
        updatedClube.setAtivo(true);

        ClubeDTO updatedDTO = new ClubeDTO();
        updatedDTO.setClubeId(1);
        updatedDTO.setNomeClube("Fluminense");
        updatedDTO.setUfClube("RJ");
        updatedDTO.setDataFundacao(LocalDate.of(1902, 7, 21));
        updatedDTO.setAtivo(true);
        updatedDTO.setEstadio(estadioDTO);

        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));
        when(clubeMapper.toEntity(updatedDTO)).thenReturn(updatedClube);
        when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);
        when(clubeMapper.toDTO(clube)).thenReturn(updatedDTO);

        ClubeDTO result = clubeService.update(1, updatedDTO);

        assertEquals("Fluminense", result.getNomeClube());
        verify(clubeRepository).findById(1);
        verify(estadioRepository).findById(1);
    }

    @Test
    void testDelete_Success() {
        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        clubeService.delete(1);

        assertFalse(clube.getAtivo());
        verify(clubeRepository).findById(1);
        verify(clubeRepository).save(clube);
    }

    @Test
    void testFindClubsFilter() {
        Pageable pageable = mock(Pageable.class);
        Page<Clube> clubes = new PageImpl<>(Collections.singletonList(clube));
        when(clubeRepository.findClubsFilter("Flamengo", "RJ", true, pageable)).thenReturn(clubes);
        when(clubeMapper.toDTO(clube)).thenReturn(clubeDTO);

        Page<ClubeDTO> page = clubeService.findClubsFilter("Flamengo", "RJ", true, pageable);

        assertEquals(1, page.getContent().size());
        assertEquals("Flamengo", page.getContent().get(0).getNomeClube());
    }

    @Test
    void testObterRetrospectoGeralClube() {
        Clube mandante = new Clube();
        mandante.setClubeId(1);
        Clube visitante = new Clube();
        visitante.setClubeId(2);

        Partida p1 = new Partida();
        p1.setClubeMandante(mandante);
        p1.setClubeVisitante(visitante);
        p1.setGolsMandante(3);
        p1.setGolsVisitante(1);

        Partida p2 = new Partida();
        p2.setClubeMandante(visitante);
        p2.setClubeVisitante(mandante);
        p2.setGolsMandante(2);
        p2.setGolsVisitante(2);

        Partida p3 = new Partida();
        p3.setClubeMandante(visitante);
        p3.setClubeVisitante(mandante);
        p3.setGolsMandante(2);
        p3.setGolsVisitante(1);

        when(partidaRepository.findByClube(1)).thenReturn(Arrays.asList(p1, p2, p3));

        RetrospectoDTO retrospecto = clubeService.obterRetrospectoGeralClube(1);

        assertEquals(1, retrospecto.getVitorias());
        assertEquals(1, retrospecto.getEmpates());
        assertEquals(1, retrospecto.getDerrotas());
        assertEquals(6, retrospecto.getGolsFeitos());
        assertEquals(5, retrospecto.getGolsSofridos());
    }

    @Test
    void testObterRetrospectoContraAdversarios() {
        Clube clube = new Clube(); clube.setClubeId(1); clube.setNomeClube("Clube 1");
        Clube adv1 = new Clube(); adv1.setClubeId(2); adv1.setNomeClube("Clube 2");
        Clube adv2 = new Clube(); adv2.setClubeId(3); adv2.setNomeClube("Clube 3");

        Partida p1 = new Partida();
        p1.setClubeMandante(clube); p1.setClubeVisitante(adv1); p1.setGolsMandante(1); p1.setGolsVisitante(0); // vitória contra adv1

        Partida p2 = new Partida();
        p2.setClubeMandante(adv2); p2.setClubeVisitante(clube); p2.setGolsMandante(2); p2.setGolsVisitante(2); // empate com adv2

        Partida p3 = new Partida();
        p3.setClubeMandante(clube); p3.setClubeVisitante(adv1); p3.setGolsMandante(0); p3.setGolsVisitante(2); // derrota para adv1

        when(partidaRepository.findByClube(1)).thenReturn(Arrays.asList(p1, p2, p3));

        List<RetrospectoAdversarioDTO> advs = clubeService.obterRetrospectoContraAdversarios(1);

        assertEquals(2, advs.size());
        RetrospectoAdversarioDTO contraAdv1 = advs.stream().filter(a -> a.getAdversarioId() == 2).findFirst().get();
        RetrospectoAdversarioDTO contraAdv2 = advs.stream().filter(a -> a.getAdversarioId() == 3).findFirst().get();

        assertEquals(1, contraAdv1.getRetrospecto().getVitorias());
        assertEquals(1, contraAdv1.getRetrospecto().getDerrotas());
        assertEquals(0, contraAdv1.getRetrospecto().getEmpates());

        assertEquals(0, contraAdv2.getRetrospecto().getVitorias());
        assertEquals(0, contraAdv2.getRetrospecto().getDerrotas());
        assertEquals(1, contraAdv2.getRetrospecto().getEmpates());
    }

    @Test
    void testObterConfrontoDireto() {
        Clube clubeA = new Clube(); clubeA.setClubeId(1);
        Clube clubeB = new Clube(); clubeB.setClubeId(2);
        Partida p1 = new Partida();
        p1.setClubeMandante(clubeA); p1.setClubeVisitante(clubeB);
        p1.setGolsMandante(3); p1.setGolsVisitante(1);

        Partida p2 = new Partida();
        p2.setClubeMandante(clubeB); p2.setClubeVisitante(clubeA);
        p2.setGolsMandante(2); p2.setGolsVisitante(2);

        Partida p3 = new Partida();
        p3.setClubeMandante(clubeB); p3.setClubeVisitante(clubeA);
        p3.setGolsMandante(2); p3.setGolsVisitante(1);

        when(partidaRepository.findConfrontosEntreDoisClubes(1, 2)).thenReturn(Arrays.asList(p1, p2, p3));
        when(partidaMapper.toDTO(any())).thenReturn(new PartidaDTO());

        ConfrontoDiretoDTO confronto = clubeService.obterConfrontoDireto(1, 2);

        assertEquals(3, confronto.getPartidas().size());
        assertEquals(1, confronto.getRetrospectoClubeA().getVitorias());
        assertEquals(1, confronto.getRetrospectoClubeA().getEmpates());
        assertEquals(1, confronto.getRetrospectoClubeA().getDerrotas());
        assertEquals(1, confronto.getRetrospectoClubeB().getVitorias());
        assertEquals(1, confronto.getRetrospectoClubeB().getEmpates());
        assertEquals(1, confronto.getRetrospectoClubeB().getDerrotas());
    }

    @Test
    void testRankingPontos() {
        Clube clube1 = new Clube(); clube1.setClubeId(1); clube1.setNomeClube("C1");
        Clube clube2 = new Clube(); clube2.setClubeId(2); clube2.setNomeClube("C2");

        when(clubeRepository.findAll()).thenReturn(Arrays.asList(clube1, clube2));

        Partida p1 = new Partida();
        p1.setClubeMandante(clube1); p1.setClubeVisitante(clube2);
        p1.setGolsMandante(2); p1.setGolsVisitante(0);

        Partida p2 = new Partida();
        p2.setClubeMandante(clube2); p2.setClubeVisitante(clube1);
        p2.setGolsMandante(1); p2.setGolsVisitante(1);

        when(partidaRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<RankingClubeDTO> ranking = clubeService.ranking("pontos", null, null, null);

        assertEquals(2, ranking.size());
        assertEquals("C1", ranking.get(0).getNomeClube());
        assertEquals("C2", ranking.get(1).getNomeClube());

        assertEquals(4, ranking.get(0).getPontos());
        assertEquals(1, ranking.get(1).getPontos());
    }
}