package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.*;
import com.example.federacao_develop.mapper.ClubeMapper;
import com.example.federacao_develop.repository.EstadioRepository;
import com.example.federacao_develop.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/clubes")
public class ClubeController {

    private ClubeService clubeService;
    private EstadioRepository estadioRepository;
    private ClubeMapper clubeMapper;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @GetMapping
    public Page<ClubeDTO> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        return clubeService.findClubsFilter(nome, uf, ativo, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clubeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClubeDTO> create(@RequestBody @Valid ClubeDTO dto) {
        ClubeDTO salvo = clubeService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeDTO> update(@PathVariable Integer id, @RequestBody @Valid ClubeDTO dto) {
        ClubeDTO atualizado = clubeService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clubeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clubeId}/retrospecto")
    public RetrospectoDTO retrospecto(@PathVariable Integer clubeId) {
        return clubeService.obterRetrospectoGeralClube(clubeId);
    }

    @GetMapping("/{clubeId}/retrospecto-adversarios")
    public List<RetrospectoAdversarioDTO> retrospectoAdversarios(@PathVariable Integer clubeId) {
        return clubeService.obterRetrospectoContraAdversarios(clubeId);
    }

    @GetMapping("/confrontos")
    public ConfrontoDiretoDTO confronto(
            @RequestParam Integer clubeA,
            @RequestParam Integer clubeB
    ) {
        return clubeService.obterConfrontoDireto(clubeA, clubeB);
    }

    @GetMapping("/ranking")
    public List<RankingClubeDTO> ranking(
            @RequestParam String criterio,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante,
            @RequestParam(required = false) Boolean goleada
    ) {
        return clubeService.ranking(criterio, mandante, visitante, goleada);
    }
}