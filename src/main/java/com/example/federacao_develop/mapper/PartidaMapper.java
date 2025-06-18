package com.example.federacao_develop.mapper;

import com.example.federacao_develop.dto.PartidaDTO;
import com.example.federacao_develop.model.Partida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PartidaMapper {
    @Mapping(source = "clubeMandante.clubeId", target = "clubeMandanteId")
    @Mapping(source = "clubeVisitante.clubeId", target = "clubeVisitanteId")
    PartidaDTO toDTO(Partida entity);

    @Mapping(target = "clubeMandante", ignore = true)
    @Mapping(target = "clubeVisitante", ignore = true)
    Partida toEntity(PartidaDTO dto);
}