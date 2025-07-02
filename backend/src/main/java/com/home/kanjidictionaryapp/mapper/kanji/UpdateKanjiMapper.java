package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.KanjiFormDto;
import com.home.kanjidictionaryapp.dto.kanji.UpdateKanjiDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.radical.ReadUpdateRadicalFormMapper;
import com.home.kanjidictionaryapp.model.Kanji;
import com.home.kanjidictionaryapp.model.RadicalForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateKanjiMapper {

    private final ReadUpdateKanjiMeaningMapper readUpdateKanjiMeaningMapper;
    private final ReadUpdateKanjiReadingMapper readUpdateKanjiReadingMapper;

    public Kanji toEntity(UpdateKanjiDto dto, RadicalForm radicalForm){
        Kanji kanji = new Kanji();
        kanji.setId(dto.getId());
        kanji.setSpelling(dto.getSpelling());
        kanji.setMeanings(dto.getMeanings());
        kanji.setCategory(dto.getCategory());
        kanji.setEtymology(dto.getEtymology());
        kanji.setBaseMeanings(dto.getBaseMeanings().stream().map(readUpdateKanjiMeaningMapper::toEntity).collect(Collectors.toList()));
        kanji.setReadings(dto.getReadings().stream().map(readUpdateKanjiReadingMapper::toEntity).collect(Collectors.toList()));
        kanji.setUnicode(dto.getUnicode());
        kanji.setJisCode(dto.getJisCode());
        kanji.setStrokeCount(dto.getStrokeCount());
        kanji.setRadicalForm(radicalForm);
        kanji.setJlptLvl(dto.getJlptLvl());
        kanji.setKankenLvl(dto.getKankenLvl());
        kanji.setTraditionalFormIds(dto.getTraditionalForms());
        kanji.setSimplifiedFormIds(dto.getSimplifiedForms());
        kanji.setAlternativeFormIds(dto.getAlternativeForms());
        return kanji;
    }

}
