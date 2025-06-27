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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.federacao_develop.dto.ConfrontoDiretoDTO;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class ClubeService {

        private final ClubeRepository clubeRepository;
        private final EstadioRepository estadioRepository;
        private final PartidaRepository partidaRepository;
        private final ClubeMapper clubeMapper;
        private final PartidaMapper partidaMapper;

        public ClubeService(
                ClubeRepository clubeRepository,
                EstadioRepository estadioRepository,
                PartidaRepository partidaRepository,
                ClubeMapper clubeMapper,
                PartidaMapper partidaMapper
        ) {
            this.clubeRepository = clubeRepository;
            this.estadioRepository = estadioRepository;
            this.partidaRepository = partidaRepository;
            this.clubeMapper = clubeMapper;
            this.partidaMapper = partidaMapper;
        }


    public Page<ClubeDTO> findClubsFilter(String nomeClube, String ufClube, Boolean ativo, Pageable pageable) {
        return clubeRepository.findClubsFilter(nomeClube, ufClube, ativo, pageable)
                .map(clubeMapper::toDTO);
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

        if (Boolean.FALSE.equals(clube.getAtivo())) {
            throw new NotFoundException(MensagemExceptionEnum.CLUBE_NAO_ENCONTRADO);
        }

        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public RetrospectoDTO obterRetrospectoGeralClube(Integer clubeId) {
        List<Partida> partidas = partidaRepository.findByClube(clubeId);
        int vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;
        for (Partida p : partidas) {
            int golsPro, golsContra;
            boolean mandante = p.getClubeMandante().getClubeId().equals(clubeId);
            if (mandante) {
                golsPro = p.getGolsMandante();
                golsContra = p.getGolsVisitante();
            } else {
                golsPro = p.getGolsVisitante();
                golsContra = p.getGolsMandante();
            }
            golsFeitos += golsPro;
            golsSofridos += golsContra;
            if (golsPro > golsContra) vitorias++;
            else if (golsPro == golsContra) empates++;
            else derrotas++;
        }
        return new RetrospectoDTO(vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }

    public List<RetrospectoAdversarioDTO> obterRetrospectoContraAdversarios(Integer clubeId) {
        List<Partida> partidas = partidaRepository.findByClube(clubeId);
        Map<Integer, RetrospectoAdversarioDTO> adversarioMap = new HashMap<>();
        for (Partida p : partidas) {
            Clube adversario = p.getClubeMandante().getClubeId().equals(clubeId) ?
                    p.getClubeVisitante() : p.getClubeMandante();
            int adversarioId = adversario.getClubeId();
            RetrospectoAdversarioDTO dto = adversarioMap.computeIfAbsent(adversarioId, id -> {
                RetrospectoAdversarioDTO d = new RetrospectoAdversarioDTO();
                d.setAdversarioId(adversarioId);
                d.setAdversarioNome(adversario.getNomeClube());
                d.setRetrospecto(new RetrospectoDTO(0,0,0,0,0));
                return d;
            });
            RetrospectoDTO r = dto.getRetrospecto();
            boolean mandante = p.getClubeMandante().getClubeId().equals(clubeId);
            int golsPro = mandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsContra = mandante ? p.getGolsVisitante() : p.getGolsMandante();
            r.setGolsFeitos(r.getGolsFeitos() + golsPro);
            r.setGolsSofridos(r.getGolsSofridos() + golsContra);
            if (golsPro > golsContra) r.setVitorias(r.getVitorias() + 1);
            else if (golsPro == golsContra) r.setEmpates(r.getEmpates() + 1);
            else r.setDerrotas(r.getDerrotas() + 1);
        }
        return new ArrayList<>(adversarioMap.values());
    }

    public ConfrontoDiretoDTO obterConfrontoDireto(Integer clubeA, Integer clubeB) {
        List<Partida> partidas = partidaRepository.findConfrontosEntreDoisClubes(clubeA, clubeB);
        RetrospectoDTO retroA = new RetrospectoDTO(0,0,0,0,0);
        RetrospectoDTO retroB = new RetrospectoDTO(0,0,0,0,0);
        for (Partida p : partidas) {
            boolean mandanteA = p.getClubeMandante().getClubeId().equals(clubeA);
            int golsA = mandanteA ? p.getGolsMandante() : p.getGolsVisitante();
            int golsB = mandanteA ? p.getGolsVisitante() : p.getGolsMandante();
            retroA.setGolsFeitos(retroA.getGolsFeitos() + golsA);
            retroA.setGolsSofridos(retroA.getGolsSofridos() + golsB);
            retroB.setGolsFeitos(retroB.getGolsFeitos() + golsB);
            retroB.setGolsSofridos(retroB.getGolsSofridos() + golsA);

            if (golsA > golsB)      { retroA.setVitorias(retroA.getVitorias() + 1); retroB.setDerrotas(retroB.getDerrotas() + 1);}
            else if (golsA == golsB){ retroA.setEmpates(retroA.getEmpates() + 1);  retroB.setEmpates(retroB.getEmpates() + 1);}
            else                   { retroA.setDerrotas(retroA.getDerrotas() + 1); retroB.setVitorias(retroB.getVitorias() + 1);}
        }
        List<PartidaDTO> partidasDTO = partidas.stream().map(partidaMapper::toDTO).collect(Collectors.toList());
        ConfrontoDiretoDTO dto = new ConfrontoDiretoDTO();
        dto.setPartidas(partidasDTO);
        dto.setRetrospectoClubeA(retroA);
        dto.setRetrospectoClubeB(retroB);
        return dto;
    }

    public List<RankingClubeDTO> ranking(
            String criterio,
            Boolean mandante,
            Boolean visitante,
            Boolean goleada
    ) {
        List<Clube> clubes = clubeRepository.findAll();
        List<Partida> partidas = partidaRepository.findAll();

        List<RankingClubeDTO> resultado = new ArrayList<>();

        for (Clube clube : clubes) {
            int jogos = 0, vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0, pontos = 0;

            for (Partida p : partidas) {
                boolean isMandante = p.getClubeMandante().getClubeId().equals(clube.getClubeId());
                boolean isVisitante = p.getClubeVisitante().getClubeId().equals(clube.getClubeId());

                if (Boolean.TRUE.equals(mandante) && !isMandante) continue;
                if (Boolean.TRUE.equals(visitante) && !isVisitante) continue;

                int golsPro = isMandante ? p.getGolsMandante() : (isVisitante ? p.getGolsVisitante() : 0);
                int golsContra = isMandante ? p.getGolsVisitante() : (isVisitante ? p.getGolsMandante() : 0);

                if (goleada != null && goleada) {
                    if (Math.abs(golsPro - golsContra) < 3) continue;
                }

                if (!isMandante && !isVisitante) continue;

                jogos++;
                golsFeitos += golsPro;
                golsSofridos += golsContra;
                if (golsPro > golsContra) vitorias++;
                else if (golsPro == golsContra) empates++;
                else derrotas++;
            }
            pontos = vitorias * 3 + empates;

            boolean add = false;
            switch (criterio.toLowerCase()) {
                case "jogos":    add = jogos > 0; break;
                case "vitorias": add = vitorias > 0; break;
                case "gols":     add = golsFeitos > 0; break;
                case "pontos":   add = pontos > 0; break;
            }
            if (add) {
                RankingClubeDTO dto = new RankingClubeDTO();
                dto.setClubeId(clube.getClubeId());
                dto.setNomeClube(clube.getNomeClube());
                dto.setJogos(jogos);
                dto.setVitorias(vitorias);
                dto.setEmpates(empates);
                dto.setDerrotas(derrotas);
                dto.setGolsFeitos(golsFeitos);
                dto.setGolsSofridos(golsSofridos);
                dto.setPontos(pontos);
                resultado.add(dto);
            }
        }


        Comparator<RankingClubeDTO> comparator;
        switch (criterio.toLowerCase()) {
            case "jogos":    comparator = Comparator.comparing(RankingClubeDTO::getJogos).reversed(); break;
            case "vitorias": comparator = Comparator.comparing(RankingClubeDTO::getVitorias).reversed(); break;
            case "gols":     comparator = Comparator.comparing(RankingClubeDTO::getGolsFeitos).reversed(); break;
            case "pontos":   comparator = Comparator.comparing(RankingClubeDTO::getPontos).reversed(); break;
            default:         comparator = Comparator.comparing(RankingClubeDTO::getPontos).reversed(); break;
        }
        resultado.sort(comparator);

        return resultado;
    }

}