package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.kanji.*;
import com.home.kanjidictionaryapp.model.Kanji;
import com.home.kanjidictionaryapp.model.KanjiCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KanjiService {

    Optional<ReadKanjiDto> getById(Long id);

    Kanji getKanjiById(Long id);

    Page<ReadKanjiDto> getAll(Pageable pageable);

    Page<ReadKanjiDto> getAllWithFilter(Integer jlptLvl,
                                        Float kankenLvl,
                                        Integer strokeCount,
                                        List<KanjiCategory> categories,
                                        String reading,
                                        String meaning,
                                        String spelling,
                                        Pageable pageable);

    Page<ReadKanjiDto> getAllByRadicalId(Long radicalId, Pageable pageable);

    KanjiFormDto getKanjiForm(Long id);

    Optional<ReadKanjiDto> update(Long id, UpdateKanjiDto kanji);

    ReadKanjiDto create(CreateKanjiDto kanji);

    boolean delete(Long id);

}
