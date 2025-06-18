package com.example.federacao_develop.mapper;

import com.example.federacao_develop.dto.ClubeDTO;
import com.example.federacao_develop.model.Clube;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClubeMapper {

    @Mapping(target = "estadio.nomeEstadio", source = "estadio.nomeEstadio")
    ClubeDTO toDTO(Clube entity);
    Clube toEntity(ClubeDTO dto);
}