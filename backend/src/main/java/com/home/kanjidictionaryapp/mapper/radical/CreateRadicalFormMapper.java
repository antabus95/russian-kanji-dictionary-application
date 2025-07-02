package com.home.kanjidictionaryapp.mapper.radical;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.model.RadicalForm;
import org.springframework.stereotype.Component;

@Component
public class CreateRadicalFormMapper implements CreateMapper<RadicalForm, CreateRadicalFormDto> {
    @Override
    public RadicalForm toEntity(CreateRadicalFormDto createRadicalFormDto) {
        RadicalForm radicalForm = new RadicalForm();
        radicalForm.setFormName(createRadicalFormDto.getFormName());
        radicalForm.setSpelling(createRadicalFormDto.getSpelling());
        radicalForm.setAltSpelling(createRadicalFormDto.getAltSpelling());
        return radicalForm;
    }

    @Override
    public CreateRadicalFormDto toDto(RadicalForm radicalForm) {
        return new CreateRadicalFormDto(
                radicalForm.getFormName(),
                radicalForm.getSpelling(),
                radicalForm.getAltSpelling()
        );
    }
}
