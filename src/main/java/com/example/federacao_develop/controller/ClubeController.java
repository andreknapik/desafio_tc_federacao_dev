package com.example.federacao_develop.controller;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @GetMapping
    public List<ClubeDTO> listAll() {
        return clubeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clubeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClubeDTO> create(@RequestBody ClubeDTO dto) {
        ClubeDTO salvo = clubeService.save(dto);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeDTO> update(@PathVariable Integer id, @RequestBody ClubeDTO dto) {
        ClubeDTO atualizado = clubeService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clubeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}