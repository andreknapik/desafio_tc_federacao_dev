package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.service.EstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/estadios")
public class EstadioController {

    private EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @GetMapping
    public Page<EstadioDTO> listarPaginado(Pageable pageable) {
        return estadioService.findAllPage(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadioService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadioDTO create(@RequestBody @Valid EstadioDTO estadioDTO) {
        return estadioService.save(estadioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioDTO> update(@PathVariable Integer id, @RequestBody @Valid EstadioDTO dto) {
        return ResponseEntity.ok(estadioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}