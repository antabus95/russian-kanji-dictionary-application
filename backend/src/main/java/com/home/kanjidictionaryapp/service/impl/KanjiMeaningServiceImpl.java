package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiMeaningDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiMeaningDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.mapper.kanji.CreateKanjiMeaningMapper;
import com.home.kanjidictionaryapp.model.KanjiMeaning;
import com.home.kanjidictionaryapp.repository.KanjiMeaningRepository;
import com.home.kanjidictionaryapp.service.KanjiMeaningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanjiMeaningServiceImpl implements KanjiMeaningService {

    private final KanjiMeaningRepository kanjiMeaningRepository;
    private final ReadUpdateMapper<KanjiMeaning, ReadUpdateKanjiMeaningDto> readUpdateKanjiMeaningMapper;
    private final CreateMapper<KanjiMeaning, CreateKanjiMeaningDto> createKanjiMeaningMapper;

    @Override
    public Optional<ReadUpdateKanjiMeaningDto> getById(Long id) {
        return kanjiMeaningRepository.findById(id).map(readUpdateKanjiMeaningMapper::toDto);
    }

    @Transactional
    @Override
    public ReadUpdateKanjiMeaningDto create(CreateKanjiMeaningDto createKanjiMeaningDto) {
        return Optional.of(createKanjiMeaningDto)
                .map(createKanjiMeaningMapper::toEntity)
                .map(kanjiMeaningRepository::save)
                .map(readUpdateKanjiMeaningMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Could not create kanji meaning"));
    }

    @Transactional
    @Override
    public Optional<ReadUpdateKanjiMeaningDto> update(ReadUpdateKanjiMeaningDto readUpdateKanjiMeaningDto) {
        return kanjiMeaningRepository.findById(readUpdateKanjiMeaningDto.getId())
                .map(entity -> {
                    entity.setMeaning(readUpdateKanjiMeaningDto.getMeaning());
                    return entity;
                })
                .map(kanjiMeaningRepository::saveAndFlush)
                .map(readUpdateKanjiMeaningMapper::toDto);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return kanjiMeaningRepository.findById(id)
                .map(entity -> {
                    kanjiMeaningRepository.delete(entity);
                    kanjiMeaningRepository.flush();
                    return true;
                }).orElse(false);
    }
}
