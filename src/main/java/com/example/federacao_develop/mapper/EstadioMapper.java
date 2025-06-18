package com.example.federacao_develop.mapper;

import com.example.federacao_develop.dto.EstadioDTO;
import com.example.federacao_develop.model.Estadio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadioMapper {
    EstadioDTO toDTO(Estadio entity);
    Estadio toEntity(EstadioDTO dto);
}