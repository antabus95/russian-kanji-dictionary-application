package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RadicalService {

    Optional<ReadUpdateRadicalDto> getById(Long id);

    Optional<ReadUpdateRadicalDto> getByNumber(Integer number);

    Page<ReadUpdateRadicalDto> getAll(Pageable pageable);

    Page<ReadUpdateRadicalDto> getAllWithFilter(Integer strokeCount, Integer number, String spelling, Pageable pageable);

    Optional<ReadUpdateRadicalDto> getBySpelling(String spelling);

    Optional<ReadUpdateRadicalDto> update(Long id, ReadUpdateRadicalDto radical);

    ReadUpdateRadicalDto create(CreateRadicalDto radical);

    boolean delete(Long id);

}
