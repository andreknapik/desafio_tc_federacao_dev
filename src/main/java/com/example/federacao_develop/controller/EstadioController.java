package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.service.EstadioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadios")
public class EstadioController {

    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @GetMapping
    public List<EstadioDTO> listAll() {
        return estadioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EstadioDTO> create(@RequestBody EstadioDTO dto) {
        return ResponseEntity.ok(estadioService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioDTO> update(@PathVariable Integer id, @RequestBody EstadioDTO dto) {
        return ResponseEntity.ok(estadioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}