package com.home.kanjidictionaryapp.dto.kanji;

import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.model.KanjiCategory;
import lombok.Value;

import java.util.List;

@Value
public class ReadKanjiDto {

    Long id;

    String spelling;

    List<ReadUpdateKanjiMeaningDto> baseMeanings;

    List<ReadUpdateKanjiReadingDto> readings;

    String meanings;

    String etymology;

    Integer strokeCount;

    KanjiCategory category;

    ReadUpdateRadicalFormDto radicalForm;

    Integer jlptLvl;

    Float kankenLvl;

    String jisCode;

    String unicode;

    List<KanjiFormDto> traditionalForms;

    List<KanjiFormDto> simplifiedForms;

    List<KanjiFormDto> alternativeForms;

}
