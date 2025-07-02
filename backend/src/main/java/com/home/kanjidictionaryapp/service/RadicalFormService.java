package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalFormDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;

import java.util.List;
import java.util.Optional;

public interface RadicalFormService {

    Optional<ReadUpdateRadicalFormDto> getById(Long id);

    ReadUpdateRadicalFormDto create(CreateRadicalFormDto createRadicalFormDto);

    Optional<ReadUpdateRadicalFormDto> update(ReadUpdateRadicalFormDto readUpdateRadicalFormDto);

    List<ReadUpdateRadicalFormDto> getAll();

    boolean deleteById(Long id);

}
