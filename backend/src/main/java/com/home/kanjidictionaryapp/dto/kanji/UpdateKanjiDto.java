package com.home.kanjidictionaryapp.dto.kanji;

import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.model.KanjiCategory;
import lombok.Value;

import java.util.List;

@Value
public class UpdateKanjiDto {

    Long id;

    String spelling;

    List<ReadUpdateKanjiMeaningDto> baseMeanings;

    List<ReadUpdateKanjiReadingDto> readings;

    String meanings;

    String etymology;

    Integer strokeCount;

    KanjiCategory category;

    Long radicalFormId;

    Integer jlptLvl;

    Float kankenLvl;

    String jisCode;

    String unicode;

    List<Long> traditionalForms;

    List<Long> simplifiedForms;

    List<Long> alternativeForms;

}
