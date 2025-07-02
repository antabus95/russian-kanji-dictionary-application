package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.kanjiList.*;
import com.home.kanjidictionaryapp.mapper.kanji.ReadKanjiMapper;
import com.home.kanjidictionaryapp.model.KanjiList;
import com.home.kanjidictionaryapp.repository.KanjiListRepository;
import com.home.kanjidictionaryapp.service.KanjiListService;
import com.home.kanjidictionaryapp.service.KanjiService;
import com.home.kanjidictionaryapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanjiListServiceImpl implements KanjiListService {

    private final KanjiListRepository kanjiListRepository;
    private final UserService userService;
    private final ReadKanjiMapper readKanjiMapper;
    private final KanjiService kanjiService;

    @Transactional
    @Override
    public KanjiListResponseDto create(KanjiListRequestDto dto) {
        KanjiList kanjiList = new KanjiList();
        kanjiList.setName(dto.getName());
        kanjiList.setUser(userService.getCurrentUser());
        kanjiListRepository.save(kanjiList);
        return this.kanjiListToKanjiListResponseDto(kanjiList);
    }

    @Transactional
    @Override
    public KanjiListResponseDto update(Long id, KanjiListRequestDto dto) {
        KanjiList kanjiList = kanjiListRepository.findByIdAndUserId(id, userService.getCurrentUser().getId()).orElseThrow(EntityNotFoundException::new);
        kanjiList.setName(dto.getName());
        kanjiListRepository.save(kanjiList);
        return this.kanjiListToKanjiListResponseDto(kanjiList);
    }


    @Override
    public Page<KanjiListResponseDto> getAllByUser(Pageable pageable) {
        return kanjiListRepository.findAllByUserId(userService.getCurrentUser().getId(), pageable)
                .map(this::kanjiListToKanjiListResponseDto);
    }

    @Override
    public KanjiListResponseDto getByName(String name) {
        return kanjiListRepository.findByUserIdAndName(userService.getCurrentUser().getId(), name)
                .map(this::kanjiListToKanjiListResponseDto)
                .orElseThrow();
    }

    @Override
    public Page<KanjiListResponseDto> getAllByKanjiSpelling(String spelling, Pageable pageable) {
        return kanjiListRepository.findAllByUserIdAndKanjiSpelling(userService.getCurrentUser().getId(),
                spelling, pageable).map(this::kanjiListToKanjiListResponseDto);
    }

    @Override
    public KanjiListResponseDto getById(Long id) {
        return kanjiListRepository.findByIdAndUserId(id, userService.getCurrentUser().getId())
                .map(this::kanjiListToKanjiListResponseDto).orElseThrow();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        KanjiList kanjiList = kanjiListRepository.findByIdAndUserId(id, userService.getCurrentUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Лист иероглифов не найден"));

        kanjiListRepository.delete(kanjiList);
        kanjiListRepository.flush();
    }


    private KanjiListResponseDto kanjiListToKanjiListResponseDto(KanjiList kl) {
        return new KanjiListResponseDto(kl.getId(),
                kl.getName(),
                kl.getElements().stream().map(e ->
                        new KanjiListElementResponseDto(e.getId(),
                                e.getNote(),
                                e.getAddedAt(),
                                e.isLearned(),
                                readKanjiMapper.toDto(e.getKanji(),
                                        e.getKanji().getTraditionalFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                                        e.getKanji().getSimplifiedFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                                        e.getKanji().getAlternativeFormIds().stream().map(kanjiService::getKanjiForm).toList())
                        )).toList());
    }
}
