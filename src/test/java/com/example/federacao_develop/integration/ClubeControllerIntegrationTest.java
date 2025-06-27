package com.example.federacao_develop.integration;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.dto.EstadioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClubeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private EstadioDTO criarEstadio(String nomeEstadio) throws Exception {
        EstadioDTO estadio = new EstadioDTO();
        estadio.setNomeEstadio(nomeEstadio);

        String estadioJson = objectMapper.writeValueAsString(estadio);
        String estadioResp = mockMvc.perform(post("/api/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estadioJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(estadioResp, EstadioDTO.class);
    }

    @Test
    void clube_crud_integration() throws Exception {
        EstadioDTO estadioSalvo = criarEstadio("Morumbi");

        ClubeDTO clube = new ClubeDTO();
        clube.setNomeClube("Palmeiras");
        clube.setUfClube("SP");
        clube.setDataFundacao(LocalDate.of(1900, 1, 1));
        clube.setAtivo(true);
        clube.setEstadio(estadioSalvo);

        String clubeJson = objectMapper.writeValueAsString(clube);
        String response = mockMvc.perform(post("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clubeJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ClubeDTO clubeSalvo = objectMapper.readValue(response, ClubeDTO.class);

        mockMvc.perform(get("/api/clubes/" + clubeSalvo.getClubeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeClube").value("Palmeiras"));

        mockMvc.perform(get("/api/clubes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].nomeClube").isNotEmpty());

        clubeSalvo.setNomeClube("Palmeiras Atualizado");
        mockMvc.perform(put("/api/clubes/" + clubeSalvo.getClubeId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeSalvo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeClube").value("Palmeiras Atualizado"));

        mockMvc.perform(delete("/api/clubes/" + clubeSalvo.getClubeId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/clubes/" + clubeSalvo.getClubeId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/clubes/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClube_fails_whenNomeIsBlank() throws Exception {
        ClubeDTO clubeInvalido = new ClubeDTO();
        clubeInvalido.setUfClube("SP");
        clubeInvalido.setAtivo(true);

        String json = objectMapper.writeValueAsString(clubeInvalido);

        mockMvc.perform(post("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscar_filtrarPorNomeUf() throws Exception {
        EstadioDTO estadio = criarEstadio("Allianz Parque");

        ClubeDTO clube = new ClubeDTO();
        clube.setNomeClube("Palmeiras");
        clube.setUfClube("SP");
        clube.setDataFundacao(LocalDate.of(1900, 1, 1));
        clube.setAtivo(true);
        clube.setEstadio(estadio);

        String clubeJson = objectMapper.writeValueAsString(clube);
        mockMvc.perform(post("/api/clubes")
                        .contentType(MediaType.APPLICATION_JSON).content(clubeJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/clubes")
                        .param("nome", "Palmeiras")
                        .param("uf", "SP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.nomeClube=='Palmeiras')]").exists());
    }

    @Test
    void update_clubeNaoExiste() throws Exception {
        EstadioDTO estadioValido = new EstadioDTO();
        estadioValido.setNomeEstadio("Estádio Teste");

        String estadioJson = objectMapper.writeValueAsString(estadioValido);
        String resposta = mockMvc.perform(post("/api/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estadioJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EstadioDTO estadioSalvo = objectMapper.readValue(resposta, EstadioDTO.class);

        ClubeDTO clubeFake = new ClubeDTO();
        clubeFake.setNomeClube("Inexistente");
        clubeFake.setUfClube("SP");
        clubeFake.setDataFundacao(LocalDate.of(2000, 1, 1));
        clubeFake.setAtivo(true);
        clubeFake.setEstadio(estadioSalvo);

        mockMvc.perform(put("/api/clubes/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubeFake)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_clubeNaoExiste() throws Exception {
        mockMvc.perform(delete("/api/clubes/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void retrospectoGeral_returns_ok() throws Exception {
        EstadioDTO estadio = criarEstadio("Castelão");

        ClubeDTO clube = new ClubeDTO();
        clube.setNomeClube("Ceará");
        clube.setUfClube("CE");
        clube.setAtivo(true);
        clube.setEstadio(estadio);
        clube.setDataFundacao(LocalDate.of(1914, 6, 2));
        String clubeJson = objectMapper.writeValueAsString(clube);
        String resp = mockMvc.perform(post("/api/clubes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubeJson)).andReturn().getResponse().getContentAsString();
        ClubeDTO salvo = objectMapper.readValue(resp, ClubeDTO.class);

        mockMvc.perform(get("/api/clubes/" + salvo.getClubeId() + "/retrospecto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vitorias").exists());
    }
}