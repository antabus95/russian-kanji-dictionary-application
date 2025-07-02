package com.home.kanjidictionaryapp.mapper;

public interface ReadUpdateMapper<Entity, ReadUpdateDto> {
    Entity toEntity(ReadUpdateDto readUpdateDto);
    ReadUpdateDto toDto(Entity entity);
}
