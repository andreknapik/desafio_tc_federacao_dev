package com.example.federacao_develop.mapper;

import com.example.federacao_develop.dto.*;
import com.example.federacao_develop.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PartidaMapper {
    @Mapping(source = "clubeMandante", target = "clubeMandante")
    @Mapping(source = "clubeVisitante", target = "clubeVisitante")
    PartidaDTO toDTO(Partida entity);

    default ClubeResumoDTO toClubeResumoDTO(Clube clube) {
        if (clube == null) return null;
        return new ClubeResumoDTO(clube.getClubeId(), clube.getNomeClube());
    }

    @Mapping(target = "clubeMandante", ignore = true)
    @Mapping(target = "clubeVisitante", ignore = true)
    Partida toEntity(PartidaDTO dto);
}