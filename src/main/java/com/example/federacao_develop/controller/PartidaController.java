package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.service.PartidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping
    public List<PartidaDTO> listAll() {
        return partidaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(partidaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PartidaDTO> create(@RequestBody PartidaDTO dto) {
        return ResponseEntity.ok(partidaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> update(@PathVariable Integer id, @RequestBody PartidaDTO dto) {
        return ResponseEntity.ok(partidaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        partidaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}