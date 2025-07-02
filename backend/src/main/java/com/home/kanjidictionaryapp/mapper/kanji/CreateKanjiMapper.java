package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiDto;
import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiMeaningDto;
import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiReadingDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.radical.ReadUpdateRadicalFormMapper;
import com.home.kanjidictionaryapp.model.Kanji;
import com.home.kanjidictionaryapp.model.KanjiMeaning;
import com.home.kanjidictionaryapp.model.KanjiReading;
import com.home.kanjidictionaryapp.model.RadicalForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateKanjiMapper {


    private final CreateKanjiReadingMapper createKanjiReadingMapper;
    private final CreateKanjiMeaningMapper createKanjiMeaningMapper;


    public Kanji toEntity(CreateKanjiDto dto, RadicalForm radicalForm){
        Kanji kanji = new Kanji();
        kanji.setSpelling(dto.getSpelling());
        kanji.setMeanings(dto.getMeanings());

        List<KanjiReading> kanjiReadings = new ArrayList<>();
        for(CreateKanjiReadingDto readingDto : dto.getReadings()){
            KanjiReading kanjiReading = createKanjiReadingMapper.toEntity(readingDto);
            kanjiReading.setKanji(kanji);
            kanjiReadings.add(kanjiReading);
        }
        kanji.setReadings(kanjiReadings);

        List<KanjiMeaning> kanjiBaseMeanings = new ArrayList<>();
        for(CreateKanjiMeaningDto dtoMeaning : dto.getBaseMeanings()){
            KanjiMeaning kanjiMeaning = createKanjiMeaningMapper.toEntity(dtoMeaning);
            kanjiMeaning.setKanji(kanji);
            kanjiBaseMeanings.add(kanjiMeaning);
        }
        kanji.setBaseMeanings(kanjiBaseMeanings);

        kanji.setEtymology(dto.getEtymology());
        kanji.setCategory(dto.getCategory());
        kanji.setUnicode(dto.getUnicode());
        kanji.setJisCode(dto.getJisCode());
        kanji.setJlptLvl(dto.getJlptLvl());
        kanji.setKankenLvl(dto.getKankenLvl());
        kanji.setRadicalForm(radicalForm);
        kanji.setStrokeCount(dto.getStrokeCount());
        kanji.setTraditionalFormIds(dto.getTraditionalForms());
        kanji.setSimplifiedFormIds(dto.getSimplifiedForms());
        kanji.setAlternativeFormIds(dto.getAlternativeForms());
        return kanji;
    }
}
