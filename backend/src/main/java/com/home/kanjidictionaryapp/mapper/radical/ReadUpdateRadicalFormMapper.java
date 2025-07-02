package com.home.kanjidictionaryapp.mapper.radical;

import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.RadicalForm;
import org.springframework.stereotype.Component;

@Component
public class ReadUpdateRadicalFormMapper implements ReadUpdateMapper<RadicalForm, ReadUpdateRadicalFormDto> {
    @Override
    public RadicalForm toEntity(ReadUpdateRadicalFormDto readUpdateRadicalFormDto) {
        RadicalForm radicalForm = new RadicalForm();
        radicalForm.setId(readUpdateRadicalFormDto.getId());
        radicalForm.setFormName(readUpdateRadicalFormDto.getFormName());
        radicalForm.setSpelling(readUpdateRadicalFormDto.getSpelling());
        radicalForm.setAltSpelling(readUpdateRadicalFormDto.getAltSpelling());
        return radicalForm;
    }

    @Override
    public ReadUpdateRadicalFormDto toDto(RadicalForm radicalForm) {
        return new ReadUpdateRadicalFormDto(
                radicalForm.getId(), radicalForm.getFormName(), radicalForm.getSpelling(), radicalForm.getAltSpelling(),false
        );
    }
}
