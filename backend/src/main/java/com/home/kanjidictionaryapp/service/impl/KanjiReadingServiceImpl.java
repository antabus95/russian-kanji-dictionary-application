package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiReadingDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiReadingDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.KanjiReading;
import com.home.kanjidictionaryapp.repository.KanjiReadingRepository;
import com.home.kanjidictionaryapp.service.KanjiReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanjiReadingServiceImpl implements KanjiReadingService {

    private final KanjiReadingRepository kanjiReadingRepository;
    private final ReadUpdateMapper<KanjiReading, ReadUpdateKanjiReadingDto> readUpdateKanjiReadingMapper;
    private final CreateMapper<KanjiReading, CreateKanjiReadingDto> createKanjiReadingMapper;

    @Override
    public Optional<ReadUpdateKanjiReadingDto> getById(Long id) {
        return kanjiReadingRepository.findById(id).map(readUpdateKanjiReadingMapper::toDto);
    }

    @Transactional
    @Override
    public ReadUpdateKanjiReadingDto create(CreateKanjiReadingDto createKanjiReadingDto) {
        return Optional.of(createKanjiReadingDto)
                .map(createKanjiReadingMapper::toEntity)
                .map(kanjiReadingRepository::save)
                .map(readUpdateKanjiReadingMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Could not create kanji reading"));
    }

    @Transactional
    @Override
    public Optional<ReadUpdateKanjiReadingDto> update(ReadUpdateKanjiReadingDto readUpdateKanjiReadingDto) {
        return kanjiReadingRepository.findById(readUpdateKanjiReadingDto.getId())
                .map(entity -> {
                    entity.setText(readUpdateKanjiReadingDto.getText());
                    entity.setReadingType(readUpdateKanjiReadingDto.getReadingType());
                    entity.setChineseReadingCategory(readUpdateKanjiReadingDto.getChineseReadingCategory());
                    entity.setJoyo(readUpdateKanjiReadingDto.isJoyo());
                    return entity;
                })
                .map(kanjiReadingRepository::saveAndFlush)
                .map(readUpdateKanjiReadingMapper::toDto);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return kanjiReadingRepository.findById(id)
                .map(entity -> {
                    kanjiReadingRepository.delete(entity);
                    kanjiReadingRepository.flush();
                    return true;
                }).orElse(false);
    }
}
