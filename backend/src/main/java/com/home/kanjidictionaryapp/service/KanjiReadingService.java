package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiReadingDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiReadingDto;

import java.util.Optional;

public interface KanjiReadingService {

    Optional<ReadUpdateKanjiReadingDto> getById(Long id);

    ReadUpdateKanjiReadingDto create(CreateKanjiReadingDto createKanjiReadingDto);

    Optional<ReadUpdateKanjiReadingDto> update(ReadUpdateKanjiReadingDto readUpdateKanjiReadingDto);

    boolean deleteById(Long id);

}
