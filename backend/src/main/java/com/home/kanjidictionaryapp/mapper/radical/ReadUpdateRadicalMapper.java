package com.home.kanjidictionaryapp.mapper.radical;

import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalDto;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.Radical;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReadUpdateRadicalMapper implements ReadUpdateMapper<Radical, ReadUpdateRadicalDto> {

    private final ReadUpdateRadicalFormMapper formMapper;

    @Override
    public Radical toEntity(ReadUpdateRadicalDto readUpdateRadicalDto) {
        Radical radical = new Radical();
        radical.setId(readUpdateRadicalDto.getId());
        radical.setName(readUpdateRadicalDto.getName());
        radical.setNumber(readUpdateRadicalDto.getNumber());
        radical.setStrokeCount(readUpdateRadicalDto.getStrokeCount());
        radical.setSpelling(readUpdateRadicalDto.getSpelling());
        radical.setRadicalForms(readUpdateRadicalDto.getRadicalForms().stream().map(formMapper::toEntity).collect(Collectors.toList()));
        return radical;
    }

    @Override
    public ReadUpdateRadicalDto toDto(Radical radical) {
        return new ReadUpdateRadicalDto(
                radical.getId(),
                radical.getName(),
                radical.getNumber(),
                radical.getStrokeCount(),
                radical.getSpelling(),
                radical.getRadicalForms().stream().map(formMapper::toDto).collect(Collectors.toList())
        );
    }
}
