package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiMeaningDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiMeaningDto;

import java.util.Optional;

public interface KanjiMeaningService {

    Optional<ReadUpdateKanjiMeaningDto> getById(Long id);

    ReadUpdateKanjiMeaningDto create(CreateKanjiMeaningDto createKanjiMeaningDto);

    Optional<ReadUpdateKanjiMeaningDto> update(ReadUpdateKanjiMeaningDto readUpdateKanjiMeaningDto);

    boolean deleteById(Long id);

}
