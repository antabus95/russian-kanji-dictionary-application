package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementRequestDto;
import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementResponseDto;

public interface KanjiListElementService {

    KanjiListElementResponseDto create(Long listId, KanjiListElementRequestDto dto);
    KanjiListElementResponseDto update(Long listId, Long id, KanjiListElementRequestDto dto);
    void delete(Long listId, Long id);

}
