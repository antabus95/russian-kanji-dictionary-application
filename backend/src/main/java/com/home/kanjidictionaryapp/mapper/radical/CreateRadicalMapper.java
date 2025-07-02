package com.home.kanjidictionaryapp.mapper.radical;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.model.Radical;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateRadicalMapper implements CreateMapper<Radical, CreateRadicalDto> {

    private final CreateRadicalFormMapper createRadicalFormMapper;

    @Override
    public Radical toEntity(CreateRadicalDto createRadicalDto) {
        Radical radical = new Radical();
        radical.setName(createRadicalDto.getName());
        radical.setNumber(createRadicalDto.getNumber());
        radical.setStrokeCount(createRadicalDto.getStrokeCount());
        radical.setSpelling(createRadicalDto.getSpelling());
        radical.setRadicalForms(createRadicalDto.getRadicalForms().stream()
                .map(createRadicalFormMapper::toEntity)
                .peek(form -> form.setRadical(radical))
                .collect(Collectors.toList()));
        return radical;
    }

    @Override
    public CreateRadicalDto toDto(Radical radical) {
        return new CreateRadicalDto(
                radical.getName(),
                radical.getNumber(),
                radical.getStrokeCount(),
                radical.getSpelling(),
                radical.getRadicalForms().stream().map(createRadicalFormMapper::toDto).collect(Collectors.toList())
        );
    }
}
