package com.home.kanjidictionaryapp.mapper;

public interface CreateMapper<Entity, CreateDto> {
    Entity toEntity(CreateDto createDto);
    CreateDto toDto(Entity entity);
}
