package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {

    private PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping
    public Page<PartidaDTO> buscar(
            @RequestParam(required = false) Integer clubeId,
            @RequestParam(required = false) Integer estadioId,
            Pageable pageable
    ) {
        return partidaService.findPartidasFilter(clubeId, estadioId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(partidaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PartidaDTO> create(@RequestBody @Valid PartidaDTO dto) {
        PartidaDTO saved = partidaService.save(dto);
        URI location = URI.create("/api/partidas/" + saved.getPartidaId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> update(@PathVariable Integer id, @RequestBody @Valid PartidaDTO dto) {
        return ResponseEntity.ok(partidaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        partidaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}