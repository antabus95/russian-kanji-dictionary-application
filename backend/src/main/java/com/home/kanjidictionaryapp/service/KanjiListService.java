package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.kanjiList.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KanjiListService {

    KanjiListResponseDto create(KanjiListRequestDto dto);
    KanjiListResponseDto update(Long id, KanjiListRequestDto dto);
    Page<KanjiListResponseDto> getAllByUser(Pageable pageable);
    KanjiListResponseDto getByName(String name);
    Page<KanjiListResponseDto> getAllByKanjiSpelling(String spelling, Pageable pageable);
    KanjiListResponseDto getById(Long id);
    void delete(Long id);

}
